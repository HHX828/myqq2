package _Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��4�� ����8:11:30
*/
public class Service {
	public void startService() {
		try {
			ServerSocket ss = new ServerSocket(2222);
			Socket socket = null;
			
			while(true) {
				socket = ss.accept();
				ServerThread thread = new ServerThread(socket);
				thread.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
