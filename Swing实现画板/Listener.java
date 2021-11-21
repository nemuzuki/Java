package Painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

//监听器：按下松开鼠标，移动鼠标，点击按键
public class Listener implements MouseListener, MouseMotionListener, ActionListener{
	int x1,y1,x2,y2;
	String name="line";
	Color color=Color.black;
	Color backgroundColor=Color.white;
	Graphics g;//画笔
	PainterFrame p;
	ArrayList<Shape> shapeList=new ArrayList<>();
	boolean fillRegion=false;//是否填充图形
	int fontSize=30;
	
	//初始化
	public void setGraphics(PainterFrame painter,Graphics graphics) {
		g=graphics;
		p=painter;
	}
	//清屏
	public void clearScreen() {
		g.setColor(Color.white);
		g.fillRect(95,85, 1090, 678);
	}
	//把Shape数组里存的所有图形输出
	public void drawAll(){
		for(Shape s:shapeList){
			s.drawShape(g);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {//按键动作
		name=e.getActionCommand();//当前点击的按键名
	}

	@Override
	public void mousePressed(MouseEvent e) {//鼠标按下不放
		x1=e.getX();
		y1=e.getY();
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {//鼠标点击
		x2=e.getX();
		y2=e.getY();
		if(name.equals("word")) {//在点击的位置插入文字
			g.setFont(new Font("Consolas", Font.PLAIN, fontSize));//设置文字格式
			String textString=p.textField.getText();
			g.drawString(textString,x2,y2);
			shapeList.add(new Word(x1,y1,x2,y2,textString,color,fillRegion));
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {//鼠标拖动
		x2=e.getX();
		y2=e.getY();
		p.positionJLabel.setText("("+x2+","+y2+")");
		g.setColor(color);
	}



	@Override
	public void mouseReleased(MouseEvent e) {//鼠标松开
		x2=e.getX();
		y2=e.getY();
		g.setColor(color);
		if(name.equals("line")){
			Shape line=new Line(x1,y1,x2,y2,"line",color,fillRegion);
			shapeList.add(line);//画完一个图形后存入shapeList，以后可以恢复
			line.drawShape(g);
		}
		else if(name.equals("circle")) {
	        int r=Math.abs(x2 - x1);
	        Shape circle=new Circle(x1,y1,x2,y2,"circle",color,fillRegion);
			shapeList.add(circle);
			circle.drawShape(g);
		}
		else if(name.equals("ellipse")) {
			Shape ellipse=new Ellipse(x1,y1,x2,y2,"ellipse",color,fillRegion);
			shapeList.add(ellipse);
			ellipse.drawShape(g);
		}
		else if(name.equals("rectangle")) {
			Shape rectangle=new Rectangle(x1,y1,x2,y2,"rectangle",color,fillRegion);
			shapeList.add(rectangle);
			rectangle.drawShape(g);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x=e.getX(),y=e.getY();
		p.positionJLabel.setText("("+x+","+y+")");
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

