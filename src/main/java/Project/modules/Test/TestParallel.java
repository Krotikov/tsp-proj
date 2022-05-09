package Project.modules.Test;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;
import Project.modules.evolution.genome.Genome;
import Project.modules.evolution.score.Score;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TestParallel {
    final List<Genome> paramList; // params of stools
    final HashMap<Integer, Score> resultMap = new HashMap<>(); // map of result params

    public TestParallel(List<Genome> paramList) {
        this.paramList = paramList;
    }

    public List<Score> run() {
        ForkJoinPool fgr = new ForkJoinPool();

        TestStoolOfGame testStoolOfGame = new TestStoolOfGame(paramList, 0, paramList.size(), resultMap);

        fgr.invoke(testStoolOfGame);

        return new TreeMap<>(resultMap).values().stream().toList();
    }
}

class TestStoolOfGame extends RecursiveAction {
    List<Genome> params; // params of stools
    Map<Integer, Score> totalVal; // map of result params
    int start;
    int end;
    final int seqThread = 2;

    TestStoolOfGame(List<Genome> params, int start, int end, Map<Integer, Score> totalVal) {
        this.params = params;
        this.start = start;
        this.end = end;
        this.totalVal = totalVal;
    }

    @Override
    protected void compute() {
        Game game;
        if ((end - start) < seqThread) {
            for (int i = start; i < end; i++) {
                game = new Game();
                new Stool(params.get(i).params(), game, Color.AQUAMARINE);
                game.initObjects();
                totalVal.put(i, game.TrainRun().get(0));
            }

        } else {
            int middle = (start + end) / 2;

            TestStoolOfGame subTaskA = new TestStoolOfGame(params, start, middle, totalVal);
            TestStoolOfGame subTaskB = new TestStoolOfGame(params, middle, end, totalVal);

            // start tasks branching
            invokeAll(subTaskA, subTaskB);
        }
    }
}


