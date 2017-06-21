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
					JFrame.setDefaultLookAndFeelDecorated(true);	// 화면 뷰 변경
					ChattingRoom frame = new ChattingRoom();
					frame.setVisible(true);
					frame.setTitle("My room1");		// JFrame 제목 설정
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
		roomName.setText("일반 대화방");
		roomName.setHorizontalAlignment(JTextField.CENTER);	// 텍스트필드 가운데 정렬
		roomName.setBounds(10, 10, 100, 30);
		panel.add(roomName);
		roomName.setColumns(10);
		
		
		peopleNum = new JTextField();	// 대화방 참여 인원 수
		peopleNum.setEnabled(false);	// 사용 불가
		peopleNum.setEditable(false);	// 수정 불가
		peopleNum.setDisabledTextColor(Color.black);
		peopleNum.setText("4명 참여 중 | 윤창근, 허문용, 박지용, 이해준");
		peopleNum.setHorizontalAlignment(JTextField.CENTER);
		peopleNum.setBounds(10, 40, 390, 30);
		panel.add(peopleNum);
		peopleNum.setColumns(10);
		

		ImageIcon imgBattle = new ImageIcon("g:\\btnBattle.jpg");	// 오목 아이콘 삽입
		btnBattle = new JButton(imgBattle);	// 1대1 대전 게임(오목)버튼
		btnBattle.setToolTipText("오목 게임 신청");	// 마우스를 갖다 대면 글씨가 보이게끔 설정
		btnBattle.setBounds(310, 10, 30, 30);
		panel.add(btnBattle);
		
		
		ImageIcon imgLeave = new ImageIcon("g:\\btnLeave.jpg");	// 나가기 아이콘 삽입
		btnLeave = new JButton(imgLeave);	// 방 나가기 버튼
		btnLeave.setToolTipText("방에서 나가기");
		btnLeave.setBounds(340, 10, 30, 30);
		panel.add(btnLeave);

		
		ImageIcon imgSettings = new ImageIcon("g:\\btnSettings.jpg");	// 설정 아이콘 삽입
		btnSettings = new JButton(imgSettings);	// 설정 버튼
		btnSettings.setToolTipText("설정");
		btnSettings.setBounds(370, 10, 30, 30);
		panel.add(btnSettings);

		
		chattingSpace = new JTextArea();	// 대화 영역
		chattingSpace.setLineWrap(true);	// 한줄이 길어지면 자동개행
		chattingSpace.requestFocus();
		chattingSpace.setEditable(false);
		chattingSpace.setFont(font);
		chattingSpace.setText("박지용 : 안녕하세요\n                                  윤창근 : 네 안녕하세요\n박지용 : 안녕히계세요                                                                           이해준 : 안녕히가세요~\n                                 허문용 : 안녕히가세요" );
		JScrollPane scroll1 = new JScrollPane(chattingSpace);	// 수직 스크롤바
		scroll1.setBounds(10, 80, 390, 450);
		panel.add(scroll1);
		
		
		writeSpace = new JTextArea();	// 메시지 작성 보드
		writeSpace.setLineWrap(true);	// 한줄이 길어지면 자동개행
		writeSpace.requestFocus();	// 디폴트 포커스 지정
		writeSpace.setText("입력 : ");	
		writeSpace.selectAll();		// 텍스트가 모두 선택되게 함
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