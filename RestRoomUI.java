package Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.tree.*;

public class RestRoomUI extends JFrame implements ActionListener {

	public int lastRoomNum = 1;
	public JButton makeRoomBtn, makeAnonymousRoomBtn, makeNormalRoomBtn, getInRoomBtn, whisperBtn, sendBtn,friendBtn;
	public JTree userTree;
	public JTree friendTree;//친구목록트리!!
	public JList roomList;
	public Room[] roomArray;	//+방 객체들의 목록
	public JTextField chatField;
	public JTextArea restRoomArea;
	public JLabel lb_id, lb_nick, lb_name;
	public JTextField lb_ip;

	private SixClient client;
	public ArrayList<User> userArray; // 사용자 목록 배열
	public String currentSelectedTreeNode;
	public DefaultListModel model;
	public DefaultMutableTreeNode level_1;
	public DefaultMutableTreeNode level_2_1;
	public DefaultMutableTreeNode level_2_2;

	public DefaultMutableTreeNode level1;//친구  트리노드추가
	public DefaultMutableTreeNode level21;
	public DefaultMutableTreeNode level22;
	public DefaultMutableTreeNode level2;
	
	
	public RestRoomUI(SixClient sixClient) 
	{
		setTitle("대기실");
		userArray = new ArrayList<User>();
		client = sixClient;
		initialize();
	}

	private void initialize() 
	{
		setBounds(100, 100, 700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// 대기실 창을 끄면 열려있는 연결된 모든 창이 꺼짐

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu basicMenus = new JMenu("일반");
		basicMenus.addActionListener(this);
		menuBar.add(basicMenus);

		JMenuItem changeNickItem = new JMenuItem("닉네임 변경");
		changeNickItem.addActionListener(this);
		basicMenus.add(changeNickItem);

		JMenuItem exitItem = new JMenuItem("끝내기");
		exitItem.addActionListener(this);
		basicMenus.add(exitItem);

		JMenu helpMenus = new JMenu("도움말");
		helpMenus.addActionListener(this);
		menuBar.add(helpMenus);

		JMenuItem proInfoItem = new JMenuItem("프로그램 정보");
		proInfoItem.addActionListener(this);
		helpMenus.add(proInfoItem);
		getContentPane().setLayout(null);

		JPanel room = new JPanel();
		room.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "채팅방",TitledBorder.CENTER, TitledBorder.TOP, null, null));
		room.setBounds(12, 10, 477, 215);
		getContentPane().add(room);
		room.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		room.add(scrollPane, BorderLayout.CENTER);

		// 리스트 객체와 모델 생성
		roomList = new JList(new DefaultListModel());
		roomList.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// 채팅방 목록 중 하나를 선택한 경우,
				// 선택한 방의 방번호를 전송
				String temp = (String) roomList.getSelectedValue();

				try
				{
					client.getUser().getDos().writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + "/"+ temp.substring(0, 3));
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		model = (DefaultListModel) roomList.getModel();
		roomList.setBackground(new Color(224, 255, 255));
		scrollPane.setViewportView(roomList);

		JPanel panel_2 = new JPanel();
		room.add(panel_2, BorderLayout.SOUTH);

		makeRoomBtn = new JButton("일반 채팅방 만들기");	// +일반 채팅방 생성
		makeRoomBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) { }
		});
		makeRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{	// 방만들기 버튼 클릭
				String roomName = JOptionPane.showInputDialog(null, "방 제목을 입력하세요.");	// 방 이름을 입력받음
				createRoom(roomName);
			}
		});
		
		makeAnonymousRoomBtn = new JButton("익명 채팅방 만들기");	// +익명 채팅방 생성
		makeAnonymousRoomBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) { }
		});
		makeAnonymousRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{	// 방만들기 버튼 클릭
				String roomName = JOptionPane.showInputDialog(null, "방 제목을 입력하세요.");	// 방 이름을 입력받음
				createAnonymousRoom(roomName);
			}
		});
		
		makeRoomBtn.setSize(250, 50);
		makeAnonymousRoomBtn.setSize(250, 50);
		
		panel_2.add(makeRoomBtn);
		panel_2.add(makeAnonymousRoomBtn);

		getInRoomBtn = new JButton("방 입장하기");
		getInRoomBtn.setSize(250, 50);
		getInRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				// 방 들어가기
				getIn();
			}
		});
		panel_2.add(getInRoomBtn);

