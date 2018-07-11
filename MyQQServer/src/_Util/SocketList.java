package _Util;

import java.net.Socket;
import java.util.HashMap;

import Entity.SocketEntity;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��5�� ����2:01:56
*/
public class SocketList {
	private static HashMap<String, Socket> map = new HashMap<String, Socket>();
	public static void addSocket(SocketEntity socketEntity) {
		map.put(socketEntity.getName(), socketEntity.getSocket());	
	}
	
	//ͨ���ǳƷ���socket ���socklist�ڿͻ��˴��� ChatUIList
	public static Socket getSocket(String name) {
		return map.get(name);
	}
	
	public static HashMap<String, Socket> getMap(){
		return map;
	}
	public static void deleteSocket(String name) {
		if(map.get(name) != null) {
			map.remove(name);
		}
		return;
	}
}
