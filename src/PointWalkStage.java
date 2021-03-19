import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class PointWalkStage extends Stage {
    private final Random random;

    public PointWalkStage(int interval) {
        super(interval);
        this.random = new Random();
    }

    public Image step(Tile[] tiles) {
        int size = tiles[0].size;

        Image visualisation = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D visualisationGraphics = (Graphics2D) visualisation.getGraphics();

        Image buffer = new BufferedImage(800, 400, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();

        Tiling tiling = Tiling.create(tiles, random, 3, 3);

        visualisationGraphics.setColor(Color.BLUE);
        for (Tile.Point point : tiling.get(1, 1).points) {
            visualisationGraphics.fill(point.getShape());
        }

        Tile.Point[] points = IntStream.range(0, 3).mapToObj(
                x -> IntStream.range(0, 3).mapToObj(
                        y -> tiling.get(x, y).points.stream()
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

        for (Tile.Point point : tiling.get(1, 1).points) {
            OptionalInt indexOption = IntStream.range(0, points.length).filter(
                    i -> points[i].equals(new Tile.Point(point.x + size, point.y + size))
            ).findFirst();
            if (indexOption.isEmpty()) continue;
            int index = indexOption.getAsInt();
            point.x = Math.min(size, Math.max(0, totals[index].x / counts[index] - size));
            point.y = Math.min(size, Math.max(0, totals[index].y / counts[index] - size));
        }

        tiling.get(1, 1).redraw();

        g2.drawImage(visualisation, 351, 151, null);
        tiling.draw(g2, 300, 100);

        return buffer;
    }
}
