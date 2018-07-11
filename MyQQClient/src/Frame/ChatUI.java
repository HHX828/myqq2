package Frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import UserSocket.Client;
import _Util.ChatUIList;
import _Util.CommandTranser;

/**
* @author zzz
* @version ����ʱ�䣺2018��7��4�� ����1:41:36
*/
public class ChatUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextArea chat_windows; //˫��������Ϣ���ı���
	private JTextField message_txt; //д��Ϣ���ı���
	private JButton send_btn; //���Ͱ�ť
	private JPanel panel;
	private String owner_name;
	private String friend_name;
	private String who;
	private Client client; //�ڷ���Ϣ�õ��� ����Ϣ�����������
	//private ChatTread thread; //������Ϣ�߳�
	
	public ChatUI(String ower_name, String friend_name, String who, Client client){
		this.owner_name = ower_name;
		this.friend_name = friend_name;
		this.client = client;
		this.who = who;
		
		//��������ҳ��
		init();
		
		setTitle(ower_name + "���ں�" + friend_name + "����");
		setSize(350, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		// �����ͻ��˽�����Ϣ�߳�,���յ�����Ϣ������ chat_windows
		//thread = new ChatTread(client); //��Ψһclient����
		//thread.start();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		panel = new JPanel();
		message_txt = new JTextField(20);
		send_btn = new JButton("����");
		panel.add(message_txt);
		panel.add(send_btn);
		chat_windows = new JTextArea();
		chat_windows.setEditable(false);
		chat_windows.add(new JScrollBar(JScrollBar.VERTICAL)); //������
		add(chat_windows, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		send_btn.addActionListener(this);
		
		//���ڹر��¼�
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatUIList.deletChatUI(friend_name);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				ChatUIList.deletChatUI(friend_name);
			}
		});
	}
	//���ͷ���Ϣ
	public JTextArea getChatWin() {
		return chat_windows;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//������Ͱ�ť
		if(e.getSource() == send_btn) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
			
			String message = "��˵ : " + message_txt.getText() + "\t"
					+ sdf.format(date) + "\n";
			//����������Ϣ
			chat_windows.append(message);
			
			//����
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("message");
			if("WorldChat".equals(owner_name)) {
				cmd.setCmd("WorldChat");
			}
			cmd.setSender(who);
			cmd.setReceiver(friend_name);
			cmd.setData(message_txt.getText());
			
			//����
			client.sendData(cmd);
			
			//��������Ϣ���������������
			message_txt.setText(null);
		}
	}
	
}
