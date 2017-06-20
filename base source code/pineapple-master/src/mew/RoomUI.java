package mew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RoomUI extends JFrame {
	private SixClient client;
	private Room room;
	//------------------------------------------------------------------//
	// ����� �������� ������ �� �ʿ��� ���� ���� tab
	private textAndBackground tab;
	//------------------------------------------------------------------//

	public RestRoomUI restRoom;
	public JTextArea chatArea;
	public JTextField chatField;
	public JList uList;
	public DefaultListModel model;
	
	//------------------------------------------------------------------//
	// RoomUI�� �Ķ���ͷ� textAndBackground tab �߰�
	public RoomUI(SixClient client, Room room, textAndBackground tab) {
		this.client = client;
		this.room = room;
		this.tab = tab;	// ���� ������ �޾ƿ� �� ����
		setTitle(room.getRoomName() + "(" + room.getRoomType() + " ä�ù�)");	// +�� ������ ���� �̸� ����
		initialize();
	}
	//------------------------------------------------------------------//

	private void initialize() 
	{
		setSize(502, 481);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ä��", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setSize(300, 358);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

 		chatArea = new JTextArea();
 		//------------------------------------------------------------------//
 				// ä��â�� ������ ���� ����
 				Font font = new Font("", Font.BOLD, 12);
 				chatArea.setBackground(new Color(224, 255, 255));
 				if(tab.getBackColor() != null)
 				{
 					chatArea.setBackground(tab.getBackColor());
 				}
 				if(tab.getSize() != 0)
 				{
 					font = new Font("", Font.PLAIN, tab.getSize());

 				}
 				if(tab.getFont() == 0 || tab.getFont() == 1 || tab.getFont() == 2)
 				{
 					if(tab.getFont() == 1)
 					{
 						font = new Font("", Font.BOLD, tab.getSize());
 					}
 					else if(tab.getFont() == 2)
 					{
 						font = new Font("", Font.ITALIC, tab.getSize());
 					}
 					else if(tab.getFont() == 0)
 					{
 						font = new Font("", Font.PLAIN, tab.getSize());
 					}
 				}
 				chatArea.setFont(font);
 				//------------------------------------------------------------------//
		chatArea.setEditable(false);
		chatArea.setToolTipText("'/invite'Ű�� ��� ������ id�� �Է��ϸ� ģ���� �濡 �ʴ��� �� �ֽ��ϴ�. ex) /invite james ");	// +���� �߰�
		scrollPane.setViewportView(chatArea);
		chatArea.append("��ä�ù��� �����Ǿ����ϴ�.��\r\n");

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		chatField = new JTextField();
		panel_1.add(chatField);
		chatField.setToolTipText("'/'Ű�� ��� ������ id�� �Է��ϸ� ģ���� �濡 �ʴ��� �� �ֽ��ϴ�. ex) /james ");	// +���� �߰�
		chatField.setColumns(10);
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
		
		// + �ʴ��ϱ� ��ư, ���� ��ư, â ���� ��ư(�濡�� ������ �Ͱ� �ٸ�) �߰�
		JButton btnInvite = new JButton();	// �ʴ� ��ư
		btnInvite.setText("�ʴ��ϱ�");
		btnInvite.setBounds(324, 10, 40, 40);
		btnInvite.setVisible(true);
		btnInvite.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String id = (String)JOptionPane.showInputDialog(null, "�ʴ��Ͻ� ����� id�� �Է����ּ���.");
				
				if(invite(id))	// ���� ���ǿ� �ɸ��� ������
				{
					try{
						client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()+"/"+room.getRoomType());
					}catch(Exception e1)
					{ e1.getMessage(); }
				}
			}
		});
		getContentPane().add(btnInvite);
		
		JButton btnOmok = new JButton();	// +���� ���� ��ư
		btnOmok.setText("����");
		btnOmok.setBounds(364, 10, 40, 40);
		btnOmok.setVisible(true);
		btnOmok.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				OmokGame playOmok = new OmokGame(19);
			}
		});
		getContentPane().add(btnOmok);
		
		JButton btnClose = new JButton();	// +â �ݱ� ��ư
		btnClose.setText("�ݱ�");
		btnClose.setBounds(404, 10, 40, 40);
		btnClose.setVisible(true);
		btnClose.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setVisible(false);	// �� â�� ����
			}
		});
		getContentPane().add(btnClose);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "������",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(324, 50, 150, 350);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
	 
		JPopupMenu pm = new JPopupMenu();	// +��Ŭ�� �� ��Ÿ�� �˾� �޴� ����
		JMenuItem infoItem = new JMenuItem("����� ����");	// +�˾��޴� �����۵�
	    JMenuItem friendAddItem = new JMenuItem("ģ�� �߰�");
	    pm.add(infoItem);
	    pm.add(friendAddItem);
	    
	    uList.addMouseListener(new MouseAdapter() {	// +���콺 ��Ŭ�� �� �˾� �޴� �߱�
            public void mouseClicked(MouseEvent e)
            {
        		JList c = (JList)e.getComponent();
        		int x = e.getX();
        		int y = e.getY();
        		
        		if(!uList.isSelectionEmpty()&& uList.locationToIndex(e.getPoint()) == uList.getSelectedIndex())
        		{  
        			int count = c.getModel().getSize();
        			int cal = count * 18;
        			if(y <= cal)
        			{
        				pm.show(uList, x, y);
        			}
        		}
            }
	    });
		scrollPane_1.setViewportView(uList);

		infoItem.addActionListener(new ActionListener(){	// ģ�� ���� ������ Ŭ�� ��
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		if(room.getRoomType().equals("�͸�"))
	    			JOptionPane.showMessageDialog(null, "�͸� ä�ù濡���� �� �� �����ϴ�.");
	    		int selectedIndex = uList.getSelectedIndex();	// ����Ʈ���� ������ ��ü�� �ε���
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    	    {
	    			if(uList.getSelectedValue().toString().equals(client.getUserArray().get(i).toString()))
	    			{	// �ش� ����ڰ� �����ϸ�
	    				FriendInfo fi = new FriendInfo(client.getUserArray().get(i));
	    				fi.setLocationRelativeTo(panel);
	    				fi.setVisible(true);	// ģ�� ���� ����
	    				break;
	    			}
	    		}
	    	}
	    });
		
		friendAddItem.addActionListener(new ActionListener(){	// ģ�� �߰� ������ Ŭ�� ��
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		if(room.getRoomType().equals("�͸�"))
	    			JOptionPane.showMessageDialog(null, "�͸� ä�ù濡���� �� �� �����ϴ�.");
	    		int selectedIndex = uList.getSelectedIndex();	// ����Ʈ���� ������ ��ü�� �ε���
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    	    {
	    			if(uList.getSelectedValue().toString().equals(client.getUserArray().get(i).toString()))
	    			{	// �ش� ����ڰ� �����ϸ�
	    				String friend = uList.getSelectedValue().toString();
	    				restRoom.addFriend(friend);	// ģ�� ��Ͽ� �߰�
	    				break;
	    			}
	    		}
	    	}
		});
		
		JButton roomSendBtn = new JButton("������");
		roomSendBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				msgSummit();
				chatField.requestFocus();
			}
		});
		
		roomSendBtn.setBounds(324, 378, 150, 34);
		getContentPane().add(roomSendBtn);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		chatField.requestFocus();
		
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				try {
					client.getUser().getDos().writeUTF(User.GETOUT_ROOM + "/" + room.getRoomNum());
					for (int i = 0; i < client.getUser().getRoomArray().size(); i++) 
					{
						if (client.getUser().getRoomArray().get(i).getRoomNum() == room.getRoomNum()) 
						{
							client.getUser().getRoomArray().remove(i);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void msgSummit() {
		String string = chatField.getText();
		String rType = room.getRoomType();
		String id = "";
		StringTokenizer token = new StringTokenizer(string, " "); // ��ū	
		String trash=token.nextToken();
		if(token.hasMoreTokens())
			id = token.nextToken(); // ��ū���� �и��� ��Ʈ��
		
		if (!string.equals("")) {
			try {
				// ä�ù濡 �޽��� ����
				if(rType.equals("�Ϲ�"))	// �Ϲ� ��ȭ��� �͸� ��ȭ���� ����
				{
					if (trash.equals("/invite") && invite(id)) 	// +�ʴ� �߰��Ѻκ� �Ϲ�ä�ù�
						client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()+"/"+room.getRoomType());
					else
						client.getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNameString() + string);
				}
				else
				{
					if (trash.equals("/invite") && invite(id)) 	// +�ʴ� �߰��Ѻκ� �Ϲ�ä�ù�
						client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()+"/"+room.getRoomType());
					else
						client.getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNickNameString() + string);
				}
				chatField.setText("");
			}
			catch (IOException e) {
				e.getMessage();
			}
		}
	}

	// +������ ���� �ʴ� �� ���� ���׵�
	public boolean invite(String id)
	{
		if(client.getUser().getId()==null || client.getUser().getId().equals(null))
		{
			System.out.println("�ùٸ� ����ڰ� �ƴմϴ�.");
			JOptionPane.showMessageDialog(null, "�ùٸ� ����ڰ� �ƴմϴ�.");
			return false;
		}
		else if(id.equals(client.getUser().getId()))
		{
			System.out.println("�ڽ��� �ʴ��� �� �����ϴ�.");
			JOptionPane.showMessageDialog(null, "�ڽ��� �ʴ��� �� �����ϴ�");
			return false;
		}
		else
		{
			boolean onLine = false;	// +����ڰ� �¶��� ������ �Ǵ��ϴ� �÷���
			for(int j=0; j<client.getUserArray().size(); j++)
			{
				if(client.getUserArray().get(j).getId().equals(id))
				{
					System.out.println("�̹� �濡 ���� �ִ� ����� �Դϴ�.");
					JOptionPane.showMessageDialog(null, "�̹� �濡 ���� �ִ� ����� �Դϴ�.");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * @return the restRoom
	 */
	public RestRoomUI getRestRoom() {
		return restRoom;
	}

	/**
	 * @param restRoom the restRoom to set
	 */
	public void setRestRoom(RestRoomUI restRoom) {
		this.restRoom = restRoom;
	}
	
	// +���� UI�� ���� getter, setter
	
}