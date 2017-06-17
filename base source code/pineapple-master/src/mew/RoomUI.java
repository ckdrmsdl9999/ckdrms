package mew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RoomUI extends JFrame {
	private SixClient client;
	private Room room;

	public JTextArea chatArea;
	public JTextField chatField;
	public JList uList;
	public DefaultListModel model;

	public RoomUI(SixClient client, Room room) {
		this.client = client;
		this.room = room;
		setTitle(room.getRoomName() + "(" + room.getRoomType() + " ä�ù�)");	// +�� ������ ���� �̸� ����
		initialize(room);
	}

	private void initialize(Room room) 
	{
		setBounds(100, 100, 502, 481);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "ä��", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel.setBounds(12, 10, 300, 358);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

 		chatArea = new JTextArea();
		chatArea.setBackground(new Color(224, 255, 255));
		chatArea.setEditable(false);
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
				.getBorder("TitledBorder.border"), "������",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_2.setBounds(324, 10, 150, 358);
		getContentPane().add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		uList = new JList(new DefaultListModel());
		model = (DefaultListModel) uList.getModel();
		
		JPopupMenu pm = new JPopupMenu();	// +��Ŭ�� �� ��Ÿ�� �˾� �޴� ����
		JMenuItem infoItem = new JMenuItem("ģ�� ����");	// +�˾��޴� �����۵�
	    JMenuItem friendAddItem = new JMenuItem("ģ�� �߰�");
	    JMenuItem omokItem = new JMenuItem("���� ��û");
	    pm.add(infoItem);
	    pm.add(friendAddItem);
	    pm.add(omokItem);
	    
		// +���콺 ��Ŭ�� �� �˾� �޴� �߱�
	    uList.addMouseListener(new MouseAdapter() {
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
	    
	    infoItem.addActionListener(new ActionListener(){	// ģ�� ���� ������ Ŭ�� ��
	    	public void actionPerformed(ActionEvent e1)
	    	{
	    		for(int i=0; i<client.getUserArray().size(); i++)
	    		{
	    			if(client.getUserArray().get(i).toString().equals(uList.getSelectedValue().toString()))
	    			{	
	    				FriendInfo fi = new FriendInfo(client.getUserArray().get(i));	// ���� ����� ���ͼ� ��� �ȿ� Ŭ���� ��ü�� ������� ��� ģ�� ���� ���
	    				fi.setVisible(true);
	    				break;
	    			}
	    		}
	    	}
	    });
	    
	    friendAddItem.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e2)
	    	{
	    		try
	    		{
	    			int lineNum = 19;
	    			int portNum = (int)(Math.random()*4443+5556);	// +�̹� ���� �ִ� 5555�� ��Ʈ �����ϰ� �� �ں��� 9999��  ��Ʈ���� �� �������� �ο�
	    			
	    			client.getDos().writeUTF(User.OMOK_INVITE + "/" + client.getUser().getId() + "/" + Integer.toString(lineNum) + "/" + Integer.toString(portNum));
	    			OmokGame omok = new OmokGame(lineNum, false, portNum);

	    		}
	    		catch(Exception e)
	    		{
	    			e.getMessage();
	    		}
	    	}
	    });	 
	    
	    omokItem.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e3)
	    	{
	    		OmokGame omok = new OmokGame(19);	// 19�� ������ ���� ���� ����
	    	}
	    });
	    
		scrollPane_1.setViewportView(uList);

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
		
		if (!string.equals("")) {
			try {
				// ä�ù濡 �޽��� ����
				if(rType.equals("�Ϲ�"))	// �Ϲ� ��ȭ��� �͸� ��ȭ���� ����
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

// ģ�� ���� �˾� Ŭ����
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
		this.setBounds(300, 100, 200, 200);
		this.setLayout(null);
		this.setModal(true);
	}
}