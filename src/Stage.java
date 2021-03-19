import java.awt.*;
import java.util.Optional;

public abstract class Stage {
    public final int interval;

    public Stage(int interval) {
        this.interval = interval;
    }

    abstract Image step(Tile[] tiles);

    public abstract Optional<Stage> next();
    public abstract Optional<Stage> previous();
}
