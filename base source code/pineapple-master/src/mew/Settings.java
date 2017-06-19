package mew;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
//------------------------------------------------------------------//
// ActionListener 인터페이스 상속
public class Settings extends JFrame implements ActionListener, ItemListener{
//------------------------------------------------------------------//
	private JPanel contentPane;
	private JLabel name, id, nickName, pw;
	private JLabel userName, userId, userNickName;
	private JPasswordField userPw;
	private JTextField row;
	private JLabel letterSize;
	private Choice letterSizeChange;
	private JLabel backgroundColor;
	private JLabel fontSet;
	private JButton backgroundYellowColor, backgroundGreenColor, backgroundSkyColor;
	private JButton fontBold, fontItalic, fontPlain;
	private JButton passwordChange, setOn, reset, memberLeave;
 //------------------------------------------------------------------//
 // 변경된 설정값을 전달할 때 필요한 참조 변수 tab
	private textAndBackground tab;
	private SixClient client;
 //------------------------------------------------------------------//

 //------------------------------------------------------------------//
 // Settings의 실행 내용은 같으나 생성자에서 tab값을 받아서 저장하고 set메소드를 실행하도록 틀만 변경
	public Settings(textAndBackground tab, SixClient client){         
		this.tab = tab;
		this.client = client;
		set();
	}


   //------------------------------------------------------------------//
	public void set()   // main메소드를 set으로 변경해서 RestRoomUI와 연결
   //------------------------------------------------------------------//
	{
      // TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					JFrame.setDefaultLookAndFeelDecorated(true);   // 화면 뷰 변경
					settingUI();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
      
	}
   
	public void settingUI(){
		setSize(420, 390);   // 바운더리 크기 조절
		contentPane = new JPanel();      // 설정 패널 생성
		setContentPane(contentPane);
		contentPane.setLayout(null);
	      
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 410, 380);   // 바운더리 크기 조절
		contentPane.add(panel);            // 설정 패널에 추가
		panel.setLayout(null);
	      
		name = new JLabel("이름");      // 이름 레이블
		name.setBounds(30, 20, 60, 20);   // 바운더리 크기 조절
		panel.add(name);         // panel에 추가
	      
		userName = new JLabel(); // 실제 이름이 들어가는 레이블
		userName.setText(client.getUser().getName());      // 레이블에 스트링 출력
		userName.setBounds(110, 20, 270, 20);   // 바운더리 크기 조절
		userName.setForeground(Color.BLUE);
		panel.add(userName);
		
		id = new JLabel("아이디");      // 이름 레이블
		id.setBounds(30, 50, 60, 20);   // 바운더리 크기 조절
		panel.add(id);         // panel에 추가
		
		userId = new JLabel(); // 실제 이름이 들어가는 레이블
		userId.setText(client.getUser().getId());      // 레이블에 스트링 출력
		userId.setBounds(110, 50, 270, 20);   // 바운더리 크기 조절
		userId.setForeground(Color.BLUE);
		panel.add(userId);
		
		nickName = new JLabel("닉네임");      // 닉네임 레이블
		nickName.setBounds(30, 80, 60, 20);   // 바운더리 크기 조절
		panel.add(nickName);         // panel에 추가
		
		userNickName = new JLabel();      // 실제 닉네임이 들어가는 레이블
		userNickName.setText(client.getUser().getNickName());      // 레이블에 스트링 출력
		userNickName.setBounds(110, 80, 270, 20);   // 바운더리 크기 조절
		userNickName.setForeground(Color.BLUE);
		panel.add(userNickName);         // panel에 추가
		
		pw = new JLabel("비밀번호");      // 이름 레이블
		pw.setBounds(30, 110, 60, 20);   // 바운더리 크기 조절
		panel.add(pw);         // panel에 추가
		
		row = new JTextField();		// 경계선
		row.setBounds(10, 142, 380, 2);
		row.setEditable(false);
		panel.add(row);
		
		userPw = new JPasswordField();
		userPw.setBounds(110, 110, 140, 20);
		panel.add(userPw);
		
