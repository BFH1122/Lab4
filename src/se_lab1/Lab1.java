/**
 * 
 */
package se_lab1;

import java.awt.*;
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
			// Test queryBridgeWords
			/*
			for(int i = 0;i<t.TreeNodes.size();i++) {
				for(int j = 0;j<t.TreeNodes.size();j++) {
					System.out.println(t.queryBridgeWords(t.TreeNodes.get(i).getWord(), t.TreeNodes.get(j).getWord())+"\n");
				}
			}
			*/
			
			// Test generateNewText
			//System.out.println(t.generateNewText("Seek to explore new and exciting synergies"));
			
			// Test calcShortestPath
			/*
			try {
				System.out.println(t.calcShortestPath("new", "strange"));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			// Test randomWalk
			// System.out.println(t.randomWalk());
			createFlowChartFrame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
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