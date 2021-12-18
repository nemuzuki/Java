package fiveChess;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import javazoom.jl.decoder.JavaLayerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientReceiveThread extends Thread {// 专门用来接收服务器信息的线程

	void numProcess(String[] item) {
		int clientNum = Integer.parseInt(item[1]);
		Client.getInstance().clientNum = clientNum;
		System.out.println("玩家" + item[1] + "上线！");
		if (Client.getInstance().clientNum % 2 == 0) {
			Model.getInstance().nowColor = Model.BLACK;
		} else {
			Model.getInstance().nowColor = Model.WHITE;
		}
	}

	void putProcess(String[] item) throws IOException {
		int row = Integer.parseInt(item[1]);
		int col = Integer.parseInt(item[2]);
		int color = Integer.parseInt(item[3]);

		// 分别在物理棋盘和逻辑棋盘上落下对方的子
		PhysicalBoard.getInstance().putChessOnPhysicalBoard(row, col, color);
		Model.getInstance().putChessOnLogicalBoard(row, col, color);
		// 一旦对方落子，启动倒计时
		ChessBoardWindow.getInstance().startCountDown();
	}

	void regProcess(String[] item) throws IOException {
		PhysicalBoard.getInstance().regret();// 更新棋盘
		ChessBoardWindow.getInstance().stopCountDown();// 倒计时停止
	}


	public void run() {
		while (true) {
			try {
				String message = Client.getInstance().in.readLine();// 读取对方的信息
				System.out.println("Server:"+message);
				// 获得服务器转发的指令后，进行解析
				String[] item = message.split(",");

				if (item[0].equals("num")) {// num指令，获取用户编号
					numProcess(item);
				} else if (item[0].equals("put")) {// put指令，用来落子
					putProcess(item);
				} else if (item[0].equals("reg")) {// 对方悔棋
					regProcess(item);
				} else {// 只是普通的聊天
					ChessBoardWindow.getInstance().logText.append(message + "\n");
				}
			} catch (IOException e) {
				break;
			}
		}

	}
}

public class Client {
	int clientNum;// 编号
	Socket s;
	BufferedReader in;
	PrintWriter out;
	String username;

	private Client() {
	}

	void connect() throws IOException {
		s = new Socket("localhost", 6666);// 服务端的端口号是6666
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);

		new ClientReceiveThread().start();
	}

	private static Client instance;

	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public static void main(String[] args) throws IOException {
		new ClientLoginWindow();
	}
}

//用户登录界面
class ClientLoginWindow {
	JFrame loginFrame = new JFrame("Login");
	Container container = loginFrame.getContentPane();
	JPanel fieldPanel = new JPanel();

	JLabel playChessLabel = new JLabel("五子棋");
	JLabel usernameLabel = new JLabel("用户名");
	JTextArea usernameTextArea = new JTextArea();
	JLabel passwordLabel = new JLabel("Password");
	JTextArea passwordTextArea = new JTextArea();
	JButton loginButton = new JButton("登录");
	

	JButton onlineButton = new JButton("在线下棋");
	JButton offlineButton = new JButton("本地下棋");

	String username;
	String password;

	void initUI() {
		fieldPanel.setLayout(null);
		playChessLabel.setBounds(125, 20, 200, 20);
		usernameLabel.setBounds(50, 50, 60, 20);// x,y,width,height
		usernameTextArea.setBounds(110, 50, 120, 20);
//		passwordLabel.setBounds(50, 80, 60, 20);
//		passwordTextArea.setBounds(110, 80, 120, 20);
//		loginButton.setBounds(100, 120, 120, 20);
		onlineButton.setBounds(50, 80, 100, 20);
		offlineButton.setBounds(160, 80, 100, 20);

		fieldPanel.add(playChessLabel);
		fieldPanel.add(usernameLabel);
		fieldPanel.add(usernameTextArea);
//		fieldPanel.add(passwordLabel);
//		fieldPanel.add(passwordTextArea);
//		fieldPanel.add(loginButton);
		fieldPanel.add(onlineButton);
		fieldPanel.add(offlineButton);
		container.add(fieldPanel, "Center");
	}

	void initListener() {
		loginButton.addActionListener(new ActionListener() {// 登录
			@Override
			public void actionPerformed(ActionEvent e) {
				username = usernameTextArea.getText();
				password = passwordTextArea.getText();
				Client.getInstance().username = username;
				loginFrame.setVisible(false);// 一旦点击登录，就关闭当前界面

				try {
					if (Control.onlineGame) {
						Client.getInstance().connect();
					}
					ChessBoardWindow.getInstance().init("My chess");
				} catch (JavaLayerException | InterruptedException | IOException e2) {
					e2.printStackTrace();

				}

			}
		});
		onlineButton.addActionListener(new ActionListener() {// 在线
			@Override
			public void actionPerformed(ActionEvent e) {
				Control.onlineGame=true;
				username = usernameTextArea.getText();
				password = passwordTextArea.getText();
				Client.getInstance().username = username;
				loginFrame.setVisible(false);// 一旦点击登录，就关闭当前界面

				try {
					if (Control.onlineGame) {
						Client.getInstance().connect();
					}
					ChessBoardWindow.getInstance().init("My chess");
				} catch (JavaLayerException | InterruptedException | IOException e2) {
					e2.printStackTrace();

				}

			}
		});
		offlineButton.addActionListener(new ActionListener() {// 本地
			@Override
			public void actionPerformed(ActionEvent e) {
				Control.onlineGame=false;
				username = usernameTextArea.getText();
				password = passwordTextArea.getText();
				Client.getInstance().username = username;
				loginFrame.setVisible(false);// 一旦点击登录，就关闭当前界面

				try {
					ChessBoardWindow.getInstance().init("My chess");
				} catch (JavaLayerException | InterruptedException | IOException e2) {
					e2.printStackTrace();

				}

			}
		});
	}

	public ClientLoginWindow() {
		loginFrame.setBounds(600, 200, 300, 220);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setVisible(true);
		loginFrame.setAlwaysOnTop(!loginFrame.isAlwaysOnTop());// 窗口总在最前
		initUI();
		initListener();
	}
}
