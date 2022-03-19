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
        contacts = Utility_Functions.intersects(A, B);
        List<UserPair> PointsMapA = Utility_Functions.getIntersectsPointListOnePoint(contacts,A);
        List<UserPair> PointsMapB = Utility_Functions.getIntersectsPointListOnePoint(contacts,B);
        List<Point2D> Points = Utility_Functions.IntersectsPoints(A, B);

        System.out.println(Points);
        System.out.println(PointsMapA);
        System.out.println(PointsMapB);

        if(PointsMapA != null && PointsMapA.size() != 0 && PointsMapB != null && PointsMapB.size() != 0){
            // относительно блока А
            Point2D A = PointsMapA.get(0).getKEY1();
            Point2D B = PointsMapB.get(0).getKEY1();
            Point2D C = PointsMapA.get(0).getVALUE1();
            Point2D K = PointsMapA.get(0).getVALUE2();
            Vec2 CB = new Vec2(C,B);
            Vec2 KB = new Vec2(C,B);
            CB.calculate();
            KB.calculate();
            Point2D normal1 = CB.NormalVec(A);
            Point2D normal2 = CB.NormalVec(K);
            double depth1 = CB.DistanceVecPoint(A);
            double depth2 = CB.DistanceVecPoint(K);
            if(Math.abs(normal1.getY()) < Math.abs(normal2.getY()) ){
                normal1 = normal2;
                depth1 = depth2;
            }

            Point2D normal11 = KB.NormalVec(A);
            Point2D normal12 = KB.NormalVec(C);
            double depth11 = KB.DistanceVecPoint(A);
            double depth12 = KB.DistanceVecPoint(C);

            if(Math.abs(normal11.getY()) < Math.abs(normal12.getY()) ){
                normal11 = normal12;
                depth11 = depth12;
            }
            Point2D nrml;
            double depth;
            if(Math.abs(normal11.getY()) < Math.abs(normal1.getY()) ){
                nrml = normal11;
                depth = depth11;
            }else{
                nrml = normal1;
                depth = depth1;
            }
            normal = nrml.normalize();
           // normal = normal.multiply(-1);
            displacement = depth;

        }else if(PointsMapA != null && PointsMapA.size() == 1) {
            // относительно первого рассматриваем
            Point2D B = PointsMapA.get(0).getKEY1();
            Point2D A = PointsMapA.get(0).getVALUE1();
            Point2D C = PointsMapA.get(0).getVALUE2();
            Vec2 AC = new Vec2(A, C);
            AC.calculate();
            normal = AC.NormalVec(B).normalize();
            displacement = AC.DistanceVecPoint(B);
        }else if(PointsMapB != null && PointsMapB.size() == 1) {
            // относительно
            Point2D D = PointsMapB.get(0).getKEY1();
            Point2D A = PointsMapB.get(0).getVALUE1();
            Point2D C = PointsMapB.get(0).getVALUE2();
            Vec2 AC = new Vec2(A,C);
            AC.calculate();
            normal = AC.NormalVec(D).normalize();
            displacement = AC.DistanceVecPoint(D);
            System.out.println(normal);
        }else if (Points.size() == 2){
            Point2D A = Points.get(0);
            Point2D B = Points.get(1);
            Vec2 AB = new Vec2(A,B);
            AB.calculate();
            Point2D C = contacts.get(0);
            Point2D D = contacts.get(1);
            Point2D CD = C.add(D).multiply(0.5);
            normal = AB.NormalVec(CD).normalize();
            displacement = AB.DistanceVecPoint(C);
            normal = normal.multiply(-1.0);
        }

        System.out.println(displacement);
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
        A.physics_model.setPosition(new Point2D(A.getRectangle().getX() - dist.multiply(iMassA).getX(), A.getRectangle().getY() - dist.multiply(iMassA).getY()));
        B.physics_model.setPosition(new Point2D(B.getRectangle().getX() - dist.multiply(iMassB).getX(), B.getRectangle().getY() - dist.multiply(iMassB).getY()));
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
