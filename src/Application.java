import java.awt.*;
import java.awt.event.*;
import java.util.Optional;
import java.util.Random;

public class Application extends Frame implements KeyListener {
    private final Tile[] tiles;
    private Image stageImage = null;
    private final Tiling fixedTiling;
    private Stage stage = new RandomPointsStage();

    public Application() {
        super("An implementation of Lloyd's Algorithm");
        prepareGUI();
        int size = 50;
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

        addKeyListener(this);

        this.fixedTiling = Tiling.create(tiles, new Random(), 10, 7);

        new Thread(() -> {
            while (true) {
                this.stageImage = stage.step(tiles);

                Application.this.repaint();

                try {
                    Thread.sleep(stage.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Application awtGraphicsDemo = new Application();
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

        if (this.stageImage != null) {
            bufferGraphics.drawImage(this.stageImage, 0, 0, null);
        }

        fixedTiling.draw((Graphics2D) bufferGraphics, 100, 400);

        g.drawImage(buffer, 0, 0, this);
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Optional<Stage> stageOption = stage.next();
            stageOption.ifPresent(value -> stage = value);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Optional<Stage> stageOption = stage.previous();
            stageOption.ifPresent(value -> stage = value);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }
}