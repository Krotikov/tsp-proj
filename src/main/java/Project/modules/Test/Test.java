package Project.modules.Test;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Test {
    List<Map<Character, Double>> paramList = new ArrayList<>();
    void addStool(Map<Character, Double> a){
        paramList.add(a);
    }
    List<Double> test(){
        ForkJoinPool fgr = new ForkJoinPool();

        TestStoolOfGame testStoolOfGame = new TestStoolOfGame(paramList,0,paramList.size());
        fgr.invoke(testStoolOfGame);
        return null;
    }
}
class TestStoolOfGame extends RecursiveAction {
    List<Map<Character, Double>> params;
    int start;
    int end;
    final int seqThread = 2;
    TestStoolOfGame(List<Map<Character, Double>> params, int start, int end){
        this.params = params;
        this.start = start;
        this.end = end;
    }
    @Override
    protected void compute() {
        Game game;
        Stool stool;
        if((end - start) < seqThread){
            for(int i = start; i < end;i++){
                game = new Game();
                new Stool(params, game);
                game.initObjects();
                System.out.println(game.TrainRun());
            }
        }else{

            int middle = (start + end)/2;

            TestStoolOfGame subTaskA = new TestStoolOfGame(params,start,middle);
            TestStoolOfGame subTaskB = new TestStoolOfGame(params,middle,end);

            // start tasks branching
            invokeAll(subTaskA,subTaskB);
        }
    }
}


