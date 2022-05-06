package Project.modules.Test;

import Project.modules.Physics.Game;
import Project.modules.Physics.Stool;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class TestParallel {
    List<Double> paramList = new ArrayList<>(); // params of stools
    final HashMap<Integer,Double> resultMap = new HashMap<>(); // map of result params
    public void addParam(double a){
        paramList.add(a);
    }
    public  TestParallel(){}
    public  TestParallel(List<Double> paramList){
        this.paramList = paramList;
    }

    public HashMap<Integer,Double> test(){
        ForkJoinPool fgr = new ForkJoinPool();

        TestStoolOfGame testStoolOfGame = new TestStoolOfGame(paramList,0,paramList.size(),resultMap);

        fgr.invoke(testStoolOfGame);

        return resultMap;
    }
}
class TestStoolOfGame extends RecursiveAction {
    List<Double> params; // params of stools
    HashMap<Integer,Double> totalVal; // map of result params
    int start;
    int end;
    final int seqThread = 3;
    TestStoolOfGame(List<Double> params, int start, int end, HashMap<Integer,Double> totalVal){
        this.params = params;
        this.start = start;
        this.end = end;
        this.totalVal = totalVal;
    }
    @Override
    protected void compute() {
        Game game;
        if((end - start) < seqThread){
            for(int i = start; i < end;i++){
                game = new Game();
                new Stool(params.get(i),game,Color.AQUAMARINE);
                game.initObjects();
                totalVal.put(i,game.TrainRun().get(0));
            }
        }else{
            int middle = (start + end)/2;

            TestStoolOfGame subTaskA = new TestStoolOfGame(params,start,middle,totalVal);
            TestStoolOfGame subTaskB = new TestStoolOfGame(params,middle,end,totalVal);

            // start tasks branching
            invokeAll(subTaskA,subTaskB);
        }
    }
}


