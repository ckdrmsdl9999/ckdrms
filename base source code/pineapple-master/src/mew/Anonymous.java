package mew;

import java.awt.*;
import javax.swing.*;

public class Anonymous extends JFrame {
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
					JFrame.setDefaultLookAndFeelDecorated(true);	// 화면 뷰 변경
					Anonymous frame = new Anonymous();
					frame.setVisible(true);
					frame.setTitle("익명1(익명)");		// JFrame 제목 설정
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public Anonymous()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 700);	// 위치 및 크기 지정
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

	
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 400, 680);
		contentPane.add(panel);
		panel.setLayout(null);
		
		
		Font font = new Font("arian", Font.ITALIC, 18);
		roomName = new JTextField();	// 방 이름 부분
		roomName.setEnabled(false);		// 사용안함
		roomName.setEditable(false);	// 변경 불가
		roomName.setDisabledTextColor(Color.black);		// 비활성된 상태의 글씨 색 변경
		roomName.setText("익명 대화방");
		roomName.setHorizontalAlignment(JTextField.CENTER);	// 텍스트필드 가운데 정렬
		roomName.setBounds(10, 10, 100, 30);
		panel.add(roomName);
		roomName.setColumns(10);
		
		
		peopleNum = new JTextField();	// 채팅방 인원 수
		peopleNum.setEnabled(false);
		peopleNum.setEditable(false);
		peopleNum.setDisabledTextColor(Color.black);
		peopleNum.setText("4명 참여 중 | 까치, 참새, 원숭이, 송어");
		peopleNum.setHorizontalAlignment(JTextField.CENTER);
		peopleNum.setBounds(10, 40, 390, 30);
		panel.add(peopleNum);
		peopleNum.setColumns(10);
		
		
		
		ImageIcon imgBattle = new ImageIcon("g:\\btnBattle.jpg");	// 이미지 삽입
		btnBattle = new JButton(imgBattle);	// 1대1 대전 게임 버튼(오목)
		btnBattle.setToolTipText("오목 게임 신청");	// 마우스를 갖다 대면 글씨가 보이게끔 해줌
		btnBattle.setBounds(310, 10, 30, 30);
		panel.add(btnBattle);
		
		
		ImageIcon imgLeave = new ImageIcon("g:\\btnLeave.jpg");	// 이미지 삽입
		btnLeave = new JButton(imgLeave);
		btnLeave.setToolTipText("방에서 나가기");
		btnLeave.setBounds(340, 10, 30, 30);
		panel.add(btnLeave);

		
		ImageIcon imgSettings = new ImageIcon("g:\\btnSettings.jpg");	// 이미지 삽입
		btnSettings = new JButton(imgSettings);
		btnSettings.setToolTipText("설정");
		btnSettings.setBounds(370, 10, 30, 30);
		panel.add(btnSettings);

		
		chattingSpace = new JTextArea();	// 긴 텍스트 영역 생성
		chattingSpace.setLineWrap(true);	// 한줄이 길어지면 자동개행
		chattingSpace.requestFocus();
		chattingSpace.setEditable(false);
		chattingSpace.setFont(font);
		chattingSpace.setText("까치 : 안녕하세요\n                                    참새 : 네 안녕하세요\n까치 : 안녕히계세요                                                                                원숭이 : 안녕히가세요~\n                                   송어 : 안녕히가세요" );
		JScrollPane scroll1 = new JScrollPane(chattingSpace);	// 수직 스크롤바
		scroll1.setBounds(10, 80, 390, 450);
		panel.add(scroll1);
		
		
		writeSpace = new JTextArea();	// 긴 텍스트 영역 생성
		writeSpace.setLineWrap(true);	// 한줄이 길어지면 자동개행
		writeSpace.requestFocus();
		writeSpace.setText("입력 : ");
		writeSpace.selectAll();
		writeSpace.setSelectedTextColor(Color.gray);
		JScrollPane scroll2 = new JScrollPane(writeSpace);	// 수직 스크롤바
		scroll2.setBounds(10, 530, 360, 100);
		panel.add(scroll2);
		
		
		ImageIcon imgSend = new ImageIcon("g:\\btnSend.jpg");	// 보내기 아이콘 삽입
		btnSend = new JButton(imgSend);		// 보내기 버튼
		btnSend.setToolTipText("보내기");
		btnSend.setBounds(370, 600, 30, 30);
		panel.add(btnSend);
	}
}