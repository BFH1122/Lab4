package se_lab1;

import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MyFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904245993409935448L;
	private static final int WIDTH = 220;
	private static final int HEIGHT = 250;
	private static MyPanel p;
	
	public MyFrame() {
		setTitle("Test Frame");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		Container c = new Container();
		c = getContentPane();
		p = new MyPanel();
		c.add(p);
		setVisible(true);
	}
}

class MyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -466385864846654643L;
	
	public MyPanel(){
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
