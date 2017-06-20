package mew;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MemberUI extends JFrame {

	public boolean confirm = false;
	public JTextField idText;
	public JTextField pwText;
	public JTextField nameText; // +name �ؽ�Ʈ�ʵ�
	public JButton signUpBtn, cancelBtn;
	private SixClient client;

	public MemberUI(SixClient client) {
		setTitle("ȸ������");

		this.client = client;
		join();
	}

	private void join() {
		setBounds(100, 100, 335, 220);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(12, 10, 295, 200);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel idLabel = new JLabel("���̵�");
		idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		idLabel.setBounds(60, 40, 57, 15);
		panel.add(idLabel);

		JLabel pwLabel = new JLabel("��й�ȣ");
		pwLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pwLabel.setBounds(60, 65, 57, 15);
		panel.add(pwLabel);

		JLabel nameLabel = new JLabel("�̸�");	// +�̸� ���̺�
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(60, 90, 57, 15);
		panel.add(nameLabel);
		
		idText = new JTextField();
		idText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		idText.setBounds(129, 40, 116, 21);
		panel.add(idText);
		idText.setColumns(10);

		pwText = new JTextField();
		pwText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		
		pwText.setBounds(129, 65, 116, 21);
		panel.add(pwText);
		pwText.setColumns(10);

		nameText = new JTextField();	// +�̸� �ؽ�Ʈ�ʵ�
		nameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}
			}
		});
		nameText.setBounds(129, 90, 116, 21);
		panel.add(nameText);
		nameText.setColumns(10);
		
		signUpBtn = new JButton("����");
		signUpBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						msgSummit();
					}
				}).start();
				dispose();
			}
		});
		signUpBtn.setBounds(50, 120, 97, 23);
		panel.add(signUpBtn);

		cancelBtn = new JButton("���");
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
		cancelBtn.setBounds(148, 120, 97, 23);
		panel.add(cancelBtn);
		setVisible(true);
	}

	private void msgSummit() {// ���ϻ���
		if (client.serverAccess()) {
			try {
				// ȸ����������(���̵�+�н�����) ����
				client.getDos().writeUTF(
						User.MEMBERSHIP + "/" + idText.getText() + "/"
								+ pwText.getText() + "/" + nameText.getText());	// +�̸� ���� �߰�
				setVisible(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
