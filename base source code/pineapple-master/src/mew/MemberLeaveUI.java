package mew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class MemberLeaveUI extends JFrame implements ActionListener {	// 회원 탈퇴 시 재확인 하는 UI
	public boolean confirm = false;
	public JPanel contentPane;
	public JLabel question;
	public JLabel warning1;
	public JLabel warning2;
	public JButton yes;
	public JButton no;
	private SixClient client;
	
	public MemberLeaveUI(SixClient client)
	{
		
		this.client = client;
		initialize();
	}
	
	private void initialize() {
		setSize(300, 190);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(20, 20, 300, 190);
		contentPane.add(panel);
		panel.setLayout(null);
		
		question = new JLabel("탈퇴하시겠습니까?");
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setBounds(30, 30, 200, 15);
		panel.add(question);
		
		warning1 = new JLabel("(탈퇴하시면 회원정보가 삭제되고,");
		warning1.setHorizontalAlignment(SwingConstants.CENTER);
		warning1.setBounds(30, 45, 200, 15);
		panel.add(warning1);
		
		warning2 = new JLabel(" 모든 창이 종료됩니다.)");
		warning2.setHorizontalAlignment(SwingConstants.CENTER);
		warning2.setBounds(30, 60, 200, 15);
		panel.add(warning2);
		
		yes = new JButton("네");
		yes.setHorizontalAlignment(SwingConstants.CENTER);
		yes.setBounds(30, 80, 80, 20);
		yes.addActionListener(this);
		panel.add(yes);
		
		no = new JButton("아니오");
		no.setHorizontalAlignment(SwingConstants.CENTER);
		no.setBounds(150, 80, 80, 20);
		no.addActionListener(this);
		panel.add(no);
		
		this.setTitle("회원 탈퇴");
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == yes)	// 회원 탈퇴를 원하는 경우 생성된 회원 txt파일 삭제
		{
			String s = "C:\\Users\\Park\\workspace\\MEW\\src\\members\\" + client.getUser().getId() + ".txt";
			File f = new File(s);
			if(f.delete())
			{
				System.out.println("회원 정보 삭제 완료");
				System.exit(0);
			}
			else
			{
				System.out.println("삭제 실패");
			}
		}
		
		else if(e.getSource() == no)
		{
			dispose();
		}
	}
}
