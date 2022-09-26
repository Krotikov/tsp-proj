package Project.modules.evolution.genome;

/**
 * @param p - период [1 * 10^-3, 10]
 * @param o - отклонение [-pi, pi]
 * @param m - нижняя амплитуда [-piR/2, M], где R - длинна ноги
 * @param M - вверхняя амплитуда [-piR/2, piR/2] где R - длинна ноги
 *          M > m
 */

public class GenomeConfig {
    public static final Double MAX_VALUE_P=100.;
    public static final Double MIN_VALUE_P=.01;
    public static final int NORM = 1000;

    public static final Double MAX_VALUE_O=Math.PI;
    public static final Double MIN_VALUE_O=-Math.PI;

    public static final Double MAX_VALUE_m=0.;
    public static final Double MIN_VALUE_m=-0.25;

    public static final Double MAX_VALUE_M=0.25;

    public static final int POPULATION_SIZE = 24;
    public static final int MAX_EPOCH = 1000;

    public static final Double MAX_TIME =  20.;

    public static final double TRIG_PERIOD_LIM = 0.3;

    public static final Integer LEG_LENGTH = 5;

    public static final int PARAMS_COUNTS = 8;
    public static final int LEG_PARAMS_COUNTS = 4;

    public static Character[] LEG_LIST_PARAMS = {'p', 'o', 'M', 'm'};

}
