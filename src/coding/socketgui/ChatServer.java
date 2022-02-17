package coding.socketgui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class ChatServer {
	   List<ClientThread> vc;
	
	public ChatServer() {
		vc = new ArrayList<>();
		ServerSocket server = null;
		try {
			server=new ServerSocket(5000);
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("****Ŭ���̾�Ʈ�� ������ ��ٸ��� �ֽ��ϴ�****");
		try {
			while (true) {
				Socket sock = server.accept();
				ClientThread ct=new ClientThread(sock);
                vc.add(ct);
                System.out.println("���� �����ڼ� " + vc.size());
                new Thread(ct).start();
			
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		
		
	}
	
	
	
	
	
	
	public void sendAllClient(String msg) {
		for(ClientThread ct: vc) {
			ct.sendMessage("[��ü]" +msg);
		}
		
	}
	
	public void sendSelectClient(String username,String msg) {
		for(ClientThread ct: vc) {
			if (ct.user.equals(username)) {
				ct.sendMessage("[�ӼӸ�]" +msg);
				}
		}
		
	}
	
	
	class ClientThread implements Runnable{
		ChatServer cs;
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String user;
        boolean isLogin = true;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
		try {
			out.println("����Ͻ� ���̵� �Է��ϼ���.");
			user=in.readLine();
			sendAllClient("["+user+"] �Բ��� �����ϼ̽��ϴ�.");
			String data=null;
			while ((data=in.readLine())!=null) {
				sendAllClient(data);
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(sock+"������");
		}
		
		while (isLogin) {
            try {
                String inputData = in.readLine();
                System.out.println("from  Ŭ���̾�Ʈ : " + inputData);
                // �޼����� �޾����ϱ� List < �����㽺���� > ������Ʈ <== ���⿡ ���
                // ��� Ŭ���̾�Ʈ���� �޼����� ���� for each�� ������!
            } catch (Exception e) {
                try {
                    System.out.println("�������� : " + e.getMessage());
                    isLogin = false;
                    vc.remove(this);
                    in.close();
                    out.close();
                    sock.close();
                } catch (Exception e1) {
                    // TODO: handle exception
                    System.out.println("�������� ���μ��� ���� : " + e1.getMessage());
                }
            }
        }
		
		
		}
		
		public ClientThread(Socket sock) {
			// TODO Auto-generated constructor stub
			this.sock=sock;
			try {
				in=new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				out=new PrintWriter(sock.getOutputStream(),true);
				System.out.println(sock+"���ӵ�");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void sendMessage(String msg) {
			// TODO Auto-generated method stub
			out.write( user + ":" + msg + "\n");
			out.flush();
		}
		
        

	}

	
	public static void main(String[] args) {
		new ChatServer();
	}
}
