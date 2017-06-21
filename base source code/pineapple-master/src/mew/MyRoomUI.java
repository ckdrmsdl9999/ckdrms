package mew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;


import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MyRoomUI extends JFrame {		//RoomUI ����

	private SixClient client;
	private Room room;

	public JTextArea chatArea;
	public JTextField chatField;	
	public JList uList;
	public DefaultListModel model;
//////(����, �߰�)	
	public boolean botSwitchOff = true;	//chatBot�� ���� ��. default���� true����. chatBot��ü ���� ��, false�� �ٲ�.
	public ChatBot5 maggie = null;		//chatBot5 �ڷ��� maggie
/////
	public MyRoomUI(SixClient client, Room room) {		//client�� room���� ���� �Ǹ� ����Ǵ� Ŭ����
		this.client = client;					//������
		this.room = room;						//���ӹ�
		setTitle("�¹� ä�ù� : " + room.toProtocol());
		initialize();				//�ʱ�ȭ
	}

	private void initialize() {
		setBounds(100, 100, 428, 495);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

/////////�����ڸ� ��Ÿ���� �ڵ�
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "������",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(14, 12, 181, 52);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		scrollPane_1.setViewportView(uList);
		
		
//////////ä�� dialog�� Ȯ���� �� �ִ� â
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ä��",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(12, 65, 385, 301);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		chatArea = new JTextArea();		
		chatArea.setBackground(new Color(224, 255, 255));
		chatArea.setEditable(false);
		scrollPane.setViewportView(chatArea);
		chatArea.append("�߳����� ä���� ���� �Ǿ���.\n");		//������ ä�ù��� �����Ǹ� Ȯ�� ������ ����

///////ä�� �Է��ϴ� â
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		chatField = new JTextField();
		panel_1.add(chatField);		//chatField�� �Էµ� ���� panel_1�� ��Ÿ��
		chatField.setColumns(10);		//��������
		
		chatField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {			//Ű �Է��̶�� �̺�Ʈ �߻�
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {		//���͸� ������
					if(botSwitchOff){msgSummit();}		//chatBot�� ����������
					else{chatBotMsgSummit(maggie);}
				}
			}
		});
///////������ ��ư
		JButton roomSendBtn = new JButton("������");			//������ ��ư
		roomSendBtn.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent arg0) {		//������ ��ư�� ���ȴٸ�
				msgSummit();							//������ �޽��� ����
				chatField.requestFocus();				//chatField.
			}
		});
		roomSendBtn.setBounds(314, 378, 83, 34);
		getContentPane().add(roomSendBtn);
		

////////////(����, �߰�)ê��, Ķ����, ����, ������ ������ �ִ� ĭ
		//chatbot��ư
			JButton chatbot_button = new JButton(new ImageIcon("c:\\chatbot.png"));			//�̹���
			
			chatbot_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(botSwitchOff == false)
					{
						maggie = null;
						botSwitchOff = true;
						chatArea.append("��ChatBot�� ��Ȱ��ȭ �Ǿ���.\n");
					}
					else{
						maggie = new ChatBot5();			//ChatBot5�ڷ����� maggie ��ü ����
						botSwitchOff = false;				//chatBot�� �������ϴ�.
						
						chatArea.append("��ChatBot�� Ȱ��ȭ �Ǿ���.\n");		//ChatBot����(��ü����)�� ���������� �Ǿ���.
						chatArea.append("Maggie(ChatBot) " + maggie.getGreeting());		//�λ�
					
						roomSendBtn.addActionListener(new ActionListener() {		
							public void actionPerformed(ActionEvent arg0) {		//������ ��ư�� ���ȴٸ�
							chatBotMsgSummit(maggie);							//������ �޽��� ����
							chatField.requestFocus();				//�ԷµǴ� �ؽ�Ʈ��ŭ Ű���� Ŀ�� ������
							}
						});
					}	
				}		
			});
					chatbot_button.setFocusPainted(false);
					chatbot_button.setContentAreaFilled(false);
					chatbot_button.setBorderPainted(false);
					chatbot_button.setBackground(Color.RED);
					chatbot_button.setBounds(209, 20, 42, 44);
					getContentPane().add(chatbot_button);
					
			//Ķ���� ��ư
			JButton calendar_button = new JButton(new ImageIcon("c:/calendarBTN.jpg"));		
				calendar_button.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) {
					new SwingCalendar();
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					}
				});
				calendar_button.setBackground(Color.red);
				calendar_button.setBounds(255, 20, 42, 44);
				calendar_button.setBorderPainted(false);
				calendar_button.setFocusPainted(false);
				calendar_button.setContentAreaFilled(false);
				getContentPane().add(calendar_button);
			
			//ȯ�漳��
			JButton option_button = new JButton(new ImageIcon("c:/settingbtn.png"));	
				option_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				option_button.setBackground(Color.red);
				option_button.setBounds(294, 14, 50, 50);
				option_button.setBorderPainted(false);
				option_button.setFocusPainted(false);
				option_button.setContentAreaFilled(false);
				getContentPane().add(option_button);
				
			//�濡�� ������ �ʰ� â ����
			JButton out_button = new JButton(new ImageIcon("c:/outbtn.png"));		
				out_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				out_button.setBackground(Color.red);
				out_button.setBounds(346, 14, 50, 50);
				out_button.setBorderPainted(false);
				out_button.setFocusPainted(false);
				out_button.setContentAreaFilled(false);
				getContentPane().add(out_button);
				
///////�� ���� menubar				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		chatField.requestFocus();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					client.getUser()
							.getDos()
							.writeUTF(
									User.GETOUT_ROOM + "/" + room.getRoomNum());
					for (int i = 0; i < client.getUser().getRoomArray().size(); i++) {
						if (client.getUser().getRoomArray().get(i).getRoomNum() == room
								.getRoomNum()) {
							client.getUser().getRoomArray().remove(i);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void msgSummit() {			//�޽��� ����		
		String string = chatField.getText();		//chatField���� �Էµ� �޽����� string�� ����
		if (!string.equals("")) {					//string�� ������ �ƴ϶��
			try {
				// ä�ù濡 �޽��� ����<������ ���̴� ����>
				client.getDos().writeUTF(
						User.ECHO03 + "/" + room.getRoomNum() + "/"
								+ client.getUser().toString() + string);
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

///////(����, �߰�)   chatBot���� ��ȭ�� ���� �ڵ�	
	private void chatBotMsgSummit(ChatBot5 maggie){
		String sendBotMsg = chatField.getText();	//chatBot�� ���� �޽��� sendBot�� ����
		if (!sendBotMsg.equals("")) {
			try {
				// ä�ù濡 �Է��� �޽��� ������
				client.getDos().writeUTF(
						User.ECHO03 + "/" + room.getRoomNum() + "/"
								+ client.getUser().toString() + " " + sendBotMsg);
				chatField.setText("");
				
				String chatBotAns = maggie.getResponse(sendBotMsg);		//chatBot�� �� String
				
				client.getDos().writeUTF(								//chatBot�� �� ����
						User.ECHOBOT + "/" + room.getRoomNum() + "/"
								+ "Maggie(ChatBot)" + " "+ chatBotAns);		
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//////////////////////////////	
}
