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
		
		System.out.println("****클라이언트의 접속을 기다리고 있습니다****");
		try {
			while (true) {
				Socket sock = server.accept();
				ClientThread ct=new ClientThread(sock);
                vc.add(ct);
                System.out.println("동시 접속자수 " + vc.size());
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
			ct.sendMessage("[전체]" +msg);
		}
		
	}
	
	public void sendSelectClient(String username,String msg) {
		for(ClientThread ct: vc) {
			if (ct.user.equals(username)) {
				ct.sendMessage("[귓속말]" +msg);
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
			out.println("사용하실 아이디를 입력하세요.");
			user=in.readLine();
			sendAllClient("["+user+"] 님께서 입장하셨습니다.");
			String data=null;
			while ((data=in.readLine())!=null) {
				sendAllClient(data);
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println(sock+"끊어짐");
		}
		
		while (isLogin) {
            try {
                String inputData = in.readLine();
                System.out.println("from  클라이언트 : " + inputData);
                // 메세지를 받았으니까 List < 고객전담스레드 > 고객리스트 <== 여기에 담긴
                // 모든 클라이언트에게 메세지를 전송 for each문 돌려서!
            } catch (Exception e) {
                try {
                    System.out.println("오류내용 : " + e.getMessage());
                    isLogin = false;
                    vc.remove(this);
                    in.close();
                    out.close();
                    sock.close();
                } catch (Exception e1) {
                    // TODO: handle exception
                    System.out.println("연결해제 프로세스 실패 : " + e1.getMessage());
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
				System.out.println(sock+"접속됨");
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
