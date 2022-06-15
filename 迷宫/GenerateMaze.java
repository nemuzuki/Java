/**
 * @author:Mika
 * @version:生成逻辑迷宫
 * 路和墙都是格子。初始将所有（偶数，偶数）格子作为路，其余格子为墙，之后不断打通两个路格之间的墙，即墙变成路
 * 记录每个路是否访问过，避免成环
 */

import java.util.*;


//每面墙由两个格子的下标确定
class Point {
    int x, y;

    Point(int a, int b) {
        x = a;
        y = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

public class GenerateMaze {
    int ROW;
    int COL;
    int[][] map;
    int[][] visit;
    HashMap<Point, List<Point>> wallSet = new HashMap<>();

    public GenerateMaze(int row, int col) {
        ROW=row;
        COL=col;
        map = new int[ROW][COL];
        visit = new int[ROW+1][COL+1];
    }

    void printMap() {
        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                System.out.printf("%d ", map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    void run() {
        Random random = new Random();
        int sx = 0, sy = 0;
        //初始化地图
        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                if (i % 2 == 0 && j % 2 == 0) {
                    map[i][j] = 0;//路为0
                } else {
                    map[i][j] = 1;//墙为1
                    if (i % 2 == 1) {//奇数行的墙是上下两块的间隔
                        wallSet.put(new Point(i, j), Arrays.asList(new Point(i - 1, j), new Point(i + 1, j)));
                    } else {//偶数行的墙是左右两块的间隔
                        wallSet.put(new Point(i, j), Arrays.asList(new Point(i, j - 1), new Point(i, j + 1)));
                    }
                }
            }
        }

        List<Point> set = new ArrayList<>();//墙的集合
        addNeighbourWall(set, sx, sy);
        visit[sx][sy] = 1;
        while (!set.isEmpty()) {
            int r = random.nextInt(set.size());
            Point wall = set.get(r);//随机选墙
            map[wall.x][wall.y] = 0;//需要连通两侧，所以该墙变成路
            set.remove(r);

            Point block1 = wallSet.get(wall).get(0);
            Point block2 = wallSet.get(wall).get(1);
            int x1 = block1.x, y1 = block1.y;
            int x2 = block2.x, y2 = block2.y;
            //没访问过的一侧作为起点
            if (visit[x1][y1] == 0) {
                sx = x1;
                sy = y1;
            } else {
                sx = x2;
                sy = y2;
            }
            visit[sx][sy] = 1;
            addNeighbourWall(set, sx, sy);//将起点四周的墙加入集合
            removeInvalidWall(set);//删除集合中两侧都访问过的墙
        }
        printMap();

    }

    //删除集合中两侧都访问过的墙
    void removeInvalidWall(List<Point> set) {
        List<Point> tmp = new ArrayList<>(set);//先复制一个副本，否则遍历时会出错
        for (Point wall : tmp) {
            Point block1 = wallSet.get(wall).get(0);
            Point block2 = wallSet.get(wall).get(1);
            int x1 = block1.x, y1 = block1.y;
            int x2 = block2.x, y2 = block2.y;
            if (visit[x1][y1] == 1 && visit[x2][y2] == 1) {
                set.remove(wall);
            }
        }
    }

    //将(sx,sy)四周的墙加入集合
    void addNeighbourWall(List<Point> set, int x, int y) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < 4; ++i) {
            int nx = x + dx[i], ny = y + dy[i];
            Point point = new Point(nx, ny);
            if (nx >= 0 && nx < ROW && ny >= 0 && ny < COL && !set.contains(point)) {
                set.add(point);
            }
        }
    }

    public static void main(String[] args) {
        GenerateMaze gm = new GenerateMaze(16,16);
        gm.run();
    }
}
