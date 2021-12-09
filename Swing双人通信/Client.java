package hello;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class ClientReceiveThread extends Thread{//专门用来接收信息的线程
	ClientFrame frame;
	public ClientReceiveThread(ClientFrame frame) {
		this.frame=frame;
	}
	public void run() {
		while(true) {
			try {
				String message=frame.in.readLine();//读取对方的信息
				System.out.println(message);
				frame.logText.append("Server: "+message+"\n");
			} catch (IOException e) {
				break;
			}
		}
		
	}
}

class ClientFrame extends JFrame{
	Box box = Box.createVerticalBox();
	
	JPanel panel1=new JPanel();
	JLabel serverIPLabel=new JLabel("Server IP:");
	JTextArea serverIPText=new JTextArea("localhost");
	JLabel serverPortLabel=new JLabel("port:");
	JTextArea serverPortText=new JTextArea("6666");

	JLabel messagePleaseLabel=new JLabel("Input message");
	JTextArea messageText=new JTextArea(5,10);//输入框

	JPanel panel2=new JPanel();
	JButton sendButton=new JButton("Send");
	
	JLabel logLabel=new JLabel("Log");
	JTextArea logText=new JTextArea(5,10);

	
	void initUI(){
		panel1.add(serverIPLabel);
		panel1.add(serverIPText);
		panel1.add(serverPortLabel);
		panel1.add(serverPortText);
		box.add(panel1);
		
		box.add(messagePleaseLabel);
		box.add(messageText);
		messageText.setFont(new Font("Consolas", 1, 20));
		
		panel2.add(sendButton);
		box.add(panel2);
		box.add(logLabel);
		box.add(logText);
		logText.setFont(new Font("Consolas", 1, 20));
		logText.setLineWrap(true);//自动换行
		add(box);
	}

	Socket s;
	BufferedReader in;
	PrintWriter out;
	String ip=serverIPText.getText();
	int port=Integer.parseInt(serverPortText.getText());
	//通信模块
	void communicate() throws IOException {
		s=new Socket(ip,port);
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintWriter(s.getOutputStream(),true);//true表示flush，直接推出输出流的信息，不在缓冲区里保存
	
		new ClientReceiveThread(this).start(); 
		
		
		sendButton.addActionListener(new ActionListener() {//发送
			@Override
			public void actionPerformed(ActionEvent e) {
				String message=messageText.getText();
				logText.append("Send to server: "+message+"\n");
				messageText.setText("");//清空输入框
				out.println(message);//向对方发送信息
			}
		});
		
		
	}
	public ClientFrame(String name) {
		super(name);
		setSize(400, 600);//x轴长度，y轴长度
		setLocation(900, 200);// 窗口位置
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setAlwaysOnTop(!isAlwaysOnTop());//窗口总在最前
	}
}

public class Client {
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		ClientFrame frame=new ClientFrame("Client");
		frame.initUI();
		frame.communicate();
	}
}
