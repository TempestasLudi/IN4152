import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ApplicationNew extends Frame implements KeyListener {
    private final Tile[] tiles;
    private final int size = 50;
    private Image stageImage = null;
    private final Random random = new Random();
    private boolean globalBool = false;
    private int globalStageCount = 0;
    private final Tile[][] fixedTiling;

    public ApplicationNew() {
        super("Java AWT Examples");
        prepareGUI();
        tiles = new Tile[]{new Tile(size, Color.RED, Color.GREEN, Color.RED, Color.GREEN), new Tile(size, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW), new Tile(size, Color.RED, Color.YELLOW, Color.RED, Color.YELLOW), new Tile(size, Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN), new Tile(size, Color.BLUE, Color.GREEN, Color.BLUE, Color.GREEN), new Tile(size, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW), new Tile(size, Color.BLUE, Color.YELLOW, Color.BLUE, Color.YELLOW), new Tile(size, Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN),};
        Stage stage = new RandomPointsStage();
        addKeyListener(this);

        this.fixedTiling = createTiling(10, 7);

        new Thread(() -> {
            while (true) {
                ApplicationNew.this.repaint();
                this.stageImage = stage.step(tiles);
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

    @Override
    public void paint(Graphics g) {
        int stageOne = 1;
        int stageTwo = 2;
        if (globalBool) {
            if (globalStageCount == stageOne) {
                paintFirstStage(g);
            } else if (globalStageCount == stageTwo) {
                paintWangTiling(g);
            }
        }

        drawTiling((Graphics2D)g, fixedTiling, 100, 400);
    }

    public void paintFirstStage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.draw(g2, 0, 100 + (int) (i * size * 1.1));
        }
        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }

    public void paintWangTiling(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int TILES_X = 3;
        int TILES_Y = 3;
        Tile[][] tiling = createTiling(TILES_X, TILES_Y);

        drawTiling(g2, tiling, 0, 100);

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
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            globalBool = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            globalBool = true;
            globalStageCount++;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            globalBool = true;
            globalStageCount--;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_LEFT) {
            globalBool = true;
        }
    }
}