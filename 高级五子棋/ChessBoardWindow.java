package fiveChess;

/**
 * 下棋界面类
 */
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javazoom.jl.decoder.JavaLayerException;

class ChessBoardWindow {
	JFrame chessBoardFrame = new JFrame();
	
	JMenuBar menuBar=new JMenuBar();
	JMenu helpMenu = new JMenu("Help");
	JMenuItem authorInfoItem=new JMenuItem("Author's information");

	JPanel north = new JPanel();
	JButton startButton = new JButton("Start");
	JButton regretButton = new JButton("Regret");
	JButton lastButton = new JButton("←");
	JButton nextButton = new JButton("→");

	JPanel west = new JPanel();
	JLabel timeLabel = new JLabel("60");// 倒计时

	JPanel chessBoard = new JPanel();// 棋盘本体

	Box east = Box.createVerticalBox();// 聊天区域
	JLabel messagePleaseLabel = new JLabel("Input");
	JTextArea messageText = new JTextArea(5, 5);// 输入框
	JButton sendButton = new JButton("Send");
	JLabel logLabel = new JLabel("Log");
	JTextArea logText = new JTextArea(5, 10);

	JPanel south = new JPanel();
	JLabel usernameLabel = new JLabel();

	void initUI() {
		menuBar.add(helpMenu);
		helpMenu.add(authorInfoItem);
		chessBoardFrame.setJMenuBar(menuBar);
		// 上方的操作栏
		north.add(startButton);
		north.add(regretButton);
		north.add(lastButton);
		north.add(nextButton);

		west.add(timeLabel);

		// 聊天区域
		east.add(messagePleaseLabel);
		east.add(new JScrollPane(messageText));
		east.add(sendButton);
		east.add(logLabel);
		east.add(new JScrollPane(logText));
		messageText.setFont(new Font("Consolas", 1, 20));
		logText.setFont(new Font("Consolas", 1, 20));
		messageText.setLineWrap(true);// 自动换行
		logText.setLineWrap(true);// 自动换行
		// 计时器
		timeLabel.setFont(new Font("Consolas", 1, 30));
		south.add(usernameLabel);
		usernameLabel.setFont(new Font("Consolas", 1, 20));
		usernameLabel.setText("Username:" + Client.getInstance().username);

		chessBoardFrame.add(BorderLayout.NORTH, north);
		chessBoardFrame.add(BorderLayout.WEST, west);
		chessBoardFrame.add(BorderLayout.CENTER, chessBoard);
		chessBoardFrame.add(BorderLayout.EAST, east);
		chessBoardFrame.add(BorderLayout.SOUTH, south);
	}

	void initListener() {
		Graphics g = chessBoardFrame.getGraphics();
		PhysicalBoard.getInstance(g);
		chessBoardFrame.addMouseListener(PhysicalBoard.getInstance());// 绑定鼠标事件监听
		chessBoardFrame.addMouseMotionListener(PhysicalBoard.getInstance());// 绑定鼠标移动事件监听

		authorInfoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel helpPanel=new JPanel();
				JOptionPane.showMessageDialog(helpPanel, "Mika 2021/12", "作者信息",JOptionPane.WARNING_MESSAGE);  
			}
		});

		startButton.addActionListener(new ActionListener() {// 重新开局
			@Override
			public void actionPerformed(ActionEvent e) {// 各种类的初始化
				Model.getInstance().initializeMap();
				PhysicalBoard.getInstance().init();
				stopCountDown();

			}
		});
		regretButton.addActionListener(new ActionListener() {// 悔棋
			@Override
			public void actionPerformed(ActionEvent e) {
				PhysicalBoard.getInstance().regret();
				if (Control.onlineGame) {
					Client.getInstance().out.println("reg");
				}
				startCountDown();
			}
		});

		lastButton.addActionListener(new ActionListener() {// 复盘-后退

			@Override
			public void actionPerformed(ActionEvent e) {
				PhysicalBoard.getInstance().stepBack();
			}
		});

		nextButton.addActionListener(new ActionListener() {// 复盘-前进
			@Override
			public void actionPerformed(ActionEvent e) {
				PhysicalBoard.getInstance().stepForward();

			}
		});

		currentTimeThread = new currentTimeThread();// 倒计时线程

		sendButton.addActionListener(new ActionListener() {// 发送信息
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = messageText.getText();
				messageText.setText("");// 清空输入框
				logText.append("Me:" + message + "\n");
				Client.getInstance().out.println(message);
			}
		});
	}

	// 倒计时模块
	currentTimeThread currentTimeThread;

	void startCountDown() {// 倒计时启动
		stopCountDown();//先把之前的倒计时线程结束
		currentTimeThread = new currentTimeThread();
		currentTimeThread.start();
	}

	void stopCountDown() {// 倒计时停止
		currentTimeThread.exit = true;
		timeLabel.setText("60");
	}

	// 建立一个线程，不断更新当前时间
	class currentTimeThread extends Thread {
		boolean exit = false;// 是否要退出线程
		int internelTime = 60;

		public currentTimeThread() {
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (exit == false) {// 不退出线程时，不断输出间隔时间
					internelTime--;
					if (internelTime == 0) {
						exit = true;
					}
					timeLabel.setText(internelTime + "");
				}
			}
		}
	}

	//警告弹窗
	static void showWarningWindow(String title,String message) {
		JPanel helpPanel=new JPanel();
		JOptionPane.showMessageDialog(helpPanel, message, title,JOptionPane.WARNING_MESSAGE);  
	}
	
	void init(String name) throws IOException, JavaLayerException, InterruptedException {
		chessBoardFrame.setTitle(name);
		chessBoardFrame.setSize(1000, 700);// x轴长度，y轴长度
		chessBoardFrame.setLocation(300, 100);// 窗口位置
		chessBoardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chessBoardFrame.setVisible(true);
		initUI();
		initListener();
		if (Control.openVoice) {
			Voice.getInstance().playBGM();// 播放背景音乐
		}
	}
	public ChessBoardWindow() {}

	private static ChessBoardWindow instance;

	public static ChessBoardWindow getInstance() throws IOException {
		if (instance == null) {
			instance = new ChessBoardWindow();
		}
		return instance;
	}
	public static void main(String[] args) throws IOException, JavaLayerException, InterruptedException {
		ChessBoardWindow.getInstance().init("Gobang!");
	}
}