import java.awt.*;
import java.awt.event.*;

public class Application extends Frame {
    private Tile[] tiles;

    private final int size = 50;
    private Image stageImage = null;

    public Application(){
        super("Java AWT Examples");
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

        new Thread(() -> {
            while (true) {
                Application.this.repaint();
                this.stageImage = stage.step(tiles);
                try {
                    Thread.sleep(stage.interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args){
        Application awtGraphicsDemo = new Application();
        awtGraphicsDemo.setVisible(true);
    }

    private void prepareGUI(){
        setSize(800,800);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.draw(g2, 0, 100 + (int) (i * size * 1.1));
        }

        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }
}
