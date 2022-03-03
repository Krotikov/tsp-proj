package entity;

import math.Vec2;
import java.util.ArrayList;

public class Polygon{
    public ArrayList<Vec2> p = new ArrayList<>(); // transformed points
    public ArrayList<Vec2> o = new ArrayList<>(); // Model of shape (original form)
    public Vec2 pos;                              // Center pos

    public float angle;                           // direction of the shape
    public boolean overlap = false;               // indicate if overlap has occurred
}
