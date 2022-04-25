package Project.modules.Physics;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.util.List;

public class Manifold {
    public Block A;
    public Block B;
    public boolean isCollide = true;

    public Manifold(Block A, Block B){
        this.A = A;
        this.B = B;
        isCollide = !A.bindBlocks.contains(B);


        sf = Math.sqrt(A.physics_model.sFriction * A.physics_model.sFriction + B.physics_model.sFriction * B.physics_model.sFriction);
        df = Math.sqrt(A.physics_model.dFriction * A.physics_model.dFriction + B.physics_model.dFriction * B.physics_model.dFriction);
        e = Math.min(A.physics_model.restitution, B.physics_model.restitution);
    }

    public void solveCollision(){ // Generate contact information
        normal = new Point2D(0,0);
        displacement = 0;
        contacts = Utility_Functions.IntersectsPoints(A, B);

        // TODO: watch here!
        A.physics_model.contacts = contacts;

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
        List<Point2D> pointsA = blockA.getPoints();
        List<Point2D> pointsB = blockB.getPoints();

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
        return new Pair<>(BestDistance, normals.get(BestIndex));
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

    // TODO: Исправить эту функцию
    public void applyImpulse(){  // solve impulse
        // We need coordinates of center mass
        Point2D centerA = A.CenterBlock();
        Point2D centerB = B.CenterBlock();

        for (Point2D contact : contacts){
            Point2D RA = contact.subtract(centerA);
            Point2D RB = contact.subtract(centerB);
            final double inertiaA = A.physics_model.inertia;
            final double inertiaB = B.physics_model.inertia;

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
                    + RACrossN * RACrossN / inertiaA
                    + RBCrossN * RBCrossN / inertiaB;

            double j = -(1.0 + e) * RVContact / iMassSum;
            j /= contacts.size();

            Point2D impulse = normal.multiply(j);
            A.physics_model.applyImpulse(impulse.multiply(-1.0), RA, inertiaA);
            B.physics_model.applyImpulse(impulse, RB, inertiaB);

            // friction impulse
            RV = getResultSpeed(RA, RB);
            Point2D tang = RV.subtract(normal.multiply(RV.dotProduct(normal)));
            tang = tang.normalize();

            // j for tangent force
            double jt = -(RV.dotProduct(tang)) / iMassSum;
            jt /= contacts.size();

            if (Math.abs(jt) < 0.1){
                return;
            }

            Point2D tangentImpulse = Math.abs(jt) < j * sf ? tang.multiply(jt) : tang.multiply(-j * df);


            A.physics_model.applyImpulse(tangentImpulse.multiply(-1.0), RA, inertiaA);
            B.physics_model.applyImpulse(tangentImpulse, RB, inertiaB);
        }
    }

    public void posCorr()  {
        final Point2D norm = normal.multiply(-displacement);
        final Point2D start_point = new Point2D(A.getRectangle().getX(), A.getRectangle().getY());
        Point2D new_norm = new Point2D(start_point.getX() + norm.getX(), start_point.getY() + norm.getY());
        A.physics_model.setPosition(new_norm);

    }

    private Point2D getResultSpeed(final Point2D RA, final Point2D RB){
        Point2D tempB = B.physics_model.velocity.add(Utility_Functions.Cross(B.physics_model.wVelocity, RB));
        Point2D tempA = A.physics_model.velocity.add(Utility_Functions.Cross(A.physics_model.wVelocity, RA));

        return tempB.subtract(tempA);
    }

    private final double e;
    private final double sf;
    private final double df;
    public Point2D normal;           // From A to B
    public List<Point2D> contacts;
    public double displacement;     // Depth of collision
}