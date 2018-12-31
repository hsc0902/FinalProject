package hulubrothers;

import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Creatures extends Thread {
    protected int id;
    protected final int WorldIndex;
    protected String name;
    protected double HP;
    protected double MP;
    protected double MaxHP;
    protected double MaxMP;
    protected ImageView Appearance;
    protected ImageView AppearanceUI;
    private ImageView AppearanceVertical;
    private int MidX;
    private int MidY;
    ColoredProgressBar HPProgressBar;
    ColoredProgressBar MPProgressBar;
    private int height;
    private int width;
    protected boolean isliving;
    protected int deadRound;
    protected Point position;
    protected World MyWorld;
    protected Animation animation;
    protected final int camp;
    protected int ATKRange;
    protected int RunMode;
    public int AtkValue;

    Creatures(int n, World world, int x, int y, int MyCamp, int Mode) {
        id = n;
        WorldIndex = id;
        MyWorld = world;
        camp = MyCamp;
        position = new Point();
        position.setX(x);
        position.setY(y);
        MyWorld.map.Arrive(x, y, this);
        RunMode = Mode;
        //读取配置文件
        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        //初始化
        if (id < 10) {
            name = properties.getProperty("name" + (id + ""));
            MaxHP = Double.parseDouble(properties.getProperty("HuLuWaMAXHP" + (id + "")));
            MaxMP = Double.parseDouble(properties.getProperty("HuLuWaMAXMP" + (id + "")));
            ATKRange = Integer.parseInt(properties.getProperty("ATKrang" + (id + "")));
            AtkValue = Integer.parseInt(properties.getProperty("ATKValue" + (id + "")));
        } else {
            name = properties.getProperty("name" + "10");
            MaxHP = Double.parseDouble(properties.getProperty("HuLuWaMAXHP" + "10"));
            MaxMP = Double.parseDouble(properties.getProperty("HuLuWaMAXMP" + "10"));
            ATKRange = Integer.parseInt(properties.getProperty("ATKrang" + "10"));
            AtkValue = Integer.parseInt(properties.getProperty("ATKValue" + "10"));
        }
        HP = MaxHP;
        MP = MaxMP;
        isliving = true;
        Appearance = new ImageView();
        AppearanceUI = new ImageView();
        Appearance.setImage(MyWorld.Img[WorldIndex]);
        AppearanceUI.setImage(MyWorld.ImgUI[WorldIndex]);
        HPProgressBar = MyWorld.HPProgressBar[WorldIndex];
        MPProgressBar = MyWorld.MPProgressBar[WorldIndex];
        height = Integer.parseInt(properties.getProperty("CreatureHeight"));
        width = Integer.parseInt(properties.getProperty("CreatureWidth"));
        Appearance.setFitHeight(height);
        Appearance.setFitWidth(width);
        Appearance.setX(MyWorld.map.point_of_map[x][y].X() - Appearance.getFitWidth() / 2);
        Appearance.setY(MyWorld.map.point_of_map[x][y].Y() - Appearance.getFitHeight() / 2);
        Appearance.setVisible(true);
        AppearanceUI.setVisible(false);
        HPProgressBar.setVisible(false);
        MPProgressBar.setVisible(false);
        MyWorld.Root.getChildren().addAll(Appearance, AppearanceUI, HPProgressBar, MPProgressBar);
    }

    public void AddAnimation(Animation animation) {
        this.animation=animation;
    }

    public void Speak() {
        Sound sound = new Sound("/sound/1.mp3");
        int SpeakTime=sound.getTime();
        sound.start();
        try {
            sleep(SpeakTime+1);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        /*Sound sound2 = new Sound("/sound/2.mp3");
        int SpeakTime2=sound2.getTime();
        sound2.start();
        try {
            sleep(SpeakTime2+1);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }*/
    }

    public void ShowVertical(boolean Start) {
        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        if (Start == true) {
            AppearanceVertical = new ImageView();
            AppearanceVertical.setImage(MyWorld.ImgVertical[WorldIndex]);
            AppearanceVertical.setFitHeight(MyWorld.ImgVertical[WorldIndex].getHeight());
            AppearanceVertical.setFitWidth(MyWorld.ImgVertical[WorldIndex].getWidth());
            AppearanceVertical.setX(-800);
            AppearanceVertical.setY(-800);
            MidX = Integer.parseInt(properties.getProperty("LayoutXVertical" + (camp + "")))+((int)AppearanceVertical.getFitWidth()/2);
            MidY = Integer.parseInt(properties.getProperty("LayoutYVertical" + (camp + "")))+((int)AppearanceVertical.getFitHeight()/2);
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MyWorld.Root.getChildren().add(AppearanceVertical);
                    }
                });
                sleep( 50);
            } catch (Exception e) {
                System.out.println("thread interrupted");
            }
            if (camp == 0)
                animation.CreatureMoveAnimation(AppearanceVertical, -500, 1000, MidX, MidY, 600);
            else
                animation.CreatureMoveAnimation(AppearanceVertical, 2340, 1000, MidX, MidY, 600);
        } else {
            if (camp == 0)
                animation.CreatureMoveAnimation(AppearanceVertical, MidX, MidY, 2340, 800, 200);
            else
                animation.CreatureMoveAnimation(AppearanceVertical, MidX, MidY, -500, 800, 200);
            Speak();
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        MyWorld.Root.getChildren().remove(AppearanceVertical);
                    }
                });
                sleep( 100);
            } catch (Exception e) {
                System.out.println("thread interrupted");
            }
        }
    }
    //搜寻具体坐标，查看是否有敌人并攻击
    protected boolean SearchAndAttack(int x, int y) {
        if (!MyWorld.map.isempty[x][y] && MyWorld.map.creatures_in_map[x][y].isliving && camp + MyWorld.map.creatures_in_map[x][y].camp == 1) {
            MyWorld.SetUIVisible(MyWorld.map.creatures_in_map[x][y].id, true);
            MyWorld.EnterAttack();
            while(this.isliving&&MyWorld.map.creatures_in_map[x][y].isliving) {
                ShowVertical(true);
                MyWorld.map.creatures_in_map[x][y].UnderAttack(AtkValue);
                ShowVertical(false);
                if(!this.isliving||!MyWorld.map.creatures_in_map[x][y].isliving)
                    break;
                MyWorld.map.creatures_in_map[x][y].ShowVertical(true);
                this.UnderAttack(MyWorld.map.creatures_in_map[x][y].AtkValue);
                MyWorld.map.creatures_in_map[x][y].ShowVertical(false);
            }
            MyWorld.LeaveAttack();
            MyWorld.SetUIVisible(MyWorld.map.creatures_in_map[x][y].id, false);
            return true;
        } else {
            return false;
        }
    }
    //每回合的攻击行为
    protected synchronized void Attack() {
        boolean ATK_flag=false;
        for(int i=position.X()-ATKRange; i<=position.X()+ATKRange; i++) {
            for(int j=position.Y()-ATKRange; j<=position.Y()+ATKRange; j++) {
                if(i>=0&&i<MyWorld.map.height&&j>=0&&j<MyWorld.map.width&&!(i==position.X()&&j==position.Y())) {
                    ATK_flag=SearchAndAttack(i, j);
                    if(ATK_flag)
                        break;
                }
            }
            if(ATK_flag)
                break;
        }
    }

    protected void UnderAttack(int ATKValue) {
        HP = HP - ATKValue;
        HP = HP >= 0 ? HP : 0;
        if (HP == 0) {
            deadRound = 0;
            if (camp == 0)
                MyWorld.map.hulu_num--;
            else
                MyWorld.map.mon_num--;
            MyWorld.map.body_num++;
            isliving = false;
            Appearance.setImage(MyWorld.ImgDead[WorldIndex]);
        }
        MyWorld.UpdateUI(id);
    }
    //移动至具体坐标
    protected synchronized boolean moveto(int x, int y) {
        if (MyWorld.map.isempty[x][y]) {
            MyWorld.map.Arrive(x, y, this);
            MyWorld.map.Leave(position.X(), position.Y());
            int FromX = MyWorld.map.point_of_map[position.X()][position.Y()].X();
            int FromY = MyWorld.map.point_of_map[position.X()][position.Y()].Y();
            int ToX = MyWorld.map.point_of_map[x][y].X();
            int ToY = MyWorld.map.point_of_map[x][y].Y();
            animation.CreatureMoveAnimation(Appearance, FromX, FromY, ToX, ToY, 200);
            position.setX(x);
            position.setY(y);
            return true;
        } else {
            return false;
        }
    }
    //寻找下一步的方向
    protected synchronized int FindNextSite() {
        int index=0;
        int trend=(int) (Math.random() * 100);
        if(trend<80) {
            int[][] vector = new int[2][2];
            int max_x = 0, max_y = 0;
            int max_sum = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    int sum_temp = 0;
                    for (int sum_i = i * (MyWorld.map.height/2); sum_i < i * (MyWorld.map.height/2) + (MyWorld.map.height/2); sum_i++) {
                        for (int sum_j = j * (MyWorld.map.width/2); sum_j < j * (MyWorld.map.width/2) + (MyWorld.map.width/2); sum_j++) {
                            if (!MyWorld.map.isempty[sum_i][sum_j] && camp + MyWorld.map.creatures_in_map[sum_i][sum_j].camp == 1) {
                                sum_temp++;
                            }
                        }
                    }
                    vector[i][j] = sum_temp;
                    if (max_sum < sum_temp) {
                        max_x = i;
                        max_y = j;
                        max_sum = sum_temp;
                    }
                }
            }
            int my_vector_x = position.X() - (MyWorld.map.height/2) <= 0 ? 0 : 1;
            int my_vector_y = position.Y() - (MyWorld.map.width/2) <= 0 ? 0 : 1;
            my_vector_x = max_x - my_vector_x;
            my_vector_y = max_y - my_vector_y;
            if (my_vector_x < 0) {
                if (my_vector_y == 0) {
                    if (position.X() - 1 >= 0) {
                        index=0;
                    }
                }
                if (my_vector_y < 0) {
                    int index_temp = (int) (Math.random() * 2);
                    if (index_temp == 0)
                        index = 0;
                    else
                        index = 3;
                }
                if (my_vector_y > 0) {
                    int index_temp = (int) (Math.random() * 2);
                    if (index_temp == 0)
                        index = 0;
                    else
                        index = 1;
                }
            } else if (my_vector_x > 0) {
                if (my_vector_y == 0) {
                    index = 2;
                }
                if (my_vector_y < 0) {
                    int index_temp = (int) (Math.random() * 2);
                    if (index_temp == 0)
                        index = 2;
                    else
                        index = 3;
                }
                if (my_vector_y > 0) {
                    int index_temp = (int) (Math.random() * 2);
                    if (index_temp == 0)
                        index = 2;
                    else
                        index = 1;
                }
            } else if (my_vector_y > 0) {
                index = 1;
            } else if (my_vector_y < 0) {
                index = 3;
            } else {
                index = (int) (Math.random() * 3);
            }
        }
        else {
            index = (int) (Math.random() * 3);
        }
        return index;
    }
    //每回合的移动行为：根据寻找的方向实行相应的行动
    protected synchronized int Move(int index) {
        boolean move_flag = false;
        if (position.X() - 1 >= 0 && index == 0) {
            move_flag = moveto(position.X() - 1, position.Y());
            if(move_flag)
                return index;
        } else if (position.Y() + 1 < MyWorld.map.width && index == 1) {
            move_flag = moveto(position.X(), position.Y() + 1);
            if(move_flag)
                return index;
        } else if (position.X() + 1 < MyWorld.map.height && index == 2) {
            move_flag = moveto(position.X() + 1, position.Y());
            if(move_flag)
                return index;
        } else if (position.Y() - 1 >= 0 && index == 3) {
            move_flag = moveto(position.X(), position.Y() - 1);
            if(move_flag)
                return index;
        }
        if (!move_flag) {
            if (position.X() - 1 >= 0 && !move_flag) {
                move_flag = moveto(position.X() - 1, position.Y());
                if(move_flag)
                    return 0;
            }
            if (position.Y() + 1 < MyWorld.map.width && !move_flag) {
                move_flag = moveto(position.X(), position.Y() + 1);
                if(move_flag)
                    return 1;
            }
            if (position.X() + 1 < MyWorld.map.height && !move_flag) {
                move_flag = moveto(position.X() + 1, position.Y());
                if(move_flag)
                    return 2;
            }
            if (position.Y() - 1 >= 0 && !move_flag) {
                move_flag = moveto(position.X(), position.Y() - 1);
                if(move_flag)
                    return 3;
            }
        }
        return -1;
    }

}
