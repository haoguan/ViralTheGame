package game;

import interm.AutoActivateManager;
import interm.ClassFinder;
import interm.Playable;
import interm.SpellActivateManager;
import interm.SpellFinder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.GamePlayState;

public class PlayerGui extends JComponent {

	public enum STATES {
		START_GAME_STATE, CHANGE_PLAYER_STATE, ROLL_STATE, DRAW_CARD_STATE, MOVEMENT_STATE, USE_CARD_STATE, USE_SPELL_STATE, PAUSE_GAME_STATE, GAME_OVER_STATE
	}

	HashMap<String, Integer> cardID = new HashMap<String, Integer>();
	HashMap<String, Integer> classID = new HashMap<String, Integer>();
	JFrame frame = new JFrame();
	JPanel bgPanel = new BgPanel();

	JPanel cardPanel = new JPanel();
	JPanel activityPanel = new JPanel();
	JPanel utilityPanel = new JPanel();
	JPanel headerPanel = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel radioPanel = new JPanel();
	JLabel description = new JLabel();
	JLabel[] fillLabel = new JLabel[2];

	Color ButtonColor = Color.MAGENTA;
	Color buttonText = Color.green;

	JButton classButton;
	JButton spellButton;
	JButton drawCard = new JButton("Draw Spell");
	JButton rollDice = new JButton("Roll Dice");
	JButton plantCell = new JButton("Plant Cell");
	JButton activateCard = new JButton("Activate!");
	JButton endTurn = new JButton("End Turn");
	JButton makeMove = new JButton("Make Move!");
	JButton status = new JButton("Status");
	JButton classCard = new JButton("Draw Class");
	ButtonGroup bg = new ButtonGroup();
	JRadioButton spellRadio = new JRadioButton("Spell");
	JRadioButton classRadio = new JRadioButton("Class");
	public JComboBox playerList;

	JTextPane cardDescription = new JTextPane();
	JScrollPane scroll;

	ArrayList<JButton> spellButtonList = new ArrayList<JButton>();
	ArrayList<JButton> classButtonList = new ArrayList<JButton>();

	DrawCardListener drawCardListener = new DrawCardListener();
	SpellCardListener spellButtonListener = new SpellCardListener();
	ClassCardListener classButtonListener = new ClassCardListener();
	ActivateListener activateListener = new ActivateListener();
	EndTurnListener endTurnListener = new EndTurnListener();
	MakeMoveListener makeMoveListener = new MakeMoveListener();
	RollDiceListener rollDiceListener = new RollDiceListener();
	PlantCellListener plantCellListener = new PlantCellListener();
	RadioListener radioListener = new RadioListener();
	PlayerListener playerListener = new PlayerListener();

	int maxSpellCards = 7; // Maximum amount of cards in hands
	int maxClassCards = 3; // Maximum amount of classes in hands
	int cardCount = 0; // Amount of card(s) in hands
	int classCount = 0;
	int currCardID; // Current location of spell card
	String currType;
	String commonText = "";
	boolean stopThread = false;
	StyledDocument doc = cardDescription.getStyledDocument();
	SimpleAttributeSet center = new SimpleAttributeSet();
	Random rand = new Random();
	GamePlayState gamePlayState;
	SpellFinder spells;
	ClassFinder classes;
	SpellActivateManager spellManager;
	Player player;
	Player[] players;
	Player targetPlayer = null;
	Playable playable;
	Deck deck;
	boolean classActivate = false;
	boolean rolled = false;
	String currName;
	final String[] playersPick = { "None", "Player 1", "Player 2", "Player 3", "Player 4" };
	JProgressBar graceBar;
	static final int MY_MINIMUM = 0;
	static final int MY_MAXIMUM = 100;

	// Test Variables
	String cardToTest = "Constellation";
	String classToTest = "Burner";

	public PlayerGui() {
	}

	public PlayerGui(GamePlayState gps, Player player, SpellFinder spells, ClassFinder classes, Deck deck) {
		gamePlayState = gps;
		this.player = player;
		this.spells = spells;
		this.deck = deck;
		this.classes = classes;
	}

