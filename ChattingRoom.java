package mew;
import java.awt.*;
import javax.swing.*;

public class ChattingRoom extends JFrame {
	private JPanel contentPane;
	private JTextField roomName;
	private JTextField peopleNum;
	private JButton btnBattle;
	private JButton btnSettings;
	private JButton btnLeave;
	private JTextArea chattingSpace;
	private JTextArea writeSpace;
	private JButton btnSend;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try 
				{
					JFrame.setDefaultLookAndFeelDecorated(true);	// ȭ�� �� ����
					ChattingRoom frame = new ChattingRoom();
					frame.setVisible(true);
					frame.setTitle("My room1");		// JFrame ���� ����
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public ChattingRoom()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 700);	// ��ġ �� ũ�� ����
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 400, 680);
		contentPane.add(panel);
		panel.setLayout(null);
		
		Font font = new Font("arian", Font.ITALIC, 18);
		roomName = new JTextField();	// �� �̸� �κ�
		roomName.setEnabled(false);		// ������
		roomName.setEditable(false);	// ���� �Ұ�
		roomName.setDisabledTextColor(Color.black);		// ��Ȱ���� ������ �۾� �� ����
		roomName.setText("�Ϲ� ��ȭ��");
		roomName.setHorizontalAlignment(JTextField.CENTER);	// �ؽ�Ʈ�ʵ� ��� ����
		roomName.setBounds(10, 10, 100, 30);
		panel.add(roomName);
		roomName.setColumns(10);
		
		
		peopleNum = new JTextField();	// ��ȭ�� ���� �ο� ��
		peopleNum.setEnabled(false);	// ��� �Ұ�
		peopleNum.setEditable(false);	// ���� �Ұ�
		peopleNum.setDisabledTextColor(Color.black);
		peopleNum.setText("4�� ���� �� | ��â��, �㹮��, ������, ������");
		peopleNum.setHorizontalAlignment(JTextField.CENTER);
		peopleNum.setBounds(10, 40, 390, 30);
		panel.add(peopleNum);
		peopleNum.setColumns(10);
		

		ImageIcon imgBattle = new ImageIcon("g:\\btnBattle.jpg");	// ���� ������ ����
		btnBattle = new JButton(imgBattle);	// 1��1 ���� ����(����)��ư
		btnBattle.setToolTipText("���� ���� ��û");	// ���콺�� ���� ��� �۾��� ���̰Բ� ����
		btnBattle.setBounds(310, 10, 30, 30);
		panel.add(btnBattle);
		
		
		ImageIcon imgLeave = new ImageIcon("g:\\btnLeave.jpg");	// ������ ������ ����
		btnLeave = new JButton(imgLeave);	// �� ������ ��ư
		btnLeave.setToolTipText("�濡�� ������");
		btnLeave.setBounds(340, 10, 30, 30);
		panel.add(btnLeave);

		
		ImageIcon imgSettings = new ImageIcon("g:\\btnSettings.jpg");	// ���� ������ ����
		btnSettings = new JButton(imgSettings);	// ���� ��ư
		btnSettings.setToolTipText("����");
		btnSettings.setBounds(370, 10, 30, 30);
		panel.add(btnSettings);

		
		chattingSpace = new JTextArea();	// ��ȭ ����
		chattingSpace.setLineWrap(true);	// ������ ������� �ڵ�����
		chattingSpace.requestFocus();
		chattingSpace.setEditable(false);
		chattingSpace.setFont(font);
		chattingSpace.setText("������ : �ȳ��ϼ���\n                                  ��â�� : �� �ȳ��ϼ���\n������ : �ȳ����輼��                                                                           ������ : �ȳ���������~\n                                 �㹮�� : �ȳ���������" );
		JScrollPane scroll1 = new JScrollPane(chattingSpace);	// ���� ��ũ�ѹ�
		scroll1.setBounds(10, 80, 390, 450);
		panel.add(scroll1);
		
		
		writeSpace = new JTextArea();	// �޽��� �ۼ� ����
		writeSpace.setLineWrap(true);	// ������ ������� �ڵ�����
		writeSpace.requestFocus();	// ����Ʈ ��Ŀ�� ����
		writeSpace.setText("�Է� : ");	
		writeSpace.selectAll();		// �ؽ�Ʈ�� ��� ���õǰ� ��
		writeSpace.setSelectedTextColor(Color.gray);
		JScrollPane scroll2 = new JScrollPane(writeSpace);	// ���� ��ũ�ѹ�
		scroll2.setBounds(10, 530, 360, 100);
		panel.add(scroll2);
		
		
		ImageIcon imgSend = new ImageIcon("g:\\btnSend.jpg");	// ������ ������ ����
		btnSend = new JButton(imgSend);		// ������ ��ư
		btnSend.setToolTipText("������");
		btnSend.setBounds(370, 600, 30, 30);
		panel.add(btnSend);
	}
}