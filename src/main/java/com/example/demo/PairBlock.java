package com.example.demo;

import javafx.geometry.Point2D;

public class PairBlock {
    private final Block one;
    private final Block two;
    private Point2D intersection;
    private final Manifold manifold;
    private final boolean right;
    PairBlock(Block one, Block two, boolean right){
        this.one = one;
        this.two = two;
        this.right = right;
        this.manifold = new Manifold(one,two);
        Utility_Functions.bindBlocks(one, two);
    }
    public void run(Block block){
        if(right) {
            if(block == one) {
                updateRightLeg(one,two);
            }else if (block == two){
                updateRightLeg(two,one);
            }
        }else{
            if(block == one) {
                updateLeftLef(one,two);
            }else if (block == two){
                updateLeftLef(two,one);
            }

        }
    }

    private void SolveCollision(){
        manifold.solveCollision();
        manifold.applyImpulse();
    }
    private void updateLeftLef(Block one, Block two){
        SolveCollision();
        Point2D point2D = one.getXY().subtract(two.getXY());
        two.getRectangle().setX(two.getRectangle().getX() + point2D.getX());
        two.getRectangle().setY(two.getRectangle().getY() + point2D.getY());
    }
    private void updateRightLeg(Block one, Block two){
        SolveCollision();
        Point2D point2D = one.getPoints().get(1).subtract(two.getPoints().get(1));
        two.getRectangle().setX(two.getRectangle().getX() + point2D.getX());
        two.getRectangle().setY(two.getRectangle().getY() + point2D.getY());
    }
    public boolean hasBlock(Block block){
        return (one == block) ||(block == two) ;
    }
}
