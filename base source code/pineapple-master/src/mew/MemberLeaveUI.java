package mew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class MemberLeaveUI extends JFrame implements ActionListener {	// ȸ�� Ż�� �� ��Ȯ�� �ϴ� UI
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
		
		question = new JLabel("Ż���Ͻðڽ��ϱ�?");
		question.setHorizontalAlignment(SwingConstants.CENTER);
		question.setBounds(30, 30, 200, 15);
		panel.add(question);
		
		warning1 = new JLabel("(Ż���Ͻø� ȸ�������� �����ǰ�,");
		warning1.setHorizontalAlignment(SwingConstants.CENTER);
		warning1.setBounds(30, 45, 200, 15);
		panel.add(warning1);
		
		warning2 = new JLabel(" ��� â�� ����˴ϴ�.)");
		warning2.setHorizontalAlignment(SwingConstants.CENTER);
		warning2.setBounds(30, 60, 200, 15);
		panel.add(warning2);
		
		yes = new JButton("��");
		yes.setHorizontalAlignment(SwingConstants.CENTER);
		yes.setBounds(30, 80, 80, 20);
		yes.addActionListener(this);
		panel.add(yes);
		
		no = new JButton("�ƴϿ�");
		no.setHorizontalAlignment(SwingConstants.CENTER);
		no.setBounds(150, 80, 80, 20);
		no.addActionListener(this);
		panel.add(no);
		
		this.setTitle("ȸ�� Ż��");
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == yes)	// ȸ�� Ż�� ���ϴ� ��� ������ ȸ�� txt���� ����
		{
			String s = "C:\\Users\\Park\\workspace\\MEW\\src\\members\\" + client.getUser().getId() + ".txt";
			File f = new File(s);
			if(f.delete())
			{
				System.out.println("ȸ�� ���� ���� �Ϸ�");
				System.exit(0);
			}
			else
			{
				System.out.println("���� ����");
			}
		}
		
		else if(e.getSource() == no)
		{
			dispose();
		}
	}
}
