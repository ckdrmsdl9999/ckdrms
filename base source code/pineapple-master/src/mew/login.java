package mew;

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

public class login extends JFrame {
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
					login frame = new login();
					frame.setVisible(true);
			} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 274, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Image img=new ImageIcon().getImage();
		final ImageIcon icon11=new ImageIcon("C:\\Users\\윤창근\\Desktop\\javawork\\changgeun\\src\\changgeun\\사진3.jpg");
		//이미지 경로
		JPanel panel = new JPanel(){//사진파일 받기위해 객체선언후 내용
		    public void paintComponent(Graphics g) {//이미지 받는부분
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
		textField.setText("아이디 :");//아이디 텍스트 위치
		textField.setBounds(32, 58, 56, 21);
		
		panel.add(textField);
		textField.setColumns(10);

		txtID = new JTextField();
		txtID.setBounds(111, 58, 116, 21);//아이디입력하는 하얀부분
		panel.add(txtID);
		txtID.setColumns(10);
		txtID.setText("아이디를 입력");//입력부분에 미리 써져잇는부분
		
		textField_1 = new JTextField();
		textField_1.setText("비밀번호 :");
		textField_1.setEnabled(false);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(32, 88, 67, 21);//비밀번호 텍스트 위치
		panel.add(textField_1);
		
		
		pw = new JPasswordField();
		pw.setBounds(111, 88, 116, 21);//비밀번호 입력하는부분 위치
		panel.add(pw);
		
		rdbtnNewRadioButton = new JRadioButton("아이디저장");//아이디저장하는버튼 선언
		rdbtnNewRadioButton.setBounds(32, 118, 95, 23);//위치
		panel.add(rdbtnNewRadioButton);


		btnNewButton = new JButton("로그인");
		btnNewButton.setBounds(32, 178, 195, 23);//로그인버튼 위치
		panel.add(btnNewButton);
		
		btnIdPwFind = new JButton("아이디/비밀번호 찾기");
		btnIdPwFind.setBounds(32, 208, 195, 23);//아이디 비밀번호찾기 위치
		panel.add(btnIdPwFind);

		btnJoin = new JButton("회원가입");
		btnJoin.setBounds(32, 246, 195, 23);//회원가입 위치
		panel.add(btnJoin);

		


	}

}
