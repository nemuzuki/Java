package fiveChess;

import java.io.BufferedInputStream;
/**
 *
 *右击该工程->Properties->Java Build Path->Libraries->Add External JARs...
在弹出对话框中选择你所要加上的jmf.jar包。
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Voice {
	String path = "D:\\CloudMusic\\藤永龍太郎 - Sanctuary (Instrumental).mp3";
	
	String chessVoice="C:\\Users\\Mika\\Desktop\\mika_Java\\Chess\\src\\fiveChess\\下棋音效.wav";
	private static Voice instance;
	public static Voice getInstance() {
		if (instance == null) {
			instance = new Voice();
		}
		return instance;
	}
	//背景音乐
	void playBGM() throws IOException, JavaLayerException, InterruptedException{
		File music=new File(path);
        BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(music));
        Player player = new Player(buffer);
        new Thread(()-> {//lambda表达式
        	try {
				player.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
        }).start();
        
        System.out.println("start play "+path);
	}
	//下棋音效
	void putChessVoice() throws IOException, JavaLayerException, InterruptedException{
		File music=new File(path);
        BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(music));
        Player player = new Player(buffer);
        new Thread(()-> {//lambda表达式
        	try {
				player.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
        }).start();
        Thread.sleep(1000);
        player.close();
	}
	public static void main(String[] args) throws IOException, JavaLayerException, InterruptedException {
		Voice.getInstance().putChessVoice();
	}

}
