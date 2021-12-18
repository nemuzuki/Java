package fiveChess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;

class Position {// 物理坐标类
	int x;
	int y;
	int color;

	public Position() {
	}

	public Position(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
}

//物理棋盘类
class PhysicalBoard implements MouseListener, MouseMotionListener {
	Graphics g;
	int sx = 200, sy = 100;// 棋盘的左上角
	int gap = 25;// 线间距
	int tx = sx + gap * 19, ty = sy + gap * 19;// 棋盘的右下角
	int chessRadius = 10;// 棋子半径

	Position[] positionHistory = new Position[100];// 存储历史步数的数组
	int cnt = 0;// 当前的棋子数
	int stepOffset = 0;// 复盘时的偏移量，向前一步则-1

	private PhysicalBoard(Graphics g) {
		this.g = g;
	}

	private static PhysicalBoard instance;

	public static PhysicalBoard getInstance(Graphics g) {
		if (instance == null) {
			instance = new PhysicalBoard(g);
		}
		return instance;
	}

	public static PhysicalBoard getInstance() {
		if (instance == null) {
			System.out.println("没有指定g，不能用该构造函数！");
		}
		return instance;
	}

	// 初始化
	void init() {
		clearBoard();// 清屏
		stepOffset = 0;
		cnt = 0;
	}

	// 清屏
	void clearBoard() {
		g.setColor(Color.GRAY);
		g.fillRect(sx, sy, tx - sx, ty - sy);// 起点，长宽

		g.setColor(Color.YELLOW);
		for (int i = 0; i < 19; i++) {// 每两条线间隔gap，共19行19列
			g.drawLine(sx, sy + gap * i, tx, sy + gap * i);// 横线
			g.drawLine(sx + gap * i, sy, sx + gap * i, ty);// 竖线
		}
	}

	// 悔棋
	void regret() {
		if (cnt == 0) {
			System.out.println("没有棋子，不能悔棋！");
			ChessBoardWindow.showWarningWindow("警告", "没有棋子，不能悔棋！");
			return;
		}

		int x=positionHistory[cnt-1].x;
		int y=positionHistory[cnt-1].y;
		int row = (x - sx) / gap;
		int col = (y - sy) / gap;
		Model.getInstance().regretOnLogicalBoard(row, col);//修改逻辑棋盘
		Model.getInstance().lastColor *= -1;
		if(Control.onlineGame==false) {
			Model.getInstance().nowColor*=-1;
		}

		cnt--;// 下过的棋子数-1
		clearBoard();//清屏并重摆前面的棋子
		for (int i = 0; i < cnt; ++i) {
			Position position = positionHistory[i];
			drawChess(position.x, position.y, position.color);
		}
	}

	void stepBack() {// 复盘后退
		if (stepOffset == -cnt) {
			System.out.println("没有棋子，不能后退！");
			ChessBoardWindow.showWarningWindow("警告", "没有棋子，不能后退！");
			return;
		}
		// 先清屏，再重摆前面所有棋子
		clearBoard();
		stepOffset--;// 当前步数偏移量-1
		int now = cnt + stepOffset;
		for (int i = 0; i < now; ++i) {
			Position position = positionHistory[i];
			drawChess(position.x, position.y, position.color);
		}

	}

	void stepForward() {// 复盘前进
		if (stepOffset == 0) {
			System.out.println("没有更多步，无法前进！");
			ChessBoardWindow.showWarningWindow("警告", "没有更多步，无法前进！");
			return;
		}
		stepOffset++;// 当前步数偏移量+1
		int now = cnt - 1 + stepOffset;
		Position position = positionHistory[now];
		drawChess(position.x, position.y, position.color);
	}

	// 绘制棋子
	void drawChess(int x, int y, int color) {
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

	// 在物理棋盘上摆棋
	void putChessOnPhysicalBoard(int row, int col, int color) {
		int x = sx + row * gap;
		int y = sy + col * gap;
		drawChess(x, y, color);
		Position position = new Position(x, y, color);
		positionHistory[cnt++] = position;// 加入历史记录
	}

	@Override
	// 鼠标点击棋盘落子
	public void mouseClicked(MouseEvent e) {
		int clientNum = Client.getInstance().clientNum;
		int lastColor = Model.getInstance().lastColor;
		if (Control.onlineGame && ((clientNum % 2 == 0 && lastColor == Model.BLACK)
				|| (clientNum % 2 == 1 && lastColor == Model.WHITE))) {
			System.out.println("一位玩家不能连续下两步！");
			ChessBoardWindow.showWarningWindow("警告", "一位玩家不能连续下两步！");
			return;
		}

		int x = e.getX(), y = e.getY();
		if (x < sx || x > tx || y < sy || y > ty) {
			System.out.println("落子越界！");
			ChessBoardWindow.showWarningWindow("警告", "落子越界！");
			return;// 越界
		}

		// 逻辑坐标
		int row = (x - sx) / gap;
		int col = (y - sy) / gap;
		if ((x - sx) % gap > gap / 2)
			row++;
		if ((y - sy) % gap > gap / 2)
			col++;

		// 物理坐标
		x = sx + row * gap;
		y = sy + col * gap;

		// 如果此处已经有棋子则不能覆盖
		if (Model.getInstance().getChess(row, col) != Model.SPACE) {
			System.out.println("该位置已有棋子!");
			ChessBoardWindow.showWarningWindow("警告", "该位置已有棋子!");
			return;
		}

		int color = Model.getInstance().nowColor;
		putChessOnPhysicalBoard(row, col, color);// 在物理棋盘上落子
		Model.getInstance().putChessOnLogicalBoard(row, col, color);// 在逻辑棋盘上落子

		if (Control.onlineGame) {// 如果在线游戏
			// 将put指令发送给服务器，去更新对方的棋盘
			Client.getInstance().out.println("put," + row + "," + col + "," + color);
			try {
				ChessBoardWindow.getInstance().stopCountDown();// 落子后倒计时停止
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} else {
			System.out.println(Model.getInstance().nowColor);
			Model.getInstance().nowColor *= -1;
			// 倒计时停止
			try {
				ChessBoardWindow.getInstance().stopCountDown();// 停止上一个计时器
				ChessBoardWindow.getInstance().startCountDown();// 重新开始计时
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

		if (Control.openVoice) {// 下棋音效
			try {
				Voice.getInstance().putChessVoice();
			} catch (IOException | JavaLayerException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
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