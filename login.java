package changgeun;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JEditorPane;
public class woorilogin extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtID;
	private JTextField textField_1;
	private JPasswordField pw;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton radioButton;
	private JButton btnNewButton;
	private JButton btnJoin;
	private JButton btnIdPwFind;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					woorilogin frame = new woorilogin();
					frame.setVisible(true);
			} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public woorilogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 274, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Image img=new ImageIcon().getImage();
		final ImageIcon icon11=new ImageIcon("C:\\Users\\��â��\\Desktop\\javawork\\changgeun\\src\\changgeun\\����3.jpg");
		//�̹��� ���
		JPanel panel = new JPanel(){//�������� �ޱ����� ��ü������ ����
		    public void paintComponent(Graphics g) {//�̹��� �޴ºκ�
                g.drawImage(icon11.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
               } 
	
		};
		panel.setBounds(0, 0, 258, 492);
		contentPane.add(panel);
		panel.setLayout(null);
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setText("���̵� :");//���̵� �ؽ�Ʈ ��ġ
		textField.setBounds(32, 58, 56, 21);
		
		panel.add(textField);
		textField.setColumns(10);

		txtID = new JTextField();
		txtID.setBounds(111, 58, 116, 21);//���̵��Է��ϴ� �Ͼ�κ�
		panel.add(txtID);
		txtID.setColumns(10);
		txtID.setText("���̵� �Է�");//�Էºκп� �̸� �����մºκ�
		
		textField_1 = new JTextField();
		textField_1.setText("��й�ȣ :");
		textField_1.setEnabled(false);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(32, 88, 67, 21);//��й�ȣ �ؽ�Ʈ ��ġ
		panel.add(textField_1);
		
		
		pw = new JPasswordField();
		pw.setBounds(111, 88, 116, 21);//��й�ȣ �Է��ϴºκ� ��ġ
		panel.add(pw);
		
		rdbtnNewRadioButton = new JRadioButton("���̵�����");//���̵������ϴ¹�ư ����
		rdbtnNewRadioButton.setBounds(32, 118, 95, 23);//��ġ
		panel.add(rdbtnNewRadioButton);


		btnNewButton = new JButton("�α���");
		btnNewButton.setBounds(32, 178, 195, 23);//�α��ι�ư ��ġ
		panel.add(btnNewButton);
		
		btnIdPwFind = new JButton("���̵�/��й�ȣ ã��");
		btnIdPwFind.setBounds(32, 208, 195, 23);//���̵� ��й�ȣã�� ��ġ
		panel.add(btnIdPwFind);

		btnJoin = new JButton("ȸ������");
		btnJoin.setBounds(32, 246, 195, 23);//ȸ������ ��ġ
		panel.add(btnJoin);

		


	}

}
