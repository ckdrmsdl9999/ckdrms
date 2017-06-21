package mew;
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

public class join extends JFrame {
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
					join frame = new join();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public join() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 335, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Image img=new ImageIcon().getImage();//배경그림사용하기위해 선언
		final ImageIcon icon11=new ImageIcon("g:\\사진1.png");
		//파일경로
		JPanel panel = new JPanel(){//배경화면 받기위한 내용
		    public void paintComponent(Graphics g) {
                g.drawImage(icon11.getImage(), 0, 0, null);
                setOpaque(false);
                super.paintComponent(g);
               } 
	
		};
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(0, 0, 335, 565);//외각부분 크기
		//panel.setBackground(Color.green);
		contentPane.add(panel);
		panel.setLayout(null);	

		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setFont(new Font("굴림체", Font.BOLD, 18));
		textField.setText("회 원 가 입");
		textField.setBounds(107, 21, 118, 31);//맨위 상단 회원가입부분
		panel.add(textField);
		textField.setColumns(10);
		
		txtId = new JTextField();
		txtId.setEnabled(false);
		txtId.setEditable(false);
		txtId.setText(" 아이디 ");//아이디부분
		txtId.setBounds(51, 89, 55, 21);//아이디좌표
		panel.add(txtId);

		txtId.setColumns(10);	
		txtId_1 = new JTextField();//아이디텍스트선언
		txtId_1.setBounds(153, 89, 136, 21);//아이디텍스트부분
		panel.add(txtId_1);
		txtId_1.setColumns(10);	
		txtId_1.setText("아이디를 입력");//아이디입력부분에 입력전 아이디를입력 써놓는부분
		
		JButton btnIDCheck = new JButton("ID 중복확인");
		btnIDCheck.setBounds(153, 120, 129, 23);//ID중복확인 위치
		panel.add(btnIDCheck);		

		txtPw = new JTextField();//비밀번호 텍스트선언
		txtPw.setText("비밀번호 ");
		txtPw.setEnabled(false);
		txtPw.setEditable(false);
		txtPw.setColumns(10);
		txtPw.setBounds(51, 153, 65, 21);//비밀번호 텍스트 위치
		panel.add(txtPw);

		

		txtPW = new JTextField();
		txtPW.setColumns(10);
		txtPW.setBounds(153, 153, 116, 21);//비밀번호 입력하는칸위치
		panel.add(txtPW);
		txtPW.setText("비밀번호 입력");
		

		txtRpw = new JTextField();
		txtRpw.setText("비밀번호 확인 ");//비밀번호확인텍스트
		txtRpw.setEnabled(false);
		txtRpw.setEditable(false);
		txtRpw.setColumns(15);
		txtRpw.setBounds(51, 202, 79, 21);
		panel.add(txtRpw);
		
		
	
		txtRPW = new JTextField();
		txtRPW.setColumns(10);
		txtRPW.setBounds(153, 202, 116, 21);//빔리번호 재입력위치
		panel.add(txtRPW);
		txtRPW.setText("비밀번호 재입력");//비밀번호 재입력 
		
		txtNick = new JTextField();
		txtNick.setText(" 닉네임  ");//닉네임 선언
		txtNick.setEnabled(false);
		txtNick.setEditable(false);
		txtNick.setColumns(10);
		txtNick.setBounds(51, 236, 50, 21);//닉네임 텍스트위치
		panel.add(txtNick);

		txtNick_1 = new JTextField();
		txtNick_1.setColumns(10);
		txtNick_1.setBounds(153, 236, 116, 21);//닉네임 입력하는 하얀칸위치
		panel.add(txtNick_1);
		txtNick_1.setText("닉네임 입력");//입력전 하얀칸에 미리 써놓는글

		JButton btnNickCheck = new JButton("닉네임 중복확인");//닉네임 중복확인 버튼
		btnNickCheck.setBounds(153, 268, 130, 23);
		panel.add(btnNickCheck);

		txtName = new JTextField();
		txtName.setText(" 이름 ");//이름텍스트
		txtName.setEnabled(false);
		txtName.setEditable(false);
		txtName.setColumns(10);
		txtName.setBounds(51, 303, 47, 21);
		panel.add(txtName);

		txtName_1 = new JTextField();
		txtName_1.setColumns(55);
		txtName_1.setBounds(153, 303, 116, 21);//이름 입력하는곳 위치
		panel.add(txtName_1);
		txtName_1.setText("이름 입력");
		

		JButton btnJoin = new JButton("가입");//가입버튼
		btnJoin.setBounds(71, 465, 73, 23);
		panel.add(btnJoin);

		

		JButton btnCancel = new JButton("취소");
		btnCancel.setBounds(186, 465, 73, 23);//취소버튼위치
		panel.add(btnCancel);

	}

}

