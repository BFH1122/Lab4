/**
 * 
 */
package se_lab1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Lab1 extends JComponent {
	private static final long serialVersionUID = -4654513992552014113L;
	public static frame f;
	public static String fileUrl;
	public static String[] words;
	public static tree t;
	public static int imgState = 0;


	public static void readInFile(){
		File file = new File(fileUrl);
		String wordsStr = "";
		Scanner in;
		try {
			in = new Scanner(file);
			while(in.hasNextLine()){
				String str = in.nextLine();
				wordsStr = wordsStr.concat(replaceStr(str)+" ");
			}
			words = wordSplit(wordsStr);
			t = new tree(words);
			t.calculateNodeLevel();
			generateDotFile("Verdana", 12);
			// createFlowChartFrame(); 暂时使用Graphviz方案
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean generateDotFile(String fontname, int fontsize) {
		NodeList<treeNode> queue = new NodeList<treeNode>();
		treeNode nowNode = t.head;
		File dotFile = new File(fileUrl.replace("txt", "dot"));
		try {
			dotFile.createNewFile();
			BufferedWriter outBuffer = new BufferedWriter(new FileWriter(dotFile));
			outBuffer.write(String.format("digraph %s {\n\tfontname = \"%s\";\n\tfontsize = %d;\n\n", "test", fontname, fontsize)); // 这里的名字可以替换的其实
			outBuffer.write(String.format("\tnode [ fontname = \"%s\", fontsize = %d ]\n", fontname, fontsize));
			outBuffer.write(String.format("\tedge [ fontname = \"%s\", fontsize = %d ]\n\n", fontname, fontsize));
			for(int i = 0; i < words.length; i++) {
				outBuffer.write(String.format("\t%s;\n", words[i]));
			}
			queue.push(nowNode);			
			while(queue.isEmpty() != true) {
				nowNode = queue.pop();
				if (nowNode != null) {
					for(int i = 0; i < nowNode.childList.size(); i++) {
						treeNode nowChildNode = nowNode.childList.get(i);
						outBuffer.write(String.format("\t%s -> %s [label=\"%d\"];\n", nowNode.word, nowChildNode.word, nowNode.getWeightOfNode(nowChildNode)));
						if (nowNode.level < nowChildNode.level && queue.indexOf(nowChildNode) == -1) {
							queue.push(nowChildNode);
						}
					}
				}
			}
			outBuffer.write("}");
			outBuffer.flush();
			outBuffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Runnable createGraphRunnable = new CreateGraphRunnable();
		Runnable showWaitingRunnable = new ShowWaitingRunnable();

		Thread createGraphThread = new Thread(createGraphRunnable);
		Thread showWaitingThread = new Thread(showWaitingRunnable);

		// 初始化图片生成状态
		Lab1.imgState = 0;
		
		// 开始进程
		createGraphThread.start();
		showWaitingThread.start();
		
		return true;
	}
	
	public Lab1() {
		setBackground(Color.WHITE);
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

/** 调用Graphviz创建有向图图片的进程
 * @author Yumi
 *
 */
class CreateGraphRunnable implements Runnable {
		
	@Override
	public void run() {
		// 运行dot(已配置好环境变量的情况下)
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec(String.format("dot -Tpng %s -o %s", Lab1.fileUrl.replace(".txt", ".dot"), Lab1.fileUrl.replace(".txt", ".png")));
			process.waitFor();
			// 生成完图片标记imgState通知Thread2结束
			Lab1.imgState = 1;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}

/** 等待Graphviz创建图片结束的进程
 * @author Yumi
 *	
 */
class ShowWaitingRunnable implements Runnable {
		
	@Override
	public void run() {
		// 预留出来的线程（做其他，比如显示等待图片或者进度条）
		
		// while等待图片加载
		while(Lab1.imgState == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 图片加载完毕后进行操作
		
		// 用于测试的输出
		System.out.println("Create Img Success!");
		picDisplayPanel.setPic(Lab1.fileUrl.replace("txt", "png"));
	}


}

class treeNode {
	String word;
	int level;
	NodeList<treeNode> parentList;
	NodeList<treeNode> childList;
	ArrayList<Integer> childPathWeightList;
	
	public treeNode(String word, treeNode parent) {
		this.word = word;
		this.parentList = new NodeList<treeNode>();
		this.parentList.add(parent);
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
	
	/** 调用节点方法输入子节点进行查询邻接边权值
	 * @param childNode 查询邻接子节点
	 * @return 返回0则为不邻接，返回正整数权值则邻接
	 */
	public int getWeightOfNode(treeNode childNode) {
		int weight,childIndex;
		childIndex = this.childList.indexOf(childNode);
		if(childIndex == -1) {
			// 没有找到该节点,即未邻接当前node
			weight = 0;
		} else {
			// 找到节点，返回weight值
			weight = this.childPathWeightList.get(childIndex).intValue();
		}
		return weight;
	}
	
	public void setNodeLevel(int level) {
		this.level = level;
	}
	
	public int getNodeLevel() {
		return this.level;
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
	
	/** 利用队列结构计算出treeNodes中各treeNode的level值
	 *  
	 *  @注  在treeNode中直接调用treeNode.getNodeLevel()即可获得level值
	 */
	public void calculateNodeLevel() {
		NodeList<treeNode> Queue = new NodeList<treeNode>();
		NodeList<treeNode> CopyList = new NodeList<treeNode>();
		CopyList.addAll(treeNodes);
		treeNode presentNode,childNode;
		int presentLevel;
		head.setNodeLevel(1);
		Queue.push(this.head);
 		while(CopyList.size() != 0) {
 			presentNode = Queue.pop();
 			presentLevel = presentNode.getNodeLevel() + 1;
 			for(int i = 0;i<presentNode.childList.size();i++) {
 				childNode = presentNode.childList.get(i);
 				if(CopyList.indexOf(childNode) != -1) {
 					childNode.setNodeLevel(presentLevel);
 					if(Queue.indexOf(childNode) == -1) {
 						Queue.push(childNode);
 					}
 				}
 			}
 			CopyList.remove(presentNode);
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
	int longestWordLength = 0;
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
	
	/* 重写添加节点方法，在添加节点时计算最长单词长度
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		// TODO Auto-generated method stub
		if(e != null) {
			treeNode addNode = (treeNode) e;
			String word = addNode.word;
			this.longestWordLength = word.length() > this.longestWordLength ? word.length():this.longestWordLength;
		}
		return super.add(e);
	}
	
	/** 返回当前List中最长词长
	 * @return int 最长词长
	 */
	public int getLongestWordLength() {
		return this.longestWordLength;
	}
	
	/** 添加到AList的队列操作 PUSH
	 * @param pushNode
	 * @return 操作返回布尔值
	 */
	public boolean push(E pushNode) {
		boolean flag;
		flag = this.add(pushNode);
		return flag;
	}
	
	/** 添加到AList的队列操作 POP
	 * @return 当前List的首元素出队
	 */
	public treeNode pop() {
		treeNode popNode;
		if(this.size() != 0) {
			popNode = (treeNode) this.get(0);
			this.remove(0);
		} else {
			popNode = null;
		}
		return popNode;
	}
}

class frame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904245993409935448L;
	private static final int WIDTH = 220;
	private static final int HEIGHT = 250;
	picDisplayPanel picPanel;
	
	public frame() {
		setTitle("Flow chart");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		Container c = getContentPane();
		GridLayout twoSeprateLayout = new GridLayout(1,2);
		c.setLayout(twoSeprateLayout);
		
		// 菜单栏 定义
		JMenuBar mb = new JMenuBar();JMenu mFile = new JMenu("文件(F)");
		// File菜单
		mFile.setMnemonic('F');
		JMenuItem miOpen = new JMenuItem("打开");
		miOpen.setMnemonic('O');
		mFile.add(miOpen);
		miOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
				fc.setFileFilter(filter);
				fc.setDialogTitle("Choose file");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.showOpenDialog(Lab1.f);
				File filename = fc.getSelectedFile();
				if (filename != null){
					Lab1.fileUrl = fc.getSelectedFile().getAbsolutePath();
					Lab1.readInFile();
				}
			}
		});
		// 将主菜单添加到菜单栏
		mb.add(mFile);
		// 将菜单栏添加到主程序
		setJMenuBar(mb);
		
		picDisplayPanel picPanel = new picDisplayPanel();
		JScrollPane sp = new JScrollPane(picPanel);
		sp.validate();
		c.add(sp, twoSeprateLayout);
		functionPanel funcPanel = new functionPanel();
		c.add(funcPanel, twoSeprateLayout);
		setVisible(true);
	}
}

class functionPanel extends JPanel {
	private static final long serialVersionUID = -1104559035947942491L;
	private JTabbedPane tp = new JTabbedPane(JTabbedPane.BOTTOM);
	private String[] tabNames = {"选项卡1", "选项卡2", "选项卡3", "选项卡4", "选项卡5"};
	public functionPanel() {
		JPanel tab1 = new JPanel();
		tp.add(tabNames[0], tab1);
		JPanel tab2 = new JPanel();
		tp.add(tabNames[1], tab2);
		add(tp);
	}
}

class picDisplayPanel extends JPanel {
	private static final long serialVersionUID = -466385864846654643L;
	public static JLabel picLabel;
	public static int WIDTH;
	public static int HEIGHT;
	public static ImageIcon pic;
	public picDisplayPanel(){
		picLabel = new JLabel();
		add(picLabel);
	}
	
	public static void setPic(String path) {
		pic = new ImageIcon(path);
		WIDTH = pic.getIconWidth();
		HEIGHT = pic.getIconHeight();
		picLabel.setIcon(pic);
	}
	
	public static void changeSize(int percent) {
		pic.setImage(pic.getImage().getScaledInstance(percent * WIDTH, percent * HEIGHT, Image.SCALE_DEFAULT));
		picLabel.setIcon(pic);
	}
}