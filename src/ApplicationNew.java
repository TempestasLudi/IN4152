import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

public class ApplicationNew extends Frame implements KeyListener {
    private final Tile[] tiles;
    private final int size = 50;
    private Image stageImage = null;
    private final Random random = new Random();
    private int stageIndex = 1;
    private final Tile[][] fixedTiling;

    public ApplicationNew() {
        super("An implementation of Lloyd's Algorithm");
        prepareGUI();
        tiles = new Tile[]{
                new Tile(size, Color.RED, Color.GREEN, Color.RED, Color.GREEN),
                new Tile(size, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW),
                new Tile(size, Color.RED, Color.YELLOW, Color.RED, Color.YELLOW),
                new Tile(size, Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN),
                new Tile(size, Color.BLUE, Color.GREEN, Color.BLUE, Color.GREEN),
                new Tile(size, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW),
                new Tile(size, Color.BLUE, Color.YELLOW, Color.BLUE, Color.YELLOW),
                new Tile(size, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN),
        };

        Stage stage = new RandomPointsStage();
        addKeyListener(this);

        this.fixedTiling = createTiling(10, 7);

        new Thread(() -> {
            while (true) {
                ApplicationNew.this.repaint();
                if (stageIndex == 1) {
                    this.stageImage = stage.step(tiles);
                }

                try {
                    Thread.sleep(stage.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        ApplicationNew awtGraphicsDemo = new ApplicationNew();
        awtGraphicsDemo.setVisible(true);
    }

    private void prepareGUI() {
        setSize(800, 800);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        Image buffer = createImage(getWidth(), getHeight());
        Graphics bufferGraphics = buffer.getGraphics();

        switch (stageIndex) {
            case 1 -> paintFirstStage(bufferGraphics);
            case 2 -> paintWangTiling(bufferGraphics);
        }

        drawTiling((Graphics2D) bufferGraphics, fixedTiling, 100, 400);

        g.drawImage(buffer, 0, 0, this);
    }

    public void paintFirstStage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.draw(g2, 100 + i * (size + 14), 100);
        }
        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }

    public synchronized void paintWangTiling(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Image visualisation = createImage(size, size);
        Graphics2D visualisationGraphics = (Graphics2D) visualisation.getGraphics();

        Tile[][] tiling = createTiling(3, 3);

        visualisationGraphics.setColor(Color.RED);
        for (Tile.Point point : tiling[1][1].points) {
            visualisationGraphics.fill(point.getShape());
        }

        Tile.Point[] points = IntStream.range(0, 3).mapToObj(
                x -> IntStream.range(0, 3).mapToObj(
                        y -> tiling[y][x].points.stream()
                                .map(point -> new Tile.Point(size * x + point.x, size * y + point.y))
                ).flatMap(s -> s)
        ).flatMap(s -> s).toArray(Tile.Point[]::new);

        int[] counts = Arrays.stream(points).mapToInt(p -> 0).toArray();
        Tile.Point[] totals = Arrays.stream(points).map(p -> new Tile.Point(0, 0)).toArray(Tile.Point[]::new);
        int[][] indices = new int[size][size];

        for (int x = 0; x < 3 * size; x++) {
            for (int y = 0; y < 3 * size; y++) {
                double d = Double.POSITIVE_INFINITY;
                int index = -1;
                for (int i = 0; i < points.length; i++) {
                    Tile.Point point = points[i];
                    double d2 = (x - point.x) * (x - point.x) + (y - point.y) * (y - point.y);
                    if (d2 < d) {
                        index = i;
                        d = d2;
                    }
                }
                counts[index]++;
                totals[index].x += x;
                totals[index].y += y;

                if (size <= x && x < size * 2 && size <= y && y < size * 2) {
                    indices[y - size][x - size] = index;
                }
            }
        }

        visualisationGraphics.setColor(Color.GREEN);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (
                        (x > 0 && indices[y][x - 1] != indices[y][x]) ||
                        (x < size - 1 && indices[y][x + 1] != indices[y][x]) ||
                        (y > 0 && indices[y - 1][x] != indices[y][x]) ||
                        (y < size - 1 && indices[y + 1][x] != indices[y][x])
                ) {
                    visualisationGraphics.drawLine(x, y, x, y);
                }
            }
        }

        for (Tile.Point point : tiling[1][1].points) {
            OptionalInt indexOption = IntStream.range(0, points.length).filter(
                    i -> points[i].equals(new Tile.Point(point.x + size, point.y + size))
            ).findFirst();
            if (indexOption.isEmpty()) continue;
            int index = indexOption.getAsInt();
            point.x = Math.min(size, Math.max(0, totals[index].x / counts[index] - size));
            point.y = Math.min(size, Math.max(0, totals[index].y / counts[index] - size));
        }

        tiling[1][1].redraw();

        g.drawImage(visualisation, 351, 151, this);
        drawTiling(g2, tiling, 300, 100);

        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }

    public Tile[][] createTiling(int w, int h) {
        Tile[][] tiling = new Tile[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (y == 0 && x == 0) {
                    tiling[y][x] = getFirstTile();
                    continue;
                }
                if (y == 0) {
                    tiling[y][x] = getTileFirstRow(tiling[y][x - 1]);
                    continue;
                }
                if (x == 0) {
                    tiling[y][x] = getFirstTilePerRow(tiling[y - 1][x]);
                    continue;
                }
                tiling[y][x] = getTileRestRow(tiling[y][x - 1], tiling[y - 1][x]);
            }
        }
        return tiling;
    }

    public void drawTiling(Graphics2D g, Tile[][] tiling, int x0, int y0) {
        final int border = 1;
        for (int y = 0; y < tiling.length; y++) {
            for (int x = 0; x < tiling[y].length; x++) {
                tiling[y][x].draw(g, x0 + x * (size + border), y0 + y * (size + border));
            }
        }
    }

    public Tile getFirstTile() {
        return tiles[random()];
    }

    /*        Second step for stochastic tiling algorithm     */

    public Tile getTileFirstRow(Tile PervLeftTile) {
        while (true) {
            Tile tile = tiles[random()];
            if (PervLeftTile.east == tile.west) {
                return tile;
            }
        }
    }

    public Tile getFirstTilePerRow(Tile PervUpperTile) {
        while (true) {
            Tile tile = tiles[random()];
            if (PervUpperTile.south == tile.north) {
                return tile;
            }
        }
    }

    public Tile getTileRestRow(Tile PervLeftTile, Tile PervUpperTile) {
        while (true) {
            Tile tile = tiles[random()];
            if (PervLeftTile.east == tile.west && PervUpperTile.south == tile.north) {
                return tile;
            }
        }
    }

    public int random() {
        return random.nextInt(tiles.length);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            stageIndex++;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            stageIndex--;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}