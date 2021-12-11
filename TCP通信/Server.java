package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	public static void main(String[] args) throws IOException {
		ServerSocket ss=new ServerSocket(6666);//服务端的端口号是6666
		System.out.println("begin listen");
		Socket s=ss.accept();
		System.out.println("receive client"+s);
		
		BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out=new PrintWriter(s.getOutputStream(),true);//输出流
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
		ss.close();
	}
}
