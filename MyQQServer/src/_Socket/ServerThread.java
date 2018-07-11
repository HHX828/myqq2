package _Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Entity.SocketEntity;
import Entity.User;
import _Service.UserService;
import _Util.CommandTranser;
import _Util.SocketList;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��4�� ����8:23:23
*/
public class ServerThread extends Thread{
	private Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		ObjectOutputStream oos1 = null;
		ObjectOutputStream oos2 = null;
		//ObjectOutputStream oos3 = null;
		
		while(socket != null) {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				CommandTranser cmd = (CommandTranser) ois.readObject();
				
				//ִ���������Կͻ��˵�����
				cmd = execute(cmd);
				
				//��Ϣ�Ի����󣬷�������sender��������Ϣ���͸�receiver
				if("message".equals(cmd.getCmd())) {
					//��� msg.ifFlag�� ����������ɹ� ���������ѷ�����Ϣ ���������������Ϣʧ�� ��Ϣ���͸������߱���
					if(cmd.isFlag()) {
						//System.out.println("�Է�����");
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					} else {
						//System.out.println("�Է�δ����");
						
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				if ("WorldChat".equals(cmd.getCmd())) {
					HashMap<String, Socket> map = SocketList.getMap();
					Iterator<Map.Entry<String, Socket>> it = map.entrySet().iterator();
					while(it.hasNext()) {
						Map.Entry<String, Socket> entry = it.next();
						if(!entry.getKey().equals(cmd.getSender())) {
							oos1 = new ObjectOutputStream(entry.getValue().getOutputStream());
							oos1.writeObject(cmd);
						}
					}
					continue;
					
				}
				//��¼���� �����ݷ��͸�sender
				if ("login".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//ע������ �����ݷ��͸�sender
				if ("register".equals(cmd.getCmd())) {
					System.out.println("��ͻ��˷�����Ϣ");
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//��Ӻ����������ݷ��͸� receiver
				if ("requeste_add_friend".equals(cmd.getCmd())) {
					//���ߣ������󷢸�receiver
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
//						oos3 = new ObjectOutputStream(socket.getOutputStream());
//						CommandTranser newcmd =  new CommandTranser();
//						newcmd = cmd;
//						newcmd.setCmd("successful");
//						oos3.writeObject(newcmd);
					} else {
						//�����ڲ����߶�Ҫ���ͷ���ʾ��Ϣ���ͳɹ�
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				//ͬ����Ӻ����������ݷ��͸� receiver��sender
				if ("accept_add_friend".equals(cmd.getCmd())) {
					//�����Ƿ�ɹ��������ݿⶼҪ��������������п����������Ŀͻ�������
					oos1 = new ObjectOutputStream(socket.getOutputStream());
					if(SocketList.getSocket(cmd.getReceiver()) != null) {
						oos2 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					}
				}
				
//				if("updatefriendlist".equals(cmd.getCmd())) {
//					oos1 = new ObjectOutputStream(socket.getOutputStream());
//				}
				
				//�ܾ���Ӻ����������ݷ��͸� receiver
				if ("refuse_to_add".equals(cmd.getCmd())) {
					//���ܾ�������
					if(cmd.isFlag()) {
						oos1 = new ObjectOutputStream(SocketList.getSocket(cmd.getReceiver()).getOutputStream());
					}else { //���ܷ�����������ܾ���������Ϣ
						oos2 = new ObjectOutputStream(socket.getOutputStream());
					}
				}
				
				//�޸��������� ���͸�sender ������δʵ��
				if ("changeinfo".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�޸��������� �����ݷ��͸�sender
				if ("changepwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�������� ���͸�sender
				if ("forgetpwd".equals(cmd.getCmd())) {
					oos1 = new ObjectOutputStream(socket.getOutputStream());
				}
				
				//�û�����
				if("logout".equals(cmd.getCmd())) {
					//
				}
				
				if(oos1 != null) {
					oos1.writeObject(cmd);	
				}
				if(oos2 != null) {
					oos2.writeObject(cmd);	
				}
			} catch(IOException e) {
				socket = null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//����ͻ��˷���������
	private CommandTranser execute(CommandTranser cmd) {
		
		//��¼����
		if("login".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.checkUser(user));
			//�����½�ɹ������ÿͻ��˼����Ѿ����ӳɹ���map�������� ���ҿ������û��Ľ����߳�
			if(cmd.isFlag()) {
				// �����̼߳������ӳɹ���map����
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				
				//�����ݿ��ȡ������б���������б������ͻ���
				cmd.setData(userservice.getFriendsList(user));
				cmd.setResult("��½�ɹ�");
			} else {
				cmd.setResult("�������");
			}
		}
		
		//ע������
		if("register".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.registerUser(user));
			//���ע��ɹ�
			if(cmd.isFlag()) {
				SocketEntity socketEntity = new SocketEntity();
				socketEntity.setName(cmd.getSender());
				socketEntity.setSocket(socket);
				SocketList.addSocket(socketEntity);
				cmd.setData(userservice.getFriendsList(user));
				//��ע��Ŀ϶�û�к��� 
				cmd.setResult("ע��ɹ�");
			} else {
				cmd.setResult("ע��ʧ�ܿ��ܸ��û��Ѵ���");
			}
		}
		
		//�޸��������� ������δʵ��
		if("changeInfo".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changeInfo(user));
			if(cmd.isFlag()) {
				cmd.setResult("�޸���Ϣ�ɹ�");
			} else {
				cmd.setResult("�޸���Ϣʧ��");
			}
		}
		
		//��Ӻ���
		if("requeste_add_friend".equals(cmd.getCmd())) {
			//����û��Ƿ�����
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				cmd.setResult("�Է��յ������ĺ�������");
			} else {
				cmd.setFlag(false);
				cmd.setResult("��ǰ�û������߻��߸��û�������");
			}
		}
		
		//ͬ����Ӻ�������
		if("accept_add_friend".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			cmd.setFlag(userservice.addFriend(cmd.getReceiver(), cmd.getSender()));
			if(cmd.isFlag()) {
				cmd.setResult("������ӳɹ������µ�½ˢ��");
			} else {
				cmd.setResult("���������ϵ�����Ӻ���ʧ�ܻ��������Ѿ�Ϊ����");
			}
		} 
		
//		if("updatefriendlist".equals(cmd.getCmd())) {
//			UserService userservice = new UserService();
//			User user = (User)cmd.getData();
//			user = userservice.getFriendsList(user);
//			if(user.getFriendsNum() == 0) {
//				cmd.setFlag(false);
//			} else {
//				cmd.setData(user);
//			}
//		}
		
		//�ܾ���Ӻ���
		if("refuse_to_add".equals(cmd.getCmd())) {
			//����Ƿ�����
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				cmd.setFlag(true);
				cmd.setResult("���� " + cmd.getSender() +  " �ܾ���");
			} else {
				cmd.setFlag(false);
				cmd.setResult("�Է������߲�֪����ܾ������ĺ�������");
			}
		}
		
		//������Ϣָ��
		if("message".equals(cmd.getCmd())) {
			//����Ƿ�����
			if(SocketList.getSocket(cmd.getReceiver()) != null) {
				//System.out.println("����");
				cmd.setFlag(true);
				//cmd.setResult("�Է��ɹ��յ�������Ϣ");
			} else {
				//System.out.println("���氡");
				cmd.setFlag(false);
				cmd.setResult("��ǰ�û�������");
			}
		}
		
		if("WordChat".equals(cmd.getCmd())) {
			cmd.setFlag(true);
		}
		//��������ָ�� �������Ҫ���û�������ʹ𰸷���
		if("forgetpwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			user = userservice.getUser(user);
			//����û�����
			if(user != null ) {
				cmd.setResult("��ѯ�ɹ�");
				cmd.setData(user);
				cmd.setFlag(true);
			} else {
				cmd.setResult("�û����ܲ�����");
				cmd.setFlag(false);
			}
		}	
		
		
		if ("changepwd".equals(cmd.getCmd())) {
			UserService userservice = new UserService();
			User user = (User)cmd.getData();
			cmd.setFlag(userservice.changePassword(user));
			System.out.println("there 111 ");
			System.out.println(user.getUsername());
			if(cmd.isFlag()) {
				cmd.setResult("�޸�����ɹ�");
			}else {
				cmd.setResult("�޸�����ʧ��");
			}
		}
		
		if("logout".equals(cmd.getCmd())) {
			SocketList.getSocket(cmd.getSender());
		}
		
		return cmd;
	}
}
