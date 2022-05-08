package Project.modules.Physics;

import javafx.geometry.Point2D;
import javafx.scene.Group;

public class Camera {
    private Point point = null;
    private Point2D startPos;
    private Group group = null;
    private Point2D startDeltaPos= new Point2D(0,0);


    public void setGroup(Group group){
        this.group = group;
    }
    public Group getGroup() {
        return group;
    }
    public void bindPoint(Point point){
        this.point = point;
        this.startPos = point.getPos();
        this.startDeltaPos = new Point2D(group.getScene().getWidth()/2 - startPos.getX() - 40,group.getScene().getHeight()/2 - startPos.getY() - 200 );
        update();
    }
    public Camera(Game game){
        game.setCamera(this);
    }
    public void update(){
        if(point != null && group != null) {
            Point2D pos = point.getPos();
            group.setTranslateX(-(pos.getX() - startPos.getX() - startDeltaPos.getX()));
            //group.setTranslateY(-(pos.getY() - startPos.getY() - startDeltaPos.getY()));
        }
    }

}
