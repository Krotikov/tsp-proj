package com.example.demo;

import javafx.geometry.Point2D;

public class Stool {
    private final Block body;
    private final Block leftLeg;

    private Point2D Point;
    private final Manifold manileft;
    private final boolean right;

    Stool(final Block body, final  Block leftLeg, boolean right){
        this.right = right;
        this.body = body;
        this.leftLeg = leftLeg;
        this.manileft = new Manifold(leftLeg,body);

        Utility_Functions.bindBlocks(body, leftLeg);
    }


    void run(Block block, double t){
           if(right) {
               if(block == leftLeg) {
                   updateRightLeg(leftLeg,body);
               }else if (block == body){
                   updateRightLeg(body,leftLeg);
               }
           }else{
               if(block == leftLeg) {
                   updateLeftLef(leftLeg,body);
               }else if (block == body){
                   updateLeftLef(body,leftLeg);
               }

        }
    }

    void updateLeftLef(Block one, Block two){
        manileft.solveCollision();
        manileft.applyImpulse();

        Point2D point2D = one.getXY().subtract(two.getXY());
        two.getRectangle().setX(two.getRectangle().getX() + point2D.getX());
        two.getRectangle().setY(two.getRectangle().getY() + point2D.getY());
    }
    void updateRightLeg(Block one, Block two){
        manileft.solveCollision();
        manileft.applyImpulse();
        Point2D point2D = one.getPoints().get(1).subtract(two.getPoints().get(1));
        two.getRectangle().setX(two.getRectangle().getX() + point2D.getX());
        two.getRectangle().setY(two.getRectangle().getY() + point2D.getY());
    }






    public boolean hasBlock(Block block){
        return (leftLeg == block) ||(block == body) ;
    }

    public Block getBody(){
        return body;
    }

    public Block getLeftLeg() {
        return leftLeg;
    }


}
