package Project.modules.Physics;

import Project.modules.evolution.Evolution;
import Project.modules.evolution.EvolutionAlg;
import Project.modules.evolution.score.Score;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


enum SHAPES {
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
    private static Evolution algorithm;
    private final Group group = new Group();
    private Camera camera = null;
    private Block platform;
    boolean DEBUG = false;
    private static final double MAX_TIME = 20;

    public static Evolution getAlgorithm() {
        if (algorithm == null) {
            algorithm = EvolutionAlg.getInstance();
        }
        return algorithm;
    }

    private void setWorldForView() {
        final int weight = 50;
        final int high = 100;
        final int startInt = 1000;
        Rectangle rectangle;
        Text text;
        Font font = new Font(30);
        for (int i = 0; i < 100; i++) {
            rectangle = new Rectangle(startInt + i * 1000, platform.getXY().getY() - high, weight, high);
            rectangle.setFill(Color.BROWN);
            text = new Text(rectangle.getX() - 10, rectangle.getY() - 10, Integer.toString((startInt + i * 1000) / 100));
            text.setFill(Color.GREEN);
            text.setFont(new Font(30));
            group.getChildren().add(rectangle);
            group.getChildren().add(text);
        }
    }

    public void setStool(Stool stool) {
        stoolList.add(stool);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<Score> TrainRun() {
        Manifold manifold = new Manifold();
        final double dt = 6 * 1. / 50;
        double t = 0;
        boolean life;
        while (t < MAX_TIME) {
            Utility_Functions.sortOSX(blocklist);
            Utility_Functions.sortOSY(blocklist);
            t += dt / 8.0;
            for (Point point : pointList) {
                point.run(dt);
            }
            life = true;
            for (Stool stool : stoolList) {
                if (stool.isLife()) {
                    stool.run(t);
                    life = false;
                }
            }
            if (life) {
                break;
            }
            for (int i = 0; i < 7; i++) {
                makeLink();
                makeCollision(manifold);
            }
            makeLink();
        }
        List<Score> results = new ArrayList<>();
        for (Stool stool : stoolList) {
            var dist = stool.getDist();
            results.add(new Score(dist >= 0 ? dist : 0, t));
        }
        return results;
    }

    public void initObjects() {
        platform = addBlock(
                new Point2D(-500, 500),
                1000000,
                100,
                1000000,
                Color.GREEN
        );
        platform.setWeightlessness();
        platform.switchPowers();
        Utility_Functions.bindStools(stoolList);

    }

    private void makeCollision(Manifold manifold) {
        for (int k1 = 0; k1 < blocklist.size(); k1++) {
            for (int k2 = 0; k2 < blocklist.size(); k2++) {
                if (k1 == k2) {
                    continue;
                }
                if (Utility_Functions.IntersectsPoints(blocklist.get(k1), blocklist.get(k2)).size() > 0) {
                    manifold.setBlocks(blocklist.get(k1), blocklist.get(k2));
                    if (manifold.isCollide) {
                        manifold.solveCollision();
                        manifold.posCorr();
                    }
                }
            }
        }
    }

    private void makeLink() {
        for (int i = 0; i < 5; i++) {
            for (Link link : linkList) {
                link.solve();
            }
        }
    }

    public void setDEBUG(boolean debug) {
        DEBUG = debug;
    }

    public void run(SubScene stage) {
        stage.setRoot(group);
        setWorldForView();
        Manifold manifold = new Manifold();

        if (camera != null) {
            camera.setGroup(group);
        }


        new AnimationTimer() {
            final double dt = 6 * 1. / 50;
            private double t = 0;

            @Override
            public void handle(long currentNanoTime) {
                for (int j = 0; j < 4; j++) {
                    if (!DEBUG) {
                        Utility_Functions.sortOSX(blocklist);
                        Utility_Functions.sortOSY(blocklist);

                        t += dt / 8.0;

                        makeLink();

                        for (Point point : pointList) {
                            point.run(dt);
                        }
                        for (Stool stool : stoolList) {
                            if (stool.isLife()) {
                                stool.run(t);
                            }
                        }
                        for (int i = 0; i < 7; i++) {
                            makeLink();
                            makeCollision(manifold);
                        }
                        makeLink();

                        // update camera pos
                        if (camera != null) {
                            camera.update();
                        }
                        // paint
                        for (Block block : blocklist) {
                            block.update();
                        }
                    }
                }

            }
        }.start();

        for (Point point : pointList) {
            group.getChildren().add(point.circle);
        }

    }

    public Block addBlock(Point2D one, double weight, double height, double mass, Paint paint) {
        final double n = 4;
        final double rad = 0;


        Point2D two = new Point2D(one.getX() + weight, one.getY());
        Point2D three = new Point2D(one.getX() + weight, one.getY() + height);
        Point2D four = new Point2D(one.getX(), one.getY() + height);

        Point onePoint = new Point(new Circle(one.getX(), one.getY(), rad));
        Point twoPoint = new Point(new Circle(two.getX(), two.getY(), rad));
        Point threePoint = new Point(new Circle(three.getX(), three.getY(), rad));
        Point fourPoint = new Point(new Circle(four.getX(), four.getY(), rad));

        Block newBlock = new Block(onePoint, twoPoint, threePoint, fourPoint, mass, (int) n);
        newBlock.getAllPointList().add(onePoint);


        // up
        Point point, point1;
        Point2D pointk;
        point = onePoint;

        for (int i = 1; i <= n; i++) {
            if (i == n) {
                point1 = twoPoint;
            } else {
                pointk = new Point2D(one.getX() + i * weight / n, two.getY());
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                addPoint(point1,onePoint,twoPoint,threePoint,fourPoint);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, weight / n));
            point = point1;
        }

        newBlock.getAllPointList().add(twoPoint);

        // right
        point = twoPoint;

        for (int i = 1; i <= n; i++) {
            if (i == n) {
                point1 = threePoint;
            } else {
                pointk = new Point2D(two.getX(), two.getY() + i * height / n);
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                addPoint(point1,onePoint,twoPoint,threePoint,fourPoint);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, height / n));
            point = point1;
        }

