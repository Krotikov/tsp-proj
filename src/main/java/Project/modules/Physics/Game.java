package Project.modules.Physics;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


enum SHAPES{
    TEXT(0);
    public final int id;
    SHAPES(int id) {
        this.id = id;
    }
}


public class Game {
    public final List<Link> linkList = new ArrayList<>();
    private final List<Point> pointList = new ArrayList<>();
    private final List<Block> blocklist = new ArrayList<>();
    public final List<Stool> stoolList = new ArrayList<>();
    private final Group group = new Group();
    boolean DEBUG = false;
    boolean mouse = true;
    private final double MAX_TIME = 20;
    public Block bl;


    public void setStool(Stool stool){
        stoolList.add(stool);
    }
    public List<Double> TrainRun(){
        Manifold manifold = new Manifold();
        final double dt =  6 * 1./50;
        double t = 0;
        while(t  < MAX_TIME) {
                Utility_Functions.sortOSX(blocklist);
                Utility_Functions.sortOSY(blocklist);
                t += dt / 8;
                for (Point point : pointList) {
                    point.run(dt);
                }
                for (Stool stool : stoolList) {
                    if(stool.isLife()) {
                        stool.run(t);
                    }
                }
                for (int i = 0; i < 7; i++) {
                    makeLink();
                    makeCollision(manifold);
                }
        }
        List<Double> DistList = new ArrayList<>();
        for (Stool stool : stoolList) {
            DistList.add(stool.getDist());
        }
        return DistList;
    }
    public void initObjects(){
        Block platform = addBlock(
                new Point2D(-500,500),
                5000,
                100,
                1000000,
                Color.GREEN
        );
        platform.setGravity(false);
        platform.switchPowers();
        Utility_Functions.bindStools(stoolList);

    }


    private void makeCollision(Manifold manifold){

        for (int k1 = 0; k1 < blocklist.size(); k1++){
            for (int k2 = 0; k2 < blocklist.size(); k2++){
                if (k1 == k2){
                    continue;
                }
                if (Utility_Functions.IntersectsPoints(blocklist.get(k1), blocklist.get(k2)).size() > 0){
                    manifold.setBlocks(blocklist.get(k1), blocklist.get(k2));
                    if(manifold.isCollide) {
                        manifold.solveCollision();
                        manifold.posCorr();
                    }
                }
            }
        }
    }

    private void makeLink(){
        for (int i = 0; i < 5; i++) {
            for (Link link : linkList) {
                link.solve();
            }
        }
    }

    public void setDEBUG(boolean debug){
        DEBUG = debug;
    }
    public void run(SubScene stage){
        stage.setRoot(group);
        Manifold manifold = new Manifold();


        new AnimationTimer(){
            final double dt = 6 * 1./50;
            private double t = 0;
            @Override
            public void handle(long currentNanoTime) {
                for(int j = 0;j < 4;j++) {
                    if (!DEBUG) {
                        Utility_Functions.sortOSX(blocklist);
                        Utility_Functions.sortOSY(blocklist);
                        t += dt / 8;
                        for (Point point : pointList) {
                            point.run(dt);
                        }
                        for (Stool stool : stoolList) {
                            if(stool.isLife()) {
                                stool.run(t);
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            makeLink();
                            makeCollision(manifold);
                        }
                        for (Block block : blocklist) {
                            block.update();
                        }
                    }
                }

            }
        }.start();

        for(Point point : pointList){
            group.getChildren().add(point.circle);
        }

    }

    public Block addBlock(Point2D one, double weight, double height, double mass, Paint paint){
        final double n = 4;
        final double rad = 2;


        Point2D two = new Point2D(one.getX() + weight, one.getY());
        Point2D three = new Point2D(one.getX() + weight,one.getY() + height);
        Point2D four = new Point2D(one.getX(), one.getY() + height);

        Point onePoint = new Point(new Circle(one.getX(),one.getY(),rad));
        Point twoPoint = new Point(new Circle(two.getX(),two.getY(),rad));
        Point threePoint = new Point(new Circle(three.getX(),three.getY(),rad));
        Point fourPoint = new Point(new Circle(four.getX(),four.getY(),rad));

        Block newBlock = new Block(onePoint,twoPoint,threePoint,fourPoint,mass, (int) n);
        newBlock.getAllPointList().add(onePoint);
        newBlock.getAllPointList().add(twoPoint);
        newBlock.getAllPointList().add(threePoint);
        newBlock.getAllPointList().add(fourPoint);



        // up
        Point point,point1;
        Point2D pointk;
        point = onePoint;

        for(int i =1; i <= n;i++) {
            if(i == n){
                point1 = twoPoint;
            }else {
                pointk = new Point2D(one.getX() + i * weight / n, two.getY());
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, weight / n));
            //addPoint(point1,onePoint,twoPoint,threePoint,fourPoint);
            point = point1;
        }

        // right
        point = twoPoint;

        for(int i =1; i <= n;i++) {
            if(i == n){
                point1 = threePoint;
            }else {
                pointk = new Point2D(two.getX(), two.getY() + i * height / n);
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, height / n));
            point = point1;
        }

        // down
        point = threePoint;

        for(int i =1; i <= n;i++) {
            if(i == n){
                point1 = fourPoint;
            }else {
                pointk = new Point2D(three.getX() - i * weight/n, three.getY());
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, weight / n));
            point = point1;
        }

        // left
        point = fourPoint;

        for(int i =1; i <= n;i++) {
            if(i == n){
                point1 = onePoint;
            }else {
                pointk = new Point2D(four.getX() , four.getY() - i * height/n);
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                newBlock.getAllPointList().add(point1);
            }

            linkList.add(new Link(point1.circle, point.circle, height / n));
            point = point1;
        }


        double diag_value = onePoint.getPos().subtract(threePoint.getPos()).magnitude();

        pointList.add(onePoint);
        pointList.add(twoPoint);
        pointList.add(threePoint);
        pointList.add(fourPoint);

        Link up = new Link(onePoint.circle,twoPoint.circle,weight);
        Link right = new Link(twoPoint.circle, threePoint.circle,height);
        Link down = new Link(threePoint.circle,fourPoint.circle,weight);
        Link left = new Link(fourPoint.circle, onePoint.circle,height);
        Link diagLeft = new Link(twoPoint.circle,fourPoint.circle,diag_value);



        blocklist.add(newBlock);
        linkList.add(up);
        linkList.add(right);
        linkList.add(down);
        linkList.add(left);
        linkList.add(diagLeft);

        newBlock.getPolygon().setFill(paint);
        group.getChildren().add(newBlock.getPolygon());

        return newBlock;
    }
    private void addPoint(Point point, Point onePoint, Point twoPoint, Point threePoint, Point fourPoint){
        linkList.add(new Link(point.circle, fourPoint.circle, point.getPos().subtract(fourPoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, threePoint.circle, point.getPos().subtract(threePoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, twoPoint.circle, point.getPos().subtract(twoPoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, onePoint.circle, point.getPos().subtract(onePoint.getPos()).magnitude()));
    }

}