package Server;

import java.net.ServerSocket;


public class main_Server {

	public static void main(String[] args) throws Exception{  
		ServerSocket ss = new ServerSocket(7856);
		ss.setReuseAddress(true);
		try {
			while(true) { 
				serverThread t = new serverThread(ss.accept());
				t.start();
			}
		}finally {
			ss.close();
		}
	}

}
