package fiveChess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fiveChess.Listener.Position;

//棋盘的监听器
class Listener implements MouseListener, MouseMotionListener {
	CheckerBoard frame;
	Graphics g;
	int chessRadius = 8;// 棋子半径
	int stepOffset=0;//复盘时的偏移量，向前一步则-1
	int cnt=0;//当前的棋子数
	
	class Position{//坐标类
		int x;
		int y;
		int color;
		public Position() {}
		public Position(int x,int y,int color) {
			this.x=x;
			this.y=y;
			this.color=color;
		}
	}
	Position []positionList=new Position[100];//存储历史步数的数组
	
	//初始化
	void init() {
		clearBoard();//清屏
		stepOffset=0;
		cnt=0;
	}
	// 清屏
	void clearBoard() {
		g.setColor(Color.GRAY);
		g.fillRect(10, 70, 1000, 1000);
		Model.getInstance().initializeMap();// 初始化逻辑棋盘

		g.setColor(Color.YELLOW);
		for (int i = 0; i < 19; i++) {
			// 每两条线间隔20px，共19行19列
			g.drawLine(10, 70 + 20 * i, 370, 70 + 20 * i);
			g.drawLine(10 + 20 * i, 70, 10 + 20 * i, 430);
		}
	}
	
	public Listener() {
	}

	public Listener(CheckerBoard frame, Graphics g) {
		this.frame = frame;
		this.g = g;
		g.setColor(Color.BLACK);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		frame.positionJLabel.setText("(" + x + "," + y + ")");
	}

	//在物理棋盘上摆棋
	void putChessOnPhysicalBoard(int x,int y,int color) {
		if (color == Model.WHITE) {
			// 白棋画空心圆
			g.drawOval(x - chessRadius, y - chessRadius, chessRadius * 2, chessRadius * 2);
			g.setColor(Color.WHITE);
			g.fillOval(x - chessRadius, y - chessRadius, chessRadius * 2, chessRadius * 2);
			
		} else {
			// 黑棋画实心圆
			g.setColor(Color.BLACK);
			g.fillOval(x - chessRadius, y - chessRadius, chessRadius * 2, chessRadius * 2);
			
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		if (x < 10 || x > 370 || y < 70 || y > 430)
			return;// 越界

		// 因为难以正好点击到交叉点上，所以找最近的点
		// [0,19]归为10，[20,39]归为30，以此类推，x=10+x/20*20
		x = 10 + x / 20 * 20;
		y = 10 + y / 20 * 20;

		// 把物理位置转为棋盘坐标，然后在逻辑棋盘上落下棋子。注：左上角是(10,70)
		int row = (x - 10) / 20;
		int col = (y - 70) / 20;
		
		// 如果此处已经有棋子则不能覆盖
		if (Model.getInstance().getChess(row, col) != Model.SPACE) {
			System.out.println("Error: This position has been occupied!");
			return;
		}

		// 在物理棋盘上落下棋子
		int color=Control.getInstance().color;
		putChessOnPhysicalBoard(x, y,color);
		Position position=new Position(x, y,color);
		positionList[cnt++]=position;//加入历史记录

		// 落下棋子后判断输赢
		System.out.println("(" + row + "," + col + ")");
		Control.getInstance().localUserPutChess(row, col);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class CheckerBoard extends JFrame {
	JButton startButton = new JButton("Start");
	JButton retractButton = new JButton("Retract");
	JButton lastButton = new JButton("←");
	JButton nextButton = new JButton("→");
	
	
	JPanel paintingBoard = new JPanel();// 画布
	JLabel positionJLabel = new JLabel();

	
	void init() {
		Graphics g = this.getGraphics();
		Listener listener = new Listener(this, g);
		addMouseListener(listener);// 绑定鼠标事件监听
		addMouseMotionListener(listener);// 绑定鼠标移动事件监听

		//上方的操作栏
		JPanel above=new JPanel();
		above.add(startButton);
		above.add(retractButton);
		above.add(lastButton);
		above.add(nextButton);
		
		//棋盘
		paintingBoard.setSize(500, 500);
		paintingBoard.setBackground(Color.GRAY);
		
		startButton.addActionListener(new ActionListener() {//重新开局
			@Override
			public void actionPerformed(ActionEvent e) {//各种类的初始化
				Model.getInstance().initializeMap();
				Control.getInstance().init();
				listener.init();
			}
		});
		
		retractButton.addActionListener(new ActionListener() {//悔棋
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener.cnt==0) {
					System.out.println("没有棋子，不能悔棋");
					return;
				}
				Control.getInstance().color*=-1;//修改当前颜色
				System.out.println("Change color to "+Control.getInstance().color);
				
				//先清屏，再重摆前面所有棋子
				listener.cnt--;//下过的棋子数-1
				listener.clearBoard();
				for(int i=0;i<listener.cnt;++i) {
					Position position=listener.positionList[i];
					listener.putChessOnPhysicalBoard(position.x, position.y,position.color);
				}
			}
		});
		
		lastButton.addActionListener(new ActionListener() {//复盘-后退
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener.stepOffset==-listener.cnt) {
					System.out.println("没有棋子，不能后退");
					return;
				}
				//先清屏，再重摆前面所有棋子
				listener.clearBoard();
				listener.stepOffset--;//当前步数偏移量-1
				int now=listener.cnt+listener.stepOffset;
				for(int i=0;i<now;++i) {
					Position position=listener.positionList[i];
					listener.putChessOnPhysicalBoard(position.x, position.y,position.color);
				}
			}
		});
		
		nextButton.addActionListener(new ActionListener() {//复盘-前进
			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener.stepOffset==0) {
					System.out.println("不能前进了");
					return;
				}
				listener.stepOffset++;//当前步数偏移量+1
				int now=listener.cnt-1+listener.stepOffset;
				Position position=listener.positionList[now];
				listener.putChessOnPhysicalBoard(position.x, position.y,position.color);
				
			}
		});
		add(BorderLayout.CENTER, paintingBoard);
		add(BorderLayout.NORTH, above);
		add(BorderLayout.SOUTH, positionJLabel);
	}

	public CheckerBoard(String name) {
		super(name);
		setSize(600, 600);
		setLocation(300, 200);// 窗口位置
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
