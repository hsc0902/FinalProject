package hulubrothers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class HuLuBrothers extends Creatures{

    HuLuBrothers(int n, World world, int x, int y, int Mode) {
        super(n, world, x, y, 0, Mode);
        Properties properties = new Properties();
        try {
            InputStream Is = HuLuBrothers.class.getClassLoader().getResourceAsStream("HuLuBrothers.properties");
            properties.load(Is);
        } catch (Exception ex) {
            System.out.println("Read properties error!");
        }
        AppearanceUI.setFitHeight(Integer.parseInt(properties.getProperty("HeightUI0")));
        AppearanceUI.setFitWidth(Integer.parseInt(properties.getProperty("WidthUI0")));
        AppearanceUI.setX(Integer.parseInt(properties.getProperty("LayoutXUI0")) - AppearanceUI.getFitWidth() / 2);
        AppearanceUI.setY(Integer.parseInt(properties.getProperty("LayoutYUI0")) - AppearanceUI.getFitHeight() / 2);
    }

    public void run() {
        if (RunMode == 0) {
            while (!((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0) && isliving) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0)) {
                    String str=this.id+"";
                    str=str+' ';
                    str=str+(-9+"");
                    str=str+" "+this.name;
                    Main.FileList.add(str);
                    Main.mutexi[(id + 1) % Main.thread_num].release();
                    return;
                }
                else if(!isliving) {
                    String str=this.id+"";
                    str=str+' ';
                    str=str+(-1+"");
                    str=str+" "+this.name;
                    Main.FileList.add(str);
                    deadRound++;
                    Main.mutexi[(id + 1) % Main.thread_num].release();
                    break;
                }
                MyWorld.SetUIVisible(id, true);
                int index = FindNextSite();
                String str=this.id+"";
                str=str+' ';
                str=str+(Move(index)+"");
                str=str+" "+this.name;
                Attack();
                Main.FileList.add(str);
                try {
                    sleep(200);
                }catch(Exception ex) {

                }
                MyWorld.SetUIVisible(id, false);
                Main.mutexi[(id + 1) % Main.thread_num].release();
            }
            while (!((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0) && !isliving && deadRound<3) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String str=this.id+"";
                str=str+' ';
                str=str+(-2+"");
                str=str+" "+this.name;
                Main.FileList.add(str);
                deadRound++;
                Main.mutexi[(id + 1) % Main.thread_num].release();
            }
            if (isliving) {
                isliving = false;
                MyWorld.map.Leave(position.X(), position.Y());
            }
            else if(deadRound==3) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String str=this.id+"";
                str=str+' ';
                str=str+(-3+"");
                str=str+" "+this.name;
                Main.FileList.add(str);
                MyWorld.map.RemoveCreature(position.X(), position.Y());
                Appearance.setVisible(false);
                for (int i = this.id; i < Main.thread_num - 1; i++) {
                    Main.mutexi[i] = Main.mutexi[i + 1];
                    MyWorld.creatures[i] = MyWorld.creatures[i + 1];
                }
                for (int i = this.id; i < Main.thread_num - 1; i++) {
                    MyWorld.creatures[i].id--;
                }
                Main.thread_num--;
                Main.mutexi[(id) % Main.thread_num].release();
            }
        }
        else {
            while (!((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0) && isliving) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Run "+id+" "+deadRound);
                if(((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0)) {
                    Main.readrecord.Release();
                    return;
                }
                else if(!isliving) {
                    deadRound++;
                    Main.readrecord.Release();
                    break;
                }
                MyWorld.SetUIVisible(id, true);
                int index=Main.readrecord.GetMoveIndex();
                if (index == 0) {
                    moveto(position.X() - 1, position.Y());
                } else if (index == 1) {
                    moveto(position.X(), position.Y() + 1);
                } else if (index == 2) {
                    moveto(position.X() + 1, position.Y());
                } else if (index == 3) {
                    moveto(position.X(), position.Y() - 1);
                }
                Attack();
                try {
                    sleep(200);
                }catch(Exception ex) {

                }
                MyWorld.SetUIVisible(id, false);
                Main.readrecord.Release();
            }
            while (!((MyWorld.map.mon_num == 0 || MyWorld.map.hulu_num == 0) && MyWorld.map.body_num > 0) && !isliving && deadRound<3) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deadRound++;
                Main.readrecord.Release();
            }
            if (isliving) {
                isliving = false;
                MyWorld.map.Leave(position.X(), position.Y());
            }
            else if(deadRound==3) {
                try {
                    Main.mutexi[id].acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyWorld.map.RemoveCreature(position.X(), position.Y());
                Appearance.setVisible(false);
                for (int i = this.id; i < Main.thread_num - 1; i++) {
                    Main.mutexi[i] = Main.mutexi[i + 1];
                    MyWorld.creatures[i] = MyWorld.creatures[i + 1];
                }
                for (int i = this.id; i < Main.thread_num - 1; i++) {
                    MyWorld.creatures[i].id--;
                }
                Main.thread_num--;
                Main.readrecord.Release();
            }
        }
    }
}
