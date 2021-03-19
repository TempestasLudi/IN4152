import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ApplicationNew extends Frame implements KeyListener{
    private Tile[] tiles;
    JLabel label;

    private final int size = 50;
    private Image stageImage = null;
    int TILES_X = 3, TILES_Y = 3;
    int original_x = 0, original_y = 0;
    Random random = new Random();
    boolean globalBool = false;
    int globalStageCount = 0;

    public ApplicationNew(){
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
        addKeyListener(this);

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

    public static void main(String[] args){
        ApplicationNew awtGraphicsDemo = new ApplicationNew();
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
    public void paint(Graphics g){
        //KeyEventInfo keyInfo = new KeyEventInfo("KeyEventInfo");
        ///int id = keyInfo.getKeyTyped().getID();
        int stageOne = 1;
        int stageTwo = 2;
        if(globalBool== true){
            if(globalStageCount == stageOne){
                    paintFirstStage(g);
            }
            else if(globalStageCount == stageTwo){
                paintWangTiling(g);

            }

        }



    }

    public void paintFirstStage(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.draw(g2, 0, 100 + (int) (i * size * 1.1));
        }

        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }

    public void paintWangTiling(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Tile[][] globalPervUpperTile = new Tile[TILES_X][TILES_Y];
        for(int row = 0; row < TILES_X; row++) {
            Tile pervTile = null;
            for (int i = 0; i < TILES_Y; i++) {

                Tile tile = tiles[i];

                if(isUpperLeftCorner(row,i)){
                    tile = getFirstTile(row,i);
                    pervTile = tile;
                    tile.draw(g2, 0, 100 + (int) (i * size * 1.1));
                }
                if(isFirstRow(row) == true && !isUpperLeftCorner(row,i) == true){
                    tile = getTileFirstRow(row,i,pervTile);
                    pervTile = tile;
                    tile.draw(g2, (int) (i * size * 1.1), 100);

                }
                if(isRestRow(row) == true && isFirstTilePerRow(i) == true){
                    if(row >= 0) {
                        tile = getFirstTilePerRow(row, i, globalPervUpperTile[row - 1][i]);
                        pervTile = tile;
                        tile.draw(g2, 0, 100 + (int) (row * size * 1.1));
                    }
                }

                if(isRestRow(row) == true && !isFirstTilePerRow(i) == true){
                    if(row >= 0) {
                        tile = getTileRestRow(row, i, pervTile,globalPervUpperTile[row - 1][i]);
                        pervTile = tile;
                        tile.draw(g2, (int) (i * size * 1.1), 100 + (int) (row * size * 1.1));
                    }

                }

                globalPervUpperTile[row][i] = tile;
                //tile.draw(g2, 100, 100 + (int) (i * size * 1.1));

            }
        }
        if (this.stageImage != null) {
            g2.drawImage(this.stageImage, 200, 200, null);
        }
    }



    /*Check if the tile is in first row or not
    *
    *
    * */
    public boolean isFirstRow(int i)
    {
        boolean determineValue = false;

        if(i == original_x){

            determineValue = true;
        }


        return determineValue;
    }

    public boolean isUpperLeftCorner(int i, int j){

        boolean determineValue = false;

        if(i == original_x && j == original_y){

            return determineValue = true;

        }

        return determineValue;
    }



    public boolean isFirstTilePerRow(int j){

        boolean determineValue = false;

        if(j == original_y){

            return determineValue = true;

        }

        return determineValue;

    }



    public boolean isRestRow(int i){
        boolean determineValue = false;

        if(i != original_x && i != TILES_X){

            determineValue = true;

        }

        return determineValue;
    }

    public Tile getFirstTile(int i, int j){

        boolean determine = isUpperLeftCorner(i, j);
        Tile tileValue = null;
        if(determine == true){

            //for(int k = 0; k< tiles.length; k++){
            Tile tile = tiles[random()];
            tileValue = tile;
            //}

        }

        return tileValue;
    }

/*
    Second step for stochastic tiling algorithm

 */
    public Tile getTileFirstRow(int i, int j, Tile PervLeftTile){
        Tile tileValue = null;
        boolean stop = false;

        while (stop != true) {
            Tile tile = tiles[random()];
            if(PervLeftTile.east == tile.west){
                stop = true;
                return tile;
            }
        }
        //for(int k = 0; k<tiles.length; k++){


        //}

        return tileValue;
    }

    public Tile getFirstTilePerRow(int i, int j, Tile PervUpperTile){
        Tile tileValue = null;
        boolean stop = false;
        while (stop != true) {
            Tile tile = tiles[random()];
            if(PervUpperTile.south == tile.north){
                stop = true;
                return tile;
            }
        }

        return tileValue;
    }

    public Tile getTileRestRow(int i, int j, Tile PervLeftTile, Tile PervUpperTile){

        Tile tileValue = null;
        boolean stop = false;
        while (stop != true) {
            Tile tile = tiles[random()];
            if(PervLeftTile.east == tile.west && PervUpperTile.south == tile.north){
                stop = true;
                return tile;
            }
        }

        return tileValue;

    }


    public int random()
    {
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            globalBool = true;
            globalStageCount++;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            globalBool = true;
            globalStageCount--;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            globalBool = true;
            globalStageCount++;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            globalBool = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            globalBool = true;
        }
    }





/*
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Space key typed");
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Space key pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("Space key Released");
        }
    }

 */
}
