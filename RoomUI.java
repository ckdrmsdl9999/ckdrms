package mew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RoomUI extends JFrame {
	private SixClient client;
	private Room room;
	//------------------------------------------------------------------//
	// ����� �������� ������ �� �ʿ��� ���� ���� tab
	private textAndBackground tab;
	//------------------------------------------------------------------//

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
		chatArea.setToolTipText("'/'Ű�� ��� ������ id�� �Է��ϸ� ģ���� �濡 �ʴ��� �� �ֽ��ϴ�. ex) /james ");	// +���� �߰�
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

	private void msgSummit() {////////////////////////�ʴ����ϴºκ� �߰���!!!!!!! /invite ���̵�  �Է½� �ش�ģ���ʴ�
		String string = chatField.getText();
		String rType = room.getRoomType();
		String trash="";
		String id="";
		StringTokenizer token = new StringTokenizer(string, " "); // ��ū																			// ����
		trash=token.nextToken();
		if(token.hasMoreTokens())
			id = token.nextToken(); // ��ū���� �и��� ��Ʈ��
		
		if (!string.equals("")||!id.equals("")) {
			try {
				// ä�ù濡 �޽��� ����
				if(rType.equals("�Ϲ�"))	// �Ϲ� ��ȭ��� �͸� ��ȭ���� ����
				{
					if (trash.equals("/invite")) //////////////////////////�ʴ� �߰��Ѻκ� �Ϲ�ä�ù�
					{
								
							client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()
							+"/"+room.getRoomType());
				
					}	
					else{
					client.getDos().writeUTF(
							User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNameString() + string);
					}
				}
				else
				{

					if (trash.equals("/invite")) //////////////////////////�ʴ� �߰��Ѻκ� �͸�ä�ù�
					{
						
						
							client.getDos().writeUTF(User.INVITE +"/"+ id + "/" + room.getRoomNum()+"/"+room.getRoomName()
							+"/"+room.getRoomType());
				
					}	
					else{
					client.getDos().writeUTF(
							User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toNickNameString() + string);}
				}
				chatField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}