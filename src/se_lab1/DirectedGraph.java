package se_lab1;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DirectedGraph {
	// 尚未完成
	/** 生成dot文件并生成png格式的最短路有向图
	 * @param t 树
	 * @param fileUrl txt文件路径
	 * @param fontname 字体名
	 * @param fontsize 字体大小
	 * @return 成功标识(true)
	 */
	public static boolean createShortestDirectedGraph(Tree t, String fileUrl, String fontname, int fontsize, String shortest) {
		String[] shroads = shortest.split("\n");
		for (int i = 0; i < shroads.length; i++) {
			shroads[i].replaceAll("Path [0-9]+ :", "");
			shroads[i].replace(".", "");
		}
		Color[] colors = {Color.getColor("#1abc9c"), Color.getColor("#3498db"), Color.getColor("#f1c40f"), Color.getColor("#8e44ad"), Color.getColor("#c0392b")};
		TreeNodeList<TreeNode> queue = new TreeNodeList<TreeNode>();
		TreeNode nowNode = t.head;
		File dotFile = new File(fileUrl.replace("txt", "dot"));
		try {
			dotFile.createNewFile();
			BufferedWriter outBuffer = new BufferedWriter(new FileWriter(dotFile));
			outBuffer.write(String.format("digraph %s {\n\tfontname = \"%s\";\n\tfontsize = %d;\n\n", "test", fontname, fontsize)); // 这里的名字可以替换的其实
			outBuffer.write(String.format("\tnode [ fontname = \"%s\", fontsize = %d ]\n", fontname, fontsize));
			outBuffer.write(String.format("\tedge [ fontname = \"%s\", fontsize = %d ]\n\n", fontname, fontsize));
			for(int i = 0; i < Lab1.words.length; i++) {
				outBuffer.write(String.format("\t%s;\n", Lab1.words[i]));
			}
			queue.push(nowNode);			
			while(queue.isEmpty() != true) {
				nowNode = queue.pop();
				if (nowNode != null) {
					for(int i = 0; i < nowNode.childList.size(); i++) {
						TreeNode nowChildNode = nowNode.childList.get(i);
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

	
	/** 生成dot文件并生成png格式的有向图
	 * @param t 树
	 * @param fileUrl txt文件路径
	 * @param fontname 字体名
	 * @param fontsize 字体大小
	 * @return 成功标识(true)
	 */
	public static boolean createDirectedGraph(Tree t, String fileUrl, String fontname, int fontsize) {
		TreeNodeList<TreeNode> queue = new TreeNodeList<TreeNode>();
		TreeNode nowNode = t.head;
		File dotFile = new File(fileUrl.replace("txt", "dot"));
		try {
			dotFile.createNewFile();
			BufferedWriter outBuffer = new BufferedWriter(new FileWriter(dotFile));
			outBuffer.write(String.format("digraph %s {\n\tfontname = \"%s\";\n\tfontsize = %d;\n\n", "test", fontname, fontsize)); // 这里的名字可以替换的其实
			outBuffer.write(String.format("\tnode [ fontname = \"%s\", fontsize = %d ]\n", fontname, fontsize));
			outBuffer.write(String.format("\tedge [ fontname = \"%s\", fontsize = %d ]\n\n", fontname, fontsize));
			for(int i = 0; i < Lab1.words.length; i++) {
				outBuffer.write(String.format("\t%s;\n", Lab1.words[i]));
			}
			queue.push(nowNode);			
			while(queue.isEmpty() != true) {
				nowNode = queue.pop();
				if (nowNode != null) {
					for(int i = 0; i < nowNode.childList.size(); i++) {
						TreeNode nowChildNode = nowNode.childList.get(i);
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