/**
 * 
 */
package se_lab1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Scanner;

import javax.swing.*;


/**
 * @author dell-pc
 *
 */
public class Lab1 {
	public static frame f;
	public static String fileurl;
	
	public static void readInFile(){
		File file = new File(fileurl);

		Scanner in;
		try {
			in = new Scanner(file);
			while(in.hasNextLine()){
				String str = in.nextLine();
				System.out.print(replaceStr(str));
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public static String replaceStr(String str){
		return str.replaceAll("[^a-zA-Z]", " ").toLowerCase();//.split("\\s+");
	}
	public static void main(String[] args) {
		f = new frame();
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
		setTitle("测试");
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
	
	class fileFilter implements FilenameFilter {
		private String type;
		public fileFilter(String type) {
			this.type = type;
		}
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(type);
		}
		
	}
	public panel(){
		JButton btnOpen = new JButton("打开");
		btnOpen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				FileDialog fd = new FileDialog(Lab1.f, "选择要打开的文件", FileDialog.LOAD);
				fd.setFilenameFilter(new fileFilter(".txt"));
				fd.setVisible(true);
				String filename = fd.getFile();
				if (filename != null){
					Lab1.fileurl = fd.getDirectory() + fd.getFile();
					Lab1.readInFile();
				}
			}
			
		});
		add(btnOpen);
	}
	
	
}