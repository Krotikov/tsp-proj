package Project.modules.utils;

import Project.modules.evolution.genome.Genome;

import java.util.Map;


public class Utils {

    public record PairParams(Map<Character, Double> left, Map<Character, Double> right) {
    }

    public record PairGenomes(Genome left, Genome right) {
    }
}
