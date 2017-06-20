package mew;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JPasswordField;
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
	ServerAddress sd;
	FileReader reader;
	String addr;
	public JTextField idText;
	public JPasswordField pwText;
	public JButton loginBtn, signUpBtn;
	public MemberUI mem;
	public JButton ipBtn;
	private SixClient client;
	
	public LoginUI(SixClient sixClient) {
		setTitle("�α���");
		sd = new ServerAddress(this);	// +IP�ּ� ġ�� â�� ������
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
		setSize(335, 218);
		setLocation(this.sd.getX(), this.sd.getY()+95);
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
			reader = new FileReader("C:\\members\\recent.txt");
			int read;

			  char[] data = new char[50];

			  while ((read=reader.read(data)) != -1)
			  {
				  recentId = new String(data, 0, read);
			  }
		}
		catch(IOException e)
		{
			e.getMessage();
		} 
		
		idText = new JTextField();
		idText.setBounds(129, 52, 116, 21);
		idText.setText(recentId);
		panel.add(idText);
		idText.setColumns(10);

		pwText = new JPasswordField();
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
				mem.setLocationRelativeTo(signUpBtn);
			}
		});
		signUpBtn.setBounds(149, 111, 97, 23);
		panel.add(signUpBtn);
		
		JLabel lblNewLabel_2 = new JLabel("���� IP �ּ�");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(12, 10, 78, 15);
		panel.add(lblNewLabel_2);

		ipBtn = new JButton("IP �ּ� �Է�");
		ipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ServerAddress sd = new ServerAddress(LoginUI.this);
				setVisible(false);
			}
		});
		ipBtn.setBounds(93, 6, 180, 23);
		panel.add(ipBtn);
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
