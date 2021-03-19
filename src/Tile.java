import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    public final List<Point> points = new ArrayList<Point>();
    public final Color north, west, south, east;
    public final int size;
    private final Image buffer;

    public Tile(int size, Color north, Color west, Color south, Color east) {
        this.size = size;
        this.north = north;
        this.west = west;
        this.south = south;
        this.east = east;

        this.buffer = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        this.redraw();
    }



    public void redraw() {
        Graphics2D g = (Graphics2D) this.buffer.getGraphics();
        g.setBackground(new Color(0, 0, 0, 0));
        g.clearRect(0, 0, size, size);

        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.setColor(north);
        g.drawLine(0, 0, size, 0);
        g.setColor(west);
        g.drawLine(0, 0, 0, size);
        g.setColor(south);
        g.drawLine(0, size, size, size);
        g.setColor(east);
        g.drawLine(size, 0, size, size);
        g.setStroke(oldStroke);

        g.setColor(Color.BLACK);
        for (Point point : points) {
            g.fill(point);
        }
    }

    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(buffer, x, y, null);
    }

    static public class Point extends Ellipse2D.Double {
        public Point(double x, double y) {
            super(x - 3, y - 3, 6, 6);
        }
    }


}
