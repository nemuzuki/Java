/**
 * @author Mika
 */
package MyCalculator;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//BorderLayout中嵌套GridLayout
class BorderLayoutDemo extends JFrame {
	private JLabel output = new JLabel("0");//结果显示框

	class GridLayoutDemo extends JPanel {
		private String operator;//当前的运算符
		private double op1;//操作数1
		private double op2;//操作数2
		private double answer;//临时结果
		
		private boolean lastPressIsNum=false;//判断上一次按下的键是不是数字
		
		private JButton btn0 = new JButton("0");
		private JButton btn1 = new JButton("1");
		private JButton btn2 = new JButton("2");
		private JButton btn3 = new JButton("3");
		private JButton btn4 = new JButton("4");
		private JButton btn5 = new JButton("5");
		private JButton btn6 = new JButton("6");
		private JButton btn7 = new JButton("7");
		private JButton btn8 = new JButton("8");
		private JButton btn9 = new JButton("9");
		
		private JButton btnAdd = new JButton("+");
		private JButton btnSub = new JButton("-");
		private JButton btnMul = new JButton("×");
		private JButton btnDiv = new JButton("÷");
		private JButton btnEql = new JButton("=");

		private JButton btnPercent = new JButton("%");
		private JButton btnCE = new JButton("CE");
		private JButton btnC = new JButton("C");
		private JButton btnBackspace = new JButton("<×");
		
		private JButton btnCountBackwards = new JButton("1/x");
		private JButton btnSquare = new JButton("x^2");
		private JButton btnSqrt = new JButton("sqrt(x)");
		
		private JButton btnChangeSign = new JButton("+/-");
		private JButton btnPoint = new JButton(".");
		
		//按下数字按钮的处理方法
		class BtnNumProcess implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(lastPressIsNum==false) {//如果刚才按下的不是数字，则清空label
					output.setText("");
				}
				lastPressIsNum=true;
				
