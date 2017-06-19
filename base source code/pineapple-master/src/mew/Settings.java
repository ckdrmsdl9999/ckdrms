package mew;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
//------------------------------------------------------------------//
// ActionListener �������̽� ���
public class Settings extends JFrame implements ActionListener, ItemListener{
//------------------------------------------------------------------//
	private JPanel contentPane;
	private JLabel name, id, nickName, pw;
	private JLabel userName, userId, userNickName;
	private JPasswordField userPw;
	private JTextField row;
	private JLabel letterSize;
	private Choice letterSizeChange;
	private JLabel backgroundColor;
	private JLabel fontSet;
	private JButton backgroundYellowColor, backgroundGreenColor, backgroundSkyColor;
	private JButton fontBold, fontItalic, fontPlain;
	private JButton passwordChange, setOn, reset, memberLeave;
 //------------------------------------------------------------------//
 // ����� �������� ������ �� �ʿ��� ���� ���� tab
	private textAndBackground tab;
	private SixClient client;
 //------------------------------------------------------------------//

 //------------------------------------------------------------------//
 // Settings�� ���� ������ ������ �����ڿ��� tab���� �޾Ƽ� �����ϰ� set�޼ҵ带 �����ϵ��� Ʋ�� ����
	public Settings(textAndBackground tab, SixClient client){         
		this.tab = tab;
		this.client = client;
		set();
	}


   //------------------------------------------------------------------//
	public void set()   // main�޼ҵ带 set���� �����ؼ� RestRoomUI�� ����
   //------------------------------------------------------------------//
	{
      // TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					JFrame.setDefaultLookAndFeelDecorated(true);   // ȭ�� �� ����
					settingUI();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
      
	}
   
	public void settingUI(){
		setSize(420, 390);   // �ٿ���� ũ�� ����
		contentPane = new JPanel();      // ���� �г� ����
		setContentPane(contentPane);
		contentPane.setLayout(null);
	      
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 410, 380);   // �ٿ���� ũ�� ����
		contentPane.add(panel);            // ���� �гο� �߰�
		panel.setLayout(null);
	      
		name = new JLabel("�̸�");      // �̸� ���̺�
		name.setBounds(30, 20, 60, 20);   // �ٿ���� ũ�� ����
		panel.add(name);         // panel�� �߰�
	      
		userName = new JLabel(); // ���� �̸��� ���� ���̺�
		userName.setText(client.getUser().getName());      // ���̺� ��Ʈ�� ���
		userName.setBounds(110, 20, 270, 20);   // �ٿ���� ũ�� ����
		userName.setForeground(Color.BLUE);
		panel.add(userName);
		
		id = new JLabel("���̵�");      // �̸� ���̺�
		id.setBounds(30, 50, 60, 20);   // �ٿ���� ũ�� ����
		panel.add(id);         // panel�� �߰�
		
		userId = new JLabel(); // ���� �̸��� ���� ���̺�
		userId.setText(client.getUser().getId());      // ���̺� ��Ʈ�� ���
		userId.setBounds(110, 50, 270, 20);   // �ٿ���� ũ�� ����
		userId.setForeground(Color.BLUE);
		panel.add(userId);
		
		nickName = new JLabel("�г���");      // �г��� ���̺�
		nickName.setBounds(30, 80, 60, 20);   // �ٿ���� ũ�� ����
		panel.add(nickName);         // panel�� �߰�
		
		userNickName = new JLabel();      // ���� �г����� ���� ���̺�
		userNickName.setText(client.getUser().getNickName());      // ���̺� ��Ʈ�� ���
		userNickName.setBounds(110, 80, 270, 20);   // �ٿ���� ũ�� ����
		userNickName.setForeground(Color.BLUE);
		panel.add(userNickName);         // panel�� �߰�
		
		pw = new JLabel("��й�ȣ");      // �̸� ���̺�
		pw.setBounds(30, 110, 60, 20);   // �ٿ���� ũ�� ����
		panel.add(pw);         // panel�� �߰�
		
		row = new JTextField();		// ��輱
		row.setBounds(10, 142, 380, 2);
		row.setEditable(false);
		panel.add(row);
		
