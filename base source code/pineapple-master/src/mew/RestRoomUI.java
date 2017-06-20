package mew;

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
	public JButton makeRoomBtn, makeAnonymousRoomBtn, makeNormalRoomBtn, getInRoomBtn, sendBtn,friendBtn;
	public JTree userTree;
	public JTree friendTree;	//친구목록트리
	public JList roomList;
	public JTextField chatField;
	public JTextArea restRoomArea;
	public JLabel lb_id, lb_nick, lb_name;
	public JTextField lb_ip;

	private SixClient client;
	//------------------------------------------------------------------//
	// 변경된 설정값을 전달할 때 필요한 참조 변수 tab
	private textAndBackground tab;
	//------------------------------------------------------------------//
	// 설정창
	public Settings settings;
	//------------------------------------------------------------------//
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
		JDialog.setDefaultLookAndFeelDecorated(true);

		userArray = new ArrayList<User>();
		//------------------------------------------------------------------//
		// 참조 변수에 생성된 객체의 주소 값 대입
		tab = new textAndBackground();
		//------------------------------------------------------------------//
		client = sixClient;
		initialize();
	}

	private void initialize() 
	{
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// 대기실 창을 끄면 열려있는 연결된 모든 창이 꺼짐
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu basicMenus = new JMenu("일반");
		basicMenus.addActionListener(this);
		menuBar.add(basicMenus);

		JMenuItem changeNickItem = new JMenuItem("닉네임 변경");
		changeNickItem.addActionListener(this);
		basicMenus.add(changeNickItem);
		
		//-----------------------------------------------------------------//
	    //  설정변경목록 추가
	    JMenuItem settingItem = new JMenuItem("\uC124\uC815 \uBCC0\uACBD");
	    settingItem.addActionListener(this);
	    basicMenus.add(settingItem);
	    getContentPane().setLayout(null);
	    //----------------------------------------------------------------//

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

		userTree.addMouseListener(new MouseAdapter()	// + 유저 트리에서 마우스 오른쪽 버튼으로 클릭 시
		{
			public void mousePressed(MouseEvent event)
			{
				if(((event.getModifiers() & InputEvent.BUTTON3_MASK ) != 0) && (userTree.getSelectionCount() > 0))
				{
					showMenuToUserTree(event.getX(), event.getY());
				}
			}
		});
		
		
		JPanel panel_1 = new JPanel();
		user.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

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
		chatField.setToolTipText("'/'키와 상대 유저의 id를 입력하고 말하면 귓속말 대화가 가능합니다. ex) /james 안녕하세요! ");	// +툴팁 추가
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
		restRoomArea.setToolTipText("'/'키와 상대 유저의 id를 입력하고 말하면 귓속말 대화가 가능합니다. ex) /james 안녕하세요! ");	// +툴팁 추가
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
		JPanel addfriendpannel = new JPanel();
		panel9.add(addfriendpannel, BorderLayout.SOUTH);
		addfriendpannel.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		friendTree = new JTree();	// 친구 목록  트리
		friendTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent arg0) {
				currentSelectedTreeNode = arg0.getPath().getLastPathComponent().toString();
			}
		});
		level1 = new DefaultMutableTreeNode("친구 목록");
		DefaultTreeModel model2 = new DefaultTreeModel(level1);
		friendTree.setModel(model2);
		
		scrollPane_9.setViewportView(friendTree);

		friendTree.addMouseListener(new MouseAdapter()	// + 친구목록 트리에서 마우스 오른쪽 버튼으로 클릭 시
		{
			public void mousePressed(MouseEvent event)
			{
				if(((event.getModifiers() & InputEvent.BUTTON3_MASK ) != 0) && (friendTree.getSelectionCount() > 0))
				{
					showMenuToFriendTree(event.getX(), event.getY());
				}
			}
		});

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
		//------------------------------------------------------------------//   
		// 설정 변경 이벤트
		case "설정 변경":
		    settings = new Settings(tab, client);
			settings.setLocationRelativeTo(this);	// 상대경로 설정
		    break;   
		//------------------------------------------------------------------//
		}
	}

	private void changeNick() 
	{
		String temp = JOptionPane.showInputDialog(this, "변경할 닉네임을 입력하세요.", "");
		if(temp.equals(client.getUser().getNickName()))	//+동일한 닉네임으로 변경했을 때
		{
			System.out.println("닉네임 변경 실패");
			JOptionPane.showMessageDialog(null, "기존의 닉네임과 다른 닉네임을 입력해주세요.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
			return;
		}
		if (temp != null && !temp.equals("")) 
		{
			try 
			{
				client.getUser().getDos().writeUTF(User.CHANGE_NICK + "/" + temp + "/" + client.getUser().getName());
			} 
			catch (IOException e) 
			{
				System.out.println("닉네임 변경 실패");
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "닉네임을 한 글자 이상 입력해주세요.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
			return;
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
				StringTokenizer token = new StringTokenizer(string, " "); // 토큰 생성
				String id="", msg="";
				
				try
				{
					id = token.nextToken(); // 토큰으로 분리된 스트링
					while(token.hasMoreTokens())	// + 띄어쓰기를 해도 귓속말이 되게끔 수정
					{
						msg += token.nextToken() + " ";
					}
				}
				catch(Exception e)
				{ }	// +토큰 자체가 존재하지 않을 때
				id = id.replace("/", "");	// + '/'를 빼줌

				if(id.equals(client.getUser().getId()))
				{
					System.out.println("자신에게는 귓속말할 수 없습니다.");
					JOptionPane.showMessageDialog(null, "자신에게는 귓속말할 수 없습니다.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
					return;
				}
				
				boolean flag = false;	// +대상이 온라인인지 확인하는 플래그
				for(int i=0; i<client.getUserArray().size(); i++)
				{
					if(client.getUserArray().get(i).getId().equals(id))
					{
						flag = true;
					}
				}
				
				if(!flag)	// +대상이 존재하지 않을 때
				{
					System.out.println("존재하는 아이디가 아니거나 접속 중인 유저가 아닙니다.");
					JOptionPane.showMessageDialog(null, "존재하는 아이디가 아니거나 접속 중인 유저가 아닙니다.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
					return;
				}
				else System.out.println("대상 존재 확인");
				
				try 
				{
					client.getDos().writeUTF(User.WHISPER + "/" + id + "/" + msg);
					restRoomArea.append("("+id+")" + "님에게 : " + msg + "\n");	// +귓속말 내용 수정
				}
				catch (IOException e) 
				{
					e.getMessage();
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
					e.getMessage();
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

	public void showMenuToUserTree(int x, int y)	// +접속 중인 사용자 목록을 오른쪽 마우스로 클릭했을 때 나오는 팝업
	{
		JPopupMenu popup = new JPopupMenu();	// 메뉴아이템 팝업 생성
	    JMenuItem friendAddItem = new JMenuItem("친구 추가");	// 친구 추가 메뉴 아이템 생성
	    popup.add(friendAddItem);

	    friendAddItem.addActionListener(new ActionListener()
	    {	// + 친구 추가 아이템 클릭 시
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		String friend = null;
	    		
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    	    {
	    	    	if(client.getUserArray().get(i).toString().equals(userTree.getLastSelectedPathComponent().toString()))
	    	    	{
	    	    		friend = client.getUserArray().get(i).toString();
	    	    	}
	    	    }
	    		addFriend(friend);
	    	}
	    });	 
		
		popup.show(userTree, x, y);
	}
	
	public void showMenuToFriendTree(int x, int y)	// +친구 목록을 오른쪽 마우스로 클릭했을 때 나오는 팝업
	{
		JPopupMenu popup = new JPopupMenu();
		JMenuItem infoItem = new JMenuItem("친구 정보");
	    popup.add(infoItem);
	    
	    infoItem.addActionListener(new ActionListener()
	    {	// +친구 정보 아이템 클릭 시
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    		{
	    			if(client.getUserArray().get(i).toString().equals(friendTree.getLastSelectedPathComponent().toString()))
	    			{	 				
	    				FriendInfo fi = new FriendInfo(client.getUserArray().get(i));	// 유저 목록을 얻어와서 목록 안에 클릭한 개체가 들어있을 경우 친구 정보 출력
	    				fi.setLocationRelativeTo(friendTree);	// 상대위치 지정
	    				fi.setVisible(true);
	    				break;
	    			}
	    		}
	    	}
	    });
		popup.show(friendTree, x, y);
	}
	
	private void createRoom(String roomName) 
	{
		Room newRoom = new Room(roomName); // 방 객체 생성
		newRoom.setRoomNum(lastRoomNum);
		newRoom.setRoomType("일반");
	    // 설정 값을 전달할 참조 변수를 파라미터로 추가
	    newRoom.setrUI(new RoomUI(client, newRoom, tab)); // UI
	    newRoom.getrUI().setLocationRelativeTo(this.chatField);	// 상대경로 지정
	    newRoom.getrUI().setRestRoom(this);
	    
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
	    // 설정 값을 전달할 참조 변수를 파라미터로 추가
	    newRoom.setrUI(new RoomUI(client, newRoom, tab)); // UI
		newRoom.getrUI().setLocationRelativeTo(this.chatField);	// 상대경로 지정
		newRoom.getrUI().setRestRoom(this);
		
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

		for(int i=0; i<client.getUser().getRoomArray().size(); i++)	// +접속 중인 방 번호 목록에 접속 시도한 방의 번호가 있을 경우(이미 접속 중일 경우) 
		{
			if(client.getUser().getRoomArray().get(i).getRoomNum() == Integer.parseInt(rNum))
			{
				client.getUser().getRoomArray().get(i).getrUI().setVisible(true);	// 창만 띄워줌
				return;
			}
		}
		
		Room theRoom = new Room(rName); // 방 객체 생성
		theRoom.setRoomNum(Integer.parseInt(rNum)); // 방번호 설정
		theRoom.setRoomType(rType);	// +방 타입 설정
		//------------------------------------------------------------------//
	    // 설정 값을 전달할 참조 변수를 파라미터로 추가
		theRoom.setrUI(new RoomUI(client, theRoom, tab)); // UI
		//------------------------------------------------------------------//
	    theRoom.getrUI().setLocationRelativeTo(this.chatField);	// 상대경로 지정
	    theRoom.getrUI().setRestRoom(this);
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
		maker.setLocationRelativeTo(this);
		maker.setVisible(true);
		maker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public void addFriend(String friend)
	{
		for(int i=0; i<friendTree.getComponentCount(); i++)
		{
			if(client.getUser().toString().equals(friend))
			{
				System.out.println("자신은 친구로 추가할 수 없습니다.");
				JOptionPane.showMessageDialog(null, "자신은 친구로 추가할 수 없습니다.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
				return;
			}
			else if(client.getUser().getfriendArray().contains(friend))	// friendArray에 이미 있거나 자신의 아이디일 때
			{
				System.out.println("이미 등록되어 있는 친구");
				JOptionPane.showMessageDialog(null, "이미 등록되어 있는 친구입니다.", "에러 메시지", JOptionPane.INFORMATION_MESSAGE);	// 에러창 띄움
				return;
			}
		}
		try {
			client.getDos().writeUTF(User.FRIEND + "/"+ client.getUser().getId() + "/" + friend);
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

		//------------------------------------------------------------------//
	    // 프로그램 정보 수정
	    JLabel j1 = new JLabel("       프로그램 제작자 : 동국대학교 공개 SW 프로젝트 - 4조");
	    JLabel j2 = new JLabel("       수정한 사람 : 윤창근, 허문용, 박지용, 이해준");
	    JLabel j3 = new JLabel("       프로그램 버전 : 1.7v  ( 17 . 6 . 18 )");
	    //------------------------------------------------------------------//

		this.add(j1);
		this.add(j2);
		this.add(j3);
	}
}

//친구 정보 팝업 클래스
class FriendInfo extends JDialog{
	JLabel lblName, lblId, lblNickName;
	JTextField txtName, txtId, txtNickName;
	
	public FriendInfo(User user)
	{
		lblName = new JLabel("이름");		// 이름 레이블
		lblName.setBounds(20, 20, 50, 30);
		
		txtName = new JTextField();	// 이름 텍스트 필드
		txtName.setText(user.getName());
		txtName.setBounds(80, 20, 100, 30);
		txtName.setEditable(false);	// 읽기 전용으로 설정(수정 불가)
		
		lblId = new JLabel("아이디");	// 아이디 레이블
		lblId.setBounds(20, 60, 50, 30);
		
		txtId = new JTextField();	// 아이디 텍스트 필드
		txtId.setText(user.getId());
		txtId.setBounds(80, 60, 100, 30);
		txtId.setEditable(false);	// 읽기 전용으로 설정(수정 불가)
		
		lblNickName = new JLabel("닉네임");	// 닉네임 레이블
		lblNickName.setBounds(20, 100, 50, 30);
		
		txtNickName = new JTextField();	// 닉네임 텍스트 필드
		txtNickName.setText(user.getNickName());
		txtNickName.setBounds(80, 100, 100, 30);
		txtNickName.setEditable(false);	// 읽기 전용으로 설정(수정 불가)
			
		this.add(lblName);
		this.add(lblId);
		this.add(lblNickName);
		this.add(txtName);
		this.add(txtId);
		this.add(txtNickName);
		this.setTitle("친구 정보");
		this.setSize(200, 200);
		this.setLayout(null);
		this.setModal(true);
	}
}