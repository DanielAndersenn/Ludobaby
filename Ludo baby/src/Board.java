import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Board extends JFrame implements ActionListener {

	ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();
	private static Client client;
	private static int hc = 3;
	private static int player;
	private static boolean turn = false, start = true;
	final JTextField tf1 = new JTextField();
	private static String currentboard, update;
	private static JButton[] grafik, BU;
	private static JTextArea chat, log;
	private JScrollPane ta1, ta2;
	private static JButton send, kast;
	private JLabel c, gl;
	private static JLabel[] stjerner, globus;
	private static JPanel p1, p2;
	private static Board board;

	public Board() {
		Dimension btnsize = new Dimension(15, 15);
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		// Opbygger Grid og JPanels
		// JPanel 1 = Brættet
		p1 = new JPanel();
		p1.setLayout(new GridLayout(15, 15));
		p1.setMaximumSize(new Dimension(825, 825));
		// JPanel 2 = Højre GUI box
		p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p2.setMaximumSize(new Dimension(300, 825));
		// JPanel 3 = Chat skriv/send
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		tf1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					String s = tf1.getText();
					tf1.setText("");
					client.sendMessage("CHAT " + s);
					// System.out.println(s);
				}
			}
		});
		send = new JButton("send");
		send.addActionListener(this);
		send.setEnabled(true);
		p3.add(tf1, BorderLayout.CENTER);
		p3.add(send, BorderLayout.LINE_END);
		p3.setMaximumSize(new Dimension(365, 30));
		// Opbygger de 225 pladser på brættet
		grafik = new JButton[225];
		for (int i = 0; i < 225; i++) {
			grafik[i] = new JButton("");
			grafik[i].setMaximumSize(btnsize);
			grafik[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			grafik[i].setEnabled(false);
			grafik[i].addActionListener(this);
			grafik[i].setBackground(new java.awt.Color(220, 220, 220));
			grafik[i].setMargin(new Insets(0, 0, 0, 0));
			p1.add(grafik[i]);
		}
		// Text Area ChatBox
		chat = new JTextArea(16, 36);
		chat.setEditable(false);
		chat.setLineWrap(true);
		DefaultCaret caret0 = (DefaultCaret) chat.getCaret();
		caret0.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane ta1 = new JScrollPane(chat);
		ta1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Text Area GameLog
		log = new JTextArea(16, 36);
		log.setEditable(false);
		log.setLineWrap(true);
		DefaultCaret caret1 = (DefaultCaret) log.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane ta2 = new JScrollPane(log);
		ta2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Kast knappen
		kast = new JButton("Start game");
		kast.setMaximumSize(new Dimension(365, 30));
		kast.setAlignmentX(Component.CENTER_ALIGNMENT);
		kast.addActionListener(this);
		kast.setEnabled(start);
		// Labels
		c = new JLabel("Chatbox");
		gl = new JLabel("Game Log");
		// opsætning af højre GUI
		p2.add(Box.createRigidArea(new Dimension(0, 15)));
		p2.add(c);
		p2.add(ta1);
		p2.add(p3);
		p2.add(Box.createRigidArea(new Dimension(0, 15)));
		p2.add(gl);
		p2.add(ta2);
		p2.add(kast);
		// Følgene giver farve til brættet
		// Invalid Spots

		ImageImport("DTU.jpg");
		JLabel label0 = new JLabel(icons.get(0));
		label0.setAlignmentX(Component.CENTER_ALIGNMENT);
		grafik[112].add(label0);

		ImageImport("bottomright.jpg");
		JLabel corner1 = new JLabel(icons.get(1));
		corner1.setAlignmentX(Component.CENTER_ALIGNMENT);
		grafik[128].add(corner1);

		ImageImport("bottomleft.jpg");
		JLabel corner2 = new JLabel(icons.get(2));
		corner2.setAlignmentX(Component.CENTER_ALIGNMENT);
		grafik[126].add(corner2);

		ImageImport("topright.jpg");
		JLabel corner3 = new JLabel(icons.get(3));
		corner3.setAlignmentX(Component.CENTER_ALIGNMENT);
		grafik[98].add(corner3);

		ImageImport("topleft.jpg");
		JLabel corner4 = new JLabel(icons.get(4));
		corner4.setAlignmentX(Component.CENTER_ALIGNMENT);
		grafik[96].add(corner4);

		for (int i = 96; i < 99; i = i + 2) {
			grafik[i].setBackground(Color.white);
			grafik[i + 30].setBackground(Color.white);
			grafik[112].setBackground(Color.white);
		}

		for (int i = 9, c = 9, g = 0; i < 15; i++) {
			// grøn
			grafik[i].setBackground(Color.green);
			grafik[c].setBackground(Color.green);
			grafik[c + 5].setBackground(Color.green);
			grafik[i + 75].setBackground(Color.green);
			grafik[41 + g].setBackground(Color.green);
			grafik[42 + g].setBackground(Color.green);
			grafik[103].setBackground(Color.green);
			grafik[i + 104].setBackground(Color.green);
			c = c + 15;
			g = 15;
		}
		for (int i = 0, c = 0, g = 0; i < 6; i++) {
			// Gul
			grafik[i].setBackground(Color.yellow);
			grafik[c].setBackground(Color.yellow);
			grafik[c + 5].setBackground(Color.yellow);
			grafik[i + 75].setBackground(Color.yellow);
			grafik[21].setBackground(Color.yellow);
			grafik[32 + g].setBackground(Color.yellow);
			grafik[33 + g].setBackground(Color.yellow);
			grafik[22 + c].setBackground(Color.yellow);
			c = c + 15;
			g = 15;
		}
		for (int i = 135, c = 135, g = 0; i < 141; i++) {
			// Blå
			grafik[i].setBackground(new java.awt.Color(0, 255, 255));
			grafik[c].setBackground(new java.awt.Color(0, 255, 255));
			grafik[c + 5].setBackground(new java.awt.Color(0, 255, 255));
			grafik[i + 75].setBackground(new java.awt.Color(0, 255, 255));
			grafik[167 + g].setBackground(new java.awt.Color(0, 255, 255));
			grafik[168 + g].setBackground(new java.awt.Color(0, 255, 255));
			grafik[121].setBackground(new java.awt.Color(0, 255, 255));
			grafik[i - 29].setBackground(new java.awt.Color(0, 255, 255));
			c = c + 15;
			g = 15;
		}
		for (int i = 144, c = 144, g = 0; i < 150; i++) {
			// Rød
			grafik[i].setBackground(Color.red);
			grafik[c].setBackground(Color.red);
			grafik[c + 5].setBackground(Color.red);
			grafik[i + 75].setBackground(Color.red);
			grafik[176 + g].setBackground(Color.red);
			grafik[177 + g].setBackground(Color.red);
			grafik[203].setBackground(Color.red);
			grafik[c - 17].setBackground(Color.red);
			c = c + 15;
			g = 15;
		}
		// Samler Content Window
		getContentPane().add(p1);
		getContentPane().add(p2);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Import af stjerner
		ImageImport("star.png");
		stjerner = new JLabel[8];
		for (int i = 0; i < 8; i++) {
			stjerner[i] = new JLabel(icons.get(5));
		}
		for (int i = 0; i < 8; i++) {
			grafik[7].add(stjerner[i]);
			i++;
			grafik[83].add(stjerner[i]);
			i++;
			grafik[95].add(stjerner[i]);
			i++;
			grafik[105].add(stjerner[i]);
			i++;
			grafik[129].add(stjerner[i]);
			i++;
			grafik[141].add(stjerner[i]);
			i++;
			grafik[119].add(stjerner[i]);
			i++;
			grafik[217].add(stjerner[i]);
			i++;

		}

		// Import af Globus
		ImageImport("Globe.png");
		globus = new JLabel[8];
		for (int i = 0; i < 8; i++) {
			globus[i] = new JLabel(icons.get(6));
		}

		for (int i = 0; i < 8; i++) {
			grafik[203].add(globus[i]);
			i++;
			grafik[132].add(globus[i]);
			i++;
			grafik[103].add(globus[i]);
			i++;
			grafik[38].add(globus[i]);
			i++;
			grafik[21].add(globus[i]);
			i++;
			grafik[92].add(globus[i]);
			i++;
			grafik[121].add(globus[i]);
			i++;
			grafik[186].add(globus[i]);
			i++;
		}

	}

	public static void main(String[] args) {

		board = new Board();
		client = new Client();
		board.setTitle("Ludo v.DTU");
		board.setResizable(false);
		board.setVisible(true);
		board.setSize(1130, 830);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send) {
			String s = tf1.getText();
			tf1.setText("");
			client.sendMessage("CHAT " + s);
			System.out.println(s);
		}
		if (start == false) {
			if (Integer.parseInt(currentboard.substring(91, 92)) == player
					&& start == false) {

				for (int i = 0; i < 80; i++) {
					if (e.getSource() == BU[i]) {
						int s = Integer
								.parseInt(currentboard.substring(93, 94));
						if (s == 6) {
							client.sendMessage("2Move " + i);
						} else {
							client.sendMessage("Move " + i);
						}

					}
				}
			}
		}

		if (e.getSource() == kast) {
			if (start) {
				client.sendMessage("LOG Start game");
				start = false;
				kast.setText("Roll the dice");
				kast.setEnabled(true);
			}

			if (currentboard.length() > 90) {
				int i = Integer.parseInt(currentboard.substring(93, 94));
				int k = Integer.parseInt(currentboard.substring(91, 92));

				client.sendMessage("LOG Player " + k + " har slået: " + i);
				treKast();

				if (i == 6) {
					client.sendMessage("LOG Player " + k + " må slå igen.");
					hc = 3;
				} else {
					// treKast();
				}
			}
		}
	}

	public static void chatSetText(String g) {
		chat.append(g.substring(5) + "\n");
		chat.setCaretPosition(chat.getDocument().getLength());
	}

	public static void logSetText(String g) {
		if(!g.substring(4).equals("Start game")) {
			log.append(g.substring(4) + "\n");
			log.setCaretPosition(log.getDocument().getLength());
		}

	}

	public static void setButton() {
		kast.setText("Roll the dice");
	}

	public static void setPlayer(int p) {
		player = p;
	}

	public static int getPlayer() {
		return player;
	}

	public static void setTurn(boolean h) {
		kast.setEnabled(h);
		kast.repaint();
		p2.repaint();
	}

	public static void boardUpdate(String g) {
		currentboard = g;
		update = g;
		BUpdate(update);
		extraUpdate();
	}

	public static void treKast() {
		if (player == 1) {
			int s = Integer.parseInt(currentboard.substring(93, 94));
			if (Character.toString(currentboard.charAt(10)).equals("d")
					&& s != 6
					|| Character.toString(currentboard.charAt(10)).equals("c")
					&& Character.toString(currentboard.charAt(71)).equals("a")
					&& s != 6
					|| Character.toString(currentboard.charAt(10)).equals("b")
					&& Character.toString(currentboard.charAt(71)).equals("b")
					&& s != 6
					|| Character.toString(currentboard.charAt(10)).equals("a")
					&& Character.toString(currentboard.charAt(71)).equals("c")
					&& s != 6) {
				if (hc > 1) {
					hc = hc - 1;
					client.sendMessage("BlankMove 00");

				}
			}
		} else

		if (player == 2) {
			int s = Integer.parseInt(currentboard.substring(93, 94));
			if (Character.toString(currentboard.charAt(11)).equals("k")
					&& s != 6
					|| Character.toString(currentboard.charAt(11)).equals("j")
					&& Character.toString(currentboard.charAt(77)).equals("h")
					&& s != 6
					|| Character.toString(currentboard.charAt(11)).equals("i")
					&& Character.toString(currentboard.charAt(77)).equals("i")
					&& s != 6
					|| Character.toString(currentboard.charAt(11)).equals("h")
					&& Character.toString(currentboard.charAt(77)).equals("j")
					&& s != 6) {
				if (hc > 1) {
					hc = hc - 1;
					client.sendMessage("BlankMove 11");

				}
			}
		} else

		if (player == 3) {
			int s = Integer.parseInt(currentboard.substring(93, 94));
			if (Character.toString(currentboard.charAt(12)).equals("p")
					&& s != 6
					|| Character.toString(currentboard.charAt(12)).equals("o")
					&& Character.toString(currentboard.charAt(83)).equals("m")
					&& s != 6
					|| Character.toString(currentboard.charAt(12)).equals("n")
					&& Character.toString(currentboard.charAt(83)).equals("n")
					&& s != 6
					|| Character.toString(currentboard.charAt(12)).equals("m")
					&& Character.toString(currentboard.charAt(83)).equals("o")
					&& s != 6) {
				if (hc > 1) {
					hc = hc - 1;
					client.sendMessage("BlankMove 22");

				}
			}
		} else

		if (player == 4) {
			int s = Integer.parseInt(currentboard.substring(93, 94));
			if (Character.toString(currentboard.charAt(13)).equals("z")
					&& s != 6
					|| Character.toString(currentboard.charAt(13)).equals("y")
					&& Character.toString(currentboard.charAt(89)).equals("w")
					&& s != 6
					|| Character.toString(currentboard.charAt(13)).equals("x")
					&& Character.toString(currentboard.charAt(89)).equals("x")
					&& s != 6
					|| Character.toString(currentboard.charAt(13)).equals("w")
					&& Character.toString(currentboard.charAt(89)).equals("y")
					&& s != 6) {
				if (hc > 1) {
					hc = hc - 1;
					client.sendMessage("BlankMove 22");

				}
			}
		}

		if (hc == 1) {
			setTurn(false);
			hc = 3;
		}
	}

	public static void BUpdate(String update) {
		BU = new JButton[80];
		for (int i = 0, j = 0; j < 1; j++) {
			BU[i] = grafik[176];
			i++;
			BU[i] = grafik[41];
			i++;
			BU[i] = grafik[32];
			i++;
			BU[i] = grafik[167];
			i++;
			BU[i] = grafik[203];
			i++;
			BU[i] = grafik[188];
			i++;
			BU[i] = grafik[173];
			i++;
			BU[i] = grafik[158];
			i++;
			BU[i] = grafik[143];
			i++;
			BU[i] = grafik[129];
			i++;
			BU[i] = grafik[130];
			i++;
			BU[i] = grafik[131];
			i++;
			BU[i] = grafik[132];
			i++;
			BU[i] = grafik[133];
			i++;
			BU[i] = grafik[134];
			i++;
			BU[i] = grafik[119];
			i++;
			BU[i] = grafik[104];
			i++;
			BU[i] = grafik[103];
			i++;
			BU[i] = grafik[102];
			i++;
			BU[i] = grafik[101];
			i++;
			BU[i] = grafik[100];
			i++;
			BU[i] = grafik[99];
			i++;
			BU[i] = grafik[83];
			i++;
			BU[i] = grafik[68];
			i++;
			BU[i] = grafik[53];
			i++;
			BU[i] = grafik[38];
			i++;
			BU[i] = grafik[23];
			i++;
			BU[i] = grafik[8];
			i++;
			BU[i] = grafik[7];
			i++;
			BU[i] = grafik[6];
			i++;
			BU[i] = grafik[21];
			i++;
			BU[i] = grafik[36];
			i++;
			BU[i] = grafik[51];
			i++;
			BU[i] = grafik[66];
			i++;
			BU[i] = grafik[81];
			i++;
			BU[i] = grafik[95];
			i++;
			BU[i] = grafik[94];
			i++;
			BU[i] = grafik[93];
			i++;
			BU[i] = grafik[92];
			i++;
			BU[i] = grafik[91];
			i++;
			BU[i] = grafik[90];
			i++;
			BU[i] = grafik[105];
			i++;
			BU[i] = grafik[120];
			i++;
			BU[i] = grafik[121];
			i++;
			BU[i] = grafik[122];
			i++;
			BU[i] = grafik[123];
			i++;
			BU[i] = grafik[124];
			i++;
			BU[i] = grafik[125];
			i++;
			BU[i] = grafik[141];
			i++;
			BU[i] = grafik[156];
			i++;
			BU[i] = grafik[171];
			i++;
			BU[i] = grafik[186];
			i++;
			BU[i] = grafik[201];
			i++;
			BU[i] = grafik[216];
			i++;
			BU[i] = grafik[217];
			i++;
			BU[i] = grafik[218];
			i++;
			BU[i] = grafik[202];
			i++;
			BU[i] = grafik[187];
			i++;
			BU[i] = grafik[172];
			i++;
			BU[i] = grafik[157];
			i++;
			BU[i] = grafik[142];
			i++;
			BU[i] = grafik[127];
			i++;
			BU[i] = grafik[118];
			i++;
			BU[i] = grafik[117];
			i++;
			BU[i] = grafik[116];
			i++;
			BU[i] = grafik[115];
			i++;
			BU[i] = grafik[114];
			i++;
			BU[i] = grafik[113];
			i++;
			BU[i] = grafik[22];
			i++;
			BU[i] = grafik[37];
			i++;
			BU[i] = grafik[52];
			i++;
			BU[i] = grafik[67];
			i++;
			BU[i] = grafik[82];
			i++;
			BU[i] = grafik[97];
			i++;
			BU[i] = grafik[106];
			i++;
			BU[i] = grafik[107];
			i++;
			BU[i] = grafik[108];
			i++;
			BU[i] = grafik[109];
			i++;
			BU[i] = grafik[110];
			i++;
			BU[i] = grafik[111];
			i++;
		}
		for (int i = 0; i < 80; i++) {
			BU[i].setText(setField(10 + i, update));
		}
	}

	public static String setField(int j, String q) {

		// Hjemme knapper farver player 1/Rød
		String start1 = Character.toString(q.charAt(10));
		if (start1.contains("-")) {
			grafik[176].setBackground(Color.red);
		} else if (start1.contains("a")) {
			grafik[176].setBackground(new java.awt.Color(165, 42, 42));
			grafik[177].setBackground(Color.red);
			grafik[191].setBackground(Color.red);
			grafik[192].setBackground(Color.red);
		} else if (start1.contains("b")) {
			grafik[176].setBackground(new java.awt.Color(165, 42, 42));
			grafik[177].setBackground(new java.awt.Color(165, 42, 42));
			grafik[191].setBackground(Color.red);
			grafik[192].setBackground(Color.red);
		}

		else if (start1.contains("c")) {
			grafik[176].setBackground(new java.awt.Color(165, 42, 42));
			grafik[177].setBackground(new java.awt.Color(165, 42, 42));
			grafik[191].setBackground(new java.awt.Color(165, 42, 42));
			grafik[192].setBackground(Color.red);
		}

		else if (start1.contains("d")) {
			grafik[176].setBackground(new java.awt.Color(165, 42, 42));
			grafik[177].setBackground(new java.awt.Color(165, 42, 42));
			grafik[191].setBackground(new java.awt.Color(165, 42, 42));
			grafik[192].setBackground(new java.awt.Color(165, 42, 42));
		}

		// Hjemme knapper farver player 2/Grøn
		String start2 = Character.toString(q.charAt(11));
		if (start2.contains("-")) {
			grafik[41].setBackground(Color.green);
		} else if (start2.contains("h")) {
			grafik[41].setBackground(new java.awt.Color(34, 139, 34));
			grafik[42].setBackground(Color.green);
			grafik[56].setBackground(Color.green);
			grafik[57].setBackground(Color.green);
		} else if (start2.contains("i")) {
			grafik[41].setBackground(new java.awt.Color(34, 139, 34));
			grafik[42].setBackground(new java.awt.Color(34, 139, 34));
			grafik[56].setBackground(Color.green);
			grafik[57].setBackground(Color.green);
		}

		else if (start2.contains("j")) {
			grafik[41].setBackground(new java.awt.Color(34, 139, 34));
			grafik[42].setBackground(new java.awt.Color(34, 139, 34));
			grafik[56].setBackground(new java.awt.Color(34, 139, 34));
			grafik[57].setBackground(Color.green);
		}

		else if (start2.contains("k")) {
			grafik[41].setBackground(new java.awt.Color(34, 139, 34));
			grafik[42].setBackground(new java.awt.Color(34, 139, 34));
			grafik[56].setBackground(new java.awt.Color(34, 139, 34));
			grafik[57].setBackground(new java.awt.Color(34, 139, 34));
		}

		// Hjemme knapper farver player 3/Gul
		String start3 = Character.toString(q.charAt(12));
		if (start3.contains("-")) {
			grafik[32].setBackground(Color.yellow);
		} else if (start3.contains("m")) {
			grafik[32].setBackground(new java.awt.Color(255, 165, 0));
			grafik[33].setBackground(Color.yellow);
			grafik[47].setBackground(Color.yellow);
			grafik[48].setBackground(Color.yellow);
		} else if (start3.contains("n")) {
			grafik[32].setBackground(new java.awt.Color(255, 165, 0));
			grafik[33].setBackground(new java.awt.Color(255, 165, 0));
			grafik[47].setBackground(Color.yellow);
			grafik[48].setBackground(Color.yellow);
		}

		else if (start3.contains("o")) {
			grafik[32].setBackground(new java.awt.Color(255, 165, 0));
			grafik[33].setBackground(new java.awt.Color(255, 165, 0));
			grafik[47].setBackground(new java.awt.Color(255, 165, 0));
			grafik[48].setBackground(Color.yellow);
		}

		else if (start3.contains("p")) {
			grafik[32].setBackground(new java.awt.Color(255, 165, 0));
			grafik[33].setBackground(new java.awt.Color(255, 165, 0));
			grafik[47].setBackground(new java.awt.Color(255, 165, 0));
			grafik[48].setBackground(new java.awt.Color(255, 165, 0));
		}

		// Hjemme knapper farver player 4/blå
		String start4 = Character.toString(q.charAt(13));
		if (start4.contains("-")) {
			grafik[167].setBackground(new java.awt.Color(0, 255, 255));
		} else if (start4.contains("w")) {
			grafik[167].setBackground(new java.awt.Color(0, 191, 255));
			grafik[168].setBackground(new java.awt.Color(0, 255, 255));
			grafik[182].setBackground(new java.awt.Color(0, 255, 255));
			grafik[183].setBackground(new java.awt.Color(0, 255, 255));
		} else if (start4.contains("x")) {
			grafik[167].setBackground(new java.awt.Color(0, 191, 255));
			grafik[168].setBackground(new java.awt.Color(0, 191, 255));
			grafik[182].setBackground(new java.awt.Color(0, 255, 255));
			grafik[183].setBackground(new java.awt.Color(0, 255, 255));
		}

		else if (start4.contains("y")) {
			grafik[167].setBackground(new java.awt.Color(0, 191, 255));
			grafik[168].setBackground(new java.awt.Color(0, 191, 255));
			grafik[182].setBackground(new java.awt.Color(0, 191, 255));
			grafik[183].setBackground(new java.awt.Color(0, 255, 255));
		}

		else if (start4.contains("z")) {
			grafik[167].setBackground(new java.awt.Color(0, 191, 255));
			grafik[168].setBackground(new java.awt.Color(0, 191, 255));
			grafik[182].setBackground(new java.awt.Color(0, 191, 255));
			grafik[183].setBackground(new java.awt.Color(0, 191, 255));
		}

		String input = Character.toString(q.charAt(j));
		String rh = Character.toString(q.charAt(14));
		String gh = Character.toString(q.charAt(27));
		String yh = Character.toString(q.charAt(40));
		String bh = Character.toString(q.charAt(53));
		// "-" Convert

		if (input.contains("-")) {
			BU[j - 10].setEnabled(false);
			if (14 < j && j < 66) {
				BU[j - 10].setBackground(new java.awt.Color(220, 220, 220));
			}

			if (65 < j && j < 72) {
				BU[j - 10].setBackground(Color.red);
			}

			if (71 < j && j < 78) {
				BU[j - 10].setBackground(Color.green);
			}

			if (77 < j && j < 84) {
				BU[j - 10].setBackground(Color.yellow);
			}

			if (83 < j && j < 90) {
				BU[j - 10].setBackground(new java.awt.Color(0, 255, 255));
			}

			if (rh.contains("-")) {
				BU[4].setBackground(Color.red);
			}

			if (gh.contains("-")) {
				BU[17].setBackground(Color.green);
			}

			if (yh.contains("-")) {
				BU[30].setBackground(Color.yellow);
			}

			if (bh.contains("-")) {
				BU[43].setBackground(new java.awt.Color(0, 255, 255));
			}

			return "";
		}

		// Player 1 Piece Convert
		else if (input.contains("a")) {

			if (player == 1) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(165, 42, 42));
			return "1";
		} else if (input.contains("b")) {
			if (player == 1) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(165, 42, 42));
			return "2";
		} else if (input.contains("c")) {
			if (player == 1) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(165, 42, 42));
			return "3";
		} else if (input.contains("d")) {
			if (player == 1) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(165, 42, 42));
			return "4";
		}

		// Player 2 Piece Convert
		else if (input.contains("h")) {
			if (player == 2) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(34, 139, 34));
			return "1";
		} else if (input.contains("i")) {
			if (player == 2) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(34, 139, 34));
			return "2";
		} else if (input.contains("j")) {
			if (player == 2) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(34, 139, 34));
			return "3";
		} else if (input.contains("k")) {
			if (player == 2) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(34, 139, 34));
			return "4";
		}
		// Player 3 Piece Convert
		else if (input.contains("m")) {
			if (player == 3) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(255, 165, 0));
			return "1";
		} else if (input.contains("n")) {
			if (player == 3) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(255, 165, 0));
			return "2";
		} else if (input.contains("o")) {
			if (player == 3) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(255, 165, 0));
			return "3";
		} else if (input.contains("p")) {
			if (player == 3) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(255, 165, 0));
			return "4";
		}
		// Player 4 Piece Convert
		else if (input.contains("w")) {
			if (player == 4) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(0, 191, 255));
			return "1";
		} else if (input.contains("x")) {
			if (player == 4) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(0, 191, 255));
			return "2";
		} else if (input.contains("y")) {
			if (player == 4) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(0, 191, 255));
			return "3";
		} else if (input.contains("z")) {
			if (player == 4) {
				BU[j - 10].setEnabled(true);
			}
			BU[j - 10].setBackground(new java.awt.Color(0, 191, 255));
			return "4";
		}

		return "???";
	}

	public static void extraUpdate() {
		BU[61].setEnabled(false);
		BU[67].setEnabled(false);
		BU[73].setEnabled(false);
		BU[79].setEnabled(false);
	}
	
	public static void boardPopup(String g) {
		String finished = g.substring(4);
		JOptionPane.showMessageDialog(null, finished, "Luuuuuuuuudooooooooo", JOptionPane.INFORMATION_MESSAGE);
	}

	public void ImageImport(String img_path) {
		try {
			BufferedImage img = ImageIO.read(new File(img_path));
			ImageIcon icon = new ImageIcon(img);
			icons.add(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
