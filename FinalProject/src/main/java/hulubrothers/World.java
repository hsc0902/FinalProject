package hulubrothers;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.io.*;
import java.util.Properties;

public class World {
    private int CreaturesNum;
    public Button button;
    public Creatures []creatures; //生物
    public Image[]Img; //生物图片
    public Image[]ImgDead; //死亡图片
    public Image[]ImgUI;   //UI图片
    public Image[]ImgVertical;  //生物立绘
    public ColoredProgressBar []HPProgressBar;
    public ColoredProgressBar []MPProgressBar;
    public Map map; //地图
    public AnchorPane Root;
    public Task<Void> LoadTask;
    private ImageView BattleScene;

    public World (AnchorPane root) {
        try {
            Root = root;
            map = new Map();
            CreaturesNum = 17;
            creatures = new Creatures[CreaturesNum];
            Img = new Image[CreaturesNum];
            ImgDead = new Image[CreaturesNum];
            ImgUI = new Image[CreaturesNum];
            ImgVertical = new Image[CreaturesNum];
            //ImgIndicator = new Image[2];
            Properties properties = new Properties();
            try {
                InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
                properties.load(Is);
            } catch (Exception ex) {
                System.out.println("Read properties error!");
            }
            LoadAllImage();
            button = new Button();
            button.setLayoutX(Double.parseDouble(properties.getProperty("ButtonLayoutX")));
            button.setLayoutY(Double.parseDouble(properties.getProperty("ButtonLayoutY")));
            button.setPrefWidth(Double.parseDouble(properties.getProperty("ButtonWidth")));
            button.setPrefHeight(Double.parseDouble(properties.getProperty("ButtonHeight")));
            button.setVisible(false);
            root.getChildren().add(button);
            InitializeProgressBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitializeProgressBar() {
        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        HPProgressBar = new ColoredProgressBar[CreaturesNum];
        MPProgressBar = new ColoredProgressBar[CreaturesNum];
        for (int i = 0; i < CreaturesNum; i++) {
            HPProgressBar[i] = new ColoredProgressBar("red-bar", 1);
            MPProgressBar[i] = new ColoredProgressBar("blue-bar", 1);
            HPProgressBar[i].setMinHeight(17);
            MPProgressBar[i].setMinHeight(17);
            HPProgressBar[i].setMaxHeight(20);
            MPProgressBar[i].setMaxHeight(20);
            if(i<=7) {
                HPProgressBar[i].setPrefSize(Integer.parseInt(properties.getProperty("WidthHPProgressBar" + "0")), Integer.parseInt(properties.getProperty("HeightHPProgressBar" + "0")));
                HPProgressBar[i].setLayoutX(Integer.parseInt(properties.getProperty("LayoutXHPProgressBar" + "0")));
                HPProgressBar[i].setLayoutY(Integer.parseInt(properties.getProperty("LayoutYHPProgressBar" + "0")));
                MPProgressBar[i].setPrefSize(Integer.parseInt(properties.getProperty("WidthMPProgressBar" + "0")), Integer.parseInt(properties.getProperty("HeightMPProgressBar" + "0")));
                MPProgressBar[i].setLayoutX(Integer.parseInt(properties.getProperty("LayoutXMPProgressBar" + "0")));
                MPProgressBar[i].setLayoutY(Integer.parseInt(properties.getProperty("LayoutYMPProgressBar" + "0")));
            }
            else {
                HPProgressBar[i].setPrefSize(Integer.parseInt(properties.getProperty("WidthHPProgressBar" + "1")), Integer.parseInt(properties.getProperty("HeightHPProgressBar" + "1")));
                HPProgressBar[i].setLayoutX(Integer.parseInt(properties.getProperty("LayoutXHPProgressBar" + "1")));
                HPProgressBar[i].setLayoutY(Integer.parseInt(properties.getProperty("LayoutYHPProgressBar" + "1")));
                MPProgressBar[i].setPrefSize(Integer.parseInt(properties.getProperty("WidthMPProgressBar" + "1")), Integer.parseInt(properties.getProperty("HeightMPProgressBar" + "1")));
                MPProgressBar[i].setLayoutX(Integer.parseInt(properties.getProperty("LayoutXMPProgressBar" + "1")));
                MPProgressBar[i].setLayoutY(Integer.parseInt(properties.getProperty("LayoutYMPProgressBar" + "1")));
            }
            HPProgressBar[i].setVisible(false);
            MPProgressBar[i].setVisible(false);
        }
    }

    public void Clear() {
        Root.getChildren().clear();
        Root.getChildren().add(button);
        BattleScene = new ImageView(this.getClass().getResource("/image/background_v3.jpg").toExternalForm());
        BattleScene.setFitHeight(900);
        BattleScene.setFitWidth(1840);
        BattleScene.setX(0);
        BattleScene.setY(0);
        BattleScene.setVisible(false);
        Root.getChildren().add(BattleScene);
        map.Clear();
    }

    public void InitializeFormation(int FormationType, int mode, int enter_flag) {
        Formation formation =new Formation();
        if (enter_flag == 0) {
            SetButtonText("数字键1-8选择妖精的阵型");
            int thread_index = 1;
            for (int i = 0; i < formation.height; i++) {
                for (int j = 0; j < formation.width; j++) {
                    if (formation.formation[FormationType][i][j] != 0) {
                        creatures[thread_index] = new HuLuBrothers(thread_index, this, i, j, mode);
                        thread_index++;
                    }
                }
            }
            switch (FormationType) {
                case 0:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2, 3, mode);
                    break;
                case 1:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2 - 3, formation.height / 2 - 4, mode);
                    break;
                case 2:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2 + 1, 0, mode);
                    break;
                case 3:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2 - 1, 0, mode);
                    break;
                case 4:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2, 0, mode);
                    break;
                case 5:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2, 0, mode);
                    break;
                case 6:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2 + 1, 0, mode);
                    break;
                case 7:
                    creatures[0] = new HuLuBrothers(0, this, formation.height / 2, 0, mode);
                    break;
                default:
                    break;
            }
        } else {
            SetButtonText("空格键开始战斗");
            int thread_index = 10;
            for (int i = 0; i < formation.height; i++) {
                for (int j = 0; j < formation.width; j++) {
                    if (formation.formation[FormationType][i][j] != 0) {
                        creatures[thread_index] = new Monsters(thread_index, this, formation.height - 1 - i, formation.width - 1 - j, mode);
                        thread_index++;
                    }
                }
            }
            switch (FormationType) {
                case 0:
                    creatures[8] = new Monsters(8, this, formation.height / 2 - 1, formation.width - 4, mode);
                    creatures[9] = new Monsters(9, this, formation.height / 2 + 1, formation.width - 4, mode);
                    break;
                case 1:
                    creatures[8] = new Monsters(8, this, formation.height - (formation.height / 2 + 1), formation.width - (formation.height / 2 + 1), mode);
                    creatures[9] = new Monsters(9, this, formation.height - (formation.height / 2 + 2), formation.width - (formation.height / 2 + 1), mode);
                    break;
                case 2:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 3, mode);
                    break;
                case 3:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2 - 1), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2), formation.width - 5, mode);
                    break;
                case 4:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 1, mode);
                    break;
                case 5:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 1, mode);
                    break;
                case 6:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2 + 2), formation.width - 1, mode);
                    break;
                case 7:
                    creatures[8] = new Monsters(8, this, formation.height - 1 - (formation.height / 2 + 1), formation.width - 1, mode);
                    creatures[9] = new Monsters(9, this, formation.height - 1 - (formation.height / 2), formation.width - 1, mode);
                    break;
                default:
                    break;
            }
        }
    }

    public int getCreaturesNum() {
        return CreaturesNum;
    }

    public void LetCreaturesRun(Animation animation) {
        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        /*Indicator = new ImageView[2];
        for (int i = 0; i < 2; i++) {
            Indicator[i] = new ImageView();
            Indicator[i].setImage(ImgIndicator[i]);
            Indicator[i].setFitWidth(Integer.parseInt(properties.getProperty("IndicatorWidth")));
            Indicator[i].setFitHeight(Integer.parseInt(properties.getProperty("IndicatorHeight")));
            Root.getChildren().add(Indicator[i]);
        }*/
        for (int i = 0; i < CreaturesNum; i++) {
            creatures[i].AddAnimation(animation);
            creatures[i].start();
        }
    }

    public void SetUIVisible(int id, boolean Visible) {
        creatures[id].AppearanceUI.setVisible(Visible);
        creatures[id].HPProgressBar.setProgress(creatures[id].HP / creatures[id].MaxHP);
        creatures[id].MPProgressBar.setProgress(creatures[id].MP / creatures[id].MaxMP);
        creatures[id].HPProgressBar.setVisible(Visible);
        creatures[id].MPProgressBar.setVisible(Visible);
    }

    public void UpdateUI(int id) {
        creatures[id].HPProgressBar.setProgress(creatures[id].HP / creatures[id].MaxHP);
        creatures[id].MPProgressBar.setProgress(creatures[id].MP / creatures[id].MaxMP);
    }


    public void SetButtonText(String str) {
        button.setText(str);
    }

    public void LoadAllImage () {
        ProgressBar LoadBar = new ProgressBar(0);
        LoadBar.setPrefSize(1000,30);
        LoadBar.setLayoutY(550);
        LoadBar.setLayoutX(100);
        Label LoadLabel = new Label("Loading...0%");
        LoadLabel.setFont(new Font(20));
        LoadLabel.setLayoutY(550);
        LoadLabel.setLayoutX(520);
        Root.getChildren().add(LoadBar);
        Root.getChildren().add(LoadLabel);

        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        String ImgDeadUrl = properties.getProperty("DeadCreatureUrl");
        String ImgUrl = properties.getProperty("CreatureUrl");
        String ImgUIUrl = properties.getProperty("UIUrl");
        String ImgVerticalUrl=properties.getProperty("VerticalImageUrl");
        String ImgIndicatorUrl=properties.getProperty("IndicatorUrl");
        LoadTask = new Task<Void>() {

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("按回车键开始游戏");
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                updateMessage("Cancelled");
            }

            @Override
            protected void failed() {
                super.failed();
                updateMessage("Failed");
            }

            @Override
            protected Void call() throws Exception {
                int LoadProgress = 0;
                for (int i = 0; i < CreaturesNum; i++) {
                    StringBuilder sbDead = new StringBuilder(ImgDeadUrl);
                    StringBuilder sb = new StringBuilder(ImgUrl);
                    StringBuilder sbUI = new StringBuilder(ImgUIUrl);
                    StringBuilder sbVertical = new StringBuilder(ImgVerticalUrl);
                    if (i >= 10) {
                        sbDead.insert(11, "10");
                        ImgDead[i] = new Image(this.getClass().getResource(sbDead.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sb.insert(11, "10");
                        Img[i] = new Image(this.getClass().getResource(sb.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sbUI.insert(11, "10");
                        ImgUI[i] = new Image(this.getClass().getResource(sbUI.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sbVertical.insert(15, "10");
                        ImgVertical[i] = new Image(this.getClass().getResource(sbVertical.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                    } else {
                        sbDead.insert(11, "0" + (i + ""));
                        ImgDead[i] = new Image(this.getClass().getResource(sbDead.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sb.insert(11, "0" + (i + ""));
                        Img[i] = new Image(this.getClass().getResource(sb.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sbUI.insert(11, "0" + (i + ""));
                        ImgUI[i] = new Image(this.getClass().getResource(sbUI.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                        sbVertical.insert(15, "0" + (i + ""));
                        ImgVertical[i] = new Image(this.getClass().getResource(sbVertical.toString()).toExternalForm());
                        updateProgress(LoadProgress, 100);
                        updateMessage("Loading..." + LoadProgress + "%");
                        LoadProgress++;
                    }
                }

                LoadProgress++;
                while(LoadProgress<=100) {
                    Thread.sleep(20);
                    updateProgress(LoadProgress, 100);
                    updateMessage("Loading..." + LoadProgress + "%");
                    LoadProgress++;
                }
                updateMessage("Finish");
                return null;
            }
        };
        LoadBar.progressProperty().bind(LoadTask.progressProperty());
        LoadLabel.textProperty().bind(LoadTask.messageProperty());
        new Thread(LoadTask).start();
    }

    public void EnterAttack() {
        for(int i=0; i<Main.thread_num; i++) {
            creatures[i].Appearance.setVisible(false);
        }
        System.out.println("A");
        BattleScene.setVisible(true);
        button.setVisible(false);
    }

    public void LeaveAttack() {
        for(int i=0; i<Main.thread_num; i++) {
            creatures[i].Appearance.setVisible(true);
        }
        BattleScene.setVisible(false);
        button.setVisible(true);
    }
}
