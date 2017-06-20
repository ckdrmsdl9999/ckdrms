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
	public JTree friendTree;	//ģ�����Ʈ��
	public JList roomList;
	public JTextField chatField;
	public JTextArea restRoomArea;
	public JLabel lb_id, lb_nick, lb_name;
	public JTextField lb_ip;

	private SixClient client;
	//------------------------------------------------------------------//
	// ����� �������� ������ �� �ʿ��� ���� ���� tab
	private textAndBackground tab;
	//------------------------------------------------------------------//
	// ����â
	public Settings settings;
	//------------------------------------------------------------------//
	public ArrayList<User> userArray; // ����� ��� �迭
	public String currentSelectedTreeNode;
	public DefaultListModel model;
	public DefaultMutableTreeNode level_1;
	public DefaultMutableTreeNode level_2_1;
	public DefaultMutableTreeNode level_2_2;

	public DefaultMutableTreeNode level1;//ģ��  Ʈ������߰�
	public DefaultMutableTreeNode level21;
	public DefaultMutableTreeNode level22;
	public DefaultMutableTreeNode level2;
	
	
	public RestRoomUI(SixClient sixClient) 
	{
		setTitle("����");
		JDialog.setDefaultLookAndFeelDecorated(true);

		userArray = new ArrayList<User>();
		//------------------------------------------------------------------//
		// ���� ������ ������ ��ü�� �ּ� �� ����
		tab = new textAndBackground();
		//------------------------------------------------------------------//
		client = sixClient;
		initialize();
	}

	private void initialize() 
	{
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// ���� â�� ���� �����ִ� ����� ��� â�� ����
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu basicMenus = new JMenu("�Ϲ�");
		basicMenus.addActionListener(this);
		menuBar.add(basicMenus);

		JMenuItem changeNickItem = new JMenuItem("�г��� ����");
		changeNickItem.addActionListener(this);
		basicMenus.add(changeNickItem);
		
		//-----------------------------------------------------------------//
	    //  ���������� �߰�
	    JMenuItem settingItem = new JMenuItem("\uC124\uC815 \uBCC0\uACBD");
	    settingItem.addActionListener(this);
	    basicMenus.add(settingItem);
	    getContentPane().setLayout(null);
	    //----------------------------------------------------------------//

		JMenuItem exitItem = new JMenuItem("������");
		exitItem.addActionListener(this);
		basicMenus.add(exitItem);

		JMenu helpMenus = new JMenu("����");
		helpMenus.addActionListener(this);
		menuBar.add(helpMenus);

		JMenuItem proInfoItem = new JMenuItem("���α׷� ����");
		proInfoItem.addActionListener(this);
		helpMenus.add(proInfoItem);
		getContentPane().setLayout(null);

		JPanel room = new JPanel();
		room.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ä�ù�",TitledBorder.CENTER, TitledBorder.TOP, null, null));
		room.setBounds(12, 10, 477, 215);
		getContentPane().add(room);
		room.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		room.add(scrollPane, BorderLayout.CENTER);

		// ����Ʈ ��ü�� �� ����
		roomList = new JList(new DefaultListModel());
		roomList.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				// ä�ù� ��� �� �ϳ��� ������ ���,
				// ������ ���� ���ȣ�� ����
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

		makeRoomBtn = new JButton("�Ϲ� ä�ù� �����");	// +�Ϲ� ä�ù� ����
		makeRoomBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) { }
		});
		makeRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{	// �游��� ��ư Ŭ��
				String roomName = JOptionPane.showInputDialog(null, "�� ������ �Է��ϼ���.");	// �� �̸��� �Է¹���
				createRoom(roomName);
			}
		});
		
		makeAnonymousRoomBtn = new JButton("�͸� ä�ù� �����");	// +�͸� ä�ù� ����
		makeAnonymousRoomBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) { }
		});
		makeAnonymousRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{	// �游��� ��ư Ŭ��
				String roomName = JOptionPane.showInputDialog(null, "�� ������ �Է��ϼ���.");	// �� �̸��� �Է¹���
				createAnonymousRoom(roomName);
			}
		});
		
		makeRoomBtn.setSize(250, 50);
		makeAnonymousRoomBtn.setSize(250, 50);
		
		panel_2.add(makeRoomBtn);
		panel_2.add(makeAnonymousRoomBtn);

		getInRoomBtn = new JButton("�� �����ϱ�");
		getInRoomBtn.setSize(250, 50);
		getInRoomBtn.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				// �� ����
				getIn();
			}
		});
		panel_2.add(getInRoomBtn);

