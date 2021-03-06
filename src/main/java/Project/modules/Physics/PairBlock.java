package Project.modules.Physics;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class PairBlock {
    private final Block one;
    private final Block two;
    private final Point2D diffPointOne;
    private final Point2D diffPointTwo;
    private final double priAngleOne;
    private final double priAngleTwo;
    private final Manifold manifold;
    PairBlock(Block one, Block two, Point2D intersection){
        Point2D oneXY = one.getXY();
        Point2D twoXY = two.getXY();

        this.one = one;
        this.two = two;

        // start angle
        this.priAngleOne = one.getRectangle().getRotate();
        this.priAngleTwo = two.getRectangle().getRotate();

        // difference of two points
        this.diffPointOne = intersection.subtract(oneXY);
        this.diffPointTwo = intersection.subtract(twoXY);

        // save intersection point

        // for processing collision
        this.manifold = new Manifold(one,two);
        Utility_Functions.bindBlocks(one, two);

    }
    private void applyImpulse(Block A, Block B){
        double wb =( A.physics_model.inertia * A.physics_model.wVelocity + B.physics_model.inertia*A.physics_model.wVelocity)/
                (A.physics_model.inertia + B.physics_model.inertia);
        Point2D newV = (A.physics_model.velocity.multiply(A.physics_model.mass).add(B.physics_model.velocity.multiply(B.physics_model.mass)));
        newV = newV.multiply(1/(A.physics_model.mass + B.physics_model.mass));


        A.physics_model.wVelocity = wb;
        B.physics_model.wVelocity = -wb;
        A.physics_model.velocity = newV;
        B.physics_model.velocity = newV;

    }
    public void run(Block block){
        if(hasBlock(block)){


                final double eps = 20;


                final Block this_Block = block;

                // for rotate vector
                Rotate rotate;

                // new intersect point for one
                rotate = new Rotate(one.getRectangle().getRotate() - priAngleOne);
                // get total new intersect
                Point2D newIntersectPosition1 = one.getXY().add(rotate.transform(diffPointOne));


                // new intersect point for two
                rotate = new Rotate(two.getRectangle().getRotate() - priAngleTwo);
                // get total new intersect
                Point2D newIntersectPosition2 = two.getXY().add(rotate.transform(diffPointTwo));

                // vector of bias
                Point2D totalInter;

                manifold.solveCollision();
                manifold.applyImpulse();

                if (block == one) {
                    totalInter = newIntersectPosition1.subtract(newIntersectPosition2);
                    block = two;
                } else {
                    totalInter = newIntersectPosition2.subtract(newIntersectPosition1);
                    block = one;
                }


                // set new poss
                block.getRectangle().setX(block.getRectangle().getX() + totalInter.getX());
                block.getRectangle().setY(block.getRectangle().getY() + totalInter.getY());

                // give an impulse along the chain
                block.testRun(this_Block);

        }
    }

    private void SolveCollision(){
        manifold.solveCollision();
        manifold.applyImpulse();
    }

    public boolean hasBlock(Block block){
        return (one == block) ||(block == two) ;
    }
}
