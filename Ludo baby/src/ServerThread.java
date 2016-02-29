import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

	private LudoServer server;
	private Socket socket;
	private int player;
	private boolean w1 = true, w2 = true, w3 = true, w4 = true;

	
	
	public ServerThread(LudoServer server, Socket socket, int player) {
		this.server = server;
		this.socket = socket;
		this.player = player;
		
		if(player == 0) {
			server.Ludo();
		}
		
		start();
		server.sendToPlayer("LOG DTU Ludo v1.0", player);
		server.sendToPlayer("CHAT Welcome! You are player " + (player+1), player);
		server.sendToAll("LOG Player " + (player+1) + " has connected to the game.");
		server.addPlayer(player + 1);
		server.sendToAll(server.toString());
		
	}
	
	public void run() {
		
		try {
		
			DataInputStream din = new DataInputStream(socket.getInputStream());
			
			while(true) {
				String message = din.readUTF();
				System.out.println(message);
				
				if(message.substring(4).equalsIgnoreCase("Start game")) {
					
					if(server.start == 0) {
					server.sendToAll("LOG Player " + (player+1) + " has now started the game.");
					server.sendToAll("LOG The server is not accepting more connections.");
					server.sendToAll("Started");
					server.setStart(false);
					server.start++;
					}
				}
				
				if(message.startsWith("CHAT ")) {
					server.sendToAll("CHAT" + " " + "Player " + (player+1) + ": " + message.substring(5));
				}
				
				if(message.startsWith("Board")) {
					server.sendToAll(message);
				}
				
				if(message.startsWith("LOG")) {
					server.sendToAll(message);
				}
				
				if(message.startsWith("Move")) {
					int i;
					if(message.length() == 6) {
						i = Integer.parseInt(message.substring(5, 6));
						System.out.println(i);
					} else {
						i = Integer.parseInt(message.substring(5, 7));
					}
					
					int k = Integer.parseInt(server.roll.substring(3, 4));
					server.move(i, k);
					
					
					if(Character.toString(server.toString().charAt(71)).equals("d") && w1 ) {
						server.sendToAll("WIN Player 1 has finished the game! Congratulations");
						w1 = false;
					}
					
					if(Character.toString(server.toString().charAt(77)).equals("p") && w2 ) {
						server.sendToAll("WIN Player 2 has finished the game! Congratulations");
						w2 = false;
					}
					
					if(Character.toString(server.toString().charAt(83)).equals("k") && w3 ) {
						server.sendToAll("WIN Player 3 has finished the game! Congratulations");
						w3 = false;
					}
					
					if(Character.toString(server.toString().charAt(89)).equals("d") && w4 ) {
						server.sendToAll("WIN Player 4 has finished the game! Congratulations");
						w4 = false;
					}
					
					server.sendToAll(server.toString() + server.roll());
					
				}
				
				if(message.startsWith("2Move")) {
					int i;
					if(message.length() == 7) {
						i = Integer.parseInt(message.substring(6, 7));
					} else {
						i = Integer.parseInt(message.substring(6, 8));
					}
					
					int k = Integer.parseInt(server.roll.substring(3, 4));
					server.move(i, k);					
					server.sendToAll(server.toString() + server.roll());
				}
				
				if(message.startsWith("BlankMove")) {
					int i = Integer.parseInt(message.substring(10, 11));
					int k = Integer.parseInt(message.substring(11, 12));
					server.move(i, k);
					server.sendToAll(server.toString() + server.roll());
				}
				
				if(message.startsWith("WIN")) {
					server.sendToAll(message);
				}
				
				if(message.substring(5).equalsIgnoreCase("Reset game")) {
					server.turn = 0;
			        
			        for (int i = 0; i < 80; i++) {
			                server.board[i] = "-";
			        }
			        
			        server.addPlayer(server.np);
			        
			        server.sendToAll("LOG  ");
			        server.sendToAll("LOG Game has been reset!"); 
			        server.sendToAll("LOG  ");
			        server.hc = 3;
			        
			        server.sendToAll(server.toString() + server.roll());
				}
				
				
			}
			
			} catch(EOFException ie) {
				
			} catch(IOException ie) {
				ie.printStackTrace();
			}
		
	}
}
