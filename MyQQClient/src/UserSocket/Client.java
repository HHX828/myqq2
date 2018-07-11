package UserSocket;
/**
* @author zzz
* @version ����ʱ�䣺2018��7��4�� ����8:47:58
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import _Util.CommandTranser;

public class Client {
	private int port = 2222; 
	private String Sever_address = "127.0.0.1"; //����������ip
	private Socket socket;
	
	//ʵ������ ��������
	public Client(){
		try {
			socket = new Socket(Sever_address, port);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	} 
	
	//�����˷�������
	public void sendData(CommandTranser cmd) {
		ObjectOutputStream oos = null; //��Ҫ������������д�������Ϣ���ȡ������Ϣ�� ������Ϣһ��д���ļ�����ô�������Ϣ�Ϳ��������־û���
		try {
			if(socket == null) {
				return;
			}
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(cmd);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "��������δ����");
		}
	}
	
	//���ܷ���˷��͵���Ϣ
	public CommandTranser getData() {
		ObjectInputStream ois = null;
		CommandTranser cmd = null;
		if(socket == null) {
			//System.out.println("weishenme");
			return null;
		}
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			cmd = (CommandTranser) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		/*
		 * ����������-----------------------------------------------------------------------------------Ӧ��û��ֻ���ڲ�ʹ���
		 */
//		if("message".equals(cmd.getCmd())) {
//			System.out.println((String)cmd.getData());
//		}
		
		return cmd;
	}

}