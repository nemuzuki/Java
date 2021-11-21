package Painter;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;

//画板的布局
class PainterFrame extends JFrame{
	JMenuBar menuBar=new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenu editMenu = new JMenu("Edit");
	JMenu helpMenu = new JMenu("Help");
	

	JLabel shapeColorLabel=new JLabel("Shape Color");
	JButton shapeColorBox=new JButton("Choose");
	JPanel shapeColorIcon=new JPanel();
	
	JLabel textLabel=new JLabel("Text");
	JTextField textField=new JTextField();//将要放进图像中的文字
	
	JLabel backgroundLabel=new JLabel("Background");
	JButton backGroundBox=new JButton("Choose");
	JPanel backgroundColorIcon=new JPanel();
	
	JLabel sizeLabel=new JLabel("Size");
	JSpinner sizeSpinner=new JSpinner();//微调器
	JLabel fillTheRegionLabel=new JLabel("Fill the region");
	JCheckBox fillTheRegion=new JCheckBox();//画图时是否将图形内部填充为当前颜色
	
	
	JPanel paintingBoard=new JPanel();//画布
	JLabel positionJLabel=new JLabel();
	
	
	void initUI(){
		//构造画笔和画笔的监听器
		Graphics graphics=this.getGraphics();
		Listener listener=new Listener();//画笔的监听器
		listener.setGraphics(this,graphics);
		addMouseListener(listener);//绑定鼠标事件监听
		addMouseMotionListener(listener);//绑定鼠标移动事件监听
		
		
		//菜单
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		JMenuItem openFileItem=new JMenuItem("Open");//打开文件
		JMenuItem saveFileItem=new JMenuItem("Save");//保存文件
		fileMenu.add(openFileItem);
		fileMenu.add(saveFileItem);
		openFileItem.addActionListener(new ActionListener(){//打开图像
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser("C:\\Users\\Mika\\Desktop\\mika_Java\\Painter\\src\\Painter");
				fileChooser.addChoosableFileFilter(new FileFilter() {//文件过滤器，只允许打开图片			
					@Override
					public String getDescription() {
						return "图片文件(*.jpg/*.png)";
					}
					
					@Override
					public boolean accept(File f) {
						if(f.getName().toLowerCase().endsWith(".jpg")||f.getName().toLowerCase().endsWith(".png")) {
							return true;
						}
						return false;
					}
				});
				fileChooser.showOpenDialog(openFileItem);//显示打开文件窗口
				File file=fileChooser.getSelectedFile();//打开的文件
				String filePath=file.getAbsolutePath();//文件的绝对路径
				System.out.println(filePath);
				
				JLabel graphLabel=new JLabel(); //在JPanel中加入图片需要先创建一个JLabel，再将其加入JPanel
				graphLabel.setIcon(new ImageIcon(filePath));
				paintingBoard.add(graphLabel);
			}
		});
		
		saveFileItem.addActionListener(new ActionListener(){//保存图像
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser("C:\\Users\\Mika\\Desktop\\mika_Java\\Painter\\src\\Painter");
				fileChooser.showSaveDialog(saveFileItem);
				File file=fileChooser.getSelectedFile();//打开的文件
				String filePath=file.getAbsolutePath();//文件的绝对路径
				
				BufferedImage image;
				//如果直接用panel.paint(g)只能保存空白图像，因此采用Robot的截图方法createScreenCapture
				try {
					image = new Robot().createScreenCapture(
							new Rectangle( paintingBoard.getX(), paintingBoard.getY(), 
									paintingBoard.getWidth(), paintingBoard.getHeight() ) );

					try {
						ImageIO.write(image, "jpg", file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (AWTException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
			}
		});
		
		JMenuItem clearScreenItem=new JMenuItem("Clear");//清屏
		clearScreenItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.clearScreen();
			}
		});
		editMenu.add(clearScreenItem);
		
		JMenuItem authorInfoItem=new JMenuItem("Author's information");
		authorInfoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel helpPanel=new JPanel();
				JOptionPane.showMessageDialog(helpPanel, "Mika 2021/11/20", "作者信息",JOptionPane.WARNING_MESSAGE);  
			}
		});
		helpMenu.add(authorInfoItem);
		setJMenuBar(menuBar);
		
		//上方
		JPanel above=new JPanel();
		above.add(shapeColorLabel);
		above.add(shapeColorIcon);
		above.add(shapeColorBox);
		above.add(backgroundLabel);
		above.add(backgroundColorIcon);
		above.add(backGroundBox);
		above.add(textLabel);
		above.add(textField);
		above.add(sizeLabel);
		above.add(sizeSpinner);
		above.add(fillTheRegionLabel);
		above.add(fillTheRegion);
		shapeColorIcon.setSize(10,10);//颜色小图标
		shapeColorIcon.setBackground(listener.color);
		shapeColorBox.setPreferredSize(new Dimension(100,20));
		shapeColorBox.addActionListener(new ActionListener() {//选色盘
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel sampleText = new JLabel("Label");
				listener.color = JColorChooser.showDialog(null, "Choose a Color", sampleText.getForeground());
				shapeColorIcon.setBackground(listener.color);
			}
		});
		
		textField.setPreferredSize(new Dimension(100,20));//文本框
		textField.setText("Type Text Here");
		
		backGroundBox.setPreferredSize(new Dimension(100,20));//选择背景颜色
		backgroundColorIcon.setSize(10,10);//颜色小图标
		backgroundColorIcon.setBackground(listener.backgroundColor);
		backGroundBox.addActionListener(new ActionListener() {//选色盘
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel sampleText = new JLabel("Label");
				listener.backgroundColor = JColorChooser.showDialog(null, "Choose a Color", sampleText.getForeground());
				backgroundColorIcon.setBackground(listener.backgroundColor);
				
				graphics.setColor(listener.backgroundColor);
				graphics.fillRect(95,85, 1090, 678);
				//每次改完背景颜色要输出一次保存的所有图形
				listener.drawAll();
			}
		});
		
		sizeSpinner.setPreferredSize(new Dimension(100,20));
		sizeSpinner.setValue(30);//文字大小微调器数值初始化
		sizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				listener.fontSize=(Integer)sizeSpinner.getValue();
			}
		});
		
		
		fillTheRegion.addActionListener(new ActionListener() {//是否填充图形
			@Override
			public void actionPerformed(ActionEvent e) {//填充选项变为相反值
				listener.fillRegion=(listener.fillRegion)?false:true;
			}
		});

		
		//形状按钮，纵向的组件用Box存
		Box left = Box.createVerticalBox();
		String[] shapeArray = { "circle", "ellipse", "line", "rectangle", "word" };
		for (int i = 0; i < shapeArray.length; i++) {
			JButton btn = new JButton(shapeArray[i]);
			btn.addActionListener(listener);//绑定监听器
			left.add(btn);//btn在box布局里，无法设置大小
		}
		
		
		//画布
		paintingBoard.setSize(1100,500);
		paintingBoard.setBackground(Color.white);
		add(BorderLayout.NORTH, above);
		add(BorderLayout.WEST, left);
		add(BorderLayout.CENTER, paintingBoard);
		add(BorderLayout.SOUTH,positionJLabel);
		

	}
	public PainterFrame(String name) {
		super(name);
		setSize(1100, 700);// 窗口大小：宽，高
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 用户单击窗口的关闭按钮时程序执行的操作
	}
}

public class Painter {
	public static void main(String[] args) {
		PainterFrame frame=new PainterFrame("Mika's Painter");
		frame.initUI();//必须在创建frame对象之后调用，不然graphics=null
		//frame.pack();
	}

}
