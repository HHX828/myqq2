package Entity;

import java.net.Socket;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��5�� ����2:35:00
*/
//��ÿһ��socketȡ�����ֱ���Ѱ�ң������SocketEntity������
public class SocketEntity {
	private Socket socket;
	private String name;
	
	public SocketEntity() {
		super();
	}
	
	public SocketEntity(Socket socket, String name) {
		super();
		this.socket = socket;
		this.name = name;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
