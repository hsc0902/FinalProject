package hulubrothers;

public final class Formation {
    public int [][][]formation;
    public int formation_num;
    public int height;
    public int width;
    public Formation() {
        height = 9;
        width = 12;
        formation_num = 8;
        formation=new int[formation_num][height][width];
        for (int n = 0; n < formation_num; n++) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    formation[n][i][j] = 0;
                }
            }
        }

        int formation_index;
        //鹤翼
        formation_index = 0;
        formation[formation_index][height / 2][0] = 1;
        for (int i = 1; i <= 3; i++) {
            formation[formation_index][height / 2 - i][i] = 1;
            formation[formation_index][height / 2 + i][i] = 1;
        }

        //雁行
        formation_index = 1;
        for (int i = 1; i <= 4; i++) {
            formation[formation_index][height / 2 - i][height / 2 - i] = 1;
        }
        for (int i = 1; i <= 3; i++) {
            formation[formation_index][height / 2 - i + 1][height / 2 - i] = 1;
        }

        //衡轭阵
        formation_index = 2;
        for (int i = 0; i < 3; i++) {
            formation[formation_index][height / 2][0 + i * 2] = 1;
        }
        formation[formation_index][height / 2][1] = 1;
        for (int i = 0; i < 3; i++) {
            formation[formation_index][height / 2 + 1][1 + i * 2] = 1;
        }


        //长蛇
        formation_index = 3;
        for (int i = 0; i < 4; i++) {
            formation[formation_index][height / 2][i] = 1;
        }
        for (int i = 1; i < 4; i++) {
            formation[formation_index][height / 2 - 1][i] = 1;
        }

        //鱼鳞
        formation_index = 4;
        formation[formation_index][height / 2][2] = 1;
        formation[formation_index][height / 2 - 2][1] = 1;
        formation[formation_index][height / 2][1] = 1;
        formation[formation_index][height / 2 + 2][1] = 1;
        formation[formation_index][height / 2 - 1][2] = 1;
        formation[formation_index][height / 2 + 1][2] = 1;
        formation[formation_index][height / 2][3] = 1;

        //方
        formation_index = 5;
        formation[formation_index][height / 2 - 1][1] = 1;
        formation[formation_index][height / 2 + 1][1] = 1;
        formation[formation_index][height / 2 - 2][2] = 1;
        formation[formation_index][height / 2 + 2][2] = 1;
        formation[formation_index][height / 2 - 1][3] = 1;
        formation[formation_index][height / 2 + 1][3] = 1;
        formation[formation_index][height / 2][4] = 1;

        //偃月
        formation_index = 6;
        formation[formation_index][height / 2 + 1][4] = 1;
        formation[formation_index][height / 2][3] = 1;
        formation[formation_index][height / 2 + 1][3] = 1;
        formation[formation_index][height / 2 - 1][2] = 1;
        formation[formation_index][height / 2][2] = 1;
        formation[formation_index][height / 2][1] = 1;
        formation[formation_index][height / 2 + 1][1] = 1;

        //锋矢
        formation_index = 7;
        for (int i = 1; i <= 5; i++) {
            formation[formation_index][height / 2][i] = 1;
        }
        formation[formation_index][height / 2 - 1][4] = 1;
        formation[formation_index][height / 2 + 1][4] = 1;
    }
}
