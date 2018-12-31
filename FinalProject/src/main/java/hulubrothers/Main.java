package hulubrothers;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main extends Application {
    private World world;
    private AnchorPane root;
    public static Semaphore[] mutexi;
    public static int thread_num;
    public static int enter_flag = -1;
    public static ReadRecord readrecord;
    public static ArrayList<String> FileList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
            //root.getStylesheets().add(getClass().getResource("/MainStyle.css").toExternalForm());
            primaryStage.setTitle("My Application");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            root.requestFocus();
            world = new World(root);
            Animation animation = new Animation(root);

            root.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER && world.LoadTask.isDone() && (world.map.hulu_num == 0 || world.map.mon_num == 0) && enter_flag == -1) {
                        world.Clear();
                        root.setStyle("-fx-background-image: url("+"'/image/background_v2.jpg'"+")");
                        world.button.setVisible(true);
                        world.SetButtonText("数字键1-8选择葫芦娃的阵型，L键战斗回放");
                        FileList.clear();
                        enter_flag = 0;

                        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
                        int noThreads = currentGroup.activeCount();
                        Thread[] lstThreads = new Thread[noThreads];
                        currentGroup.enumerate(lstThreads);
                        System.out.println(noThreads);
                    }

                    if (event.getCode() == KeyCode.SPACE && enter_flag == 2) {
                        world.SetButtonText("Enter键重新开始,L键保存记录");
                        thread_num = world.getCreaturesNum();
                        mutexi = new Semaphore[thread_num];
                        mutexi[0] = new Semaphore(1);
                        for (int i = 1; i <= thread_num - 1; i++)
                            mutexi[i] = new Semaphore(0);
                        world.LetCreaturesRun(animation);
                        enter_flag = -1;

                        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
                        int noThreads = currentGroup.activeCount();
                        Thread[] lstThreads = new Thread[noThreads];
                        currentGroup.enumerate(lstThreads);
                        System.out.println(noThreads);
                    }

                    if (event.getCode() == KeyCode.L && (world.map.hulu_num == 0 || world.map.mon_num == 0) && enter_flag == 0) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("选择回放记录");
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            try {
                                InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
                                BufferedReader bf = new BufferedReader(inputReader);
                                String str;
                                for (int i = 0; i < 2; i++) {
                                    if ((str = bf.readLine()) != null) {
                                        final int CharNum = str.charAt(0) - '0';
                                        world.InitializeFormation(CharNum, 1, enter_flag);
                                        enter_flag++;
                                    }
                                }
                                bf.close();
                                inputReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            readrecord = new ReadRecord(file);
                            thread_num = world.getCreaturesNum();
                            mutexi = new Semaphore[thread_num];
                            for (int i = 0; i <= thread_num - 1; i++)
                                mutexi[i] = new Semaphore(0);
                            readrecord.start();
                            world.LetCreaturesRun(animation);
                            enter_flag = -1;
                            world.SetButtonText("Enter键重新开始");
                        }
                    }

                    if (event.getCode() == KeyCode.L && (world.map.hulu_num == 0 || world.map.mon_num == 0) && enter_flag == -1 && !FileList.isEmpty()) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("保存记录");
                        File file = fileChooser.showSaveDialog(primaryStage);
                        if (file != null) {
                            try {
                                FileOutputStream fos = new FileOutputStream(file, true);
                                System.out.println("FileList " + FileList.size());
                                for (int i = 0; i < FileList.size(); i++) {
                                    String str = FileList.get(i);
                                    fos.write(str.getBytes());
                                    str = System.getProperty("line.separator");
                                    fos.write(str.getBytes());
                                }
                                fos.close();
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                        FileList.clear();
                    }
                }
            });

            root.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    String TypePressed = event.getCharacter();
                    final String base = "1";
                    final int BaseCompare = TypePressed.compareTo(base);
                    if ((world.map.hulu_num == 0 || world.map.mon_num == 0) && (enter_flag == 0 || enter_flag == 1) && BaseCompare >= 0 && BaseCompare <= 7) {
                        world.InitializeFormation(BaseCompare, 0, enter_flag);
                        enter_flag++;
                        String str = BaseCompare + "";
                        FileList.add(str);
                    }
                }
            });

            world.button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        root.requestFocus();

                    } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                        root.requestFocus();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}