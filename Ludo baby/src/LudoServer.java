import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.net.*;
import java.util.*;

public class LudoServer {

	static final int port = 1200;
	public int start;
	public String roll;
	static boolean pregame = true;
	private static Hashtable outputStreams = new Hashtable();
	
List<Socket> socketarray = new ArrayList<Socket>();

	static String board[];
	static int n = 0;
	static int turn = 0;
	static int np;
	static int hc = 3;

	public static void main(String[] args) {
		LudoServer l = new LudoServer();
	}

	public LudoServer() {

		ServerSocket srvsock = null;
		Socket csocket = null;
		int players = 0;
		int socketnumber = 0;
		boolean timeout = true;

		try {
			srvsock = new ServerSocket(port);
			srvsock.setSoTimeout(2000);

		} catch (IOException ex) {
			System.out.println("Error creating server socket" + ex);
			System.exit(0);
		}

		while (pregame) {
			try {
				System.out.println("Waiting for client on port "
						+ srvsock.getLocalPort() + "...");
				csocket = srvsock.accept();
				timeout = true;
				socketarray.add(csocket);

				DataOutputStream dout = new DataOutputStream(socketarray.get(
						socketnumber).getOutputStream());
				outputStreams.put(socketarray.get(socketnumber), dout);
				System.out.println("Just connected to "
						+ csocket.getRemoteSocketAddress());
				socketnumber++;
			} catch (SocketTimeoutException e) {
				System.out.println("Socket timeout");
				timeout = false;
			} catch (IOException ex) {
				System.out.println("Error accepting from socket" + ex);
				System.exit(0);
			}
			if (timeout) {
				new ServerThread(this, csocket, players);
				timeout = false;
				players++;
			}
		}
	}

	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	void sendToPlayer(String message, int player) {
		DataOutputStream dout = (DataOutputStream) outputStreams
				.get(socketarray.get(player));
		try {
			dout.writeUTF(message);
		} catch (IOException ie) {
			System.out.println(ie);
		}
	}

