package hulubrothers;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Sound extends Thread {
    private Media media;
    private MediaPlayer player;
    private long ls;
    String path;
    Sound(String path) {
        this.path=path;
        String url = getClass().getResource(path).toString();
        media = new Media(url);
        player = new MediaPlayer(media);
    }

    public int getTime() {
        return 400;
    }

    public void run() {
        try {
            player.setAutoPlay(true);
            player.setCycleCount(1);
            player.play();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
