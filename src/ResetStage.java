import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class ResetStage extends Stage {
    public ResetStage() {
        super(1000);
    }

    public Image step(Tile[] tiles) {
        for (Tile tile : tiles) {
            tile.points.clear();
            tile.redraw();
        }

        Image buffer = new BufferedImage(800, 400, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            tile.draw(g2, 100 + i * (tile.size + 14), 100);
        }
        return buffer;
    }

    @Override
    public Optional<Stage> next() {
        return Optional.of(new RandomPointsStage());
    }

    @Override
    public Optional<Stage> previous() {
        return Optional.empty();
    }
}
