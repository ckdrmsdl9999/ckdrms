package mew;

import java.awt.*;
import javax.swing.*;

public class AddFriend extends JFrame {
	private JPanel contentPane;
	private JButton btnAddFriend;
	private JTextField friendId;
	private JLabel complete;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try 
				{
					JFrame.setDefaultLookAndFeelDecorated(true);	// ȭ�� �� ����
					AddFriend frame = new AddFriend();
					frame.setVisible(true);
					frame.setTitle("ģ�� ���");
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public AddFriend()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 140);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 310, 120);
		contentPane.add(panel);
		panel.setLayout(null);

		
		friendId = new JTextField();	// ģ�� ID �ؽ�Ʈ �Է�
		friendId.setText("ģ���� ID�� �Է����ּ���.");
		friendId.selectAll();	// ���� �۾� ���� ����
		friendId.setSelectedTextColor(Color.gray);	// ���õ� �۾� ȸ������
		friendId.setToolTipText("ã�� ģ����  ID �Է�");	// ���콺 �ø��� ������ �޽���
		friendId.setBackground(Color.yellow);	// ����
		friendId.setBounds(10, 10, 170, 30);
		panel.add(friendId);

		
		btnAddFriend = new JButton("ģ�� �߰�");	// ģ�� �߰� ��ư
		btnAddFriend.setBounds(190, 10, 100, 30);
		panel.add(btnAddFriend);
		
		complete = new JLabel("ģ����Ͽ� �߰��Ǿ����ϴ�.");	// �߰� �Ϸ� �޽���
		complete.setBounds(10, 50, 300, 30);
		panel.add(complete);
	}
}