package mew;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.net.InetAddress;
import java.util.*;

public class OmokGame {
	protected OmokSocket mySocket;
	OmokGui gameGui;
	protected boolean end;
	protected int[][] board; //0 non, -1 white, 1 black
	protected int ver,hor;
	protected int turn;
	public boolean myTurn;
	public boolean solo;
	public boolean start;
	private static final String[] playMode = {"혼자 하기", "둘이서 하기"};
	private static final String[] joinMode = {"게임 생성(선공)", "게임 참여(후공)"};

	Random random = new Random();
	
	public OmokGame(int lineNum)
	{//for now with Pane
		start = false;
		dataInit(lineNum);
		gameGui = new OmokGui(lineNum, this);
		baseSettingsWithPane();
		start = true;
		if(!solo && !myTurn)
		{
			otherPut();
		}
	}

	public OmokGame(int lineNum, boolean solo)
	{//for future solo mode
		start = false;
		dataInit(lineNum);
		this.solo = solo;
		gameGui = new OmokGui(lineNum, this);
		start = true;
	}

	public OmokGame(int lineNum, boolean host, int portNum, String ipNum)
	{//for future multimode
		start = false;
		dataInit(lineNum);
		socketInit(host, portNum, ipNum);
		gameGui = new OmokGui(lineNum, this);
		start = true;
		if(!solo && !myTurn)	
		{
			otherPut();
		}
	}

	private void baseSettingsWithPane()
	{
		String input = (String) JOptionPane.showInputDialog(null, "혼자 하기 / 둘이서 하기", "플레이 모드 선택", JOptionPane.QUESTION_MESSAGE, null, playMode, playMode[0]);
		if(input.equals("혼자 하기"))
		{
			solo = true;
		}
		if(!solo)
		{
			socketInitWithPane();
		}
	}

	private void socketInitWithPane()
	{
		InetAddress addr = null;
		mySocket = new OmokSocket();
		try
		{
			addr = InetAddress.getLocalHost();	// +IP를 직접 받아올 수 있도록 바꿈
		}
		catch(IOException e)
		{
			e.getMessage();
		}
		String input = (String) JOptionPane.showInputDialog(null, "게임 생성 / 게임 참여", "참여 모드", JOptionPane.QUESTION_MESSAGE, null, joinMode, joinMode[0]);
		if(input.equals("게임 생성(선공)"))	// 게임을 생성한 쪽이 서버가 됨
		{
			String portNum = JOptionPane.showInputDialog("Enter portNum");
			mySocket.beServer(Integer.parseInt(portNum));
			myTurn = true;
		}
		else	// 게임을 참가한 쪽이 클라이언트가 됨
		{
			String ipNum = addr.getHostAddress().toString();
			String portNum = JOptionPane.showInputDialog("Enter portNum");
			mySocket.beClient(ipNum, Integer.parseInt(portNum));
			myTurn = false;
    	}
	}

	private void socketInit(boolean host, int portNum, String ipNum)
	{
		mySocket = new OmokSocket();
		if(host)
		{
			mySocket.beServer(portNum);
			myTurn = true;
		}
		else
		{
			mySocket.beClient(ipNum, portNum);
			myTurn = false;
		}
	}

	private void dataInit(int lineNum)
	{
		end = false;
		turn = 1;
		board = new int[lineNum+2][lineNum+2];
		for(int i = 1; i <= lineNum; i++)
		{
			for(int j = 1; j<= lineNum; j++)
				board[i][j] = 0;
		}
		for (int i = 0; i < lineNum; i++)
		{
			board[0][i] = 4;
			board[lineNum + 1][i] = 4;
		}
		for (int i = 0; i <lineNum; i++)
		{
			board[i][0] = 3;
			board[i][lineNum + 1] = 3;
		}
	}

	public void put(int x, int y)
	{
		if(board[x][y] != 0) return;
		board[x][y] = turn;
		turn *= -1;
		myTurn = false;
		gameGui.put(x, y);
		if(!solo)
		{
			try
			{
				mySocket.sender.writeInt(x);
				mySocket.sender.writeInt(y);
			}
			catch(IOException ioex)
			{
				System.out.println(ioex);
			}
		}
		if(WOL(x,y))
		{
		    end = true;
		    gameGui.gameEnd();
		    gameGui.dispose();	// +게임 종료 팝업 클릭 시 창도 꺼짐
		}
		if(!solo)
		{
			otherPut();
		}
	}

	public void otherPut()
	{
		int x = 0, y = 0;
		try
		{
			x = mySocket.receiver.readInt();
			y = mySocket.receiver.readInt();
		}
		catch(IOException ioex)
		{
			System.out.println(ioex);
		}
		board[x][y] = turn;
		turn *= -1;
		myTurn = true;
		gameGui.put(x, y);
		if(WOL(x,y))
		{
			end = true;
			gameGui.gameEnd();
		    gameGui.dispose();	// +게임 종료 팝업 클릭 시 창도 꺼짐
		}
	}
	
	private boolean WOL(int x, int y)
	{
		int[] ex = new int[2];
		int[] ey = new int[2];
		int i, a, b, k;

		for (i = 0; i< 2; i++)
		{
			ex[i] = x;
			ey[i] = y;
		}
		k = 1;

		for (b = -1; b < 2; b++)
		{
			for (a = 0; a < 2; a++)
			{
				for (i = 1; i < 5; i++)
				{
					if (board[ex[a] + (-1)*k][ey[a] + b*k] == turn*(-1))
					{
						ex[a] = ex[a] + (-1)*k;
						ey[a] = ey[a] + b*k;
					}
					else break;			
				}
				k = k*(-1);
			}
			if (ex[1] - ex[0] + 1 >= 5) return true;
			for (i = 0; i< 2; i++)
			{
				ex[i] = x;
				ey[i] = y;
			}
			k = 1;
		}

		for (i = 0; i< 2; i++)
		{
			ex[i] = x;
			ey[i] = y;
		}
		k = 1;
		k = -1;
		for (a = 0; a < 2; a++)
		{
			for (i = 1; i < 5; i++)
			{
				if (board[ex[a]][ey[a] + k] == turn*(-1))
				{
					ex[a] = ex[a];
					ey[a] = ey[a] + k;
				}
				else break;
			}
			k = k*-1;
		}
		if (ey[1] - ey[0] + 1 >= 5)
			return true;
		return false;
	}

	public int whoIsTurnIsIt()
	{
		return turn;
	}
	
	public boolean isItend()
	{
		return end;
	}
}
