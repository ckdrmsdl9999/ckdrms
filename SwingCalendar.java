package mew;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
 
public class SwingCalendar extends JFrame {
	DefaultTableModel model;
	Calendar cal = new GregorianCalendar();
	JLabel label;
 
	SwingCalendar()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Swing Calandar");
		this.setSize(300,200);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		JButton b1 = new JButton("←");	// 이전 달로 넘기는 버튼
   
		b1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				cal.add(Calendar.MONTH, -1);
				updateMonth();
			}
		});
		JButton b2 = new JButton("→");	// 다음 달로 넘기는 버튼
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				cal.add(Calendar.MONTH, +1);
				updateMonth();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(b1,BorderLayout.WEST);	// 이전 달 버튼은 왼쪽에
		panel.add(label,BorderLayout.CENTER);	// 몇년 몇월은 가운데에
		panel.add(b2,BorderLayout.EAST);	// 다음 달 버튼은 오른쪽에
		
		String [] columns = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
		model = new DefaultTableModel(null,columns);	// 테이블 기본 구성
		JTable table = new JTable(model);
		JScrollPane pane = new JScrollPane(table);
		this.add(panel,BorderLayout.NORTH);
		this.add(pane,BorderLayout.CENTER);
		this.updateMonth();
	}
	
	void updateMonth()
	{
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		int year = cal.get(Calendar.YEAR);
		label.setText(month + " " + year);	// 몇년몇월인지 알려주는 부분
		
		int startDay = cal.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		model.setRowCount(0);
		model.setRowCount(weeks);
		
		int i = startDay-1;
		for(int day=1;day<=numberOfDays;day++)
		{
			model.setValueAt(day, i/7 , i%7 );	// 일주일 단위로 날짜를 순서대로 적어줌
			i++;
		}
	}

	public static void main(String[] arguments)
	{
		JFrame.setDefaultLookAndFeelDecorated(true);	// 화면 뷰 변경
		SwingCalendar sc = new SwingCalendar();
	}
}