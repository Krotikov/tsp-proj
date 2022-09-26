package Project.modules.evolution;

import Project.modules.evolution.genome.Genome;
import Project.modules.evolution.score.Score;

import java.util.Iterator;
import java.util.List;

public interface Evolution extends Iterator<List<Genome>> {

    Genome bestBy(List<Score> results);
    void reset();
    @Override
    boolean hasNext();

    @Override
    List<Genome> next();
}