	void sendToAll(String message) {
		synchronized (outputStreams) {
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {

				DataOutputStream dout = (DataOutputStream) e.nextElement();

				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					System.out.println(ie);
				}

			}
		}
	}

	public void setStart(boolean j) {
		pregame = j;
		roll = roll();
		System.out.println(roll);
		sendToAll(this.toString() + roll);
		System.out.println(this.toString() + roll);
	}
	
	public void Ludo() {
		board = new String[80];

		for (int i = 0; i < 80; i++) {

			board[i] = "-";
		}

	}

	public void addPlayer(int p) {
		for (int i = 0; i < p; i++) {
			if (i == 0) {
				board[i] = "d";
			} else if (i == 1) {
				board[i] = "k";
			} else if (i == 2) {
				board[i] = "p";
			} else if (i == 3) {
				board[i] = "z";
			}
		}
	}

	public void move(int m, int n) {
		String sb = board[m];
		// tjekker om det er p1 der rykker
		// --------------------------------------------------------------------------------------------------------------
		if (sb == "a" || sb == "b" || sb == "c" || sb == "d") {
			// Ændre start positionen
			if (sb == "a") {
				board[m] = "-";
			} else if (sb == "b") {
				board[m] = "a";
			} else if (sb == "c") {
				board[m] = "b";
			} else if (sb == "d") {
				board[m] = "c";
			}
			// Afgør hvor den skal lande
			int end = m + n;
			if (m == 0) {// Tjekker om brikken skal ud af start
				if (n == 6) {
					end = 4;
				} else {
					end = 0;
				}
			} else {
				if (m < 55 && end > 54) {// mål område
					end += 1;
				}
				if (end > 61) {// mål feltet
					end = 61 - (end - 61);
				}
				// Stjerne
				if (end == 9) {
					end = 15;
				} else if (end == 15) {
					end = 22;
				} else if (end == 22) {
					end = 28;
				} else if (end == 28) {
					end = 35;
				} else if (end == 35) {
					end = 41;
				} else if (end == 41) {
					end = 48;
				} else if (end == 48) {
					end = 54;
				} else if (end == 54) {// mål stjernen
					end = 61;
				}
			}
			// Tjekker hvad den lander på
			if (board[end] == "-") {// landet på tomt felt eller p1
				board[end] = "a";
			} else if (board[end] == "a") {
				board[end] = "b";
			} else if (board[end] == "b") {
				board[end] = "c";
			} else if (board[end] == "c") {
				board[end] = "d";
			} else if (end == 4) {// Lander på start globus
				if (board[end] == "-") {
					board[end] = "a";
				} else if (board[end] == "a") {
					board[end] = "b";
				} else if (board[end] == "b") {
					board[end] = "c";
				} else if (board[end] == "c") {
					board[end] = "d";
				} else if (board[end] == "h") {
					if (board[1] == "-") {
						board[1] = "h";
					} else if (board[1] == "h") {
						board[1] = "i";
					} else if (board[1] == "i") {
						board[1] = "j";
					} else if (board[1] == "j") {
						board[1] = "k";
					}
					board[end] = "a";
				} else if (board[end] == "i") {
					if (board[1] == "-") {
						board[1] = "i";
					} else if (board[1] == "h") {
						board[1] = "j";
					} else if (board[1] == "i") {
						board[1] = "k";
					}
					board[end] = "a";
				} else if (board[end] == "j") {
					if (board[1] == "-") {
						board[1] = "j";
					} else if (board[1] == "h") {
						board[1] = "k";
					}
					board[end] = "a";
				} else if (board[end] == "k") {
					board[1] = "k";
					board[end] = "a";
				} else if (board[end] == "m") {
					if (board[2] == "-") {
						board[2] = "m";
					} else if (board[2] == "m") {
						board[2] = "n";
					} else if (board[2] == "n") {
						board[2] = "o";
					} else if (board[2] == "o") {
						board[2] = "p";
					}
					board[end] = "a";
				} else if (board[end] == "n") {
					if (board[2] == "-") {
						board[2] = "n";
					} else if (board[2] == "m") {
						board[2] = "o";
					} else if (board[2] == "n") {
						board[2] = "p";
					}
					board[end] = "a";
				} else if (board[end] == "o") {
					if (board[2] == "-") {
						board[2] = "o";
					} else if (board[2] == "m") {
						board[2] = "p";
					}
					board[end] = "a";
				} else if (board[end] == "p") {
					board[2] = "p";
					board[end] = "a";
				} else if (board[end] == "w") {
					if (board[3] == "-") {
						board[3] = "w";
					} else if (board[3] == "w") {
						board[3] = "x";
					} else if (board[3] == "x") {
						board[3] = "y";
					} else if (board[3] == "y") {
						board[3] = "z";
					}
					board[end] = "a";
				} else if (board[end] == "x") {
					if (board[3] == "-") {
						board[3] = "x";
					} else if (board[3] == "w") {
						board[3] = "y";
					} else if (board[3] == "x") {
						board[3] = "z";
					}
					board[end] = "a";
				} else if (board[end] == "y") {
					if (board[3] == "-") {
						board[3] = "y";
					} else if (board[3] == "w") {
						board[3] = "z";
					}
					board[end] = "a";
				} else if (board[end] == "z") {
					board[3] = "z";
					board[end] = "a";
				}
			}
			// lander på anden globus
			else if (end == 4 || end == 12 || end == 17 || end == 25
					|| end == 30 || end == 38 || end == 43 || end == 51) {
				if (board[0] == "-") {
					board[0] = "a";
				} else if (board[0] == "a") {
					board[0] = "b";
				} else if (board[0] == "b") {
					board[0] = "c";
				} else if (board[0] == "c") {
					board[0] = "d";
				}
			} else if (board[end] == "h") {// lander på p2 én brik
				board[end] = "a";
				if (board[1] == "-") {
					board[1] = "h";
				} else if (board[1] == "h") {
					board[1] = "i";
				} else if (board[1] == "i") {
					board[1] = "j";
				} else if (board[1] == "j") {
					board[1] = "k";
				}
			} else if (board[end] == "m") {// lander på p3 én brik
				board[end] = "a";
				if (board[2] == "-") {
					board[2] = "m";
				} else if (board[2] == "m") {
					board[2] = "n";
				} else if (board[2] == "n") {
					board[2] = "o";
				} else if (board[2] == "o") {
					board[2] = "p";
				}
			} else if (board[end] == "w") {// lander på p4 én brik
				board[end] = "a";
				if (board[3] == "-") {
					board[3] = "w";
				} else if (board[3] == "w") {
					board[3] = "x";
				} else if (board[3] == "x") {
					board[3] = "y";
				} else if (board[3] == "y") {
					board[3] = "z";
				}
			} else {// Lander på player 2,3 eller 4 med mere end én brik
				if (board[0] == "-") {
					board[0] = "a";
				} else if (board[0] == "a") {
					board[0] = "b";
				} else if (board[0] == "b") {
					board[0] = "c";
				} else if (board[0] == "c") {
					board[0] = "d";
				}
			}
			// tjekker om det er p2 der rykker
			// ---------------------------------------------------------------------------------------------------------------
		} else if (sb == "h" || sb == "i" || sb == "j" || sb == "k") {
			// Ændre start positionen
			if (sb == "h") {
				board[m] = "-";
			} else if (sb == "i") {
				board[m] = "h";
			} else if (sb == "j") {
				board[m] = "i";
			} else if (sb == "k") {
				board[m] = "j";
			}
			// Afgør hvor den skal lande
			int end = m + n;
			if (m == 1) {// Tjekker om brikken skal ud af start
				if (n == 6) {
					end = 17;
				} else {
					end = 1;
				}
			} else {
				if (55 < end && end < 62) {// hoppe over rødt mål
					end -= 52;
				}
				if (m < 16 && end > 15) {// mål område
					end += 46;
				}
				if (end > 67) {// mål feltet
					end = 67 - (end - 67);
				}// Stjerne
				if (end == 9) {
					end = 15;
				} else if (end == 15) {// mål stjernen
					end = 67;
				} else if (end == 22) {
					end = 28;
				} else if (end == 28) {
					end = 35;
				} else if (end == 35) {
					end = 41;
				} else if (end == 41) {
					end = 48;
				} else if (end == 48) {
					end = 54;
				} else if (end == 54) {
					end = 9;
				}
			}
			// Tjekker hvad den lander på
			if (board[end] == "-") {// landet på tomt felt eller p2
				board[end] = "h";
			} else if (board[end] == "h") {
				board[end] = "i";
			} else if (board[end] == "i") {
				board[end] = "j";
			} else if (board[end] == "j") {
				board[end] = "k";
			} else if (end == 17) {// Lander på start globus
				if (board[end] == "-") {
					board[end] = "h";
				} else if (board[end] == "h") {
					board[end] = "i";
				} else if (board[end] == "i") {
					board[end] = "j";
				} else if (board[end] == "j") {
					board[end] = "k";
				} else if (board[end] == "a") {
					if (board[0] == "-") {
						board[0] = "a";
					} else if (board[0] == "a") {
						board[0] = "b";
					} else if (board[0] == "b") {
						board[0] = "c";
					} else if (board[0] == "c") {
						board[0] = "d";
					}
					board[end] = "h";
				} else if (board[end] == "b") {
					if (board[0] == "-") {
						board[0] = "b";
					} else if (board[0] == "a") {
						board[0] = "c";
					} else if (board[0] == "b") {
						board[0] = "d";
					}
					board[end] = "h";
				} else if (board[end] == "c") {
					if (board[0] == "-") {
						board[0] = "c";
					} else if (board[0] == "d") {
						board[0] = "d";
					}
					board[end] = "h";
				} else if (board[end] == "d") {
					board[0] = "d";
					board[end] = "h";
				} else if (board[end] == "m") {
					if (board[2] == "-") {
						board[2] = "m";
					} else if (board[2] == "m") {
						board[2] = "n";
					} else if (board[2] == "n") {
						board[2] = "o";
					} else if (board[2] == "o") {
						board[2] = "p";
					}
					board[end] = "h";
				} else if (board[end] == "n") {
					if (board[2] == "-") {
						board[2] = "n";
					} else if (board[2] == "m") {
						board[2] = "o";
					} else if (board[2] == "n") {
						board[2] = "p";
					}
					board[end] = "h";
				} else if (board[end] == "o") {
					if (board[2] == "-") {
						board[2] = "o";
					} else if (board[2] == "m") {
						board[2] = "p";
					}
					board[end] = "h";
				} else if (board[end] == "p") {
					board[2] = "p";
					board[end] = "h";
				} else if (board[end] == "w") {
					if (board[3] == "-") {
						board[3] = "w";
					} else if (board[3] == "w") {
						board[3] = "x";
					} else if (board[3] == "x") {
						board[3] = "y";
					} else if (board[3] == "y") {
						board[3] = "z";
					}
					board[end] = "h";
				} else if (board[end] == "x") {
					if (board[3] == "-") {
						board[3] = "x";
					} else if (board[3] == "w") {
						board[3] = "y";
					} else if (board[3] == "x") {
						board[3] = "z";
					}
					board[end] = "h";
				} else if (board[end] == "y") {
					if (board[3] == "-") {
						board[3] = "y";
					} else if (board[3] == "w") {
						board[3] = "z";
					}
					board[end] = "h";
				} else if (board[end] == "z") {
					board[3] = "z";
					board[end] = "h";
				}
			}
			// lander på anden globus
			else if (end == 4 || end == 12 || end == 17 || end == 25
					|| end == 30 || end == 38 || end == 43 || end == 51) {
				if (board[1] == "-") {
					board[1] = "h";
				} else if (board[1] == "h") {
					board[1] = "i";
				} else if (board[1] == "i") {
					board[1] = "j";
				} else if (board[1] == "j") {
					board[1] = "k";
				}
			} else if (board[end] == "a") {// lander på p1 én brik
				board[end] = "h";
				if (board[0] == "-") {
					board[0] = "a";
				} else if (board[0] == "a") {
					board[0] = "b";
				} else if (board[0] == "b") {
					board[0] = "c";
				} else if (board[0] == "c") {
					board[0] = "d";
				}
			} else if (board[end] == "m") {// lander på p3 én brik
				board[end] = "h";
				if (board[2] == "-") {
					board[2] = "m";
				} else if (board[2] == "m") {
					board[2] = "n";
				} else if (board[2] == "n") {
					board[2] = "o";
				} else if (board[2] == "o") {
					board[2] = "p";
				}
			} else if (board[end] == "w") {// lander på p4 én brik
				board[end] = "h";
				if (board[3] == "-") {
					board[3] = "w";
				} else if (board[3] == "w") {
					board[3] = "x";
				} else if (board[3] == "x") {
					board[3] = "y";
				} else if (board[3] == "y") {
					board[3] = "z";
				}
			} else {// Lander på player 2,3 eller 4 med mere end én brik
				if (board[1] == "-") {
					board[1] = "h";
				} else if (board[1] == "h") {
					board[1] = "i";
				} else if (board[1] == "i") {
					board[1] = "j";
				} else if (board[1] == "j") {
					board[1] = "k";
				}
			}
			// tjekker om det er p3 der rykker
			// -----------------------------------------------------------------------------------------------------------------------------
		} else if (sb == "m" || sb == "n" || sb == "o" || sb == "p") {
			// Ændre start positionen
			if (sb == "m") {
				board[m] = "-";
			} else if (sb == "n") {
				board[m] = "m";
			} else if (sb == "o") {
				board[m] = "n";
			} else if (sb == "p") {
				board[m] = "o";
			}
			// Afgør hvor den skal lande
			int end = m + n;
			if (m == 2) {// Tjekker om brikken skal ud af start
				if (n == 6) {
					end = 30;
				} else {
					end = 2;
				}
			} else {
				if (55 < end && end < 62) {// hoppe over rødt mål
					end -= 52;
				}
				if (m < 29 && end > 28) {// mål område
					end += 39;
				}
				if (end > 73) {// mål feltet
					end = 73 - (end - 73);
				}
				if (end == 9) {
					end = 15;
				} else if (end == 15) {
					end = 22;
				} else if (end == 22) {
					end = 28;
				} else if (end == 28) {// mål stjernen
					end = 73;
				} else if (end == 35) {
					end = 41;
				} else if (end == 41) {
					end = 48;
				} else if (end == 48) {
					end = 54;
				} else if (end == 54) {
					end = 9;
				}
			}
			// Tjekker hvad den lander på
			if (board[end] == "-") {// landet på tomt felt eller p3
				board[end] = "m";
			} else if (board[end] == "m") {
				board[end] = "n";
			} else if (board[end] == "n") {
				board[end] = "o";
			} else if (board[end] == "o") {
				board[end] = "p";
			} else if (end == 30) {// Lander på start globus
				if (board[end] == "-") {
					board[end] = "m";
				} else if (board[end] == "m") {
					board[end] = "n";
				} else if (board[end] == "n") {
					board[end] = "o";
				} else if (board[end] == "o") {
					board[end] = "p";
				} else if (board[end] == "a") {
					if (board[0] == "-") {
						board[0] = "a";
					} else if (board[0] == "a") {
						board[0] = "b";
					} else if (board[0] == "b") {
						board[0] = "c";
					} else if (board[0] == "c") {
						board[0] = "d";
					}
					board[end] = "m";
				} else if (board[end] == "b") {
					if (board[0] == "-") {
						board[0] = "b";
					} else if (board[0] == "a") {
						board[0] = "c";
					} else if (board[0] == "b") {
						board[0] = "d";
					}
					board[end] = "m";
				} else if (board[end] == "c") {
					if (board[0] == "-") {
						board[0] = "c";
					} else if (board[0] == "d") {
						board[0] = "d";
					}
					board[end] = "m";
				} else if (board[end] == "d") {
					board[0] = "d";
					board[end] = "m";
				} else if (board[end] == "h") {
					if (board[1] == "-") {
						board[1] = "h";
					} else if (board[1] == "h") {
						board[1] = "i";
					} else if (board[1] == "i") {
						board[1] = "j";
					} else if (board[1] == "j") {
						board[1] = "k";
					}
					board[end] = "m";
				} else if (board[end] == "i") {
					if (board[1] == "-") {
						board[1] = "i";
					} else if (board[1] == "h") {
						board[1] = "j";
					} else if (board[1] == "i") {
						board[1] = "k";
					}
					board[end] = "m";
				} else if (board[end] == "j") {
					if (board[1] == "-") {
						board[1] = "j";
					} else if (board[1] == "h") {
						board[1] = "k";
					}
					board[end] = "m";
				} else if (board[end] == "k") {
					board[1] = "k";
					board[end] = "m";
				} else if (board[end] == "w") {
					if (board[3] == "-") {
						board[3] = "w";
					} else if (board[3] == "w") {
						board[3] = "x";
					} else if (board[3] == "x") {
						board[3] = "y";
					} else if (board[3] == "y") {
						board[3] = "z";
					}
					board[end] = "m";
				} else if (board[end] == "x") {
					if (board[3] == "-") {
						board[3] = "x";
					} else if (board[3] == "w") {
						board[3] = "y";
					} else if (board[3] == "x") {
						board[3] = "z";
					}
					board[end] = "m";
				} else if (board[end] == "y") {
					if (board[3] == "-") {
						board[3] = "y";
					} else if (board[3] == "w") {
						board[3] = "z";
					}
					board[end] = "m";
				} else if (board[end] == "z") {
					board[3] = "z";
					board[end] = "m";
				}
			}
			// lander på anden globus
			else if (end == 4 || end == 12 || end == 17 || end == 25
					|| end == 30 || end == 38 || end == 43 || end == 51) {
				if (board[2] == "-") {
					board[2] = "m";
				} else if (board[2] == "m") {
					board[2] = "n";
				} else if (board[2] == "n") {
					board[2] = "o";
				} else if (board[2] == "o") {
					board[2] = "p";
				}
			} else if (board[end] == "a") {// lander på p1 én brik
				board[end] = "m";
				if (board[0] == "-") {
					board[0] = "a";
				} else if (board[0] == "a") {
					board[0] = "b";
				} else if (board[0] == "b") {
					board[0] = "c";
				} else if (board[0] == "c") {
					board[0] = "d";
				}
			} else if (board[end] == "h") {// lander på p2 én brik
				board[end] = "m";
				if (board[2] == "-") {
					board[2] = "h";
				} else if (board[2] == "h") {
					board[2] = "i";
				} else if (board[2] == "i") {
					board[2] = "j";
				} else if (board[2] == "j") {
					board[2] = "k";
				}
			} else if (board[end] == "w") {// lander på p4 én brik
				board[end] = "m";
				if (board[3] == "-") {
					board[3] = "w";
				} else if (board[3] == "w") {
					board[3] = "x";
				} else if (board[3] == "x") {
					board[3] = "y";
				} else if (board[3] == "y") {
					board[3] = "z";
				}
			} else {// Lander på player 2,3 eller 4 med mere end én brik
				if (board[2] == "-") {
					board[2] = "m";
				} else if (board[2] == "m") {
					board[2] = "n";
				} else if (board[2] == "n") {
					board[2] = "o";
				} else if (board[2] == "o") {
					board[2] = "p";
				}
			}
			// tjekker om det er p4 der rykker
			// ---------------------------------------------------------------------------------------------
		} else if (sb == "w" || sb == "x" || sb == "y" || sb == "z") {
			// Ændre start positionen
			if (sb == "w") {
				board[m] = "-";
			} else if (sb == "x") {
				board[m] = "w";
			} else if (sb == "y") {
				board[m] = "x";
			} else if (sb == "z") {
				board[m] = "y";
			}
			// Afgør hvor den skal lande
			int end = m + n;
			if (m == 3) {// Tjekker om brikken skal ud af start
				if (n == 6) {
					end = 43;
				} else {
					end = 3;
				}
			} else {
				if (55 < end && end < 62) {// hoppe over rødt mål
					end -= 52;
				}
				if (m < 42 && end > 41) {// mål område
					end += 32;
				}
				if (end > 79) {// mål feltet
					end = 79 - (end - 79);
				}
				if (end == 9) {
					end = 15;
				} else if (end == 15) {
					end = 22;
				} else if (end == 22) {
					end = 28;
				} else if (end == 28) {
					end = 35;
				} else if (end == 35) {
					end = 41;
				} else if (end == 41) {// mål stjernen
					end = 79;
				} else if (end == 48) {
					end = 54;
				} else if (end == 54) {
					end = 9;
				}
			}
			// Tjekker hvad den lander på
			if (board[end] == "-") {// landet på tomt felt eller p4
				board[end] = "w";
			} else if (board[end] == "w") {
				board[end] = "x";
			} else if (board[end] == "x") {
				board[end] = "y";
			} else if (board[end] == "y") {
				board[end] = "z";
			} else if (end == 43) {// Lander på start globus
				if (board[end] == "-") {
					board[end] = "w";
				} else if (board[end] == "w") {
					board[end] = "x";
				} else if (board[end] == "x") {
					board[end] = "y";
				} else if (board[end] == "y") {
					board[end] = "z";
				} else if (board[end] == "a") {
					if (board[0] == "-") {
						board[0] = "a";
					} else if (board[0] == "a") {
						board[0] = "b";
					} else if (board[0] == "b") {
						board[0] = "c";
					} else if (board[0] == "c") {
						board[0] = "d";
					}
					board[end] = "w";
				} else if (board[end] == "b") {
					if (board[0] == "-") {
						board[0] = "b";
					} else if (board[0] == "a") {
						board[0] = "c";
					} else if (board[0] == "b") {
						board[0] = "d";
					}
					board[end] = "w";
				} else if (board[end] == "c") {
					if (board[0] == "-") {
						board[0] = "c";
					} else if (board[0] == "d") {
						board[0] = "d";
					}
					board[end] = "w";
				} else if (board[end] == "d") {
					board[0] = "d";
					board[end] = "w";
				} else if (board[end] == "h") {
					if (board[1] == "-") {
						board[1] = "h";
					} else if (board[1] == "h") {
						board[1] = "i";
					} else if (board[1] == "i") {
						board[1] = "j";
					} else if (board[1] == "j") {
						board[1] = "k";
					}
					board[end] = "w";
				} else if (board[end] == "i") {
					if (board[1] == "-") {
						board[1] = "i";
					} else if (board[1] == "h") {
						board[1] = "j";
					} else if (board[1] == "i") {
						board[1] = "k";
					}
					board[end] = "w";
				} else if (board[end] == "j") {
					if (board[1] == "-") {
						board[1] = "j";
					} else if (board[1] == "h") {
						board[1] = "k";
					}
					board[end] = "w";
				} else if (board[end] == "k") {
					board[1] = "k";
					board[end] = "w";
				} else if (board[end] == "m") {
					if (board[2] == "-") {
						board[2] = "m";
					} else if (board[2] == "m") {
						board[2] = "n";
					} else if (board[2] == "n") {
						board[2] = "o";
					} else if (board[2] == "o") {
						board[2] = "p";
					}
					board[end] = "w";
				} else if (board[end] == "n") {
					if (board[2] == "-") {
						board[2] = "n";
					} else if (board[2] == "m") {
						board[2] = "o";
					} else if (board[2] == "n") {
						board[2] = "p";
					}
					board[end] = "w";
				} else if (board[end] == "o") {
					if (board[2] == "-") {
						board[2] = "o";
					} else if (board[2] == "m") {
						board[2] = "p";
					}
					board[end] = "w";
				} else if (board[end] == "p") {
					board[2] = "p";
					board[end] = "w";
				}
			}
			// lander på anden globus
			else if (end == 4 || end == 12 || end == 17 || end == 25
					|| end == 30 || end == 38 || end == 43 || end == 51) {
				if (board[3] == "-") {
					board[3] = "w";
				} else if (board[3] == "w") {
					board[3] = "x";
				} else if (board[3] == "x") {
					board[3] = "y";
				} else if (board[3] == "y") {
					board[3] = "z";
				}
			} else if (board[end] == "a") {// lander på p1 én brik
				board[end] = "w";
				if (board[0] == "-") {
					board[0] = "a";
				} else if (board[0] == "a") {
					board[0] = "b";
				} else if (board[0] == "b") {
					board[0] = "c";
				} else if (board[0] == "c") {
					board[0] = "d";
				}
			} else if (board[end] == "h") {// lander på p2 én brik
				board[end] = "w";
				if (board[2] == "-") {
					board[2] = "h";
				} else if (board[2] == "h") {
					board[2] = "i";
				} else if (board[2] == "i") {
					board[2] = "j";
				} else if (board[2] == "j") {
					board[2] = "k";
				}
			} else if (board[end] == "m") {// lander på p3 én brik
				board[end] = "w";
				if (board[2] == "-") {
					board[2] = "m";
				} else if (board[2] == "m") {
					board[2] = "n";
				} else if (board[2] == "n") {
					board[2] = "o";
				} else if (board[2] == "o") {
					board[2] = "p";
				}
			} else {// Lander på player 2,3 eller 4 med mere end én brik
				if (board[3] == "-") {
					board[3] = "w";
				} else if (board[3] == "w") {
					board[3] = "x";
				} else if (board[3] == "x") {
					board[3] = "y";
				} else if (board[3] == "y") {
					board[3] = "z";
				}
			}
		}
	}

	public String roll() {
		// F¿rste tur, afg¿r antallet af spillere samt s¾tter tur til spiller 1
		if (turn == 0) {
			turn = 1;
			if (board[3] == "z") {
				np = 4;
			} else if (board[2] == "p") {
				np = 3;
			} else if (board[1] == "k") {
				np = 2;
			} else {
				np = 1;
			}
		}
		// Afg¿r om spiller har vundet og skal springes over
		if (turn == 1 && board[61] == "d") {
			if (turn + 1 > np) {
				turn = 1;
			} else {
				turn++;
			}
		}
		if (turn == 2 && board[67] == "k") {
			if (turn + 1 > np) {
				turn = 1;
			} else {
				turn++;
			}
		}
		if (turn == 3 && board[73] == "p") {
			if (turn + 1 > np) {
				turn = 1;
			} else {
				turn++;
			}
		}
		if (turn == 4 && board[79] == "z") {
			if (turn + 1 > np) {
				turn = 1;
			} else {
				turn++;
			}
		}

		String die = "";
		if (turn == 1) {

			die = "P1:" + Integer.toString((int) Math.ceil(Math.random() * 6));
			if (die.equals("P1:6")) {
				hc = 3;
				roll = die;
				return die;
			} else {
				if (board[0] == "d" || (board[0] == "c" && board[61] == "a")
						|| (board[0] == "b" && board[61] == "b")
						|| (board[0] == "a" && board[61] == "c")) {
					if (hc > 1) {
						hc--;
						roll = die; return die;
					}
				}

				if (turn + 1 > np) {
					turn = 1;
					hc = 3;
					roll = die; return die;
				} else {
					hc = 3;
					turn++;
					roll = die; return die;
				}
			}

		} else if (turn == 2) {
			die = "P2:" + Integer.toString((int) Math.ceil(Math.random() * 6));
			if (die.equals("P2:6")) {
				hc = 3;
				roll = die; return die;
			} else {
				if (board[1] == "k" || (board[1] == "j" && board[67] == "h")
						|| (board[1] == "i" && board[67] == "i")
						|| (board[1] == "h" && board[67] == "j")) {
					if (hc > 1) {
						hc--;
						roll = die; return die;
					}
				}
				if (turn + 1 > np) {
					hc = 3;
					turn = 1;
					roll = die; return die;
				} else {
					hc = 3;
					turn++;
					roll = die; return die;
				}
			}
		} else if (turn == 3) {
			die = "P3:" + Integer.toString((int) Math.ceil(Math.random() * 6));
			if (die.equals("P3:6")) {
				hc = 3;
				roll = die; return die;
			} else {
				if (board[2] == "p" || (board[2] == "o" && board[73] == "m")
						|| (board[2] == "n" && board[73] == "n")
						|| (board[2] == "m" && board[73] == "o")) {
					if (hc > 1) {
						hc--;
						roll = die; return die;
					}
				}

				if (turn + 1 > np) {
					hc = 3;
					turn = 1;
					roll = die; return die;
				} else {
					hc = 3;
					turn++;
					roll = die; return die;
				}
			}
		} else if (turn == 4) {
			die = "P4:" + Integer.toString((int) Math.ceil(Math.random() * 6));
			if (die.equals("P4:6")) {
				hc = 3;
				roll = die; return die;
			} else {
				if (board[3] == "z" || (board[3] == "y" && board[79] == "w")
						|| (board[3] == "x" && board[79] == "x")
						|| (board[3] == "w" && board[79] == "y")) {
					if (hc > 1) {
						hc--;
						roll = die; return die;
					}
				}
				if (turn + 1 > np) {
					hc = 3;
					turn = 1;
					roll = die; return die;
				} else {
					hc = 3;
					turn++;
					roll = die; return die;
				}
			}
		}

		return null;
	}
	
    public String toString() {
        String s = "Board is: ";
        for (int i = 0; i < 80; i++) {
                s = s + board[i];
        }
        return s;
}
}
