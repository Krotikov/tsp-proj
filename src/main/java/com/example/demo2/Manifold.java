package com.example.demo2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

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
        //contacts = Utility_Functions.IntersectsPoints(A,B);
        contacts = Utility_Functions.intersects(A, B);
        Pair<Double,Point2D> length_and_normal1 = FindAxisLeastPenetration(A,B);
        Pair<Double,Point2D> length_and_normal2 = FindAxisLeastPenetration(B,A);


        // choose max deep
        if (length_and_normal1.getKey() > length_and_normal2.getKey()) {
            normal = length_and_normal1.getValue();
            displacement = length_and_normal1.getKey();
        }
        else {
            normal = length_and_normal2.getValue();
            displacement = length_and_normal2.getKey();
            normal = normal.multiply(-1);

        }
        displacement = -displacement;
    }

    /*
    * Get os of Inersaption
    * */
    Pair<Double,Point2D> FindAxisLeastPenetration(Block blockA, Block blockB){
        List<Point2D> normals = blockA.getNormals();
        List<Point2D> pointsA = Utility_Functions.getPoints(blockA);
        List<Point2D> pointsB = Utility_Functions.getPoints(blockB);

        short BestIndex = -1;
        double BestDistance = -Double.POSITIVE_INFINITY;
        double dist;

        for(short i =0;i < normals.size();i++){
            //  получаем нормаль
            Point2D normal = normals.get(i);

            // опорная точка по нормале в блоке B
            Point2D s = GetSupport(normal.multiply(-1),pointsB);

            // вершина на ребре блока A
            Point2D v = pointsA.get(i);

            dist = normal.dotProduct(s.subtract(v));


            // наибольшее расстояние
            if(dist > BestDistance){
                BestDistance = dist;
                BestIndex = i;
            }
        }
        return new Pair<>(BestDistance,normals.get(BestIndex));
    }

    /*
    * extreme point on normal
    * */
    private Point2D GetSupport(Point2D direction,List<Point2D> points)
    {
        if(points == null || points.size() == 0){
            return null;
        }
        double BestDot = direction.dotProduct(points.get(0));
        Point2D BestPoint = points.get(0);
        double dot;
        for(Point2D point2D : points){
            dot = direction.dotProduct(point2D);
            if(dot > BestDot){
                BestDot = dot;
                BestPoint = point2D;
            }
        }
        return BestPoint;
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
        final double percent = 1; // [0.2; 0.8] to correction pos
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