package fiveChess;

/* 共有两个棋盘：
 * Model.data是逻辑棋盘
 * CheckerBoard是物理棋盘
 * 先在物理棋盘上点击，映射到逻辑棋盘的二维数组中，再进行处理
 */
public class Model {
	public static final int BLACK = 1;
	public static final int WHITE = -1;
	public static final int SPACE = 0;
	
	public static final int WIDTH = 19;

	private int[][] data = new int[WIDTH][WIDTH];// 棋盘，默认所有元素都是0
	private int lastRow, lastCol, lastColor;// 保存上一步的行列和颜色

	private Model() {}

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

	// 摆放棋子
	public boolean putChess(int row, int col, int color) {
		if ((row >= 0 && row < WIDTH) && (col >= 0 && col < WIDTH) && (color == BLACK || color == WHITE)) {
			if (data[row][col] == SPACE) {
				data[row][col] = color;
				lastRow = row;
				lastCol = col;
				lastColor = color;
				return true;
			}
		}
		return false;
	}

	// 判断胜负
	public int whoWin() {
		// 根据当前的行列，搜索周围，判断是否五连
		// 向左搜索的相连数+向右搜索>=5?
		int sameColorCnt = 1;//当前搜索到相连棋子数量
		int nowRow = lastRow, nowCol = lastCol;
		int maxLength=5;//五个相连就胜利
		// 向左右搜
		{
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
		}
		// 向上下搜
		sameColorCnt = 1;
		{
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
		}
		// 向左上右下搜
		sameColorCnt = 1;
		{
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
		}
		// 向右上左下搜
		sameColorCnt = 1;
		{
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
		}
		return SPACE;// 还没决出胜负
	}

}
