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

public class MyRoomUI extends JFrame {		//RoomUI 실행

	private SixClient client;
	private Room room;

	public JTextArea chatArea;
	public JTextField chatField;	
	public JList uList;
	public DefaultListModel model;
//////(문용, 추가)	
	public boolean botSwitchOff = true;	//chatBot을 위한 것. default값은 true상태. chatBot객체 생성 시, false로 바뀜.
	public ChatBot5 maggie = null;		//chatBot5 자료형 maggie
/////
	public MyRoomUI(SixClient client, Room room) {		//client와 room정보 전달 되면 실행되는 클래스
		this.client = client;					//접속자
		this.room = room;						//접속방
		setTitle("뮤뮤 채팅방 : " + room.toProtocol());
		initialize();				//초기화
	}

	private void initialize() {
		setBounds(100, 100, 428, 495);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

/////////참여자를 나타내는 코드
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "참여자",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(14, 12, 181, 52);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		scrollPane_1.setViewportView(uList);
		
		
//////////채팅 dialog를 확인할 수 있는 창
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "채팅",
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
		chatArea.append("◆나와의 채팅이 시작 되었다.\n");		//나와의 채팅방이 개설되며 확인 가능한 문구

///////채팅 입력하는 창
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		chatField = new JTextField();
		panel_1.add(chatField);		//chatField에 입력된 내용 panel_1에 나타냄
		chatField.setColumns(10);		//길이조절
		
		chatField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {			//키 입력이라는 이벤트 발생
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {		//엔터를 누르면
					if(botSwitchOff){msgSummit();}		//chatBot이 꺼져있으면
					else{chatBotMsgSummit(maggie);}
				}
			}
		});
///////보내기 버튼
		JButton roomSendBtn = new JButton("보내기");			//보내기 버튼
		roomSendBtn.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent arg0) {		//보내기 버튼이 눌렸다면
				msgSummit();							//서버에 메시지 전송
				chatField.requestFocus();				//chatField.
			}
		});
		roomSendBtn.setBounds(314, 378, 83, 34);
		getContentPane().add(roomSendBtn);
		

////////////(문용, 추가)챗봇, 캘린더, 설정, 나가기 아이콘 있는 칸
		//chatbot버튼
			JButton chatbot_button = new JButton(new ImageIcon("c:\\chatbot.png"));			//이미지
			
			chatbot_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(botSwitchOff == false)
					{
						maggie = null;
						botSwitchOff = true;
						chatArea.append("◆ChatBot이 비활성화 되었다.\n");
					}
					else{
						maggie = new ChatBot5();			//ChatBot5자료형의 maggie 객체 생성
						botSwitchOff = false;				//chatBot이 켜졌습니다.
						
						chatArea.append("◆ChatBot이 활성화 되었다.\n");		//ChatBot실행(객체생성)이 정상적으로 되었음.
						chatArea.append("Maggie(ChatBot) " + maggie.getGreeting());		//인사
					
						roomSendBtn.addActionListener(new ActionListener() {		
							public void actionPerformed(ActionEvent arg0) {		//보내기 버튼이 눌렸다면
							chatBotMsgSummit(maggie);							//서버에 메시지 전송
							chatField.requestFocus();				//입력되는 텍스트만큼 키보드 커서 움직임
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
					
			//캘린더 버튼
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
			
			//환경설정
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
				
			//방에서 나가지 않고 창 끄기
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
				
///////맨 위의 menubar				
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

	private void msgSummit() {			//메시지 제출		
		String string = chatField.getText();		//chatField에서 입력된 메시지를 string에 저장
		if (!string.equals("")) {					//string이 공백이 아니라면
			try {
				// 채팅방에 메시지 보냄<서버에 보이는 문구>
				client.getDos().writeUTF(
						User.ECHO03 + "/" + room.getRoomNum() + "/"
								+ client.getUser().toString() + string);
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

///////(문용, 추가)   chatBot과의 대화를 위한 코드	
	private void chatBotMsgSummit(ChatBot5 maggie){
		String sendBotMsg = chatField.getText();	//chatBot에 보낸 메시지 sendBot에 저장
		if (!sendBotMsg.equals("")) {
			try {
				// 채팅방에 입력한 메시지 보내기
				client.getDos().writeUTF(
						User.ECHO03 + "/" + room.getRoomNum() + "/"
								+ client.getUser().toString() + " " + sendBotMsg);
				chatField.setText("");
				
				String chatBotAns = maggie.getResponse(sendBotMsg);		//chatBot의 답 String
				
				client.getDos().writeUTF(								//chatBot의 답 띄우기
						User.ECHOBOT + "/" + room.getRoomNum() + "/"
								+ "Maggie(ChatBot)" + " "+ chatBotAns);		
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//////////////////////////////	
}