		passwordChange =  new JButton("비밀번호 변경");   // 비밀번호 변경 버튼
		passwordChange.setBounds(260, 110, 120, 20);   // 바운더리 크기 조절
		passwordChange.addActionListener(this);
		panel.add(passwordChange);      // panel에 추가
		
		letterSize = new JLabel();      // 글자크기 레이블
		letterSize.setText("글자 크기");   // 레이블에 스트링 출력
		letterSize.setBounds(30, 160, 70, 30);   // 바운더리 크기 조절
		panel.add(letterSize);         // panel에 추가
		
		letterSizeChange =  new Choice();   // 글자크기를 선택할 수 있는 창
		letterSizeChange.add("5");      // 크기 종류 추가
		letterSizeChange.add("10");
		letterSizeChange.add("15");
		letterSizeChange.add("20");
		letterSizeChange.add("25");
		letterSizeChange.add("30");
		letterSizeChange.addItemListener(this);
		letterSizeChange.setBounds(110, 160, 50, 30);   // 바운더리 크기 조절
		panel.add(letterSizeChange);   // panel에 추가
		
		backgroundColor = new JLabel();      // 배경색 레이블
		backgroundColor.setText("배경색");      // 레이블에 스트링 출력
		backgroundColor.setBounds(30, 190, 70, 30);   // 바운더리 크기 조절
		panel.add(backgroundColor);      // panel에 추가
		
		backgroundYellowColor =  new JButton("Yellow");      // 배경색을 빨강으로 변경하는 버튼
		backgroundYellowColor.setBackground(new Color(255, 255, 153));
		backgroundYellowColor.setBounds(110, 190, 80, 30);   // 바운더리 크기 조절
		backgroundYellowColor.addActionListener(this);
		panel.add(backgroundYellowColor);         // panel에 추가
		
		backgroundGreenColor =  new JButton("Green");   // 배경색을 초록색으로 변경하는 버튼
		backgroundGreenColor.setBackground(new Color(153, 255, 153));
		backgroundGreenColor.setBounds(205, 190, 80, 30);   // 바운더리 크기 조절
		backgroundGreenColor.addActionListener(this);
		panel.add(backgroundGreenColor);         // panel에 추가
		
		backgroundSkyColor =  new JButton("Blue");   // 배경색을 파란색으로 변경하는 버튼
		backgroundSkyColor.setBackground(new Color(224, 255, 255));
		backgroundSkyColor.setBounds(300, 190, 80, 30);   // 바운더리 크기 조절
		backgroundSkyColor.addActionListener(this);
		panel.add(backgroundSkyColor);         // panel에 추가
		
		fontSet = new JLabel();      // 글자색 레이블
		fontSet.setText("폰트");      // 레이블에 스트링 출력
		fontSet.setBounds(30, 230, 70, 30);   // 바운더리 크기 조절
		panel.add(fontSet);      // panel에 추가
		
		fontBold = new JButton("BOLD");      // BOLD 폰트로 변경하는 버튼
		fontBold.setBounds(110, 230, 80, 30);   // 바운더리 크기 조절
		fontBold.addActionListener(this);      
		panel.add(fontBold);      // panel에 추가
		
		fontItalic = new JButton("ITALIC");   // ITALIC 폰트로 변경하는 버튼
		fontItalic.setBounds(205, 230, 80, 30);   // 바운더리 크기 조절
		fontItalic.addActionListener(this);
		panel.add(fontItalic);      // panel에 추가
		
		fontPlain = new JButton("PLAIN");      // PLAIN 폰트로 변경하는 버튼
		fontPlain.setBounds(300, 230, 80, 30);   // 바운더리 크기 조절
		fontPlain.addActionListener(this);
		panel.add(fontPlain);      // panel에 추가
		
		setOn = new JButton("적용하기");	// 변경 내용 적용 버튼
		setOn.addActionListener(this);
		setOn.setBounds(30, 290, 165, 30);		
		panel.add(setOn);
		
		reset = new JButton("초기화");		// 초기화 버튼
		reset.addActionListener(this);
		reset.setBounds(215, 290, 165, 30);
		panel.add(reset);
		
