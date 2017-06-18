package Chat;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginUI extends JFrame {

	FileReader reader;
	String addr;
	public JTextField idText;
	public JTextField pwText;
	public JButton loginBtn, signUpBtn;
	public MemberUI mem;
	public JButton ipBtn;
	private SixClient client;
	
	public LoginUI(SixClient sixClient) {
		setTitle("�α���");
		this.client = sixClient;
		loginUIInitialize();
		
		try
		{
			addr = InetAddress.getLocalHost().getHostAddress();		// +���� IP�� �޾ƿ� �� �ְԲ� ����
		}
		catch(Exception e)
		{
			System.out.println("���� IP �������� ����");	// +IP�� ã�� ������ ���
		}
		
		this.setVisible(true);
	}

	private void loginUIInitialize() {
		setBounds(100, 100, 335, 218);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(12, 10, 295, 160);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("���̵�");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(60, 55, 57, 15);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("��й�ȣ");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(60, 86, 57, 15);
		panel.add(lblNewLabel_1);

		String recentId = null;
		try		// +�ֱ� �α��� ���̵� �о���� try~catch��
		{
			reader = new FileReader("C:\\Users\\��â��\\Desktop\\����\\recent.txt");
			int read;

			  char[] data = new char[50];

			  while ((read=reader.read(data)) != -1)
			  {
				  recentId = new String(data, 0, read);
			  }
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
		
		idText = new JTextField();
		idText.setBounds(129, 52, 116, 21);
		idText.setText(recentId);
		panel.add(idText);
		idText.setColumns(10);

		pwText = new JTextField();
		pwText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});
		pwText.setBounds(129, 83, 116, 21);
		panel.add(pwText);
		pwText.setColumns(10);

		loginBtn = new JButton("�α���");
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		loginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				msgSummit();
			}
		});
		loginBtn.setBounds(50, 111, 97, 23);
		panel.add(loginBtn);

		signUpBtn = new JButton("ȸ������");
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		signUpBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ȸ������
				mem = new MemberUI(client);
			}
		});
		signUpBtn.setBounds(149, 111, 97, 23);
		panel.add(signUpBtn);
	}
	
	private void msgSummit() {
		new Thread(new Runnable() {
			public void run() {

				// ���ϻ���
				if (client.serverAccess()) {
					try {
						// �α�������(���̵�+�н�����) ����
						client.getDos().writeUTF(
								User.LOGIN + "/" + idText.getText() + "/"+ pwText.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}).start();
	}
}
