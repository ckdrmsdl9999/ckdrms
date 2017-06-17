package Chat;

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
   private JLabel userName;
   private JLabel letterSize;
   private Choice letterSizeChange;
   private JLabel backgroundColor;
   private JButton backgroundYellowColor;
   private JButton backgroundGreenColor;
   private JButton backgroundBlueColor;
   private JLabel fontSet;
   private JButton fontBold;
   private JButton fontItalic;
   private JButton fontPlain;
   private JButton passwordChange;
   private JButton memberLeave;
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

	   setBounds(100, 100, 420, 280);   // 바운더리 크기 조절
	      contentPane = new JPanel();      // 설정 패널 생성
	      setContentPane(contentPane);
	      contentPane.setLayout(null);
	      
	      JPanel panel = new JPanel();
	      panel.setBounds(0, 0, 410, 280);   // 바운더리 크기 조절
	      contentPane.add(panel);            // 설정 패널에 추가
	      panel.setLayout(null);
	      
	      userName = new JLabel();      // 닉네임 레이블
	      userName.setText("이해준(LeeHaeJun111)");      // 레이블에 스트링 출력
	      userName.setBounds(30, 20, 300, 30);   // 바운더리 크기 조절
	      panel.add(userName);         // panel에 추가
	      
	      letterSize = new JLabel();      // 글자크기 레이블
	      letterSize.setText("글자크기");   // 레이블에 스트링 출력
	      letterSize.setBounds(30, 60, 70, 30);   // 바운더리 크기 조절
	      panel.add(letterSize);         // panel에 추가
	         
	      letterSizeChange =  new Choice();   // 글자크기를 선택할 수 있는 창
	      letterSizeChange.add("5");      // 크기 종류 추가
	      letterSizeChange.add("10");
	      letterSizeChange.add("15");
	      letterSizeChange.add("20");
	      letterSizeChange.add("25");
	      letterSizeChange.add("30");
	      letterSizeChange.addItemListener(this);
	      letterSizeChange.setBounds(110, 60, 50, 30);   // 바운더리 크기 조절
	      panel.add(letterSizeChange);   // panel에 추가
	      
	      backgroundColor = new JLabel();      // 배경색 레이블
	      backgroundColor.setText("배경색");      // 레이블에 스트링 출력
	      backgroundColor.setBounds(30, 100, 70, 30);   // 바운더리 크기 조절
	      panel.add(backgroundColor);      // panel에 추가
	      
	      backgroundYellowColor =  new JButton("Yellow");      // 배경색을 빨강으로 변경하는 버튼
	      backgroundYellowColor.setBackground(new Color(255, 255, 153));
	      backgroundYellowColor.setBounds(110, 100, 80, 30);   // 바운더리 크기 조절
	      backgroundYellowColor.addActionListener(this);
	      panel.add(backgroundYellowColor);         // panel에 추가
	      
	      backgroundGreenColor =  new JButton("Green");   // 배경색을 초록색으로 변경하는 버튼
	      backgroundGreenColor.setBackground(new Color(153, 255, 153));
	      backgroundGreenColor.setBounds(205, 100, 80, 30);   // 바운더리 크기 조절
	      backgroundGreenColor.addActionListener(this);
	      panel.add(backgroundGreenColor);         // panel에 추가
	      
	      backgroundBlueColor =  new JButton("Blue");   // 배경색을 파란색으로 변경하는 버튼
	      backgroundBlueColor.setBackground(new Color(224, 255, 255));
	      backgroundBlueColor.setBounds(300, 100, 80, 30);   // 바운더리 크기 조절
	      backgroundBlueColor.addActionListener(this);
	      panel.add(backgroundBlueColor);         // panel에 추가
	      
	      fontSet = new JLabel();      // 글자색 레이블
	      fontSet.setText("폰트");      // 레이블에 스트링 출력
	      fontSet.setBounds(30, 140, 70, 30);   // 바운더리 크기 조절
	      panel.add(fontSet);      // panel에 추가
	      
	      fontBold = new JButton("BOLD");      // BOLD 폰트로 변경하는 버튼
	      fontBold.setBounds(110, 140, 80, 30);   // 바운더리 크기 조절
	      fontBold.addActionListener(this);      
	      panel.add(fontBold);      // panel에 추가
	      
	      fontItalic = new JButton("ITALIC");   // ITALIC 폰트로 변경하는 버튼
	      fontItalic.setBounds(205, 140, 80, 30);   // 바운더리 크기 조절
	      fontItalic.addActionListener(this);
	      panel.add(fontItalic);      // panel에 추가
	      
	      fontPlain = new JButton("PLAIN");      // PLAIN 폰트로 변경하는 버튼
	      fontPlain.setBounds(300, 140, 80, 30);   // 바운더리 크기 조절
	      fontPlain.addActionListener(this);
	      panel.add(fontPlain);      // panel에 추가
	      
	      passwordChange =  new JButton("비밀번호 변경");   // 비밀번호 변경 버튼
	      passwordChange.setBounds(30, 180, 165, 30);   // 바운더리 크기 조절
	      passwordChange.addActionListener(this);
	      panel.add(passwordChange);      // panel에 추가
	      
	      memberLeave =  new JButton("회원탈퇴");      // 회원탈퇴 버튼
	      memberLeave.setBounds(215, 180, 165, 30);   // 바운더리 크기 조절
	      memberLeave.addActionListener(this);
	      panel.add(memberLeave);         // panel에 추가

	      this.setTitle("설정");            // 프레임 제목설정
	      this.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e) {		// 배경색과 폰트 선택 시 값을 set
      // TODO Auto-generated method stub
      if(e.getSource() == backgroundYellowColor)
      {
         tab.setBackColor(new Color(255, 255, 153));
      }
      
      else if(e.getSource() == backgroundGreenColor)
      {
    	  tab.setBackColor(new Color(153, 255, 153));
      }
      
      else if(e.getSource() == backgroundBlueColor)
      {
    	  tab.setBackColor(new Color(224, 255, 255));
      }
      
      else if(e.getSource() == fontBold)
      {
    	  tab.setFont(Font.BOLD);
      }
      
      else if(e.getSource() == fontItalic)
      {
    	  tab.setFont(Font.ITALIC);
      }
      
      else if(e.getSource() == fontPlain)
      {
    	  tab.setFont(Font.PLAIN);
      }
      
      else if(e.getSource() == passwordChange)	// 비밀번호 변경 버튼 누를 시
      {
    	  
      }
      
      else if(e.getSource() == memberLeave)
      {
    	  MemberLeaveUI ml = new MemberLeaveUI(client);
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