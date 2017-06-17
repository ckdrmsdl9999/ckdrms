package Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RoomUI extends JFrame {
	private SixClient client;
	private Room room;
	//------------------------------------------------------------------//
	// 변경된 설정값을 전달할 때 필요한 참조 변수 tab
	private textAndBackground tab;
	//------------------------------------------------------------------//

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
		setBounds(100, 100, 502, 481);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "채팅", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(12, 10, 300, 358);
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

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "참여자",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(324, 10, 150, 358);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		
		JPopupMenu pm = new JPopupMenu();	// +우클릭 시 나타날 팝업 메뉴 생성
		JMenuItem infoItem = new JMenuItem("친구 정보");	// +팝업메뉴 아이템들
	    JMenuItem friendAddItem = new JMenuItem("친구 추가");
	    JMenuItem omokItem = new JMenuItem("오목 신청");
	    pm.add(infoItem);
	    pm.add(friendAddItem);
	    pm.add(omokItem);
	    
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
        				infoItem.addActionListener(new ActionListener(){	// 친구 정보 아이템 클릭 시
        			    	public void actionPerformed(ActionEvent e1)
        			    	{
        			    		System.out.println(room.getUserArray());
        			    		int selectedIndex = uList.getSelectedIndex();	// 리스트에서 선택한 개체의 인덱스
        			    		if(uList.getSelectedValue().toString().equals("["+room.getUserArray().get(selectedIndex).getNickName()+"]"))
        			    		{
        			    			FriendInfo fi = new FriendInfo(room.getUserArray().get(selectedIndex));
        			    			fi.setVisible(true);
        			    		}
        			    	}
        			    });
        			    
        			    friendAddItem.addActionListener(new ActionListener(){
        			    	public void actionPerformed(ActionEvent e2)
        			    	{
        			    		OmokGame omok = new OmokGame(19);	// 19줄 판으로 오목 게임 실행
        			    	}
        			    });
        			    
        			    omokItem.addActionListener(new ActionListener(){
        			    	public void actionPerformed(ActionEvent e3)
        			    	{
        			    		OmokGame omok = new OmokGame(19);	// 19줄 판으로 오목 게임 실행
        			    	}
        			    });
        			}
        		}
            }
	    });
	    
		scrollPane_1.setViewportView(uList);

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
		
		if (!string.equals("")) {
			try {
				// 채팅방에 메시지 보냄
				if(rType.equals("일반"))	// 일반 대화방과 익명 대화방을 구분
				{
					client.getDos().writeUTF(
							User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNameString() + string);
				}
				else
				{
					client.getDos().writeUTF(
							User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNickNameString() + string);
				}
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

// 친구 정보 팝업 클래스
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
		this.setBounds(300, 100, 200, 200);
		this.setLayout(null);
		this.setModal(true);
	}
}