package Chat;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
//------------------------------------------------------------------//
// ActionListener �������̽� ���
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
 // ����� �������� ������ �� �ʿ��� ���� ���� tab
   private textAndBackground tab;
   private SixClient client;
 //------------------------------------------------------------------//

 //------------------------------------------------------------------//
 // Settings�� ���� ������ ������ �����ڿ��� tab���� �޾Ƽ� �����ϰ� set�޼ҵ带 �����ϵ��� Ʋ�� ����
   public Settings(textAndBackground tab, SixClient client){         
       this.tab = tab;
       this.client = client;
       set();
   }


   //------------------------------------------------------------------//
   public void set()   // main�޼ҵ带 set���� �����ؼ� RestRoomUI�� ����
   //------------------------------------------------------------------//
   {
      // TODO Auto-generated method stub
      EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            try
            {
               JFrame.setDefaultLookAndFeelDecorated(true);   // ȭ�� �� ����
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

	   setBounds(100, 100, 420, 280);   // �ٿ���� ũ�� ����
	      contentPane = new JPanel();      // ���� �г� ����
	      setContentPane(contentPane);
	      contentPane.setLayout(null);
	      
	      JPanel panel = new JPanel();
	      panel.setBounds(0, 0, 410, 280);   // �ٿ���� ũ�� ����
	      contentPane.add(panel);            // ���� �гο� �߰�
	      panel.setLayout(null);
	      
	      userName = new JLabel();      // �г��� ���̺�
	      userName.setText("������(LeeHaeJun111)");      // ���̺� ��Ʈ�� ���
	      userName.setBounds(30, 20, 300, 30);   // �ٿ���� ũ�� ����
	      panel.add(userName);         // panel�� �߰�
	      
	      letterSize = new JLabel();      // ����ũ�� ���̺�
	      letterSize.setText("����ũ��");   // ���̺� ��Ʈ�� ���
	      letterSize.setBounds(30, 60, 70, 30);   // �ٿ���� ũ�� ����
	      panel.add(letterSize);         // panel�� �߰�
	         
	      letterSizeChange =  new Choice();   // ����ũ�⸦ ������ �� �ִ� â
	      letterSizeChange.add("5");      // ũ�� ���� �߰�
	      letterSizeChange.add("10");
	      letterSizeChange.add("15");
	      letterSizeChange.add("20");
	      letterSizeChange.add("25");
	      letterSizeChange.add("30");
	      letterSizeChange.addItemListener(this);
	      letterSizeChange.setBounds(110, 60, 50, 30);   // �ٿ���� ũ�� ����
	      panel.add(letterSizeChange);   // panel�� �߰�
	      
	      backgroundColor = new JLabel();      // ���� ���̺�
	      backgroundColor.setText("����");      // ���̺� ��Ʈ�� ���
	      backgroundColor.setBounds(30, 100, 70, 30);   // �ٿ���� ũ�� ����
	      panel.add(backgroundColor);      // panel�� �߰�
	      
	      backgroundYellowColor =  new JButton("Yellow");      // ������ �������� �����ϴ� ��ư
	      backgroundYellowColor.setBackground(new Color(255, 255, 153));
	      backgroundYellowColor.setBounds(110, 100, 80, 30);   // �ٿ���� ũ�� ����
	      backgroundYellowColor.addActionListener(this);
	      panel.add(backgroundYellowColor);         // panel�� �߰�
	      
	      backgroundGreenColor =  new JButton("Green");   // ������ �ʷϻ����� �����ϴ� ��ư
	      backgroundGreenColor.setBackground(new Color(153, 255, 153));
	      backgroundGreenColor.setBounds(205, 100, 80, 30);   // �ٿ���� ũ�� ����
	      backgroundGreenColor.addActionListener(this);
	      panel.add(backgroundGreenColor);         // panel�� �߰�
	      
	      backgroundBlueColor =  new JButton("Blue");   // ������ �Ķ������� �����ϴ� ��ư
	      backgroundBlueColor.setBackground(new Color(224, 255, 255));
	      backgroundBlueColor.setBounds(300, 100, 80, 30);   // �ٿ���� ũ�� ����
	      backgroundBlueColor.addActionListener(this);
	      panel.add(backgroundBlueColor);         // panel�� �߰�
	      
	      fontSet = new JLabel();      // ���ڻ� ���̺�
	      fontSet.setText("��Ʈ");      // ���̺� ��Ʈ�� ���
	      fontSet.setBounds(30, 140, 70, 30);   // �ٿ���� ũ�� ����
	      panel.add(fontSet);      // panel�� �߰�
	      
	      fontBold = new JButton("BOLD");      // BOLD ��Ʈ�� �����ϴ� ��ư
	      fontBold.setBounds(110, 140, 80, 30);   // �ٿ���� ũ�� ����
	      fontBold.addActionListener(this);      
	      panel.add(fontBold);      // panel�� �߰�
	      
	      fontItalic = new JButton("ITALIC");   // ITALIC ��Ʈ�� �����ϴ� ��ư
	      fontItalic.setBounds(205, 140, 80, 30);   // �ٿ���� ũ�� ����
	      fontItalic.addActionListener(this);
	      panel.add(fontItalic);      // panel�� �߰�
	      
	      fontPlain = new JButton("PLAIN");      // PLAIN ��Ʈ�� �����ϴ� ��ư
	      fontPlain.setBounds(300, 140, 80, 30);   // �ٿ���� ũ�� ����
	      fontPlain.addActionListener(this);
	      panel.add(fontPlain);      // panel�� �߰�
	      
	      passwordChange =  new JButton("��й�ȣ ����");   // ��й�ȣ ���� ��ư
	      passwordChange.setBounds(30, 180, 165, 30);   // �ٿ���� ũ�� ����
	      passwordChange.addActionListener(this);
	      panel.add(passwordChange);      // panel�� �߰�
	      
	      memberLeave =  new JButton("ȸ��Ż��");      // ȸ��Ż�� ��ư
	      memberLeave.setBounds(215, 180, 165, 30);   // �ٿ���� ũ�� ����
	      memberLeave.addActionListener(this);
	      panel.add(memberLeave);         // panel�� �߰�

	      this.setTitle("����");            // ������ ������
	      this.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent e) {		// ������ ��Ʈ ���� �� ���� set
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
      
      else if(e.getSource() == passwordChange)	// ��й�ȣ ���� ��ư ���� ��
      {
    	  
      }
      
      else if(e.getSource() == memberLeave)
      {
    	  MemberLeaveUI ml = new MemberLeaveUI(client);
      }
   }
   
   public void itemStateChanged(ItemEvent e){	// ���� ũ�� ���� �� ���� set
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