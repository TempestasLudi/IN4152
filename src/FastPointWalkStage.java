import java.util.Optional;

public class FastPointWalkStage extends PointWalkStage {
    public FastPointWalkStage() {
        super(500);
    }

    @Override
    public Optional<Stage> next() {
        return Optional.empty();
    }

    @Override
    public Optional<Stage> previous() {
        return Optional.of(new SlowPointWalkStage());
    }
}
