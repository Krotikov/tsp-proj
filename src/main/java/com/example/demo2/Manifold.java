package com.example.demo2;


import javafx.geometry.Point2D;

import java.util.List;

public class Manifold {
    public Block A;
    public Block B;

    public Manifold(Block A, Block B){
        this.A = A;
        this.B = B;
    }

    public void solveCollision(){ // Generate contact information

    }

    public void applyImpulse(){  // solve impulse

    }

    public void posCorrection(){ // correcting position after collision and impulse applying

    }


    public Point2D normal;           // From A to B
    public List<Point2D> contacts;
    public double displacement;     // Depth of collision
}
