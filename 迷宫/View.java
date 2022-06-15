/**
 * @author:Mika
 * @version:基于Prim算法的迷宫，GUI=Swing
 */

import javax.swing.*;
import java.awt.*;

class MapPanel extends JPanel {
    int ROW;
    int COL;
    int SIZE;//单个图像大小
    int[][] map;
    Image floorImage;
    Image wallImage;

    void loadImage() {
        Toolkit tool = this.getToolkit();
        floorImage = tool.getImage("C:\\Users\\Mika\\Desktop\\mika_Java\\Maze\\src\\white.png");
        wallImage = tool.getImage("C:\\Users\\Mika\\Desktop\\mika_Java\\Maze\\src\\black.png");
    }

    MapPanel(int rows, int cols, int size) {
        ROW = rows;
        COL = cols;
        SIZE = size;
        map = new int[ROW][COL];
//        loadImage();
    }

    //绘制
    public void paint(Graphics g) {
        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                switch (map[i][j]) {
                    case 0:
                        g.setColor(Color.GRAY);
                        g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
//                        g.drawImage(floorImage,j*SIZE,i*SIZE,this);
                        break;
                    case 1:
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(j * SIZE, i * SIZE, SIZE, SIZE);
//                        g.drawImage(wallImage,j*SIZE,i*SIZE,this);
                        break;
                    default:
                        break;
                }
                g.setColor(Color.BLACK);
                g.drawRect(j * SIZE, i * SIZE, SIZE, SIZE);

            }
        }
    }
}

public class View {
    int ROW = 21;
    int COL = 31;
    int SIZE = 30;//单个图像大小

    public View() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Maze");
        frame.setSize(SIZE * COL + 20, SIZE * ROW + 50);
        frame.setLocation(300, 300);

        MapPanel mapPanel = new MapPanel(ROW, COL, SIZE);
        GenerateMaze gm = new GenerateMaze(ROW, COL);
        gm.run();
        mapPanel.map = gm.map;

        frame.add(mapPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        View maze = new View();
    }
}
