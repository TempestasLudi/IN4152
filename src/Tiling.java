import java.awt.*;
import java.util.Random;

public class Tiling {
    private final int tileSize;
    private final Tile[][] tiles;

    public Tiling(int tileSize, Tile[][] tiles) {
        this.tileSize = tileSize;
        this.tiles = tiles;
    }

    public void draw(Graphics2D g, int x0, int y0) {
        final int border = 1;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                tiles[y][x].draw(g, x0 + x * (tileSize + border), y0 + y * (tileSize + border));
            }
        }
    }

    public Tile get(int x, int y) {
        return tiles[y][x];
    }

    public static Tiling create(Tile[] tiles, Random random, int w, int h) {
        Tile[][] tiling = new Tile[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (y == 0 && x == 0) {
                    tiling[y][x] = getFirstTile(tiles, random);
                    continue;
                }
                if (y == 0) {
                    tiling[y][x] = getTileFirstRow(tiles, random, tiling[y][x - 1]);
                    continue;
                }
                if (x == 0) {
                    tiling[y][x] = getFirstTilePerRow(tiles, random, tiling[y - 1][x]);
                    continue;
                }
                tiling[y][x] = getTileRestRow(tiles, random, tiling[y][x - 1], tiling[y - 1][x]);
            }
        }
        return new Tiling(tiles[0].size, tiling);
    }

    public static Tile getFirstTile(Tile[] tiles, Random random) {
        return tiles[random.nextInt(tiles.length)];
    }

    /*        Second step for stochastic tiling algorithm     */

    public static Tile getTileFirstRow(Tile[] tiles, Random random, Tile PervLeftTile) {
        while (true) {
            Tile tile = tiles[random.nextInt(tiles.length)];
            if (PervLeftTile.east == tile.west) {
                return tile;
            }
        }
    }

    public static Tile getFirstTilePerRow(Tile[] tiles, Random random, Tile PervUpperTile) {
        while (true) {
            Tile tile = tiles[random.nextInt(tiles.length)];
            if (PervUpperTile.south == tile.north) {
                return tile;
            }
        }
    }

    public static Tile getTileRestRow(Tile[] tiles, Random random, Tile PervLeftTile, Tile PervUpperTile) {
        while (true) {
            Tile tile = tiles[random.nextInt(tiles.length)];
            if (PervLeftTile.east == tile.west && PervUpperTile.south == tile.north) {
                return tile;
            }
        }
    }
}
