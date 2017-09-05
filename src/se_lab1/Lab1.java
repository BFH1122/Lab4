/**
 * 
 */
package se_lab1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

public class Lab1 extends JComponent {
	private static final long serialVersionUID = -4654513992552014113L;
	public static frame f;
	public static String fileUrl;
	public static String[] words;
	public static tree t;
	
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
			t = new tree(words);
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
		f = new frame();
	}
}

class treeNode {
	String word;
	NodeList<treeNode> parentList;
	NodeList<treeNode> childList;
	ArrayList<Integer> childPathWeightList;
	
	public treeNode(String word, treeNode parent) {
		this.word = word;
		this.parentList = new NodeList<treeNode>();
		parentList.add(parent);
		this.childList = new NodeList<treeNode>();
		this.childPathWeightList = new ArrayList<Integer>();
	}
	
	public String getWord() {
		return this.word;
	}
	
	public void addParent(treeNode anotherParent) {
		this.parentList.add(anotherParent);
	}
	
	/** 对当前节点进行添加子节点操作，附带对子节点查重操作
	 * @param anotherChild 添加的子节点
	 */
	public void addChild(treeNode anotherChild) {
		treeNode checkNode = this.childList.nodeCheck(anotherChild.getWord());
		int nodeIndex,childPathWeight;
		if(checkNode != null) {
			// State - 3 已经桥接该点
			// 获得该点索引，对权值List进行更新
			nodeIndex = this.childList.indexOf(checkNode);
			childPathWeight = this.childPathWeightList.get(nodeIndex).intValue()+1;
			this.childPathWeightList.set(nodeIndex, new Integer(childPathWeight));
		} else {
			// State - 2 未桥接该点
			// 直接添加到子节点List，同时添加权值
			this.childList.add(anotherChild);
			this.childPathWeightList.add(new Integer(1));
		}
	}
}

class tree {
	treeNode head;
	NodeList<treeNode> treeNodes;
	
	public tree(String[] words) {
		treeNode node_pr,node_after;
		this.head = new treeNode(words[0],null);
		this.treeNodes = new NodeList<treeNode>(); 
		node_pr = this.head;
		this.treeNodes.add(node_pr);
		for(int i =1;i<words.length;i++) {
			//生成树图的新节点出现情况：
			//State-1 after节点未出现过
			//	直接桥接两个点，添加新点进List
			//State-2 after节点已经出现过 但是没有已知边桥接（交由节点方法进行处理）
			// 	直接桥接，不用添加进List
			//State-3 after节点已经出现过 并且出现过已知桥接边（交由节点方法进行处理）
			
			node_after = this.treeNodes.nodeCheck(words[i]);
			if(node_after != null) {
				// State-2/3 节点表中已经有该节点
				node_after.addParent(node_pr);
				node_pr.addChild(node_after); // 2/3情况操作交由节点方法进行处理
			} else {
				//State-1 after节点未出现过
				node_after = new treeNode(words[i],node_pr);
				node_pr.addChild(node_after);
				this.treeNodes.add(node_after);
			}
			node_pr = node_after;
		}
	}
}


/** 继承原生ArrayList类添加自定义元素查找方法
 * @author Yumi
 * 
 * @param <E> 内部添加的方法只对treeNode对象集有效 
 */
class NodeList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 682330081079347841L;
	
	/** 检查节点是否已经存在
	 * @param word 单词
	 * @return 存在节点情况(是则返回节点，否则null)
	 */
	public treeNode nodeCheck(String word) {
		treeNode existedNode = null,getNode;
		for(int i = 0;i<this.size();i++) {
			getNode = (treeNode) this.get(i);
			if(word.equals(getNode.getWord())) {
				existedNode = getNode;
				break;
			}
		}
		return existedNode;
	}
	
}

class frame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904245993409935448L;
	private static final int WIDTH = 220;
	private static final int HEIGHT = 250;
	private static panel p;
	
	
	public frame() {
		setTitle("Test Frame");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		Container c = new Container();
		c = getContentPane();
		p = new panel();
		c.add(p);
		setVisible(true);
	}
}

class panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -466385864846654643L;
	
	public panel(){
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fd = new FileDialog(Lab1.f, "Choose file", FileDialog.LOAD);
				fd.setFile("*.txt");
				fd.setVisible(true);
				String filename = fd.getFile();
				if (filename != null){
					Lab1.fileUrl = fd.getDirectory() + fd.getFile();
					Lab1.readInFile();
				}
			}
		});
		add(btnOpen);
	}
}