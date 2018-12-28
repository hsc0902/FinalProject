package hulubrothers;

import javafx.scene.control.ProgressBar;

public class ColoredProgressBar extends ProgressBar {
    ColoredProgressBar(String styleClass, double progress) {
        super(progress);
        getStyleClass().add(styleClass);
    }
}
