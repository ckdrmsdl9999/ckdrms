package changgeun;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import java.awt.Choice;
import java.awt.Color;
public class woorijoin extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtId;
	
	private JTextField txtId_1;
	private JTextField txtPw;
	private JTextField txtPW;
	private JTextField txtRpw;
	private JTextField txtRPW;
	private JTextField txtNick;
	private JTextField txtNick_1;
	private JTextField txtName;
	private JTextField txtName_1;
	private JTextField txtHp;
	private JTextField txtHP;
	private JTextField txtEmail;
	private JTextField txtEmail_1;
	private JTextField txtSex;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					woorijoin frame = new woorijoin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public woorijoin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 335, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Image img=new ImageIcon().getImage();//���׸�����ϱ����� ����
		final ImageIcon icon11=new ImageIcon("C:\\Users\\��â��\\Desktop\\javawork\\changgeun\\src\\changgeun\\����1.png");
		//���ϰ��
		JPanel panel = new JPanel(){//���ȭ�� �ޱ����� ����
		    public void paintComponent(Graphics g) {
                g.drawImage(icon11.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
               } 
	
		};
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(0, 0, 335, 565);//�ܰ��κ� ũ��
		//panel.setBackground(Color.green);
		contentPane.add(panel);
		panel.setLayout(null);	

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setFont(new Font("����ü", Font.BOLD, 18));
		textField.setText("ȸ �� �� ��");
		textField.setBounds(107, 21, 118, 31);//���� ��� ȸ�����Ժκ�
		panel.add(textField);
		textField.setColumns(10);
		
		txtId = new JTextField();
		txtId.setEnabled(false);
		txtId.setEditable(false);
		txtId.setText(" ���̵� ");//���̵�κ�
		txtId.setBounds(51, 89, 55, 21);//���̵���ǥ
		panel.add(txtId);

		txtId.setColumns(10);	
		txtId_1 = new JTextField();//���̵��ؽ�Ʈ����
		txtId_1.setBounds(153, 89, 136, 21);//���̵��ؽ�Ʈ�κ�
		panel.add(txtId_1);
		txtId_1.setColumns(10);	
		txtId_1.setText("���̵� �Է�");//���̵��Էºκп� �Է��� ���̵��Է� ����ºκ�
		
		JButton btnIDCheck = new JButton("ID �ߺ�Ȯ��");
		btnIDCheck.setBounds(153, 120, 129, 23);//ID�ߺ�Ȯ�� ��ġ
		panel.add(btnIDCheck);		

		txtPw = new JTextField();//��й�ȣ �ؽ�Ʈ����
		txtPw.setText("��й�ȣ ");
		txtPw.setEnabled(false);
		txtPw.setEditable(false);
		txtPw.setColumns(10);
		txtPw.setBounds(51, 153, 65, 21);//��й�ȣ �ؽ�Ʈ ��ġ
		panel.add(txtPw);

		

		txtPW = new JTextField();
		txtPW.setColumns(10);
		txtPW.setBounds(153, 153, 116, 21);//��й�ȣ �Է��ϴ�ĭ��ġ
		panel.add(txtPW);
		txtPW.setText("��й�ȣ �Է�");
		

		txtRpw = new JTextField();
		txtRpw.setText("��й�ȣ Ȯ�� ");//��й�ȣȮ���ؽ�Ʈ
		txtRpw.setEnabled(false);
		txtRpw.setEditable(false);
		txtRpw.setColumns(15);
		txtRpw.setBounds(51, 202, 79, 21);
		panel.add(txtRpw);
		
		
	
		txtRPW = new JTextField();
		txtRPW.setColumns(10);
		txtRPW.setBounds(153, 202, 116, 21);//������ȣ ���Է���ġ
		panel.add(txtRPW);
		txtRPW.setText("��й�ȣ ���Է�");//��й�ȣ ���Է� 
		
		txtNick = new JTextField();
		txtNick.setText(" �г���  ");//�г��� ����
		txtNick.setEnabled(false);
		txtNick.setEditable(false);
		txtNick.setColumns(10);
		txtNick.setBounds(51, 236, 50, 21);//�г��� �ؽ�Ʈ��ġ
		panel.add(txtNick);

		txtNick_1 = new JTextField();
		txtNick_1.setColumns(10);
		txtNick_1.setBounds(153, 236, 116, 21);//�г��� �Է��ϴ� �Ͼ�ĭ��ġ
		panel.add(txtNick_1);
		txtNick_1.setText("�г��� �Է�");//�Է��� �Ͼ�ĭ�� �̸� ����±�

		JButton btnNickCheck = new JButton("�г��� �ߺ�Ȯ��");//�г��� �ߺ�Ȯ�� ��ư
		btnNickCheck.setBounds(153, 268, 130, 23);
		panel.add(btnNickCheck);

		txtName = new JTextField();
		txtName.setText(" �̸� ");//�̸��ؽ�Ʈ
		txtName.setEnabled(false);
		txtName.setEditable(false);
		txtName.setColumns(10);
		txtName.setBounds(51, 303, 47, 21);
		panel.add(txtName);

		txtName_1 = new JTextField();
		txtName_1.setColumns(55);
		txtName_1.setBounds(153, 303, 116, 21);//�̸� �Է��ϴ°� ��ġ
		panel.add(txtName_1);
		txtName_1.setText("�̸� �Է�");
		

		JButton btnJoin = new JButton("����");//���Թ�ư
		btnJoin.setBounds(71, 465, 73, 23);
		panel.add(btnJoin);

		

		JButton btnCancel = new JButton("���");
		btnCancel.setBounds(186, 465, 73, 23);//��ҹ�ư��ġ
		panel.add(btnCancel);

	}

}