////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel user = new JPanel();
		user.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),"����� ���", TitledBorder.CENTER,TitledBorder.TOP, null, null));
		user.setBounds(501, 10, 171, 215);
		getContentPane().add(user);
		user.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		user.add(scrollPane_1, BorderLayout.CENTER);

		// ����ڸ��, Ʈ������
		userTree = new JTree();
		userTree.addTreeSelectionListener(new TreeSelectionListener() 
		{
			public void valueChanged(TreeSelectionEvent arg0) 
			{
				currentSelectedTreeNode = arg0.getPath().getLastPathComponent().toString();
			}
		});
		level_1 = new DefaultMutableTreeNode("������");
		level_2_1 = new DefaultMutableTreeNode("ä�ù�");
		level_2_2 = new DefaultMutableTreeNode("����");
		level_1.add(level_2_1);
		level_1.add(level_2_2);

		DefaultTreeModel model = new DefaultTreeModel(level_1);
		userTree.setModel(model);

		scrollPane_1.setViewportView(userTree);

		userTree.addMouseListener(new MouseAdapter()	// + ���� Ʈ������ ���콺 ������ ��ư���� Ŭ�� ��
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
				.getBorder("TitledBorder.border"), "�� �� ��",
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
		chatField.setToolTipText("'/'Ű�� ��� ������ id�� �Է��ϰ� ���ϸ� �ӼӸ� ��ȭ�� �����մϴ�. ex) /james �ȳ��ϼ���! ");	// +���� �߰�
		chatField.setColumns(10);

		sendBtn = new JButton("������");
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
		restRoomArea.setToolTipText("'/'Ű�� ��� ������ id�� �Է��ϰ� ���ϸ� �ӼӸ� ��ȭ�� �����մϴ�. ex) /james �ȳ��ϼ���! ");	// +���� �߰�
		scrollPane_2.setViewportView(restRoomArea);
		
///////////////////////////////////////////////////////////////////////ģ�����Ʈ��/////
		JPanel panel9 = new JPanel();		//���������� 
		panel9.setBackground(SystemColor.control);
		panel9.setBounds(501, 235, 171, 185);
		getContentPane().add(panel9);
		panel9.setLayout(new BorderLayout(0, 0));
		panel9.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),
				"ģ�� ���", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		JScrollPane scrollPane_9 = new JScrollPane();
		panel9.add(scrollPane_9, BorderLayout.CENTER);
		JPanel addfriendpannel = new JPanel();
		panel9.add(addfriendpannel, BorderLayout.SOUTH);
		addfriendpannel.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		friendTree = new JTree();	// ģ�� ���  Ʈ��
		friendTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent arg0) {
				currentSelectedTreeNode = arg0.getPath().getLastPathComponent().toString();
			}
		});
		level1 = new DefaultMutableTreeNode("ģ�� ���");
		DefaultTreeModel model2 = new DefaultTreeModel(level1);
		friendTree.setModel(model2);
		
		scrollPane_9.setViewportView(friendTree);

		friendTree.addMouseListener(new MouseAdapter()	// + ģ����� Ʈ������ ���콺 ������ ��ư���� Ŭ�� ��
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
		
//////////////////////////////////////////�������κ� �ȳ�Ÿ������
		JPanel info = new JPanel();
//		info.setBorder(new TitledBorder(UIManager
//				.getBorder("TitledBorder.border"), "�� ����",
//				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
//		info.setBounds(501, 235, 171, 185);
//		getContentPane().add(info);
//		info.setLayout(null);
///////////////////////////////////////
		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(-25, 20, 57, 15);
		info.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("���̵�");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 60, 57, 15);
		info.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("�̸�");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(0, 100, 57, 15);
		info.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("�г���");
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
		// �޴�1 ���� �޴�
		case "�г��� ����":
			changeNick();
			break;
		case "������":
			exit();
			break;
		case "���α׷� ����":
			maker();
			System.out.println("�佺Ʈ");
			break;
		//------------------------------------------------------------------//   
		// ���� ���� �̺�Ʈ
		case "���� ����":
		    settings = new Settings(tab, client);
			settings.setLocationRelativeTo(this);	// ����� ����
		    break;   
		//------------------------------------------------------------------//
		}
	}

	private void changeNick() 
	{
		String temp = JOptionPane.showInputDialog(this, "������ �г����� �Է��ϼ���.", "");
		if(temp.equals(client.getUser().getNickName()))	//+������ �г������� �������� ��
		{
			System.out.println("�г��� ���� ����");
			JOptionPane.showMessageDialog(null, "������ �г��Ӱ� �ٸ� �г����� �Է����ּ���.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
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
				System.out.println("�г��� ���� ����");
				e.printStackTrace();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "�г����� �� ���� �̻� �Է����ּ���.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
			return;
		}
	}

	private void msgSummit()
	{
		// �޽�������
		String string = chatField.getText();
		if (!string.equals("")) 
		{
			if (string.substring(0, 1).equals("/")) 
			{
				StringTokenizer token = new StringTokenizer(string, " "); // ��ū ����
				String id="", msg="";
				
				try
				{
					id = token.nextToken(); // ��ū���� �и��� ��Ʈ��
					while(token.hasMoreTokens())	// + ���⸦ �ص� �ӼӸ��� �ǰԲ� ����
					{
						msg += token.nextToken() + " ";
					}
				}
				catch(Exception e)
				{ }	// +��ū ��ü�� �������� ���� ��
				id = id.replace("/", "");	// + '/'�� ����

				if(id.equals(client.getUser().getId()))
				{
					System.out.println("�ڽſ��Դ� �ӼӸ��� �� �����ϴ�.");
					JOptionPane.showMessageDialog(null, "�ڽſ��Դ� �ӼӸ��� �� �����ϴ�.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
					return;
				}
				
				boolean flag = false;	// +����� �¶������� Ȯ���ϴ� �÷���
				for(int i=0; i<client.getUserArray().size(); i++)
				{
					if(client.getUserArray().get(i).getId().equals(id))
					{
						flag = true;
					}
				}
				
				if(!flag)	// +����� �������� ���� ��
				{
					System.out.println("�����ϴ� ���̵� �ƴϰų� ���� ���� ������ �ƴմϴ�.");
					JOptionPane.showMessageDialog(null, "�����ϴ� ���̵� �ƴϰų� ���� ���� ������ �ƴմϴ�.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
					return;
				}
				else System.out.println("��� ���� Ȯ��");
				
				try 
				{
					client.getDos().writeUTF(User.WHISPER + "/" + id + "/" + msg);
					restRoomArea.append("("+id+")" + "�Կ��� : " + msg + "\n");	// +�ӼӸ� ���� ����
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
					// ���ǿ� �޽��� ����
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

	public void showMenuToUserTree(int x, int y)	// +���� ���� ����� ����� ������ ���콺�� Ŭ������ �� ������ �˾�
	{
		JPopupMenu popup = new JPopupMenu();	// �޴������� �˾� ����
	    JMenuItem friendAddItem = new JMenuItem("ģ�� �߰�");	// ģ�� �߰� �޴� ������ ����
	    popup.add(friendAddItem);

	    friendAddItem.addActionListener(new ActionListener()
	    {	// + ģ�� �߰� ������ Ŭ�� ��
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
	
	public void showMenuToFriendTree(int x, int y)	// +ģ�� ����� ������ ���콺�� Ŭ������ �� ������ �˾�
	{
		JPopupMenu popup = new JPopupMenu();
		JMenuItem infoItem = new JMenuItem("ģ�� ����");
	    popup.add(infoItem);
	    
	    infoItem.addActionListener(new ActionListener()
	    {	// +ģ�� ���� ������ Ŭ�� ��
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    		{
	    			if(client.getUserArray().get(i).toString().equals(friendTree.getLastSelectedPathComponent().toString()))
	    			{	 				
	    				FriendInfo fi = new FriendInfo(client.getUserArray().get(i));	// ���� ����� ���ͼ� ��� �ȿ� Ŭ���� ��ü�� ������� ��� ģ�� ���� ���
	    				fi.setLocationRelativeTo(friendTree);	// �����ġ ����
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
		Room newRoom = new Room(roomName); // �� ��ü ����
		newRoom.setRoomNum(lastRoomNum);
		newRoom.setRoomType("�Ϲ�");
	    // ���� ���� ������ ���� ������ �Ķ���ͷ� �߰�
	    newRoom.setrUI(new RoomUI(client, newRoom, tab)); // UI
	    newRoom.getrUI().setLocationRelativeTo(this.chatField);	// ����� ����
	    newRoom.getrUI().setRestRoom(this);
	    
		// Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
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
	{	// +�͸� ä�ù� ���� �޼ҵ�
		Room newRoom = new Room(roomName); // �� ��ü ����
		newRoom.setRoomNum(lastRoomNum);
		newRoom.setRoomType("�͸�");
	    // ���� ���� ������ ���� ������ �Ķ���ͷ� �߰�
	    newRoom.setrUI(new RoomUI(client, newRoom, tab)); // UI
		newRoom.getrUI().setLocationRelativeTo(this.chatField);	// ����� ����
		newRoom.getrUI().setRestRoom(this);
		
		// Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
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
		// ������ �� ����
		String selectedRoom = (String) roomList.getSelectedValue();
		StringTokenizer token = new StringTokenizer(selectedRoom, "/"); // ��ū ����
		String rNum = token.nextToken();
		String rName = token.nextToken();
		String rType = token.nextToken();

		for(int i=0; i<client.getUser().getRoomArray().size(); i++)	// +���� ���� �� ��ȣ ��Ͽ� ���� �õ��� ���� ��ȣ�� ���� ���(�̹� ���� ���� ���) 
		{
			if(client.getUser().getRoomArray().get(i).getRoomNum() == Integer.parseInt(rNum))
			{
				client.getUser().getRoomArray().get(i).getrUI().setVisible(true);	// â�� �����
				return;
			}
		}
		
		Room theRoom = new Room(rName); // �� ��ü ����
		theRoom.setRoomNum(Integer.parseInt(rNum)); // ���ȣ ����
		theRoom.setRoomType(rType);	// +�� Ÿ�� ����
		//------------------------------------------------------------------//
	    // ���� ���� ������ ���� ������ �Ķ���ͷ� �߰�
		theRoom.setrUI(new RoomUI(client, theRoom, tab)); // UI
		//------------------------------------------------------------------//
	    theRoom.getrUI().setLocationRelativeTo(this.chatField);	// ����� ����
	    theRoom.getrUI().setRestRoom(this);
		// Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
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
		maker.setTitle("���α׷� ����");
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
				System.out.println("�ڽ��� ģ���� �߰��� �� �����ϴ�.");
				JOptionPane.showMessageDialog(null, "�ڽ��� ģ���� �߰��� �� �����ϴ�.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
				return;
			}
			else if(client.getUser().getfriendArray().contains(friend))	// friendArray�� �̹� �ְų� �ڽ��� ���̵��� ��
			{
				System.out.println("�̹� ��ϵǾ� �ִ� ģ��");
				JOptionPane.showMessageDialog(null, "�̹� ��ϵǾ� �ִ� ģ���Դϴ�.", "���� �޽���", JOptionPane.INFORMATION_MESSAGE);	// ����â ���
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
	    // ���α׷� ���� ����
	    JLabel j1 = new JLabel("       ���α׷� ������ : �������б� ���� SW ������Ʈ - 4��");
	    JLabel j2 = new JLabel("       ������ ��� : ��â��, �㹮��, ������, ������");
	    JLabel j3 = new JLabel("       ���α׷� ���� : 1.7v  ( 17 . 6 . 18 )");
	    //------------------------------------------------------------------//

		this.add(j1);
		this.add(j2);
		this.add(j3);
	}
}

//ģ�� ���� �˾� Ŭ����
class FriendInfo extends JDialog{
	JLabel lblName, lblId, lblNickName;
	JTextField txtName, txtId, txtNickName;
	
	public FriendInfo(User user)
	{
		lblName = new JLabel("�̸�");		// �̸� ���̺�
		lblName.setBounds(20, 20, 50, 30);
		
		txtName = new JTextField();	// �̸� �ؽ�Ʈ �ʵ�
		txtName.setText(user.getName());
		txtName.setBounds(80, 20, 100, 30);
		txtName.setEditable(false);	// �б� �������� ����(���� �Ұ�)
		
		lblId = new JLabel("���̵�");	// ���̵� ���̺�
		lblId.setBounds(20, 60, 50, 30);
		
		txtId = new JTextField();	// ���̵� �ؽ�Ʈ �ʵ�
		txtId.setText(user.getId());
		txtId.setBounds(80, 60, 100, 30);
		txtId.setEditable(false);	// �б� �������� ����(���� �Ұ�)
		
		lblNickName = new JLabel("�г���");	// �г��� ���̺�
		lblNickName.setBounds(20, 100, 50, 30);
		
		txtNickName = new JTextField();	// �г��� �ؽ�Ʈ �ʵ�
		txtNickName.setText(user.getNickName());
		txtNickName.setBounds(80, 100, 100, 30);
		txtNickName.setEditable(false);	// �б� �������� ����(���� �Ұ�)
			
		this.add(lblName);
		this.add(lblId);
		this.add(lblNickName);
		this.add(txtName);
		this.add(txtId);
		this.add(txtNickName);
		this.setTitle("ģ�� ����");
		this.setSize(200, 200);
		this.setLayout(null);
		this.setModal(true);
	}
}