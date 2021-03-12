import java.awt.*;

public abstract class Stage {
    public int interval;

    public Stage(int interval) {
        this.interval = interval;
    }

    abstract Image step(Tile[] tiles);
}
