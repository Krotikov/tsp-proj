package collision;

import entity.Polygon;
import math.Vec2;


public class utilityFunctions {

    // Проверка на пересечение, задача нового направления
   public static boolean shapeOverlapDiag(Polygon lhs, Polygon rhs){
        Polygon poly1 = lhs;
        Polygon poly2 = rhs;

        // Нужно проверить обе фигуры
        for (int shape = 0; shape < 2; shape++){

            if (shape == 1){
                poly1 = rhs;
                poly2 = lhs;
            }

            for (int p = 0; p < poly1.p.size(); p++){
                Vec2 lineR1s  = poly1.pos;
                Vec2 lineR1e = poly1.p.get(p);
                Vec2 displacement = new Vec2(0, 0);

                for (int q = 0; q < poly2.p.size(); q++){
                    Vec2 lineR2s = poly2.p.get(q);
                    Vec2 lineR2e = poly2.p.get((q + 1) % poly2.p.size());

                    // Standard "off the shelf" line intersection
                    double h = (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR1e.getY()) -
                            (lineR1s.getX() - lineR1e.getX()) * (lineR2e.getY() - lineR2s.getY());
                    double t1 = ((lineR2s.getY() - lineR2e.getY()) * (lineR1s.getX() - lineR2s.getX()) +
                            (lineR2e.getX() - lineR2s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;
                    double t2 = ((lineR1s.getY() - lineR1e.getY()) * (lineR1s.getX() - lineR2s.getX()) +
                            (lineR1e.getX() - lineR1s.getX()) * (lineR1s.getY() - lineR2s.getY())) / h;


                    if (t1 >= 0.0 && t1 < 1.0 && t2 >= 0 && t2 < 1.0){
                        displacement.setX(displacement.getX() + (1 - t1) * (lineR1e.getX() - lineR1s.getX()));
                        displacement.setY(displacement.getY() + (1 - t1) * (lineR1e.getY() - lineR1s.getY()));
                    }
                }

                // re-estimate pos
                displacement.mulVec2(shape == 0 ? -1 : 1);
                lhs.pos.plusVec2(displacement);
            }

        }

        return false;
    }
}
