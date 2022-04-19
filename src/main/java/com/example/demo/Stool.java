package com.example.demo;

import javafx.geometry.Point2D;

public class Stool {
    private final Block body;
    private final Block leftLeg;
    private final Block RightLeg;

    Stool(final Block body, final  Block leftLeg,final Block RightLeg){
        this.body = body;
        this.leftLeg = leftLeg;
        this.RightLeg = RightLeg;

        // connect with body in points
        body.connect(leftLeg,body.getPoints().get(0));
        body.connect(RightLeg,body.getPoints().get(1));

        leftLeg.name = "left";
        RightLeg.name = "right";
        body.name = "body";
        Utility_Functions.bindBlocks(this.leftLeg,this.RightLeg);
    }

}