		userPw = new JPasswordField();
		userPw.setBounds(110, 110, 140, 20);
		panel.add(userPw);
		
		passwordChange =  new JButton("��й�ȣ ����");   // ��й�ȣ ���� ��ư
		passwordChange.setBounds(260, 110, 120, 20);   // �ٿ���� ũ�� ����
		passwordChange.addActionListener(this);
		panel.add(passwordChange);      // panel�� �߰�
		
		letterSize = new JLabel();      // ����ũ�� ���̺�
		letterSize.setText("���� ũ��");   // ���̺� ��Ʈ�� ���
		letterSize.setBounds(30, 160, 70, 30);   // �ٿ���� ũ�� ����
		panel.add(letterSize);         // panel�� �߰�
		
		letterSizeChange =  new Choice();   // ����ũ�⸦ ������ �� �ִ� â
		letterSizeChange.add("5");      // ũ�� ���� �߰�
		letterSizeChange.add("10");
		letterSizeChange.add("15");
		letterSizeChange.add("20");
		letterSizeChange.add("25");
		letterSizeChange.add("30");
		letterSizeChange.addItemListener(this);
		letterSizeChange.setBounds(110, 160, 50, 30);   // �ٿ���� ũ�� ����
		panel.add(letterSizeChange);   // panel�� �߰�
		
		backgroundColor = new JLabel();      // ���� ���̺�
		backgroundColor.setText("����");      // ���̺� ��Ʈ�� ���
		backgroundColor.setBounds(30, 190, 70, 30);   // �ٿ���� ũ�� ����
		panel.add(backgroundColor);      // panel�� �߰�
		
		backgroundYellowColor =  new JButton("Yellow");      // ������ �������� �����ϴ� ��ư
		backgroundYellowColor.setBackground(new Color(255, 255, 153));
		backgroundYellowColor.setBounds(110, 190, 80, 30);   // �ٿ���� ũ�� ����
		backgroundYellowColor.addActionListener(this);
		panel.add(backgroundYellowColor);         // panel�� �߰�
		
		backgroundGreenColor =  new JButton("Green");   // ������ �ʷϻ����� �����ϴ� ��ư
		backgroundGreenColor.setBackground(new Color(153, 255, 153));
		backgroundGreenColor.setBounds(205, 190, 80, 30);   // �ٿ���� ũ�� ����
		backgroundGreenColor.addActionListener(this);
		panel.add(backgroundGreenColor);         // panel�� �߰�
		
		backgroundSkyColor =  new JButton("Blue");   // ������ �Ķ������� �����ϴ� ��ư
		backgroundSkyColor.setBackground(new Color(224, 255, 255));
		backgroundSkyColor.setBounds(300, 190, 80, 30);   // �ٿ���� ũ�� ����
		backgroundSkyColor.addActionListener(this);
		panel.add(backgroundSkyColor);         // panel�� �߰�
		
		fontSet = new JLabel();      // ���ڻ� ���̺�
		fontSet.setText("��Ʈ");      // ���̺� ��Ʈ�� ���
		fontSet.setBounds(30, 230, 70, 30);   // �ٿ���� ũ�� ����
		panel.add(fontSet);      // panel�� �߰�
		
		fontBold = new JButton("BOLD");      // BOLD ��Ʈ�� �����ϴ� ��ư
		fontBold.setBounds(110, 230, 80, 30);   // �ٿ���� ũ�� ����
		fontBold.addActionListener(this);      
		panel.add(fontBold);      // panel�� �߰�
		
		fontItalic = new JButton("ITALIC");   // ITALIC ��Ʈ�� �����ϴ� ��ư
		fontItalic.setBounds(205, 230, 80, 30);   // �ٿ���� ũ�� ����
		fontItalic.addActionListener(this);
		panel.add(fontItalic);      // panel�� �߰�
		
		fontPlain = new JButton("PLAIN");      // PLAIN ��Ʈ�� �����ϴ� ��ư
		fontPlain.setBounds(300, 230, 80, 30);   // �ٿ���� ũ�� ����
		fontPlain.addActionListener(this);
		panel.add(fontPlain);      // panel�� �߰�
		
		setOn = new JButton("�����ϱ�");	// ���� ���� ���� ��ư
		setOn.addActionListener(this);
		setOn.setBounds(30, 290, 165, 30);		
		panel.add(setOn);
		
