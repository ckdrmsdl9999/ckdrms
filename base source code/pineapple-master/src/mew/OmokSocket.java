package mew;

import java.net.Socket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class OmokSocket {
	private ServerSocket myServerSocket;
	private Socket mySocket;
	public DataOutputStream sender;
	public DataInputStream receiver;
	private int portNum;
	private String serverIP;

	public OmokSocket() {
		InetAddress addr = null;
		try
		{
			addr = InetAddress.getLocalHost();	// +IP를 직접 받아올 수 있도록 바꿈
			serverIP = addr.getHostAddress().toString();
		}
		catch(IOException e)
		{
			e.getMessage();
		}
	}

	public void beServer(int portNum){
		try{
			myServerSocket = new ServerSocket(portNum);
			mySocket = myServerSocket.accept();
			sender = new DataOutputStream(mySocket.getOutputStream());
			receiver = new DataInputStream(mySocket.getInputStream());
		}
		catch(IOException ioex){
			System.out.println(ioex);
		}
	}

	public void beClient(int portNum){
		try{
			mySocket = new Socket(serverIP, portNum);
			sender = new DataOutputStream(mySocket.getOutputStream());
			receiver = new DataInputStream(mySocket.getInputStream());
		}
		catch(IOException ioex){
			System.out.println(ioex);
		}
	}
}
