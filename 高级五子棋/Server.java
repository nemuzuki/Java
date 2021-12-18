package fiveChess;
/**
 * @author Mika
 * 服务器界面
 * 监听用户
 * 给用户分配对手
 * 接收一方发来的物理坐标，并发给另一方（put指令）
 * 接收玩家的悔棋请求（reg指令）
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class ServerReceiveThread extends Thread{//专门用来接收服务器发来的物理坐标的线程
	int clientNum;
	public ServerReceiveThread(int clientNum){
		this.clientNum=clientNum;
	}
	public void run() {
		while(true) {
			try {
				String message=Server.getInstance().clientInfos[clientNum].in.readLine();//接收用户端的信息
				int rivalNum=Server.getInstance().rivalMap[clientNum];
				System.out.println(clientNum+":"+message);
				Server.getInstance().clientInfos[rivalNum].out.println(message);
				//然后转发给另一方
			} catch (IOException e) {
				break;
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
public class Server {
	ServerSocket ss;
	Socket s;
	BufferedReader in;
	PrintWriter out;
	BufferedReader stdin;
	ClientInfo[] clientInfos = new ClientInfo[20];// 玩家信息表
	int[]rivalMap=new int[20];//玩家对手表：保存每个玩家的对手的用户号
	

	void init() throws IOException {
		ss=new ServerSocket(6666);//服务端的端口号是6666
		System.out.println("begin listen");
		int clientCount = 0;// 监听到的用户数量

		while(true) {//不断监听新的用户
			s=ss.accept();
			int clientNum = clientCount++;// 当前用户的编号
			System.out.println("Client"+clientNum+" is online");
			
			in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			out=new PrintWriter(s.getOutputStream(),true);//输出流
			ClientInfo newClientInfo = new ClientInfo(s, in, out);// 该用户的信息
			clientInfos[clientNum] = newClientInfo;
			//告知玩家自己的编号，偶黑奇白
			out.println("num,"+clientNum);
			
			//分配对手，编号相邻
			if(clientNum%2==0) {
				rivalMap[clientNum]=clientNum+1;
			}
			else {
				rivalMap[clientNum]=clientNum-1;
			}
			new ServerReceiveThread(clientNum).start(); 
		}	
	}
	private static Server instance;
	public static Server getInstance() throws IOException {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}
	public static void main(String[] args) throws IOException {
		Server.getInstance().init();
	} 
}
