/**
 * @author Mika
 * 客户服务器模型，可以支持服务器和多个用户聊天
 * 为每个用户建立一个接受信息线程
 * 2021.12.9
 */
package hello;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class ServerReceiveThread extends Thread {// 专门用来接收信息的线程
	ServerFrame frame;
	int clientNum;

	public ServerReceiveThread(int clientNum, ServerFrame frame) {
		this.frame = frame;
		this.clientNum = clientNum;
	}

	public void run() {
		ClientInfo clientInfo = frame.clientInfos[clientNum];

		BufferedReader in = clientInfo.in;
		while (true) {
			try {
				String message = in.readLine();// 读取对方的信息
				if (frame.connect) {
					frame.logText.append("Client" + clientNum + ": " + message + "\n");// 日志里显示接收到的信息
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

class ClientInfo {// 用户信息类，主要方便使用I/O接口
	Socket s;
	BufferedReader in;
	PrintWriter out;

	public ClientInfo(Socket s, BufferedReader in, PrintWriter out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}
}

class ServerFrame extends JFrame {
	Box box = Box.createVerticalBox();

	JPanel panel1 = new JPanel();
	JLabel serverPortLabel = new JLabel("Server port:");
	JTextArea serverPortText = new JTextArea("6666");
	JButton stopStartButton = new JButton("Start");

	JLabel messagePleaseLabel = new JLabel("Input message");
	JTextArea messageText = new JTextArea(5, 10);// 输入框

	JPanel panel2 = new JPanel();
	JButton sendButton = new JButton("Send to");
	JComboBox<Integer> clientComboBox = new JComboBox<>();// 选择用户

	JLabel logLabel = new JLabel("Log");
	JTextArea logText = new JTextArea(5, 10);

	void initUI() {// 初始化界面
		panel1.add(serverPortLabel);
		panel1.add(serverPortText);
		panel1.add(stopStartButton);
		box.add(panel1);

		box.add(messagePleaseLabel);
		box.add(messageText);
		messageText.setFont(new Font("Consolas", 1, 20));

		panel2.add(sendButton);
		panel2.add(clientComboBox);
		box.add(panel2);
		box.add(logLabel);
		box.add(logText);
		logText.setFont(new Font("Consolas", 1, 20));
		logText.setLineWrap(true);// 自动换行
		add(box);
	}

	ServerSocket ss;
	Socket s;
	BufferedReader in;
	PrintWriter out;
	int port = Integer.parseInt(serverPortText.getText());
	boolean connect = true;// 是否保持连接

	ClientInfo[] clientInfos = new ClientInfo[20];// 保存用户信息的数组

	void communicate() throws IOException {// 通信模块
		ss = new ServerSocket(port);
		int clientCount = 0;// 监听到的用户数量
		while (true) {// 每监听到一个用户
			s = ss.accept();
			logText.append(s + "\n");
			int clientNum = ++clientCount;// 当前用户的编号
			clientComboBox.addItem(clientNum);

			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			ClientInfo newClientInfo = new ClientInfo(s, in, out);// 该用户的信息
			clientInfos[clientNum] = newClientInfo;// 用户号即索引
			new ServerReceiveThread(clientNum, this).start();// 建立接收该用户的信息线程

			stopStartButton.addActionListener(new ActionListener() {// 连接开关
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// 如果当前是stop
						if (stopStartButton.getText().equals("Stop")) {
							stopStartButton.setText("Start");
							logText.append("Connection close.\n");
							connect = false;
						} else {
							stopStartButton.setText("Stop");
							port = Integer.parseInt(serverPortText.getText());
							logText.append("Connection open, port=" + port + "\n");
							connect = true;

							ss = new ServerSocket(port);// 重新绑定端口号
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			sendButton.addActionListener(new ActionListener() {// 发送
				@Override
				public void actionPerformed(ActionEvent e) {
					// 先获取当前要发送给的用户
					int clientNum = (Integer) clientComboBox.getSelectedItem();
					ClientInfo nowClientInfo = clientInfos[clientNum];
					PrintWriter out = nowClientInfo.out;

					String message = messageText.getText();
					if (message.equals("")) {// 为了避免每按一次发送给每个用户都发，需要判空
						return;
					}
					messageText.setText("");// 清空输入框
					if (connect) {
						logText.append("Server->Client" + clientNum + ": " + message + "\n");
						out.println(message);// 向对方发送信息
					}
				}
			});
		}
	}

	public ServerFrame(String name) {
		super(name);
		setSize(400, 600);// x轴长度，y轴长度
		setLocation(300, 200);// 窗口位置
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setAlwaysOnTop(!isAlwaysOnTop());// 窗口总在最前
	}
}

public class Server {

	public static void main(String[] args) throws IOException {
		ServerFrame frame = new ServerFrame("Server");
		frame.initUI();
		frame.communicate();
	}
}
