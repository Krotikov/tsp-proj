package Project.modules.evolution;

import Project.modules.evolution.genome.Genome;
import Project.modules.evolution.genome.GenomeConfig;
import Project.modules.evolution.genome.Genomes;
import Project.modules.evolution.genome.LegGenome;
import Project.modules.evolution.score.Score;
import Project.modules.utils.CustomRandom;
import Project.modules.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Самая простая реализация эволюционного алгоритма
 *
 * @author Андрей и Иван
 */
public class EvolutionAlg implements Evolution {
    private final static Evolution instance = new EvolutionAlg();
    private final Genomes genomes; //текущий геном
    private List<Double> genomeScores; //результаты для предыдущей эпохи
    private int epoch; //эпоха

    private EvolutionAlg() {
        epoch = 0;
        int pSize = GenomeConfig.POPULATION_SIZE;
        genomes = new Genomes(pSize);
    }

    public static Evolution getInstance() {
        return instance;
    }

    private void update() {
        if (genomeScores != null) {
            Double fixtureSum = genomeScores.stream().reduce(0., Double::sum);
            List<Double> probs = genomeScores.stream().map(value -> value / fixtureSum).toList();

            List<Utils.PairGenomes> pairs = makePairs(probs);

            for (var pair : pairs) {
                crossing(pair.left(), pair.right());
            }
        }
    }

    private void crossing(Genome first, Genome second) {
        Map<String, LegGenome> firstLegs = first.params();
        Map<String, LegGenome> secondLegs = second.params();

        for (String leg : firstLegs.keySet()) {
            int swapNumber = CustomRandom.inRange(1, GenomeConfig.LEG_PARAMS_COUNTS - 1); // - 1 чтобы не возникло бессмыссленого обемна ногамии

            List<Character> swapParams = Stream.generate(() -> CustomRandom.from(GenomeConfig.LEG_LIST_PARAMS))
                    .limit(swapNumber)
                    .toList();

            for (var gen : swapParams) {
                double swap = firstLegs.get(leg).get(gen);
                firstLegs.get(leg).put(gen, secondLegs.get(leg).get(gen));
                secondLegs.get(leg).put(gen, swap);
            }
        }
    }

    private List<Utils.PairGenomes> makePairs(List<Double> probs) {
        List<Utils.PairGenomes> pairs = new ArrayList<>();
        while (pairs.size() < GenomeConfig.POPULATION_SIZE / 2) {

            double sum = 0.;
            int i, j;
            double r1 = CustomRandom.inRange(0., 1.);

            for (i = 0; i < probs.size(); i++) {
                sum += probs.get(i);
                if (sum > r1) {
                    break;
                }
            }
            double r2 = CustomRandom.inRange(0., 1. - probs.get(i));
            sum = 0;
            for (j = 0; j < probs.size(); j++) {
                if(j == i) continue;
                sum += probs.get(j);
                if (sum > r2) {
                    break;
                }
            }
            pairs.add(new Utils.PairGenomes(genomes.get(i), genomes.get(j)));
        }
        return pairs;
    }

    @Override
    public Genome bestBy(List<Score> results) {
        genomeScores = results.stream()
                .map(score -> score.dist() + GenomeConfig.TIME_WEIGHT * (score.time() / GenomeConfig.MAX_TIME)).toList();
        int maxScoreIndex = genomeScores.indexOf(Collections.max(genomeScores));
        return genomes.get(maxScoreIndex);
    }

    @Override
    public boolean hasNext() {
        if (epoch < GenomeConfig.MAX_EPOCH) {
            epoch += 1;
            update();
            return true;
        }
        return false;
    }

    @Override
    public List<Genome> next() {
        return genomes.getAll();
    }
}
