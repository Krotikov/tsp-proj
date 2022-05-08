package Project.modules.evolution.genome;

import java.util.List;
import java.util.stream.Stream;

public class Genomes {
    private final List<Genome> genomes;

    public List<Genome> getAll() {
        return genomes;
    }

    public Genome get(int i) {
        return genomes.get(i);
    }

    public Genomes(int pSize) {
        genomes = Stream.generate(Genome::new).limit(pSize).toList();
    }

}
