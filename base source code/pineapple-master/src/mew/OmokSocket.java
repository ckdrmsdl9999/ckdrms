package omok;

import java.net.Socket;
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
	public DataInputStream reciever;
	private int portNum;
	private  String serverIP;

	public OmokSocket() {

	}

	public void beServer(int portNum){
		try{
			myServerSocket = new ServerSocket(portNum);
			mySocket = myServerSocket.accept();
			sender = new DataOutputStream(mySocket.getOutputStream());
			reciever = new DataInputStream(mySocket.getInputStream());
		}
		catch(IOException ioex){
			System.out.println(ioex);
		}
	}

	public void beClient(String serverIP, int portNum){
		this.serverIP = serverIP;
		try{
			mySocket = new Socket(serverIP, portNum);
			sender = new DataOutputStream(mySocket.getOutputStream());
			reciever = new DataInputStream(mySocket.getInputStream());
		}
		catch(IOException ioex){
			System.out.println(ioex);
		}
	}
}
