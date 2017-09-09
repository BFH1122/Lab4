/**
 * 
 */
package se_lab1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.swing.*;

public class Lab1 extends JComponent {
	private static final long serialVersionUID = -4654513992552014113L;
	public static MyFrame f;
	public static String fileUrl;
	public static String[] words;
	public static Tree t;
	
	public static void readInFile(){
		File file = new File(fileUrl);
		String wordsStr = "";
		Scanner in;
		try {
			in = new Scanner(file);
			while(in.hasNextLine()){
				String str = in.nextLine();
				wordsStr = wordsStr.concat(replaceStr(str));
			}
			words = wordSplit(wordsStr);
			t = new Tree(words);
			t.calculateNodeLevel();
			createFlowChartFrame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		// GradientPaint grdp = new GradientPaint(150, 150, Color.decode("#81FBB8"), 300, 300, Color.decode("#28C76F"));
		// g2d.setPaint(grdp);
		// g2d.fillRoundRect(150, 150, 150, 150, 15, 15);
		g2d.drawString(t.head.word, 10, 10);
	}
	
	public Lab1() {
		setBackground(Color.WHITE);
	}
	
	public static void createFlowChartFrame() {
		final JFrame f = new JFrame("流程图");  
        // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.setSize(800, 600);  
        f.add(new Lab1());  
        f.setVisible(true);  
        f.setLocationRelativeTo(f.getOwner());  
	}
	
	/** 切割字符串
	 * @param str 读入的字符串
	 * @return 切割后的String[]
	 */
	public static String[] wordSplit(String str) {
		return str.split("\\s+");
	}
	
	/** 处理读入的字符串(删除标点符号，并转换为小写)
	 * @param str 读入的字符串
	 * @return 处理后的字符串
	 */
	public static String replaceStr(String str){
		return str.replaceAll("[^a-zA-Z]", " ").toLowerCase();
	}
	
	public static void main(String[] args) {
		f = new MyFrame();
	}
}