		memberLeave =  new JButton("회원 탈퇴");      // 회원탈퇴 버튼
		memberLeave.setBounds(260, 20, 120, 30);   // 바운더리 크기 조절
		memberLeave.addActionListener(this);
		memberLeave.setBackground(Color.RED);
		panel.add(memberLeave);         // panel에 추가
		
		this.setTitle("설정");            // 프레임 제목설정
		this.setVisible(true);
	}	
   
	public void actionPerformed(ActionEvent e) {		// 배경색과 폰트 선택 시 값을 set
		// TODO Auto-generated method stub
		if(e.getSource() == backgroundYellowColor)
		{
			tab.setBackColor(new Color(255, 255, 153));
			backgroundYellowColor.setEnabled(false);	// 나머지 enable시키고 이 버튼 disable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
		}
		
		else if(e.getSource() == backgroundGreenColor)
		{
			tab.setBackColor(new Color(153, 255, 153));
			backgroundYellowColor.setEnabled(true);		// 나머지 enable시키고 이 버튼 disable
			backgroundGreenColor.setEnabled(false);
			backgroundSkyColor.setEnabled(true);
		}
		
		else if(e.getSource() == backgroundSkyColor)
		{
			tab.setBackColor(new Color(224, 255, 255));
			backgroundYellowColor.setEnabled(true);		// 나머지 enable시키고 이 버튼 disable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(false);
		}
		
		else if(e.getSource() == fontBold)
		{
			tab.setFont(Font.BOLD);
			fontBold.setEnabled(false);		// 나머지 enable시키고 이 버튼 disable
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == fontItalic)
		{
			tab.setFont(Font.ITALIC);
			fontBold.setEnabled(true);		// 나머지 enable시키고 이 버튼 disable
			fontItalic.setEnabled(false);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == fontPlain)
		{	
			tab.setFont(Font.PLAIN);
			fontBold.setEnabled(true);		// 나머지 enable시키고 이 버튼 disable
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(false);
		}
		
		else if(e.getSource() == passwordChange)	// 비밀번호 변경 버튼 누를 시
		{
			JOptionPane.showMessageDialog(this, "비밀번호가 변경 되었습니다.", "변경 완료", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if(e.getSource() == memberLeave)
		{
			MemberLeaveUI ml = new MemberLeaveUI(client);
			ml.setLocationRelativeTo(this.memberLeave);
		}
		
		else if(e.getSource() == setOn)
		{
			System.out.println("설정 적용");
			JOptionPane.showMessageDialog(this, "적용 되었습니다.", "설정 완료", JOptionPane.INFORMATION_MESSAGE);
			backgroundYellowColor.setEnabled(true);		// 모든 버튼 enable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
			fontBold.setEnabled(true);
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
		}
		
		else if(e.getSource() == reset)
		{
			System.out.println("설정 초기화");
			JOptionPane.showMessageDialog(this, "모든 설정이 초기화 되었습니다.", "설정 완료", JOptionPane.INFORMATION_MESSAGE);
			backgroundYellowColor.setEnabled(true);		// 모든 버튼 enable
			backgroundGreenColor.setEnabled(true);
			backgroundSkyColor.setEnabled(true);
			fontBold.setEnabled(true);
			fontItalic.setEnabled(true);
			fontPlain.setEnabled(true);
			
			tab.setBackColor(new Color(224, 255, 255));		// 원래 폰트와 색으로 돌려줌
			tab.setFont(Font.PLAIN);
			tab.setSize(12);
		}
	}
   
	public void itemStateChanged(ItemEvent e){	// 글자 크기 선택 시 값을 set
		Choice choice = (Choice)e.getSource();
		if(choice.getSelectedItem() == "5")
		{
			tab.setSize(5);
		}	

		else if(choice.getSelectedItem() == "10")
		{
			tab.setSize(10);
		}
	   
		else if(choice.getSelectedItem() == "15")
		{
			tab.setSize(15);
		}
	   
		else if(choice.getSelectedItem() == "20")
		{	
			tab.setSize(20);
		}
	   
		else if(choice.getSelectedItem() == "25")
		{	
			tab.setSize(25);
		}
	   
		else if(choice.getSelectedItem() == "30")
		{
			tab.setSize(30);
		}

	}
}