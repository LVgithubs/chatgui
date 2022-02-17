package coding.socketgui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFrame extends Frame {
	public MyFrame() {
	this(500,700);
	}
	
	public MyFrame(int x , int y) {
		setTitle("MyFrame");
		setSize(x,y);
		setBackground(Color.black);
        setLayout(new FlowLayout());
		setLocation(700, 300);
		
		//윈도우 창 종료 리스너
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setVisible(true);
		setResizable(true);


		
	}
	
	
	
}
