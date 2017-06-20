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
	// 변경된 설정값을 전달할 때 필요한 참조 변수 tab
	private textAndBackground tab;
	//------------------------------------------------------------------//

	public RestRoomUI restRoom;
	public JTextArea chatArea;
	public JTextField chatField;
	public JList uList;
	public DefaultListModel model;
	
	//------------------------------------------------------------------//
	// RoomUI의 파라미터로 textAndBackground tab 추가
	public RoomUI(SixClient client, Room room, textAndBackground tab) {
		this.client = client;
		this.room = room;
		this.tab = tab;	// 참조 변수에 받아온 값 전달
		setTitle(room.getRoomName() + "(" + room.getRoomType() + " 채팅방)");	// +방 종류에 따라 이름 수정
		initialize();
	}
	//------------------------------------------------------------------//

	private void initialize() 
	{
		setSize(502, 481);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "채팅", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setSize(300, 358);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

 		chatArea = new JTextArea();
 		//------------------------------------------------------------------//
 				// 채팅창에 설정한 값을 전달
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
		chatArea.setToolTipText("'/invite'키와 상대 유저의 id를 입력하면 친구를 방에 초대할 수 있습니다. ex) /invite james ");	// +툴팁 추가
		scrollPane.setViewportView(chatArea);
		chatArea.append("◆채팅방이 개설되었습니다.◆\r\n");

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(12, 378, 300, 34);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		chatField = new JTextField();
		panel_1.add(chatField);
		chatField.setToolTipText("'/'키와 상대 유저의 id를 입력하면 친구를 방에 초대할 수 있습니다. ex) /james ");	// +툴팁 추가
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
		
		// + 초대하기 버튼, 오목 버튼, 창 끄기 버튼(방에서 나가는 것과 다름) 추가
		JButton btnInvite = new JButton();	// 초대 버튼
		btnInvite.setText("초대하기");
		btnInvite.setBounds(324, 10, 40, 40);
		btnInvite.setVisible(true);
		btnInvite.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String id = (String)JOptionPane.showInputDialog(null, "초대하실 상대의 id를 입력해주세요.");
				
				if(invite(id))	// 예외 조건에 걸리지 않으면
				{
					try{
						client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()+"/"+room.getRoomType());
					}catch(Exception e1)
					{ e1.getMessage(); }
				}
			}
		});
		getContentPane().add(btnInvite);
		
		JButton btnOmok = new JButton();	// +오목 실행 버튼
		btnOmok.setText("오목");
		btnOmok.setBounds(364, 10, 40, 40);
		btnOmok.setVisible(true);
		btnOmok.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				OmokGame playOmok = new OmokGame(19);
			}
		});
		getContentPane().add(btnOmok);
		
		JButton btnClose = new JButton();	// +창 닫기 버튼
		btnClose.setText("닫기");
		btnClose.setBounds(404, 10, 40, 40);
		btnClose.setVisible(true);
		btnClose.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setVisible(false);	// 이 창을 꺼줌
			}
		});
		getContentPane().add(btnClose);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "참여자",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(324, 50, 150, 350);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
	 
		JPopupMenu pm = new JPopupMenu();	// +우클릭 시 나타날 팝업 메뉴 생성
		JMenuItem infoItem = new JMenuItem("사용자 정보");	// +팝업메뉴 아이템들
	    JMenuItem friendAddItem = new JMenuItem("친구 추가");
	    pm.add(infoItem);
	    pm.add(friendAddItem);
	    
	    uList.addMouseListener(new MouseAdapter() {	// +마우스 우클릭 시 팝업 메뉴 뜨기
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

		infoItem.addActionListener(new ActionListener(){	// 친구 정보 아이템 클릭 시
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		if(room.getRoomType().equals("익명"))
	    			JOptionPane.showMessageDialog(null, "익명 채팅방에서는 할 수 없습니다.");
	    		int selectedIndex = uList.getSelectedIndex();	// 리스트에서 선택한 개체의 인덱스
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    	    {
	    			if(uList.getSelectedValue().toString().equals(client.getUserArray().get(i).toString()))
	    			{	// 해당 사용자가 존재하면
	    				FriendInfo fi = new FriendInfo(client.getUserArray().get(i));
	    				fi.setLocationRelativeTo(panel);
	    				fi.setVisible(true);	// 친구 정보 보기
	    				break;
	    			}
	    		}
	    	}
	    });
		
		friendAddItem.addActionListener(new ActionListener(){	// 친구 추가 아이템 클릭 시
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		if(room.getRoomType().equals("익명"))
	    			JOptionPane.showMessageDialog(null, "익명 채팅방에서는 할 수 없습니다.");
	    		int selectedIndex = uList.getSelectedIndex();	// 리스트에서 선택한 개체의 인덱스
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    	    {
	    			if(uList.getSelectedValue().toString().equals(client.getUserArray().get(i).toString()))
	    			{	// 해당 사용자가 존재하면
	    				String friend = uList.getSelectedValue().toString();
	    				restRoom.addFriend(friend);	// 친구 목록에 추가
	    				break;
	    			}
	    		}
	    	}
		});
		
		JButton roomSendBtn = new JButton("보내기");
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
		StringTokenizer token = new StringTokenizer(string, " "); // 토큰	
		String trash=token.nextToken();
		if(token.hasMoreTokens())
			id = token.nextToken(); // 토큰으로 분리된 스트링
		
		if (!string.equals("")) {
			try {
				// 채팅방에 메시지 보냄
				if(rType.equals("일반"))	// 일반 대화방과 익명 대화방을 구분
				{
					if (trash.equals("/invite") && invite(id)) 	// +초대 추가한부분 일반채팅방
						client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()+"/"+room.getRoomType());
					else
						client.getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNameString() + string);
				}
				else
				{
					if (trash.equals("/invite") && invite(id)) 	// +초대 추가한부분 일반채팅방
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

	// +방으로 유저 초대 시 예외 사항들
	public boolean invite(String id)
	{
		if(client.getUser().getId()==null || client.getUser().getId().equals(null))
		{
			System.out.println("올바른 사용자가 아닙니다.");
			JOptionPane.showMessageDialog(null, "올바른 사용자가 아닙니다.");
			return false;
		}
		else if(id.equals(client.getUser().getId()))
		{
			System.out.println("자신은 초대할 수 없습니다.");
			JOptionPane.showMessageDialog(null, "자신은 초대할 수 없습니다");
			return false;
		}
		else
		{
			boolean onLine = false;	// +사용자가 온라인 인지를 판단하는 플래그
			for(int j=0; j<client.getUserArray().size(); j++)
			{
				if(client.getUserArray().get(j).getId().equals(id))
				{
					System.out.println("이미 방에 들어와 있는 사용자 입니다.");
					JOptionPane.showMessageDialog(null, "이미 방에 들어와 있는 사용자 입니다.");
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
	
	// +대기실 UI에 대한 getter, setter
	
}