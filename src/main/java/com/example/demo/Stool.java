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

        body.connect(leftLeg,true);
        body.connect(RightLeg,false);
        Utility_Functions.bindBlocks(this.leftLeg,this.RightLeg);
    }

}
