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
					JFrame.setDefaultLookAndFeelDecorated(true);	// 화면 뷰 변경
					AddFriend frame = new AddFriend();
					frame.setVisible(true);
					frame.setTitle("친구 등록");
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

		
		friendId = new JTextField();	// 친구 ID 텍스트 입력
		friendId.setText("친구의 ID를 입력해주세요.");
		friendId.selectAll();	// 위의 글씨 전부 선택
		friendId.setSelectedTextColor(Color.gray);	// 선택된 글씨 회색으로
		friendId.setToolTipText("찾을 친구의  ID 입력");	// 마우스 올리면 나오는 메시지
		friendId.setBackground(Color.yellow);	// 배경색
		friendId.setBounds(10, 10, 170, 30);
		panel.add(friendId);

		
		btnAddFriend = new JButton("친구 추가");	// 친구 추가 버튼
		btnAddFriend.setBounds(190, 10, 100, 30);
		panel.add(btnAddFriend);
		
		complete = new JLabel("친구목록에 추가되었습니다.");	// 추가 완료 메시지
		complete.setBounds(10, 50, 300, 30);
		panel.add(complete);
	}
}