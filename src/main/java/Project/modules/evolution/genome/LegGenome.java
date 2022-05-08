package Project.modules.evolution.genome;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LegGenome {
    Map<Character, Double> gens;

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
