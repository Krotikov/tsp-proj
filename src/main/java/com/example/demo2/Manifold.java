package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Manifold {
    public Block A;
    public Block B;

    public Manifold(Block A, Block B){
        this.A = A;
        this.B = B;
        solveCollision();
    }

    private void solveCollision(){ // Generate contact information

    }

    public void applyImpulse(){  // solve impulse
        Rectangle rA = A.getRectangle();
        Rectangle rB = B.getRectangle();

        // We need coordinates of center mass
        Point2D centerA = Utility_Functions.CenterRectangle(rA);
        Point2D centerB = Utility_Functions.CenterRectangle(rB);

       for (Point2D contact : contacts){
           Point2D RA = contact.subtract(centerA);
           Point2D RB = contact.subtract(centerB);

           // Разрешающая скорость
           Point2D RV = getResultSpeed(RA, RB);
           double RVContact = RV.dotProduct(normal);
           // Если положительно, то точки удаляются, либо движение сонаправлено
           if (RVContact > 0) {
               return;
           }

           double RACrossN = RA.getX() * normal.getY() - RA.getY() * normal.getX();
           double RBCrossN = RB.getX() * normal.getY() - RB.getY() * normal.getX();

           double iMassSum = 1.0 / A.physics_model.mass + 1.0 / B.physics_model.mass
                   + RACrossN * RACrossN * (1.0 / A.physics_model.inertia)
                   + RBCrossN * RBCrossN * (1.0 / B.physics_model.inertia);

           double j = -2.0 * RVContact / iMassSum;
           j /= contacts.size();

           Point2D impulse = normal.multiply(j);
           A.physics_model.applyImpulse(impulse.multiply(-1.0), RA);
           B.physics_model.applyImpulse(impulse, RB);
       }
    }

    // correcting position after collision and impulse applying (Применять при необходимости)
    public void posCorrection(){
        final double percent = 0.4; // [0.2; 0.8] to correction pos
        final double slop = 0.01;   // [0.01; 0.1] our epsilon
        final double iMassA =  1.0 / A.physics_model.mass;
        final double iMassB =  1.0 / B.physics_model.mass;

        double tempCalc = Math.max(0.0, displacement - slop) / (iMassA + iMassB) * percent;
        Point2D dist = normal.multiply(tempCalc);

        // исправляем позиции
        A.getRectangle().setX(A.getRectangle().getX() - dist.multiply(iMassA).getX());
        A.getRectangle().setY(A.getRectangle().getY() - dist.multiply(iMassA).getY());
        B.getRectangle().setX(B.getRectangle().getX() + dist.multiply(iMassB).getX());
        B.getRectangle().setY(B.getRectangle().getY() + dist.multiply(iMassB).getY());
    }

    private Point2D getResultSpeed(final Point2D RA, final Point2D RB){
        Point2D tempB = B.physics_model.velocity.add(Utility_Functions.Cross(B.physics_model.wVelocity, RB));
        Point2D tempA = A.physics_model.velocity.add(Utility_Functions.Cross(A.physics_model.wVelocity, RA));

        return tempB.subtract(tempA);
    }


    public Point2D normal;           // From A to B
    public List<Point2D> contacts;
    public double displacement;     // Depth of collision
}
