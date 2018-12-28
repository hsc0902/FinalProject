package hulubrothers;


public class Map {
    Creatures creatures_in_map[][];
    final Point point_of_map[][];
    boolean isempty[][];
    int height;
    int width;
    int hulu_num;
    int mon_num;
    int body_num;

    Map() {
        height=9;
        width=12;
        hulu_num=0;
        mon_num=0;
        body_num=0;
        creatures_in_map=new Creatures[height][width];
        point_of_map=new Point[height][width];
        isempty= new boolean[height][width];

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                point_of_map[i][j]=new Point();
            }
        }
        point_of_map[0][0].setX(508);
        point_of_map[0][0].setY(280);
        for(int i=0; i<height; i++) {
            for (int j=0; j<width; j++) {
                creatures_in_map[i][j]=null;
                isempty[i][j]=true;
                if(i>0&&j>0) {
                    point_of_map[i][j].setX(point_of_map[i-1][j].X());
                    point_of_map[i][j].setY(point_of_map[i][j-1].Y());
                }
                else if(i>0&&j==0) {
                    point_of_map[i][j].setX(point_of_map[i-1][j].X());
                    point_of_map[i][j].setY(point_of_map[i-1][j].Y()+62);
                }
                else if(i==0&&j>0) {
                    point_of_map[i][j].setX(point_of_map[i][j-1].X()+73);
                    point_of_map[i][j].setY(point_of_map[i][j-1].Y());
                }
             }
        }
    }
    public synchronized void Arrive(int x, int y, Creatures creature) {
        creatures_in_map[x][y]=creature;
        isempty[x][y]=false;
        if(creature.camp==0)
            hulu_num++;
        else
            mon_num++;
        return;
    }
    public synchronized void Leave(int x, int y) {
        if(creatures_in_map[x][y].camp==0)
            hulu_num--;
        else
            mon_num--;
        creatures_in_map[x][y]=null;
        isempty[x][y]=true;
        return;
    }

    public synchronized void RemoveCreature(int x, int y) {
        creatures_in_map[x][y]=null;
        isempty[x][y]=true;
        return;
    }

    public synchronized void Clear() {
        hulu_num=0;
        mon_num=0;
        body_num=0;
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                creatures_in_map[i][j] = null;
                isempty[i][j] = true;
            }
        }
    }
}
