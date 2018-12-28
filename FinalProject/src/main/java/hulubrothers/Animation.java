package hulubrothers;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;

import static java.lang.Thread.sleep;

public class Animation {
    private PathTransition pt;
    private AnchorPane Root;

    Animation(AnchorPane root) {
        Root = root;
        pt = new PathTransition();
    }

    public void CreatureMoveAnimation(ImageView image, int from_x, int from_y, int to_x, int to_y, int speed) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    javafx.scene.shape.Path path = new javafx.scene.shape.Path();
                    path.getElements().add(new MoveTo(from_x, from_y));
                    path.getElements().add(new LineTo(to_x, to_y));
                    PathTransition pt = new PathTransition();
                    pt.setDuration(Duration.millis(speed));
                    pt.setPath(path);
                    pt.setNode(image);
                    pt.setAutoReverse(false);
                    pt.play();
                }
            });
            sleep(speed + 10);
        } catch (Exception e) {
            System.out.println("thread interrupted");
        }
    }

    public void NormalAttackAnimation(String Url, int from_x, int from_y, int to_x, int to_y, int speed) {
        ImageView NormalATK = new ImageView(this.getClass().getResource(Url).toExternalForm());
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    NormalATK.setFitHeight(60);
                    NormalATK.setFitWidth(60);
                    NormalATK.setVisible(true);
                    NormalATK.setX(from_x);
                    NormalATK.setY(from_y);
                    Root.getChildren().add(NormalATK);
                    javafx.scene.shape.Path path = new javafx.scene.shape.Path();
                    path.getElements().add(new MoveTo(from_x, from_y));
                    path.getElements().add(new LineTo(to_x, to_y));
                    PathTransition pt = new PathTransition();
                    pt.setDuration(Duration.millis(speed));
                    pt.setPath(path);
                    pt.setNode(NormalATK);
                    pt.setAutoReverse(false);
                    pt.play();
                }
            });
            sleep(speed + 10);
        } catch (Exception e) {
            System.out.println("thread interrupted");
        }

        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Root.getChildren().remove(NormalATK);
                }
            });
            sleep(10);
        } catch (Exception e) {
            System.out.println("thread interrupted");
        }
    }
}
