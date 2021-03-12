import java.awt.*;
import java.util.Random;

public class RandomPointsStage extends Stage {
    Random random = new Random();

    public RandomPointsStage() {
        super(1000);
    }

    public Image step(Tile[] tiles) {
        for (Tile tile : tiles) {
            tile.points.add(new Tile.Point(random.nextDouble() * tile.size, random.nextDouble() * tile.size));
            tile.redraw();
        }
        return null;
    }
}
