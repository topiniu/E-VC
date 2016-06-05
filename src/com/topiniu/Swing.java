package com.topiniu;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JOptionPane;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;

class SwingMain extends JFrame implements MouseWheelListener, MouseListener{
	
	private static final long serialVersionUID = 1L;

	private MyPanel panel;
	
	private int cou = 90;//旋转角度
	private double mouseStage = 4.1;//记录鼠标点按次数，范围0~14
	private static Map<Double, Double> stageNum = new HashMap<Double, Double>();//视标数据
	private DecimalFormat df = new DecimalFormat("######0.0");//数字格式化类
	
	private static int ppi;//ppi
	private static int ppm;//每毫米所占像素数
	

	//随机数产生类
	private Random rand = new Random(System.currentTimeMillis());
	
	{
		//初始化视标数据--5m
		stageNum.put(4.0, 72.72);
		stageNum.put(4.1, 57.76);
		stageNum.put(4.2, 45.88);
		stageNum.put(4.3, 36.45);
		stageNum.put(4.4, 28.95);
		stageNum.put(4.5, 23.00);
		stageNum.put(4.6, 18.27);
		stageNum.put(4.7, 14.51);
		stageNum.put(4.8, 11.53);
		stageNum.put(4.9, 9.16);
		stageNum.put(5.0, 7.27);
		stageNum.put(5.1, 5.78);
		stageNum.put(5.2, 4.59);
		stageNum.put(5.3, 3.64);
		System.out.println("视标数据初始化完毕");
	}
	public SwingMain() {
		/*
		 * 1.设置frame的初始化参数
		 * 2.将panel添加到frame中并全覆盖
		 * 3.添加鼠标动作监听事件
		 */
		
		setSize(1000,900);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//设置frame初始化位置为屏幕中间
		setLocationRelativeTo(null);
		
		panel = new MyPanel();
		panel.setSize(this.getWidth(),this.getHeight());
		int initPicSize = (int)(stageNum.get(4.1)*ppm);
		panel.setW(initPicSize);
		panel.setH(initPicSize);
		add(panel);
		
		
		addMouseWheelListener(this);
		addMouseListener(this);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == -1)//上滑逆时针放大
		{
			if(panel.getW() == 533 && panel.getH() == 665)
			{
				JOptionPane.showMessageDialog(this, "亲，不能再大啦！");
			}else{
				panel.setC(panel.getC() + Math.toRadians(cou*rand.nextInt(24)));
				panel.repaint();
			}
		}
		
