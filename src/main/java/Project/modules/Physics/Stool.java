package Project.modules.Physics;
import Project.modules.evolution.genome.LegGenome;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stool {
    static final Point2D startPoint = new Point2D(150, 260);
    static final double weightBody = 300;
    static final double heightBody = 50;
    static final double weightLeg = 50;
    static final double heightLeg = 200;
    public Color color = Color.AQUAMARINE;
    final Block leftLeg;
    final Block rightLeg;
    final Block body;
    final Game game;
    private Point2D normalDown;
    private boolean life = true;
    private final double startPos;
    // parameters
    private final List<Map<Character, Double>> genome;
    double alpha1 = 0.5;

    public Stool(Point2D point2D, double weightBody, double heightBody, double weightLeg, double heightLeg, Game game, Color color){
        this.color = color;
        this.startPos = point2D.getX();

        this.genome = new ArrayList<>();
        genome.add(legs.get("left").getGens());
        genome.add(legs.get("right").getGens());

        this.game = game;
        this.leftLeg = game.addBlock(
                new Point2D(point2D.getX(), point2D.getY()),
                weightLeg,
                heightLeg,
                50,
                color
        );
        this.rightLeg = game.addBlock(
                new Point2D(point2D.getX() + weightBody - weightLeg, point2D.getY()),
                weightLeg,
                heightLeg,
                50,
                color
        );
        this.body = game.addBlock(
                new Point2D(point2D.getX(), point2D.getY()),
                weightBody,
                heightBody,
                50,
                color
        );
        game.linkList.add(new Link(
                leftLeg.getPointList().get(0).circle,
                body.getPointList().get(3).circle,
                0
        ));
        game.linkList.add(new Link(
                body.getPointList().get(2).circle,
                rightLeg.getPointList().get(1).circle,
                0
        ));
        game.setStool(this);
        Utility_Functions.bindBlocks(leftLeg,body);
        Utility_Functions.bindBlocks(body,rightLeg);
        Utility_Functions.bindBlocks(leftLeg,rightLeg);
    }

    public Block getBody() {
        return body;
    }

    public Block getLeftLeg() {
        return leftLeg;
    }

    public Block getRightLeg() {
        return rightLeg;
    }

    public Color getColor() {
        return color;
    }


    double evaluateAt(Map<Character, Double> gens, double time) {
        return (gens.get('M') - gens.get('m')) / 2 * (1 + Math.sin((time + gens.get('o')) * Math.PI * 2 / gens.get('p'))) + gens.get('m');
    }


    public Stool(double param, Game game, Color color){
        this.color = color;
        setParameters(param);
        Point2D point2D = startPoint;
        this.genome = param;
        this.startPos = point2D.getX();
        this.game = game;
        this.leftLeg = game.addBlock(
                new Point2D(point2D.getX(), point2D.getY()),
                weightLeg,
                heightLeg,
                50,
                color
        );
        this.rightLeg = game.addBlock(
                new Point2D(point2D.getX() + weightBody - weightLeg, point2D.getY()),
                weightLeg,
                heightLeg,
                50,
                color
        );
        this.body = game.addBlock(
                new Point2D(point2D.getX(), point2D.getY()),
                weightBody,
                heightBody,
                50,
                color
        );
        game.linkList.add(new Link(
                leftLeg.getPointList().get(0).circle,
                body.getPointList().get(3).circle,
                0
        ));
        game.linkList.add(new Link(
                body.getPointList().get(2).circle,
                rightLeg.getPointList().get(1).circle,
                0
        ));
        game.setStool(this);
        Utility_Functions.bindBlocks(leftLeg,body);
        Utility_Functions.bindBlocks(body,rightLeg);
        Utility_Functions.bindBlocks(leftLeg,rightLeg);
    }

    void setParameters(double parameters) {
        this.alpha1 = parameters;
    }

    void run(double t){
        life = constraint();
        if(life) {
            normalDown = body.getNormals().get(2);
            double phi1 = evaluateAt(genome.get(0), t);
            double phi2 = evaluateAt(genome.get(1), t);
            // left leg run
            runLeg(phi1, leftLeg.getPointList().get(0),
                    leftLeg.getPointList().get(3)
            );

            // right leg run
            runLeg(phi2, rightLeg.getPointList().get(1),
                    rightLeg.getPointList().get(2)
            );
        }else{
            dieStool();
        }

    }
    private boolean constraint(){
        return PhysicModel.norms.get(1).dotProduct(body.getNormal(1)) >= -1;
    }
    public boolean isLife(){
        return life;
    }
    private void dieStool(){
        leftLeg.setWeightlessness();
        leftLeg.switchPowers();
        rightLeg.setWeightlessness();
        rightLeg.switchPowers();
        body.setWeightlessness();
        body.switchPowers();
    }

    public double getDist(){
        return body.getPointList().get(0).circle.getCenterX() - startPos;
    }
    public void runLeg(double phi,Point OS_P, Point CorrPos_P ){


        // os of rotate
        Point2D OS = OS_P.getPos();
        Point2D startPoint = OS.add(normalDown.multiply(leftLeg.getHeight()));
        Point2D delta = OS.subtract(new Point2D(0,0));

        // move OS to x = 0 y = 0 and rotate
        startPoint = startPoint.subtract(delta);

        // new coords
        double x_ = startPoint.getX() * Math.cos(phi) - startPoint.getY()*Math.sin(phi) + delta.getX();
        double y_ = startPoint.getX() * Math.sin(phi) + startPoint.getY()*Math.cos(phi) + delta.getY();

        // set new pos of point leg
        CorrPos_P.setPos(new Point2D(x_,y_));
    }
    public List<Block> getAllBlocks(){
        List<Block> blockList = new ArrayList<>();
        blockList.add(leftLeg);
        blockList.add(body);
        blockList.add(rightLeg);
        return blockList;
    }
}

