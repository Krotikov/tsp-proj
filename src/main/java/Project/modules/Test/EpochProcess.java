package Project.modules.Test;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;
import Project.modules.evolution.Evolution;
import Project.modules.evolution.genome.Genome;
import Project.modules.evolution.score.Score;
import javafx.scene.paint.Color;

import java.util.List;

public class EpochProcess {
    public static Stool getUpdatedStool(List<Double> scores, Evolution alg, Game game, Color color) {
        List<Genome> genomes = alg.next();
        TestParallel test = new TestParallel(genomes);
        List<Score> results = test.run();
        Genome best = alg.bestBy(results);
        scores.add(best.getScore());

        return new Stool(best.params(), game, color);
    }
}