////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel user = new JPanel();
		user.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),"사용자 목록", TitledBorder.CENTER,TitledBorder.TOP, null, null));
		user.setBounds(501, 10, 171, 215);
		getContentPane().add(user);
		user.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		user.add(scrollPane_1, BorderLayout.CENTER);

		// 사용자목록, 트리구조
		userTree = new JTree();
		userTree.addTreeSelectionListener(new TreeSelectionListener() 
		{
			public void valueChanged(TreeSelectionEvent arg0) 
			{
				currentSelectedTreeNode = arg0.getPath().getLastPathComponent().toString();
			}
		});
		level_1 = new DefaultMutableTreeNode("참여자");
		level_2_1 = new DefaultMutableTreeNode("채팅방");
		level_2_2 = new DefaultMutableTreeNode("대기실");
		level_1.add(level_2_1);
		level_1.add(level_2_2);

		DefaultTreeModel model = new DefaultTreeModel(level_1);
		userTree.setModel(model);

		scrollPane_1.setViewportView(userTree);


			
		
		
		JPanel panel_1 = new JPanel();
		user.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		whisperBtn = new JButton("귓속말");
		whisperBtn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				StringTokenizer token = new StringTokenizer(currentSelectedTreeNode, "("); // 토큰 생성
				String temp = token.nextToken(); // 토큰으로 분리된 스트링
				temp = token.nextToken();

				// 닉네임 제외하고 아이디만 따옴
				chatField.setText("/" + temp.substring(0, temp.length() - 1)+ " ");
				chatField.requestFocus();
			}
		});
		panel_1.add(whisperBtn);

		JPanel restroom = new JPanel();
		restroom.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "대 기 실",
				TitledBorder.CENTER, TitledBorder.TOP, null, Color.DARK_GRAY));
		restroom.setBounds(12, 235, 477, 185);
		getContentPane().add(restroom);
		restroom.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		restroom.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JScrollPane scrollPane_4 = new JScrollPane();
		panel.add(scrollPane_4);

		chatField = new JTextField();

		chatField.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					msgSummit();
				}
			}
		});
		scrollPane_4.setViewportView(chatField);
		chatField.setColumns(10);

		sendBtn = new JButton("보내기");
		sendBtn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				msgSummit();
				chatField.requestFocus();
			}
		});
		panel.add(sendBtn);

		JScrollPane scrollPane_2 = new JScrollPane();
		restroom.add(scrollPane_2, BorderLayout.CENTER);

		restRoomArea = new JTextArea();
		restRoomArea.setBackground(new Color(224, 255, 255));
		restRoomArea.setEditable(false);
		scrollPane_2.setViewportView(restRoomArea);
		
///////////////////////////////////////////////////////////////////////친구목록트리/////
		JPanel panel9 = new JPanel();		//메인프레임 
		panel9.setBackground(SystemColor.control);
		panel9.setBounds(501, 235, 171, 185);
		getContentPane().add(panel9);
		panel9.setLayout(new BorderLayout(0, 0));
		panel9.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"친구 목록", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		JScrollPane scrollPane_9 = new JScrollPane();
		panel9.add(scrollPane_9, BorderLayout.CENTER);
//////////////////////////////////////////////////친구추가버튼
		JPanel addfriendpannel = new JPanel();
		panel9.add(addfriendpannel, BorderLayout.SOUTH);
		addfriendpannel.setLayout(new GridLayout(1, 0, 0, 0));
		
		friendBtn = new JButton("친구추가");
		friendBtn.setSize(50, 50);
		friendBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				
				addFriend();
			}
		});
		addfriendpannel.add(friendBtn);


		///////////////////////////////////////////////
		
		friendTree = new JTree();
		friendTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent arg0) {

				currentSelectedTreeNode = arg0.getPath().getLastPathComponent()

						.toString();

			}

		});
		level1 = new DefaultMutableTreeNode("친구목록");
		DefaultTreeModel model2 = new DefaultTreeModel(level1);
		friendTree.setModel(model2);
		
		scrollPane_9.setViewportView(friendTree);

		JPanel panel_9 = new JPanel();
	panel9.add(panel_9, BorderLayout.NORTH);
	panel_9.setLayout(new GridLayout(1, 0, 0, 0));
	///////////////////////////////////////////////////////////
		
//////////////////////////////////////////내정보부분 안나타나게함
		JPanel info = new JPanel();
