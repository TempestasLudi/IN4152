import java.util.Optional;

public class SlowPointWalkStage extends PointWalkStage {
    public SlowPointWalkStage() {
        super(4000);
    }

    @Override
    public Optional<Stage> next() {
        return Optional.of(new FastPointWalkStage());
    }

    @Override
    public Optional<Stage> previous() {
        return Optional.of(new RandomPointsStage());
    }
}
