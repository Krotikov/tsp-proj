package entity;

import javafx.scene.paint.Paint;
import math.Vec2;

// TODO: В данный класс нужно добавить все необходимые параметры (гравитационная постоянная и пр.)

public class Block {
    private final Polygon polygon = new Polygon();

    public Block(final Vec2 center, final Vec2 size, final Paint paint){
        polygon.pos = center;
        double x = size.getX() / 2;
        double y = size.getY() / 2;

        polygon.o.add(new Vec2(-x, -y)); polygon.p.add(new Vec2(-x, -y));
        polygon.o.add(new Vec2(-x, y)); polygon.p.add(new Vec2(-x, y));
        polygon.o.add(new Vec2(x, y));  polygon.p.add(new Vec2(x, y));
        polygon.o.add(new Vec2(x, -y)); polygon.p.add(new Vec2(x, -y));
        polygon.angle = 0.0f;
    }

    public Polygon getPolygon(){
        return polygon;
    }
}

// structure for metrics