	/**
	 * Call createsGui and displays the JFrame
	 * 
	 * @param name is the title of the playerGui
	 */

	public void init(String name) {
		createGui();
		frame.setTitle(name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setResizable(false);
		setPlayerList(false);
	}

	/**
	 * Create the playerGui (includes all JComponenets)
	 */
	public void createGui() {

		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setRightIndent(center, 5);
		StyleConstants.setLeftIndent(center, 5);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		setCurrType("spell");

		for (int i = 0; i < maxSpellCards; i++) {
			setSpellButton();
			cardPanel.add(spellButton);
			spellButtonList.add(spellButton);
		}

		for (int i = 0; i < maxClassCards; i++) {
			setClassButton();
			classButtonList.add(classButton);

		}
		cardPanel.setPreferredSize(new Dimension(90, 190));

		bg.add(spellRadio);
		bg.add(classRadio);

		spellRadio.setOpaque(false);
		spellRadio.setBackground(Color.black);
		spellRadio.setForeground(Color.green);
		spellRadio.setFocusable(false);
		classRadio.setBackground(Color.black);
		classRadio.setForeground(Color.green);
		classRadio.setFocusable(false);
		classRadio.setOpaque(false);
		radioPanel.setLayout(new GridLayout(2, 1));
		radioPanel.add(spellRadio);
		radioPanel.add(classRadio);
		radioPanel.setOpaque(false);
		// radioPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ButtonColor));
		radioPanel.setPreferredSize(new Dimension(85, 30));

		rollDice.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ButtonColor));
		rollDice.setEnabled(false);
		rollDice.setPreferredSize(new Dimension(87, 30));
		rollDice.setContentAreaFilled(false);
		rollDice.setForeground(buttonText);