				String nowText = output.getText();
				String btnNum = e.getActionCommand();// 得到按钮上的字
				try{//尝试把label中内容转为数字，如果里面是错误信息则清空
					Double.parseDouble(nowText);
				}
				catch (Exception e1) {
					nowText="";
				}
				if(nowText=="0") {
					nowText="";
				}
				output.setText(nowText + btnNum);
			}
		}
		//小数点
		class BtnPointProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=true;
				String labelContent=output.getText();
				for(int i=0;i<labelContent.length();++i){
					if(labelContent.charAt(i)=='.'){//如果当前label里有小数点
						return;
					}
				}
				output.setText(labelContent+".");
			}
		}
		//按下加减乘除运算符
		class BtnOperatorProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;

				op2=Double.parseDouble(output.getText());//操作数2
				//对于连续的运算，按下运算符首先相当于输出上一步的结果
				if(operator!=null) {
					switch (operator) {//根据之前输入的运算符计算
					case "+":
						answer=op1+op2;
						break;
					case "-":
						answer=op1-op2;
						break;
					case "×":
						answer=op1*op2;
						break;
					case "÷":
						if(op2==0) {
							output.setText("Zero cannot be divisor!");
							return;
						}
						else answer=op1/op2;
						break;
					default:
						break;
					}
					//如果结果是整数，则不写.0
					if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
						output.setText(Math.round(answer)+"");
					}
					else {
						output.setText(answer+"");
					}
					System.out.println(op1+operator+op2+"="+answer);//输出算式记录
				}
				
				
				//将当前label内容转为操作数1
				op1=Double.parseDouble(output.getText());
				operator=e.getActionCommand();//设置当前运算符
			}
		}
		//等号
		class BtnEqualProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				op2=Double.parseDouble(output.getText());//操作数2
				switch (operator) {//根据之前输入的运算符计算
				case "+":
					answer=op1+op2;
					break;
				case "-":
					answer=op1-op2;
					break;
				case "×":
					answer=op1*op2;
					break;
				case "÷":
					if(op2==0) {
						output.setText("Zero cannot be divisor!");
						return;
					}
					else answer=op1/op2;
					break;
				default:
					break;
				}
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
				System.out.println(op1+operator+op2+"="+answer);//输出算式记录
				operator=null;
			}
		}
		//倒数
		class BtnCountBackwardsProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				answer=Double.parseDouble(output.getText());
				if(answer==0) {
					output.setText("Zero cannot be divisor!");
					return;
				}
				answer=1/answer;
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
			}
		}
		//平方
		class BtnSquareProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				answer=Double.parseDouble(output.getText());
				answer*=answer;
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
			}
		}
		//开方
		class BtnSqrtProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				answer=Double.parseDouble(output.getText());
				if(answer<0) {//根号下不能是负数
					output.setText("Invalid i)nput!");
					return;
				}
				answer=Math.sqrt(answer);
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
			}
		}
		//取相反数
		class BtnChangeSignProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				answer=Double.parseDouble(output.getText());
				answer=-answer;
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
			}
		}
		//百分号
		class BtnPercentProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				answer=Double.parseDouble(output.getText());
				answer/=100;
				//如果结果是整数，则不写.0
				if(Math.abs(answer - Math.round(answer)) < Double.MIN_VALUE) {
					output.setText(Math.round(answer)+"");
				}
				else {
					output.setText(answer+"");
				}
			}
		}
		//C：清除所有内容，相当于初始化
		class BtnCProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				op1=op2=answer=0;
				operator=null;
				output.setText("0");
			}
		}
		//CE：清除op2
		class BtnCEProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				output.setText("0");
			}
		}
		//退格
		class BtnBackspaceProcess implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressIsNum=false;
				String labelContent=output.getText();
				String newContentString="";
				for(int i=0;i<labelContent.length()-1;++i) {
					newContentString+=labelContent.charAt(i);
				}
				if(newContentString.length()==0) {//清除完显示0
					output.setText("0");
				}
				else output.setText(newContentString);
			}
		}
		
		//设置按钮和对应的事件
		public GridLayoutDemo() {
			setLayout(new GridLayout(6, 4));
			
			add(btnPercent);
			add(btnCE);
			add(btnC);
			add(btnBackspace);
			btnPercent.addActionListener(new BtnPercentProcess());
			btnCE.addActionListener(new BtnCEProcess());
			btnC.addActionListener(new BtnCProcess());
			btnBackspace.addActionListener(new BtnBackspaceProcess());

			add(btnCountBackwards);
			add(btnSquare);
			add(btnSqrt);
			add(btnDiv);
			btnCountBackwards.addActionListener(new BtnCountBackwardsProcess());
			btnSquare.addActionListener(new BtnSquareProcess());
			btnSqrt.addActionListener(new BtnSqrtProcess());
			btnDiv.addActionListener(new BtnOperatorProcess());

			add(btn7);
			add(btn8);
			add(btn9);
			add(btnMul);
			btn7.addActionListener(new BtnNumProcess());
			btn8.addActionListener(new BtnNumProcess());
			btn9.addActionListener(new BtnNumProcess());
			btnMul.addActionListener(new BtnOperatorProcess());

			add(btn4);
			add(btn5);
			add(btn6);
			add(btnSub);
			btn4.addActionListener(new BtnNumProcess());
			btn5.addActionListener(new BtnNumProcess());
			btn6.addActionListener(new BtnNumProcess());
			btnSub.addActionListener(new BtnOperatorProcess());

			add(btn1);
			add(btn2);
			add(btn3);
			add(btnAdd);
			btn1.addActionListener(new BtnNumProcess());
			btn2.addActionListener(new BtnNumProcess());
			btn3.addActionListener(new BtnNumProcess());
			btnAdd.addActionListener(new BtnOperatorProcess());

			add(btnChangeSign);
			add(btn0);
			add(btnPoint);
			add(btnEql);
			btnChangeSign.addActionListener(new BtnChangeSignProcess());
			btn0.addActionListener(new BtnNumProcess());
			btnPoint.addActionListener(new BtnPointProcess());
			btnEql.addActionListener(new BtnEqualProcess());
		}
	}

	public BorderLayoutDemo(String name) {
		super(name);

		output.setFont(new java.awt.Font("Consolas", 1, 48));
		add(BorderLayout.NORTH, output);//结果标签
		add(BorderLayout.SOUTH, new GridLayoutDemo());
	}
}

public class MyCalculator {
	public static void main(String[] args) {
		BorderLayoutDemo frame = new BorderLayoutDemo("My Calculator");
		frame.setIconImage(new ImageIcon("src/MyCalculator/yuri.jpg").getImage());
		frame.setLocation(300, 300);// 窗口位置
		frame.setSize(600, 200);// 窗口大小
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 用户单击窗口的关闭按钮时程序执行的操作
		frame.pack();
		frame.setVisible(true);// 展示窗口
	}
}
