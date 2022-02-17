package coding.socketgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable{
	JPanel northPanel, southPanel;
	   TextArea chatList; // JTextArea 버그 있네...
	   ScrollPane scroll;
	   JTextField txtHost, txtPort, txtMsg;
	   JButton btnConnect, btnSend;
	BufferedReader in;
	PrintWriter out;
	String user;
	String [] chatCombobox= {"전체","귓속말"};
	JComboBox combo=new JComboBox<>(chatCombobox);

	public ChatClient() {
		// TODO Auto-generated constructor stub
		setTitle("MyChat1.0");
	      setSize(600, 700);
	      setLocationRelativeTo(null);
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	      
	      northPanel = new JPanel();
	      southPanel = new JPanel();
	      
	      chatList = new TextArea(10, 30);
	      chatList.setBackground(Color.black);
	      chatList.setForeground(Color.white);
	      scroll = new ScrollPane();
	      scroll.add(chatList); //스크롤에 textarea 달기
	      
	      txtHost = new JTextField(20); // 사이즈 20
	      txtHost.setText("127.0.0.1");
	      txtPort = new JTextField(5);
	      txtPort.setText("5000");
	      
	      txtMsg = new JTextField(25); // 사이즈 20
	      
	      btnConnect = new JButton("Connect");
	      btnSend = new JButton("Send");
	      
	      northPanel.add(txtHost);
	      northPanel.add(txtPort);
	      northPanel.add(btnConnect);
	      southPanel.add(combo);
	      southPanel.add(txtMsg);
	      southPanel.add(btnSend);
	      
	      add(northPanel, BorderLayout.NORTH);
	      add(scroll, BorderLayout.CENTER);
	      add(southPanel, BorderLayout.SOUTH);
	      
	      initListener();
	 
	      setVisible(true);

	}
	
	
	
	  public void initListener() {
		  
	      btnConnect.addActionListener((e)->{
	         String host = txtHost.getText();
	         int port = Integer.parseInt(txtPort.getText());
	         connect(host,port);
	         btnConnect.setEnabled(false);
	         txtHost.setEditable(false);
	         txtPort.setEnabled(false);
	         System.out.println(host+"서버쪽 "+port+"포트로 연결합니다.");
	      });
	      
	      btnSend.addActionListener((e)->{
	         String msg = txtMsg.getText();
	         if (user==null) {
        		 user=msg;
		         out.write(msg+"\n");
		         out.flush();
		         txtMsg.setText(""); // 비우기
		         txtMsg.requestFocus(); // 커서 두기
			}else {
		         txtMsg.setText(""); // 비우기
		         txtMsg.requestFocus(); // 커서 두기
		         out.write(msg+"\n");
		         out.flush();
			}

	         
	      });

	   }
	  
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				chatList.append(in.readLine()+"\n");
				chatList.transferFocus();				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	private void connect(String host,int port) {
		// TODO Auto-generated method stub
		try {
			Socket sock=new Socket(host,port);
			in=new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out=new PrintWriter(sock.getOutputStream(),true);
					
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		new Thread(this).start();
	}
	
	public static void main(String[] args) {
		new ChatClient();
	}

}