//		info.setBorder(new TitledBorder(UIManager
//				.getBorder("TitledBorder.border"), "내 정보",
//				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
//		info.setBounds(501, 235, 171, 185);
//		getContentPane().add(info);
//		info.setLayout(null);
///////////////////////////////////////
		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(-25, 20, 57, 15);
		info.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("아이디");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 60, 57, 15);
		info.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("이름");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(0, 100, 57, 15);
		info.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("닉네임");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setBounds(0, 140, 57, 15);
		info.add(lblNewLabel_4);

		lb_id = new JLabel("-");
		info.add(lb_id);
		lb_id.setBounds(57, 60, 102, 15);

		lb_name = new JLabel("-");
		lb_name.setBounds(57, 100, 102, 15);
		info.add(lb_name);
		
		lb_nick = new JLabel("-");
		lb_nick.setBounds(57, 140, 102, 15);
		info.add(lb_nick);
		
		lb_ip = new JTextField();
		lb_ip.setBounds(57, 20, 102, 21);
		lb_ip.setEditable(false);
		info.add(lb_ip);
		lb_ip.setColumns(10);
		chatField.requestFocus();
		setVisible(true);
		chatField.requestFocus();

		this.addWindowListener(new WindowAdapter() 
		{	
			public void windowClosing(WindowEvent e) 
			{
				exit();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		switch (e.getActionCommand()) 
		{
		// 메뉴1 파일 메뉴
		case "닉네임 변경":
			changeNick();
			break;
		case "끝내기":
			exit();
			break;
		case "프로그램 정보":
			maker();
			System.out.println("토스트");
			break;
		}
	}

	private void changeNick() 
	{
		String temp = JOptionPane.showInputDialog(this, "변경할 닉네임을 입력하세요.", "닉네임 변경");
		if (temp != null && !temp.equals("")) 
		{
			try 
			{
				client.getUser().getDos().writeUTF(User.CHANGE_NICK + "/" + temp + "/" + client.getUser().getName());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private void msgSummit()
	{
		// 메시지전송
		String string = chatField.getText();

		if (!string.equals("")) 
		{
			if (string.substring(0, 1).equals("/")) 
			{
				StringTokenizer token = new StringTokenizer(string, " "); // 토큰																			// 생성
				String id = token.nextToken(); // 토큰으로 분리된 스트링
				String msg = token.nextToken();

				try 
				{
					client.getDos().writeUTF(User.WHISPER + id + "/" + msg);
					restRoomArea.append("("+id+")" + "님에게 귓속말 : " + msg + "\n");
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				chatField.setText("");
			} 
			else
			{
				try
				{
					// 대기실에 메시지 보냄
					client.getDos().writeUTF(User.ECHO01 + "/" + string);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				chatField.setText("");
			}
		}
	}

	private void exit() 
	{
		try 
		{
			client.getUser().getDos().writeUTF(User.LOGOUT);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void createRoom(String roomName) 
	{
		Room newRoom = new Room(roomName); // 방 객체 생성
		newRoom.setRoomNum(lastRoomNum);
		newRoom.setRoomType("일반");
		newRoom.setrUI(new RoomUI(client, newRoom)); // UI

		// 클라이언트가 접속한 방 목록에 추가
		client.getUser().getRoomArray().add(newRoom);
		try
		{
			client.getDos().writeUTF(User.CREATE_ROOM + "/" + newRoom.toProtocol());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void createAnonymousRoom(String roomName) 
	{	// +익명 채팅방 생성 메소드
		Room newRoom = new Room(roomName); // 방 객체 생성
		newRoom.setRoomNum(lastRoomNum);
		newRoom.setRoomType("익명");
		newRoom.setrUI(new RoomUI(client, newRoom)); // UI
		
		// 클라이언트가 접속한 방 목록에 추가
		client.getUser().getRoomArray().add(newRoom);

		try 
		{
			client.getDos().writeUTF(User.CREATE_ROOM + "/" + newRoom.toProtocol());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void getIn() 
	{
		// 선택한 방 정보
		String selectedRoom = (String) roomList.getSelectedValue();
		StringTokenizer token = new StringTokenizer(selectedRoom, "/"); // 토큰 생성
		String rNum = token.nextToken();
		String rName = token.nextToken();
		String rType = token.nextToken();

		for(int i=0; i<client.getUser().getRoomArray().size(); i++)	// +접속 중인 방 번호 목록에 접속 시도한 방의 번호가 있을 경우 
		{
			if(client.getUser().getRoomArray().get(i).getRoomNum() == Integer.parseInt(rNum))
			{
				return;	// 입장 불가
			}
		}
		
		Room theRoom = new Room(rName); // 방 객체 생성
		theRoom.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정
		theRoom.setRoomType(rType);	// +방 타입 설정
		theRoom.setrUI(new RoomUI(client, theRoom)); // UI
		
		// 클라이언트가 접속한 방 목록에 추가
		client.getUser().getRoomArray().add(theRoom);

		try
		{
			client.getDos().writeUTF(User.GETIN_ROOM + "/" + theRoom.getRoomNum());
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void maker() 
	{
		JDialog maker = new JDialog();
		Maker m = new Maker();
		maker.setTitle("프로그램 정보");
		maker.getContentPane().add(m);
		maker.setSize(400, 170);
		maker.setVisible(true);
		maker.setLocation(400, 350);
		maker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	private void addFriend() {/////serverThread로 보내는 내용

		String friend = "";
		friend=chatField.getText();
		


		try {

			client.getDos().writeUTF(
					User.FRIEND + "/"+client.getUser().getId()+"/"+ friend);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	
}


class Maker extends JPanel {
	public Maker() 
	{
		super();
		initialize();
	}

	private void initialize() 
	{
		this.setLayout(new GridLayout(3, 1));

		JLabel j1 = new JLabel("       프로그램 제작자 : 청년취업아카데미");
		JLabel j2 = new JLabel("       수정한 사람 : CBNU");
		JLabel j3 = new JLabel("       프로그램 버전 : 1.0v  ( 13 . 8 . 29 )");

		this.add(j1);
		this.add(j2);
		this.add(j3);
	}
}