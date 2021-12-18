package fiveChess;

/* 共有两个棋盘：
 * Model.data是逻辑棋盘
 * ChessBoardWindow是物理棋盘
 * 先在物理棋盘上点击，映射到逻辑棋盘的二维数组中，再进行处理
 */
public class Model {
	private int[][] data = new int[WIDTH][WIDTH];// 棋盘，默认所有元素都是0
	int lastRow, lastCol, lastColor;// 保存上一步的行列和颜色
	public int nowColor;//当前颜色。在线对弈时，每个玩家的该变量恒定；本地游戏时，该变量每步都变化
	
	public static final int BLACK = 1;
	public static final int WHITE = -1;
	public static final int SPACE = 0;
	public static final int WIDTH = 19;


	private Model() {
		nowColor=BLACK;
	}

	private static Model instance;

	public static Model getInstance() {// Model的惰性初始化
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}
	
	//初始化棋盘
	public void initializeMap() {
		for(int i=0;i<WIDTH;++i){
			for(int j=0;j<WIDTH;++j){
				data[i][j]=SPACE;
			}
		}
	}
	//获得坐标颜色
	public int getChess(int row, int col) {
		if (row >= 0 && row < WIDTH && col >= 0 && col < WIDTH) {
			return data[row][col];
		}
		return SPACE;
	}
	//逻辑棋盘上悔棋
	void regretOnLogicalBoard(int row,int col) {
		data[row][col]=SPACE;
	}
	// 输出棋盘
	public void outputChess() {
		for (int row = 0; row < Model.WIDTH; row++) {
			for (int col = 0; col < Model.WIDTH; col++) {
				int chess = Model.getInstance().getChess(row, col);
				switch (chess) {
				case Model.BLACK:
					System.out.print("@");
					break;
				case Model.WHITE:
					System.out.print("O");
					break;
				case Model.SPACE:
					System.out.print(".");
					break;
				}
			}
			System.out.println();
		}
	}
	// 逻辑棋盘上摆放棋子
	public void putChessOnLogicalBoard(int row, int col, int color) {
		if ((row >= 0 && row < WIDTH) && (col >= 0 && col < WIDTH) && (color == BLACK || color == WHITE)) {
			data[row][col] = color;
			lastRow = row;
			lastCol = col;
			lastColor = color;
			
			int winner = whoWin(row,col);// 判断赢家
			switch (winner) {
			case Model.BLACK:
				System.out.println("Result: black win!");
				ChessBoardWindow.showWarningWindow("游戏结束","黑方胜利！");
				break;
			case Model.WHITE:
				System.out.println("Result: white win!");
				ChessBoardWindow.showWarningWindow("游戏结束","白方胜利！" );
				break;
			case Model.SPACE:
				break;
			}
//			outputChess();
		}
	}
	
	// 判断胜负
	public int whoWin(int row,int col) {
		// 根据当前的行列，搜索周围，判断是否五连
		// 向左搜索的相连数+向右搜索>=5?
		int sameColorCnt = 1;//当前搜索到相连棋子数量
		int nowRow = row, nowCol = col;
		int maxLength=5;//五个相连就胜利
		// 向左右搜
		for (int i = 1; i < maxLength; ++i) {// 向左搜
			nowRow = lastRow;
			nowCol = lastCol - i;
			if (nowCol < 0)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		for (int i = 1; i < maxLength; ++i) {// 向右搜
			nowRow = lastRow;
			nowCol = lastCol + i;
			if (nowCol >= WIDTH)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		if (sameColorCnt >= maxLength) {
			return lastColor;
		}
		
		// 向上下搜
		sameColorCnt = 1;
		
		for (int i = 1; i < maxLength; ++i) {// 向上搜
			nowRow = lastRow - i;
			nowCol = lastCol;
			if (nowRow < 0)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		for (int i = 1; i < maxLength; ++i) {// 向下搜
			nowRow = lastRow + i;
			nowCol = lastCol;
			if (nowRow >= WIDTH)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		if (sameColorCnt >= maxLength) {
			return lastColor;
		}
		
		// 向左上右下搜
		sameColorCnt = 1;
		
		for (int i = 1; i < maxLength; ++i) {// 向左上搜
			nowRow = lastRow - i;
			nowCol = lastCol - i;
			if (nowRow < 0 || nowCol < 0)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		for (int i = 1; i < maxLength; ++i) {// 向右下搜
			nowRow = lastRow + i;
			nowCol = lastCol + i;
			if (nowRow >= WIDTH || nowCol >= WIDTH)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		if (sameColorCnt >= maxLength) {
			return lastColor;
		}
		
		// 向右上左下搜
		sameColorCnt = 1;
		
		for (int i = 1; i < maxLength; ++i) {// 向右上搜
			nowRow = lastRow - i;
			nowCol = lastCol + i;
			if (nowRow < 0 || nowCol >= WIDTH)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		for (int i = 1; i < maxLength; ++i) {// 向左下搜
			nowRow = lastRow + i;
			nowCol = lastCol - i;
			if (nowRow >= WIDTH || nowCol < 0)
				break;
			if (getChess(nowRow, nowCol) == lastColor) {
				sameColorCnt++;
			}
		}
		if (sameColorCnt >= maxLength) {
			return lastColor;
		}
	
		return SPACE;// 还没决出胜负
	
	}
}
