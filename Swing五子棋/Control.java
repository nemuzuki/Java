package fiveChess;

public class Control {
	public int color = Model.BLACK;
	private Control() {
	}

	private static Control instance;

	public static Control getInstance() {
		if (instance == null) {
			instance = new Control();
		}
		return instance;
	}
	public void init() {
		color=Model.BLACK;
	}
	// 摆放棋子，并判断输赢
	public void localUserPutChess(int row, int col) {
		boolean success = Model.getInstance().putChess(row, col, color);
		if (success) {// 成功摆放棋子
			color = -color;// 当前棋子颜色改变
			int winner = Model.getInstance().whoWin();// 判断赢家
			switch (winner) {
			case Model.BLACK:
				System.out.println("Result: black win!");
				break;
			case Model.WHITE:
				System.out.println("Result: white win!");
				break;
			case Model.SPACE:
				break;
			}
		}
	}
	public static void main(String[] args) {
		CheckerBoard frame = new CheckerBoard("My Chess");
		frame.init();
	}
}