		if(e.getWheelRotation() == 1)//下滑顺时针缩小
		{
			if(panel.getW() == 21 && panel.getH() == 25)
			{
				JOptionPane.showMessageDialog(this, "亲，不能再小啦！");
			}else{
				double dou = Math.toRadians(cou);
				System.out.println(dou);
			
				//新的旋转算法，cou=90
				panel.setC(panel.getC() + Math.toRadians(cou*rand.nextInt(24)));
				System.out.println("***要转换的角度为： " + panel.getC() + " ***");
				panel.repaint();
			}
		}
		
	}
	public static void main(String[] args) {
		double screenSize = Double.parseDouble(JOptionPane.showInputDialog("请输入您的设备屏幕尺寸："));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		//计算ppi
		ppi = (int)(Math.sqrt((dimension.width*dimension.width + dimension.height*dimension.height))/screenSize);
		ppm = (int) (ppi/25.4);
		//System.out.println(ppm);
		String screenMessage = "屏幕大小：\t" + screenSize +
				"\n分辨率：\t" + dimension.width + "*" + dimension.height +
				"\nPPI：\t" + ppi;
		JOptionPane.showMessageDialog(null, screenMessage);
		new SwingMain();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
			int i = e.getButton();
			if(i == MouseEvent.BUTTON1)
			{
				if(mouseStage>4.0)
				{
					//左键--放大
					mouseStage=Double.parseDouble(df.format(mouseStage-0.1));
					System.out.println("左键放大：  " + mouseStage);
					int t = (int)(stageNum.get(mouseStage)*ppm);
					panel.setW(t);
					panel.setH(t);
					
					//panel.setImgNum(panel.getImgNum() + 0.1);
					rePaint();
					panel.setStageInfo(mouseStage);
				}else{
					System.out.println("mouseStage=" + mouseStage);
					JOptionPane.showMessageDialog(this, "已到达最大");
				}
				
			}else if(i == MouseEvent.BUTTON3)
			{
				if(mouseStage<5.3)
				{
					//右键--缩小
					mouseStage=Double.parseDouble(df.format(mouseStage+0.1));
					System.out.println("左键放大：  " + mouseStage);
					int t = (int)(stageNum.get(mouseStage)*ppm);
					panel.setW(t);
					panel.setH(t);
					//panel.setImgNum(panel.getImgNum() - 0.1);
					rePaint();
					panel.setStageInfo(mouseStage);
				}else{
					System.out.println("mouseStage=" + mouseStage);
					JOptionPane.showMessageDialog(this, "已到达最小");
				}
			}
	}

	public void rePaint()
	{
		double dou = Math.toRadians(cou);
		System.out.println(dou);
	
		//新的旋转算法，cou=90
		panel.setC(panel.getC() + Math.toRadians(cou*rand.nextInt(24)));
		
		//更新E字对应值
		panel.setStageInfo(panel.getStageInfo()-0.5);
		System.out.println("***要转换的角度为： " + panel.getC() + " ***");
		panel.repaint();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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

class MyPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	/*
	 * c--旋转角度
	 * w、h--图片的宽高(像素)
	 * screenW、screenH--JPanel的宽高(像素)
	 * stageInfo--当前视标对应的参考数值
	 */
	private double c=0;
	private int w,h;
	private int screenH=0;
	
	private double stageInfo=4.1;

	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("*** JPanel大小为:" + this.getWidth()+ "  " + this.getHeight()+ " ***");
		//获取图片资源路径
		URL imgUrl = SwingMain.class.getResource("/img/main.png");
		g.setColor(Color.RED);
		g.fillRect(0, 0, 160, this.getHeight());
		g.setColor(Color.WHITE);
		
		g.setFont(new Font("宋体", Font.BOLD, 40));
		g.drawString("当前值：",0,50);
		
		g.setFont(new Font("宋体", Font.BOLD, 90));
		g.drawString(String.valueOf(stageInfo),0,130);

		g.setFont(new Font("宋体", Font.BOLD, 30));
		g.drawString("参考距离：",0,180);
		
		g.setFont(new Font("宋体", Font.BOLD, 90));
		g.drawString("5m",0,250);
		
		g.setFont(new Font("宋体", Font.BOLD, 15));
		g.drawString("  *数据可能会有偏差", 0, this.getHeight()-10);
		
		Graphics2D d2 = (Graphics2D)g;
		
		d2.setColor(Color.white);
		d2.fillRect(160,0,this.getWidth(),this.getHeight());
		d2.rotate(c,((this.getWidth()+160)/2),(this.getHeight()/2));
		//System.out.println("*** 旋转中心X=:" + (w/2)+((this.getWidth()+160)/2) + " Y=" + (h/2)+(this.getHeight()/2)+ " ***");
		
		Image image = new ImageIcon(imgUrl).getImage();
		
		/*
		 * drawImage(Image,x,y,w,h,ImageObserver )方法
		 * img - 要绘制的指定图像。如果 img 为 null，则此方法不执行任何动作
		 *	x - x 坐标
		 *	y - y 坐标
		 *	width - 矩形的宽度
		 *	height - 矩形的高度
		 *	observer - 当转换了更多图像时要通知的对象
		 */
		d2.drawImage(image,((this.getWidth()+160)/2)-w/2, (this.getHeight()/2)-h/2, w, h,this);
		//g.drawString(String.valueOf(stageInfo),120,30);
		System.out.println(String.valueOf(stageInfo));
		//System.out.println("*** draeImage坐标为: X=" + (this.getWidth()/2)-300 + " Y= " + (this.getHeight()/2)-305 + " ***");
		System.out.println("***图片大小：w=" +w  + "  h=" + h+ "***");
		d2.dispose();
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	
	public int getScreenH() {
		return screenH;
	}
	public void setScreenH(int screenH) {
		this.screenH = screenH;
	}
	public double getStageInfo() {
		return stageInfo;
	}
	public void setStageInfo(double stageInfo) {
		this.stageInfo = stageInfo;
	}
}







