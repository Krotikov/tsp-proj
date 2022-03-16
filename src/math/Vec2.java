package math;

// Двумерный вектор
public class Vec2 {
    private double x;
    private double y;

    public Vec2(){
        x = y = 0.0;
    }
    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public Vec2(final Vec2 rhs){
        x = rhs.x;
        y = rhs.y;
    }


   public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }

    public void mulX(double c){
        x *= c;
    }
    public void mulY(double c){
        y *= c;
    }
    public void mulVec2(double c){
        mulX(c);
        mulY(c);
    }

    public void plusVec2(final Vec2 rhs){
        x += rhs.x;
        y += rhs.y;
    }
    public void minusVec2(final Vec2 rhs){
        x -= rhs.x;
        y -= rhs.y;
    }
    public void mulCordVec2(final Vec2 rhs){
        x *= rhs.x;
        y *= rhs.y;
    }
    public double mulScalarVec2(final Vec2 rhs){
        return x * rhs.x + y * rhs.y;
    }

    public void rotationAngle(double angle){
        double x1 = x * Math.cos(angle) + y * Math.sin(angle);
        y = -x * Math.sin(angle) + y * Math.cos(angle);
        x = x1;
    }

   public Vec2 absVec2(){
        return new Vec2(Math.abs(x), Math.abs(y));
    }

    public double length(){
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 getNormalize(){
        double m = length();
        double x1 = x, y1 = y;

        if (m <= 0.0000000001){
            x1 = y1 = 0;
        }
        else{
            x1 /= m;
            y1 /= m;
        }

        return new Vec2(x1, y1);
    }

    // distance between vectors
    public static double distance(final Vec2 lhs, final Vec2 rhs){
        double x1 = lhs.x - rhs.x;
        double y1 = lhs.y - rhs.y;

        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    // some standard vectors
    public static final Vec2 DownVec2 = new Vec2(0, 1);
    public static final Vec2 RightVec2 = new Vec2(1, 0);
    public static final Vec2 LeftVec2 = new Vec2(-1, 0);
    public static final Vec2 ZeroVec2 = new Vec2(0, 0);
    public static final Vec2 UpVec2 = new Vec2(0, -1);

}
