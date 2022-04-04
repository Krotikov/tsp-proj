package com.example.demo;

import javafx.geometry.Point2D;

public class Stool {
    private final Block body;
    private final Block leftLeg;
    private final Block rightLeg;

    Stool(final Block body, final  Block leftLeg, final Block rightLeg){
        this.body = body;
        this.leftLeg = leftLeg;
        this.rightLeg = rightLeg;

        Utility_Functions.bindBlocks(body, leftLeg);
        Utility_Functions.bindBlocks(body, rightLeg);
    }

    void run(){

    }

    public Block getBody(){
        return body;
    }

    public Block getLeftLeg() {
        return leftLeg;
    }

    public Block getRightLeg() {
        return rightLeg;
    }
}
