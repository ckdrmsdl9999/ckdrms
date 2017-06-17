package Chat;


import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerAddress extends JFrame {

	public JButton confirmBtn;
	public JTextField ipText;
	private LoginUI loginUI;

	public ServerAddress(LoginUI loginUI) {
		this.loginUI = loginUI;
		initialize();
	}

	private void initialize() {
		String addr = null;
		try
		{
			addr = InetAddress.getLocalHost().getHostAddress();		// +현재 IP를 받아올 수 있게끔 설정
		}
		catch(Exception e)
		{
			System.out.println("서버 IP 가져오기 실패");	// +IP를 찾지 못했을 경우
		}
		setTitle("서버 IP 주소 입력");
		setBounds(100, 100, 306, 95);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(12, 10, 266, 37);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		ipText = new JTextField();
		ipText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginUI.ipBtn.setText(ipText.getText());
					loginUI.setVisible(true);
					dispose();
					loginUI.idText.requestFocus();
				}
			}
		});
		ipText.setText(addr);	// 받아온 IP 주소를 기본값으로 써줌
		panel.add(ipText, BorderLayout.CENTER);
		ipText.setColumns(10);

		confirmBtn = new JButton("확인");
		confirmBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loginUI.ipBtn.setText(ipText.getText());
				loginUI.setVisible(true);
				dispose();
				loginUI.idText.requestFocus();
			}
		});
		panel.add(confirmBtn, BorderLayout.EAST);
		setVisible(true);
	}

}
