package Project.modules.Test;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Test {
    List<Double> paramList = new ArrayList<>();
    void addStool(double a){
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
    List<Double> params;
    int start;
    int end;
    final int seqThread = 2;
    TestStoolOfGame(List<Double> params, int start, int end){
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
                stool = new Stool(params.get(i),game);
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


