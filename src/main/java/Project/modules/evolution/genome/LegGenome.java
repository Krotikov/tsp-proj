package Project.modules.evolution.genome;

import java.util.HashMap;
import java.util.Map;

public class LegGenome {
    Map<Character, Double> gens;

    public Map<Character, Double> getGens() {
        return gens;
    }

    public LegGenome(Double p, Double o, Double M, Double m) {
        gens = new HashMap<>();
        gens.put('M', M);
        gens.put('m', m);
        gens.put('o', p);
        gens.put('p', o);
    }

    public double get(Character arg) {
        return gens.get(arg);
    }

    public void put(char k, double v) {
        gens.put(k, v);
    }
}