        newBlock.getAllPointList().add(threePoint);

        // down
        point = threePoint;

        for (int i = 1; i <= n; i++) {
            if (i == n) {
                point1 = fourPoint;
            } else {
                pointk = new Point2D(three.getX() - i * weight / n, three.getY());
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                addPoint(point1,onePoint,twoPoint,threePoint,fourPoint);
                newBlock.getAllPointList().add(point1);
            }
            linkList.add(new Link(point1.circle, point.circle, weight / n));
            point = point1;
        }

        newBlock.getAllPointList().add(fourPoint);
        // left
        point = fourPoint;

        for (int i = 1; i <= n; i++) {
            if (i == n) {
                point1 = onePoint;
            } else {
                pointk = new Point2D(four.getX(), four.getY() - i * height / n);
                point1 = new Point(new Circle(pointk.getX(), pointk.getY(), rad));
                pointList.add(point1);
                addPoint(point1,onePoint,twoPoint,threePoint,fourPoint);
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

        Link up = new Link(onePoint.circle, twoPoint.circle, weight);
        Link right = new Link(twoPoint.circle, threePoint.circle, height);
        Link down = new Link(threePoint.circle, fourPoint.circle, weight);
        Link left = new Link(fourPoint.circle, onePoint.circle, height);
        Link diagLeft = new Link(twoPoint.circle, fourPoint.circle, diag_value);


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

    private void addPoint(Point point, Point onePoint, Point twoPoint, Point threePoint, Point fourPoint) {
        linkList.add(new Link(point.circle, fourPoint.circle, point.getPos().subtract(fourPoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, threePoint.circle, point.getPos().subtract(threePoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, twoPoint.circle, point.getPos().subtract(twoPoint.getPos()).magnitude()));
        linkList.add(new Link(point.circle, onePoint.circle, point.getPos().subtract(onePoint.getPos()).magnitude()));
    }
}