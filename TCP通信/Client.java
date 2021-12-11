package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ReceiveThread extends Thread{//专门用来接收信息的线程
	BufferedReader in;
	public ReceiveThread(BufferedReader in) {
		this.in=in;
	}
	public void run() {
		while(true) {
			try {
				String message=in.readLine();//读取对方的信息
				System.out.println(message);
				if(message.equals("q")) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
public class Client {
	public static void main(String[] args) throws IOException {
		Socket s=new Socket("localhost",6666);//服务端的端口号是6666
		BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out=new PrintWriter(s.getOutputStream(),true);//true表示flush，直接推出输出流的信息，不在缓冲区里保存
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		
		new ReceiveThread(in).start(); 
		String message;
		while(true) {
			message = stdin.readLine();
			out.println(message);//向对方发送信息
			if(message.equals("q")) {
				break;
			}
		}
		in.close();
		out.close();
		s.close();
	}
}
