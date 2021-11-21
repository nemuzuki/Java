package Painter;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
	public int x1, y1, x2, y2;
	public String name;
	public Color color;
	public boolean fillRegion;//是否填充图形
    public Shape(){}
    public Shape(int x1,int y1,int x2,int y2, String name, Color color, boolean fillRegion) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.name = name;
		this.color = color;
		this.fillRegion=fillRegion;
    }
    public void drawShape(Graphics g){
    }
}

class Circle extends Shape {
	public Circle(){};
	public Circle(int x1,int y1,int x2,int y2,String name,Color color,boolean fillRegion){
		super(x1,y1,x2,y2,name,color,fillRegion);
	}
	
	public void drawShape(Graphics g){
		g.setColor(color);
        int r=Math.abs(x2 - x1);
        //圆心的x,y坐标，宽，高
		g.drawOval(Math.min(x1, x2), Math.min(y1, y2),r ,r);
		if(fillRegion) {
			g.fillOval(Math.min(x1, x2), Math.min(y1, y2),r ,r);
		}
	}
}

class Ellipse extends Shape {
	public Ellipse(){};
	public Ellipse(int x1,int y1,int x2,int y2,String name,Color color,boolean fillRegion){
		super(x1,y1,x2,y2,name,color,fillRegion);
	}
	
	public void drawShape(Graphics g){
		g.setColor(color);
        //圆心的x,y坐标，宽，高
		g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		if(fillRegion) {
			g.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		}
	}
}

class Rectangle extends Shape {
	public Rectangle(){};
	public Rectangle(int x1,int y1,int x2,int y2,String name,Color color,boolean fillRegion){
		super(x1,y1,x2,y2,name,color,fillRegion);
	}
	
	public void drawShape(Graphics g){
		g.setColor(color);
        //圆心的x,y坐标，宽，高
		g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		if(fillRegion) {
			g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		}
	}
}

class Line extends Shape{
	public Line(){}
	public Line(int x1,int y1,int x2,int y2,String name,Color color,boolean fillRegion){
		super(x1,y1,x2,y2,name,color,fillRegion);
	}
	
	public void drawShape(Graphics g){
		g.setColor(color);
        g.drawLine(x1,y1,x2,y2);	
	}
}

class Word extends Shape{
	public Word(){}
	public Word(int x1,int y1,int x2,int y2,String name,Color color,boolean fillRegion){
		super(x1,y1,x2,y2,name,color,fillRegion);
	}
	
	public void drawShape(Graphics g){
		g.setColor(color);
        g.drawString(name,x2,y2);	
	}
}