package UserSocket;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import Entity.ChatUIEntity;
import Entity.User;
import Frame.ChatUI;
import Frame.FriendsUI;
import _Util.ChatUIList;
import _Util.CommandTranser;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��4�� ����3:52:09
*/
public class ChatTread extends Thread{
	private Client client;
	private boolean isOnline = true; //û�� ------��ɾ
	private User user; //���ͬ��������� ��ˢ�º����б�
	private FriendsUI friendsUI; //ˢ�º����б���
	private String username; //��������µ����촰�ڣ�chatUI)��ô���뽫username����ȥ ����������Ϣ
	
	public ChatTread(Client client, User user, FriendsUI friendsUI) {
		this.client = client;
		this.user = user;
		this.friendsUI = friendsUI;
		this.username = user.getUsername();
		//this.chat_windows = chat_windows;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	//����û�� �Ժ�ɾ---------------------------------------
	public void setOnline(boolean isOnline) {
		 this.isOnline = true; 
	}
	
	//run()�����ǲ���Ҫ�û������õģ���ͨ��start��������һ���߳�֮�󣬵��̻߳����CPUִ��ʱ�䣬
	//�����run������ȥִ�о��������ע�⣬�̳�Thread�������дrun��������run�����ж������Ҫִ�е�����
	@Override
	public void run() {
		if(!isOnline) {
			JOptionPane.showMessageDialog(null,  "unbelievable ������");
			return;
		}
		while(isOnline) {
			
			CommandTranser cmd = client.getData();
			//�����������ͬ������յ�����Ϣ(����)
			//���ﴦ�����Է���������Ϣ(����)
			if(cmd != null) {
				
			 execute(cmd);
			 //System.out.println(cmd.getCmd());	
			}
		}
	}
	
	//������Ϣ(����)
	private void execute(CommandTranser cmd) {
		//��¼���������롢ע����Ϣδ�ڴ˴�����
		System.out.println(cmd.getCmd());
		
		//������Ϣ���� 
		if("message".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			//��ѯ�Ƿ�����ú��ѵĴ��ڸô���
			String friendname = cmd.getSender();
			ChatUI chatUI = ChatUIList.getChatUI(friendname);
			if(chatUI == null) {
				chatUI = new ChatUI(username, friendname, username, client);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName(friendname);
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //�����ǰ������������Ĵ����ڸ��� ��������ʾ
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yy-MM-dd hh:mm:ss a");
			String message = friendname + "˵��"
					+ (String) cmd.getData() + "\t" + sdf.format(date)
					+ "\n";
			chatUI.getChatWin().append(message); //׷����Ϣ
			return;
		}
		
		if("WorldChat".equals(cmd.getCmd())) {
//			if(cmd.isFlag() == false) {
//				JOptionPane.showMessageDialog(null, cmd.getResult()); 
//				return;
//			}
			//��ѯ�Ƿ�����ú��ѵĴ��ڸô���
			String friendname = cmd.getSender();
			ChatUI chatUI = ChatUIList.getChatUI("WorldChat");
			if(chatUI == null) {
				chatUI = new ChatUI("WorldChat", "WorldChat", user.getUsername(), client);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName("WorldChat");
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //�����ǰ������������Ĵ����ڸ��� ��������ʾ
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yy-MM-dd hh:mm:ss a");
			String message = friendname + "˵��"
					+ (String) cmd.getData() + "\t" + sdf.format(date)
					+ "\n";
			chatUI.getChatWin().append(message); //׷����Ϣ
			return;
		}
		
		if("requeste_add_friend".equals(cmd.getCmd())) {
			if(cmd.isFlag() == false) {
				JOptionPane.showMessageDialog(null, cmd.getResult()); 
				return;
			}
			String sendername = cmd.getSender();
			int flag = JOptionPane.showConfirmDialog(null, "�Ƿ�ͬ��" + sendername + "�ĺ�������", "��������", JOptionPane.YES_NO_OPTION);
			System.out.println(flag);
			if(flag == 0) {
				cmd.setCmd("accept_add_friend");
			} else {
				cmd.setCmd("refuse_add_friend");			
			}
			cmd.setSender(username);
			cmd.setReceiver(sendername);
			client.sendData(cmd);
			return;
		}
		
//		if("successful".equals(cmd.getCmd())) {
//			JOptionPane.showMessageDialog(null, cmd.getResult()); 
//			return;
//		}
		
		if("accept_add_friend".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			
//			CommandTranser newcmd = new CommandTranser();
//			newcmd.setCmd("updatefriendlist");
//			newcmd.setReceiver(username);
//			newcmd.setSender(username);
//			newcmd.setData(user);
//			client.sendData(cmd);
			
			return;
			
		}
		
//		if("updatefriendlist".equals(cmd.getCmd())) {
//			if(cmd.isFlag() == false) {
//				JOptionPane.showMessageDialog(null, cmd.getResult()); 
//				return;
//			}
//			
//			User tmp = (User)cmd.getData();
//			user.setFriendsList(tmp.getFriend());
//			friendsUI.validate();
//			friendsUI.repaint();
//			friendsUI.setVisible(true);
//			
//			return;
//		}
		
		if("refuse_to_add".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			return;
		}
		
		if("changepwd".equals(cmd.getCmd())) {
			JOptionPane.showMessageDialog(null, cmd.getResult());
			return;
		}
		return;
	}
	
}

