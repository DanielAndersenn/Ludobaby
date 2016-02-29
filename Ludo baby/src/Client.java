import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Client implements Runnable {
	
	private DataInputStream din;
	private DataOutputStream dout;
	private Socket sock;
	
	static Scanner input = new Scanner(System.in);
	
	public Client() {
		try {
			
			sock = new Socket("192.168.56.1", 1200);
			System.out.println("Connected to " + sock);
			
			din = new DataInputStream(sock.getInputStream());
			dout = new DataOutputStream(sock.getOutputStream());
			
			new Thread(this).start();
		
		
		}catch (IOException e) {
			System.out.println("Error While Connecting to the Server: " + e);
			System.exit(0);
        }
	}
	
	public void run() {
		try {
			
			while(true) {
				String message = din.readUTF();
				System.out.println(message);
				if(message.startsWith("CHAT ")) {
					Board.chatSetText(message);
				} 
				
				if(message.startsWith("LOG")) {
					Board.logSetText(message);
				} 
				
				if(message.startsWith("Started")) {
					Board.setButton();
				}
				
				if(message.startsWith("Board")) {
					Board.boardUpdate(message);
				}
				
				if(message.length() > 91) {
					if(Integer.parseInt(message.substring(91, 92)) == Board.getPlayer()) {
					Board.setTurn(true);
					Board.logSetText("LOG YOUR TURN!");
					} else {
						Board.setTurn(false);
					}
				}
				
				if(message.startsWith("CHAT Welcome!")) {
					int i = Integer.parseInt(message.substring(29, 30));
					Board.setPlayer(i);
				}
				
				if(message.startsWith("WIN")) {
					Board.boardPopup(message);
				}
				
			}
		} catch(IOException ie) {
			System.out.println(ie);
		}
		}
	
	public void sendMessage(String tosend) {
		int i = 0;
		try {
		while(i < 1) {	
			dout.writeUTF(tosend);
			i++;
		}
		} catch(IOException ie) {
			System.out.println(ie);
		}
	}

}