		reset = new JButton("�ʱ�ȭ");		// �ʱ�ȭ ��ư
		reset.addActionListener(this);
		reset.setBounds(215, 290, 165, 30);
		panel.add(reset);
		
		memberLeave =  new JButton("ȸ�� Ż��");      // ȸ��Ż�� ��ư
		memberLeave.setBounds(260, 20, 120, 30);   // �ٿ���� ũ�� ����
		memberLeave.addActionListener(this);
		memberLeave.setBackground(Color.RED);
		panel.add(memberLeave);         // panel�� �߰�
		
		this.setTitle("����");            // ������ ������
		this.setVisible(true);
	}	
   
	public void actionPerformed(ActionEvent e) {		// ������ ��Ʈ ���� �� ���� set
		// TODO Auto-generated method stub
		if(e.getSource() == backgroundYellowColor)
		{
			tab.setBackColor(new Color(255, 255, 153));
			backgroundYellowColor.setEnabled(false);	// ������ enable��Ű�� �� ��ư disable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
		}
		
		else if(e.getSource() == backgroundGreenColor)
		{
			tab.setBackColor(new Color(153, 255, 153));
			backgroundYellowColor.setEnabled(true);		// ������ enable��Ű�� �� ��ư disable
			backgroundGreenColor.setEnabled(false);
			backgroundSkyColor.setEnabled(true);
		}
		
		else if(e.getSource() == backgroundSkyColor)
		{
			tab.setBackColor(new Color(224, 255, 255));
			backgroundYellowColor.setEnabled(true);		// ������ enable��Ű�� �� ��ư disable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(false);
		}
		
		else if(e.getSource() == fontBold)
		{
			tab.setFont(Font.BOLD);
			fontBold.setEnabled(false);		// ������ enable��Ű�� �� ��ư disable
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == fontItalic)
		{
			tab.setFont(Font.ITALIC);
			fontBold.setEnabled(true);		// ������ enable��Ű�� �� ��ư disable
			fontItalic.setEnabled(false);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == fontPlain)
		{	
			tab.setFont(Font.PLAIN);
			fontBold.setEnabled(true);		// ������ enable��Ű�� �� ��ư disable
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(false);
		}
		
		else if(e.getSource() == passwordChange)	// ��й�ȣ ���� ��ư ���� ��
		{
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ���� �Ǿ����ϴ�.", "���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(e.getSource() == memberLeave)
		{
			MemberLeaveUI ml = new MemberLeaveUI(client);
			ml.setLocationRelativeTo(this.memberLeave);
		}
		
		else if(e.getSource() == setOn)
		{
			System.out.println("���� ����");
			JOptionPane.showMessageDialog(this, "���� �Ǿ����ϴ�.", "���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
			backgroundYellowColor.setEnabled(true);		// ��� ��ư enable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
			fontBold.setEnabled(true);
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == reset)
		{
			System.out.println("���� �ʱ�ȭ");
			JOptionPane.showMessageDialog(this, "��� ������ �ʱ�ȭ �Ǿ����ϴ�.", "���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
			backgroundYellowColor.setEnabled(true);		// ��� ��ư enable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
			fontBold.setEnabled(true);
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
			
			tab.setBackColor(new Color(224, 255, 255));		// ���� ��Ʈ�� ������ ������
			tab.setFont(Font.PLAIN);
			tab.setSize(12);
		}
	}
   
	public void itemStateChanged(ItemEvent e){	// ���� ũ�� ���� �� ���� set
		Choice choice = (Choice)e.getSource();
		if(choice.getSelectedItem() == "5")
		{
			tab.setSize(5);
		}	

		else if(choice.getSelectedItem() == "10")
		{
			tab.setSize(10);
		}
	   
		else if(choice.getSelectedItem() == "15")
		{
			tab.setSize(15);
		}
	   
		else if(choice.getSelectedItem() == "20")
		{	
			tab.setSize(20);
		}
	   
		else if(choice.getSelectedItem() == "25")
		{	
			tab.setSize(25);
		}
	   
		else if(choice.getSelectedItem() == "30")
		{
			tab.setSize(30);
		}

	}
}