		drawCard.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ButtonColor));
		drawCard.setPreferredSize(new Dimension(87, 30));
		drawCard.setOpaque(false);
		drawCard.setContentAreaFilled(false);
		drawCard.setForeground(buttonText);
		drawCard.setFocusPainted(false);
		drawCard.setEnabled(true);

		endTurn.setPreferredSize(new Dimension(85, 30));
		endTurn.setForeground(Color.RED);
		endTurn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
		endTurn.setContentAreaFilled(false);
		endTurn.setOpaque(false);
		endTurn.setFocusPainted(false);
		// endTurn.setEnabled(false);
		setEndButton(false);

		fillLabel[0] = new JLabel();
		fillLabel[1] = new JLabel();
		fillLabel[0].setPreferredSize(new Dimension(10, 30));
		fillLabel[1].setPreferredSize(new Dimension(10, 30));
		utilityPanel.setLayout(new FlowLayout());

		// utilityPanel.add(classCard);
		// utilityPanel.add(spellRadio);
		// utilityPanel.add(classRadio);
		utilityPanel.add(radioPanel);
		utilityPanel.add(fillLabel[0]);
		utilityPanel.add(rollDice);
		utilityPanel.add(drawCard);
		utilityPanel.add(fillLabel[1]);
		utilityPanel.add(endTurn);

		makeMove.setPreferredSize(new Dimension(85, 39));
		makeMove.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		makeMove.setEnabled(false);
		plantCell.setPreferredSize(new Dimension(85, 39));
		plantCell.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));
		plantCell.setEnabled(false);

		activateCard.setEnabled(false);
		activateCard.setPreferredSize(new Dimension(85, 39));
		activateCard.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLUE));

		playerList = new JComboBox(playersPick);
		playerList.setPreferredSize(new Dimension(85, 30));
		playerList.setFocusable(false);

		description.setPreferredSize(new Dimension(80, 160));
		description.setHorizontalAlignment(SwingConstants.LEFT);
		description.setVerticalAlignment(SwingConstants.TOP);
		description.setFont(new Font(null, Font.BOLD, 11));
		description.setForeground(Color.white);
		description.setText("<html>Cells left: <br><br>Fire Cells: <br><br> Water Cells:<br><br> Earth Cells: <br><br> Wind Cells: <br><br></html>");

		activityPanel.setPreferredSize(new Dimension(90, 190));
		activityPanel.add(makeMove);
		activityPanel.add(plantCell);
		activityPanel.add(activateCard);
		activityPanel.add(playerList);
		activityPanel.add(description);

		graceBar = new JProgressBar();
		graceBar.setMinimum(MY_MINIMUM);
		graceBar.setMaximum(MY_MAXIMUM);
		graceBar.setPreferredSize(new Dimension(205, 35));
		graceBar.setForeground(Color.red);
		graceBar.setOpaque(false);
		graceBar.setStringPainted(true);

		Border border = BorderFactory.createTitledBorder(null, "Reading...", TitledBorder.LEFT, TitledBorder.TOP, null, Color.yellow);
		graceBar.setBorder(border);

		setCardDesrciption();

		textPanel.add(cardDescription);

		scroll = new JScrollPane(cardDescription);
		scroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.yellow));
		textPanel.add(scroll);
		textPanel.add(graceBar);

		drawCard.addActionListener(drawCardListener);
		activateCard.addActionListener(activateListener);
		endTurn.addActionListener(endTurnListener);
		makeMove.addActionListener(makeMoveListener);
		rollDice.addActionListener(rollDiceListener);
		plantCell.addActionListener(plantCellListener);
		spellRadio.addActionListener(radioListener);
		classRadio.addActionListener(radioListener);
		playerList.addItemListener(playerListener);

		textPanel.setOpaque(false);
		utilityPanel.setOpaque(false);
		activityPanel.setOpaque(false);
		headerPanel.setOpaque(false);
		cardPanel.setOpaque(false);

		bgPanel.setLayout(new BorderLayout());
		bgPanel.add(headerPanel, BorderLayout.NORTH);
		bgPanel.add(textPanel, BorderLayout.CENTER);
		bgPanel.add(cardPanel, BorderLayout.WEST);
		bgPanel.add(utilityPanel, BorderLayout.SOUTH);
		bgPanel.add(activityPanel, BorderLayout.EAST);

		frame.setContentPane(bgPanel);

	}

	/**
	 * Set the number on the update bar
	 * 
	 * @param newValue is the value of the number
	 */
	public void updateBar(int newValue) {
		graceBar.setValue(newValue);
	}

	/**
	 * Reset all buttons to false
	 */
	public void resetButton() {
		commonText = "";
		setMoveButton(false);
		setRollDice(false);
		setDrawCard(false);
		setPlantCell(false);
		setEndButton(false);
		rollDice.repaint();
		utilityPanel.repaint();

	}

	/**
	 * Activate cards by calling doEffect
	 * 
	 * @return true (if successful) else false
	 */
	public boolean activateCards() {
		if (currType.equals("spell")) {
			// spells.doEffect("Lethargy", this);
			// spellManager = new ActivateManager(spells, cardToTest, this);
			// spellManager.execute();
			// return spellManager.isSuccess();
			return spells.doEffect(cardToTest, this, players);
		} else if (currType.equals("class")) {
			return classes.doEffect(classToTest, spells, this);
		}
		return false; // unreachable code
	}

	/**
	 * Remove used card by setting it to "empty"
	 */
	public void removeCardFromHand() {
		for (JButton button : spellButtonList)
			if (button.getText() == deck.getSpellCardList().get(getCurrCardID()).getName()) {
				player.getSpellCards().remove(deck.getSpellCardList().get(getCurrCardID()).getName());
				button.setEnabled(false);
				button.setText("empty");
			}
		decrementCardCount();
		if (getCardCount() < maxSpellCards) {
			drawCard.setEnabled(true);
		}
		try {
			playable.setRunning(false);
		} catch (Exception e) {
		}
		activateCard.setEnabled(false);
	}

	/**
	 * Increment the spell card count cards in hand
	 */
	public void incrementCardCount() {
		cardCount += 1;
	}

	/**
	 * Increment the class card count in hand
	 */
	public void incrementClassCount() {
		classCount++;
	}

	/**
	 * Decrement the spell card count in hand
	 */
	public void decrementCardCount() {
		cardCount--;
	}

	/**
	 * Decrement the class card count in hand
	 */
	public void decrementClassCount() {
		classCount--;
	}

	/**
	 * Get respective card and pass it into Playable
	 * 
	 * @param name is the name of the card
	 */
	public void setPlayable(String name) {
		playable = new Playable(this, gamePlayState, spells, classes);
		if (currType == "spell") {
			if (spells.getCards().get(cardToTest).getTargetRequired()) {
				setPlayerList(true);
			}
			playable.setSpellName(cardToTest);
		} else if (currType == "class") {
			playable.setClassName(classToTest);
		}

		playable.start();
	}

	/**
	 * Set custom layout for spell button
	 */
	public void setSpellButton() {
		spellButton = new JButton("empty");
		spellButton.setEnabled(false);
		spellButton.setPreferredSize(new Dimension(85, 40));
		spellButton.setContentAreaFilled(false);
		spellButton.setOpaque(false);
		spellButton.setFocusable(false);
		spellButton.setForeground(buttonText);
		spellButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ButtonColor));
		spellButton.addActionListener(spellButtonListener);
	}

	/**
	 * Set the custom layout for class button
	 */
	public void setClassButton() {
		classButton = new JButton("empty");
		classButton.setEnabled(false);
		classButton.setPreferredSize(new Dimension(85, 40));
		classButton.setContentAreaFilled(false);
		classButton.setOpaque(false);
		classButton.setFocusable(false);
		classButton.setForeground(buttonText);
		classButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ButtonColor));
		classButton.addActionListener(classButtonListener);
	}

	/**
	 * Set the custom layout for card description
	 */
	public void setCardDesrciption() {
		cardDescription.setBackground(Color.black);
		cardDescription.setEditable(false);
		cardDescription.setForeground(Color.yellow);
		cardDescription.setFont(new Font("Garamond", Font.BOLD, 12));
		cardDescription.setPreferredSize(new Dimension(200, 270));
	}

	/**
	 * Store data into SpellCard class
	 * 
	 * @param split are the strings obtained from Spell.txt
	 */
	public void setSpellCard(String[] split) {

		SpellCard spellCard = new SpellCard();
		spellCard.setName(split[0]);
		spellCard.setUsage(split[1]);
		spellCard.setDescription(split[2]);
		if (split.length == 4) {
			spellCard.setRequirements(split[3]);
		}
		deck.getSpellCardList().add(spellCard);
	}

	/**
	 * Reset the text pane
	 */
	public void setNewTextPane() {
		cardDescription.setText("");
	}

	/**
	 * Set the text pane with description
	 * 
	 * @param text is the description
	 */
	public synchronized void setTextPane(String text) {
		cardDescription.setText(commonText);

		try {
			doc.insertString(doc.getLength(), text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		commonText = cardDescription.getText();
	}

	/**
	 * Set the text pane with spell card information
	 * 
	 * @param n is the index of that spell card
	 */
	public void setSpellPane(int n) {
		cardDescription.setText("");

		try {
			doc.insertString(doc.getLength(), "\n----------- Name ----------\n", null);
			doc.insertString(doc.getLength(), deck.getSpellCardList().get(n).getName(), null);
			doc.insertString(doc.getLength(), "\n\n----------- Description -----------\n", center);
			doc.insertString(doc.getLength(), deck.getSpellCardList().get(n).getDescription(), center);
			doc.insertString(doc.getLength(), "\n\n----------- Special Case -----------\n", null);
			doc.insertString(doc.getLength(), deck.getSpellCardList().get(n).getRequirements(), null);
			doc.insertString(doc.getLength(), "\n\n------------- Usage -------------\n", null);
			doc.insertString(doc.getLength(), deck.getSpellCardList().get(n).getUsage(), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Set the text pane with class card information
	 * 
	 * @param n is the index of the class card
	 */
	public void setClassPane(int n) {
		cardDescription.setText("");

		try {
			doc.insertString(doc.getLength(), "\n----------- Name ----------\n", null);
			doc.insertString(doc.getLength(), deck.getClassCardList().get(n).getName(), null);
			doc.insertString(doc.getLength(), "\n\n----------- Description -----------\n", center);
			doc.insertString(doc.getLength(), deck.getClassCardList().get(n).getDescription(), center);
			doc.insertString(doc.getLength(), "\n\n----------- Requirements -----------\n", null);
			doc.insertString(doc.getLength(), deck.getClassCardList().get(n).getRequirements(), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Set the current card selected by player
	 * 
	 * @param n is the index of the card
	 */
	public void setCurrCardID(int n) {
		currCardID = n;
	}

	/**
	 * Set the current type [spell/class]
	 * 
	 * @param name is the type in string format - "spell" or "class"
	 */
	public void setCurrType(String name) {
		currType = name;
	}

	/**
	 * Set status of end button
	 * 
	 * @param status is true or false
	 */
	public void setEndButton(boolean status) {
		endTurn.setEnabled(status);
		if (!status) {
			endTurn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		} else {
			endTurn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
		}
	}

	/**
	 * Set the status of move button
	 * 
	 * @param status is true or false
	 */
	public void setMoveButton(boolean status) {
		makeMove.setEnabled(status);
	}

	/**
	 * Set the status of roll dice button
	 * 
	 * @param status is true or false
	 */
	public void setRollDice(boolean status) {
		rollDice.setEnabled(status);
	}

	/**
	 * Set the status of draw card button
	 * 
	 * @param status is true or false
	 */
	public void setDrawCard(boolean status) {
		drawCard.setEnabled(true);
	}

	/**
	 * Set the status of plant cell button
	 * 
	 * @param status is true or false
	 */
	public void setPlantCell(boolean status) {
		plantCell.setEnabled(status);
	}

	/**
	 * Set the status of activate card button
	 * 
	 * @param status is true or false
	 */
	public void setActivateCard(boolean status) {
		activateCard.setEnabled(status);
	}

	/**
	 * Set the status of player list JComboBox
	 * 
	 * @param status is true or false
	 */
	public void setPlayerList(boolean status) {
		playerList.setEnabled(status);

	}

	/**
	 * Set the target Player
	 * 
	 * @param n is the index of the character
	 * @return targetPlayer
	 */
	public Player setTargetPlayer(int n) {
		if (n == 0) {
			targetPlayer = null;
		} else
			targetPlayer = players[n - 1];
		System.out.println(n);
		return targetPlayer;
	}

	public void setPlayerList(Player[] players) {
		this.players = players;
	}

	// Getters

	public void setClassActivate(boolean status) {
		classActivate = status;
	}

	public void setRolled(boolean status) {
		rolled = status;
	}

	public void setCurrName(String name) {
		currName = name;
	}

	/**
	 * Return the class count in hand
	 * 
	 * @return classCount
	 */
	public int getClassCount() {
		return classCount;
	}

	/**
	 * Return the spell card count in hand
	 * 
	 * @return cardCount
	 */
	public int getCardCount() {
		return cardCount;
	}

	/**
	 * Return the current index of selected card
	 * 
	 * @return currCardID
	 */
	public int getCurrCardID() {
		return currCardID;
	}

	// Getters

	public HashMap<String, Integer> getCardID() {
		return cardID;
	}

	public int getMaxSpellCards() {
		return maxSpellCards;
	}

	public JButton getDrawCard() {
		return drawCard;
	}

	public Playable getPlayable() {
		return playable;
	}

	public JButton getActivateCard() {
		return activateCard;
	}

	public ArrayList<JButton> getSpellList() {
		return spellButtonList;
	}

	public ArrayList<JButton> getClassList() {
		return classButtonList;
	}

	public Deck getDeck() {
		return deck;
	}

	public SpellFinder getSpellFinder() {
		return spells;
	}

	public String getCardToTest() {
		return cardToTest;
	}

	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public PlayerGui getGui() {
		return this;
	}

	public Player getPlayer() {
		return player;
	}

	public GamePlayState getGPS() {
		return gamePlayState;
	}

	public boolean getClassActivate() {
		return classActivate;
	}

	public boolean getRolled() {
		return rolled;
	}

	public JButton getPlantCell() {
		return plantCell;
	}

	public JButton getRollDice() {
		return rollDice;
	}

	/**
	 * Display the background image
	 */
	class BgPanel extends JPanel {
		Image bg = new ImageIcon("res/ViralBoardEnhanced4.jpg").getImage();

		public void paintComponent(Graphics g) {
			g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
		}
	}

	class DrawCardListener implements ActionListener {
		Random rand = new Random();

		public void actionPerformed(ActionEvent e) {
			if (drawCard.getText() == "Draw Spell") {
				boolean repeat = false;
				int n;
				/* Checks for deck.getRepetition() */
				do {
					n = rand.nextInt(deck.getSpellCardList().size());
					if (repeat) {
						repeat = false;
					}
					for (int i : deck.getRepetition()) {
						if (n == i) {
							repeat = true;
						}
					}
				} while (repeat);

				setSpellPane(n);
				if (getCardCount() < maxSpellCards) {
					for (JButton button : spellButtonList) {
						if (button.getText() == "empty") {
							button.setText(deck.getSpellCardList().get(n).getName());
							button.setEnabled(true);
							break;
						}
					}

					cardID.put(deck.getSpellCardList().get(n).getName(), n);
					incrementCardCount();
				}
				// setDrawCard(false);
				if (getCardCount() == maxSpellCards) {
					drawCard.setEnabled(false);
				}
				player.getSpellCards().add(deck.getSpellCardList().get(n).getName());

				System.out.println(deck.getSpellCardList().get(n).getName());
				deck.getRepetition().add(n);

				/* Recycle deck when all cards are drawn */
				if (deck.getRepetition().size() == deck.getSpellCardList().size()) {
					deck.getRepetition().clear();
				}

				// Checks for immediately activated cards.
				// String currCard = deck.getSpellCardList().get(n).getName();
				// if (spells.getCards().get(currCard).isImmediateActivate()) {
				if (spells.getCards().get(cardToTest).isImmediateActivate()) {
					AutoActivateManager aa = new AutoActivateManager(getGui(), n);
					aa.execute();
				}

			} else if (drawCard.getText() == "Draw Class") {
				boolean repeat = false;
				int n;
				/* Checks for deck.getRepetition() */
				do {
					n = rand.nextInt(deck.getClassCardList().size());
					if (repeat) {
						repeat = false;
					}
					for (int i : deck.getClassRepetition()) {
						if (n == i) {
							repeat = true;
						}
					}
				} while (repeat);

				setClassPane(n);
				if (getClassCount() < maxClassCards) {
					for (JButton button : classButtonList) {
						if (button.getText() == "empty") {
							button.setText(deck.getClassCardList().get(n).getName());
							button.setEnabled(true);
							break;
						}
					}

					classID.put(deck.getClassCardList().get(n).getName(), n);
					incrementClassCount();
				}

				// drawCard.setEnabled(false);
				// setDrawCard(false);
				if (getClassCount() == maxClassCards) {
					drawCard.setEnabled(false);
				}
				player.getClassCards().add(deck.getClassCardList().get(n).getName());
				player.getClassCards().add(classToTest);
				activateCards();
				deck.getClassRepetition().add(n);

				/* Recycle deck when all cards are drawn */
				// if (deck.getRepetition().size() == deck.getSpellCardList().size()) {
				// deck.getRepetition().clear();
				// }

			}

		}

	}

	public class SpellCardListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				playable.setRunning(false);
			} catch (Exception e1) {

			}
			playable = null;

			setSpellPane(cardID.get(e.getActionCommand()));
			setCurrCardID(cardID.get(e.getActionCommand()));
			// setCurrType("spell");

			setPlayable(e.getActionCommand());

		}
	}

	class ClassCardListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				playable.setRunning(false);
			} catch (Exception e1) {

			}
			playable = null;
			setClassPane(classID.get(e.getActionCommand()));
			setCurrCardID(classID.get(e.getActionCommand()));
			setPlayable(e.getActionCommand());
			// setCurrType("class");
			// activateCard.setEnabled(true);
		}

	}

	public class ActivateListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (currType.equals("spell")) {
				SpellActivateManager am = new SpellActivateManager(getGui());
				am.execute();
				// System.out.println("Activate: " + deck.getSpellCardList().get(getCurrCardID()).getName());
				// if (targetPlayer == null && spells.getCards().get(cardToTest).getTargetRequired()) {
				// setTextPane("Please Select A Player.\n");
				// } else if (activateCards()) {
				// removeCardFromHand();
				// for (JButton button : spellButtonList)
				// if (button.getText() == deck.getSpellCardList().get(getCurrCardID()).getName()) {
				// player.getSpellCards().remove(deck.getSpellCardList().get(getCurrCardID()).getName());
				// button.setEnabled(false);
				// button.setText("empty");
				// }
				// System.out.println(getCardCount());
				// decrementCardCount();
				// if (getCardCount() < maxSpellCards) {
				// drawCard.setEnabled(true);
				// }
				// playable.setRunning(false);
				// activateCard.setEnabled(false);
				// }

			} else if (currType.equals("class")) {
				System.out.println("Activate: " + deck.getClassCardList().get(getCurrCardID()).getName());
				setClassActivate(true);
				// activateCards();
				// for (JButton button : classButtonList)
				// if (button.getText() == deck.getClassCardList().get(getCurrCardID()).getName()) {
				// button.setEnabled(false);
				// button.setText("empty");
				// }
				// // System.out.println(getCardCount());

				if (getCardCount() == maxClassCards) {
					drawCard.setEnabled(true);
				}
				playable.setRunning(false);
				// decrementClassCount();
				activateCard.setEnabled(false);
			}
		}
	}

	class EndTurnListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			commonText = "";
			setPlantCell(false);
			setRolled(false);
			gamePlayState.resetState();
			setEndButton(false);
		}

	}

	class MakeMoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			makeMove.setEnabled(false);
			// gamePlayState.setTrackMouse(false);
			gamePlayState.setDestination();
			gamePlayState.setConfirmMove(true);
		}
	}

	class RollDiceListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setRollDice(false);
			// int roll = rand.nextInt(6)+1;
			int roll = 1;
			setTextPane("You rolled a " + roll + ".\n");
			player.setRoll(roll);
			rolled = true;

		}
	}

	class RadioListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == spellRadio) {
				setCurrType("spell");
				setActivateCard(false);
				setPlayerList(false);
				cardPanel.removeAll();

				for (JButton button : spellButtonList) {
					cardPanel.add(button);
				}
				drawCard.setText("Draw Spell");
				cardPanel.repaint();
			} else if (source == classRadio) {
				setCurrType("class");
				setActivateCard(false);
				setPlayerList(false);
				cardPanel.removeAll();

				for (JButton button : classButtonList) {
					cardPanel.add(button);
				}
				drawCard.setText("Draw Class");
				cardPanel.repaint();
			}
			try {
				playable.setRunning(false);
			} catch (Exception e1) {
			}
		}

	}

	class PlantCellListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (player.getCurrentTile().numCells < player.getCurrentTile().cellPositions.size() && player.getCellList().size() < 10) {
				boolean canPlant = true;
				// check if chkpt tile and if so, make sure other chkpts have at least one cell too.
				if (player.getCurrentTile().isLocked() && player.getCurrentTile().hasCells()) {
					for (Tile chkpt : getGPS().getDLayer().getRingList().get(7)) {
						if (!chkpt.hasCells()) {
							player.getPlayerGui().setTextPane("Cannot plant. Checkpoint is locked.\n");
							canPlant = false;
							break;
						}
					}
				}
				if (canPlant) {
					setPlantCell(false);
					// gamePlayState.setTrackMouse(false);
					gamePlayState.setConfirmCell(true);
				}

			} else if (player.getCurrentTile().numCells >= player.getCurrentTile().cellPositions.size()) {
				setTextPane("Too Many Cells On Tile!\n");
			} else {
				setTextPane("You Have No More Cells!\n");
			}
		}
	}

	class PlayerListener implements ItemListener {

		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				JComboBox combo = (JComboBox) e.getSource();
				int index = combo.getSelectedIndex();
				setTargetPlayer(index);
			}
		}

	}
	
	// Getters

	// public static void main(String[] args) {
	// PlayerGui gp = new PlayerGui();
	// gp.init("test");
	// }

}
