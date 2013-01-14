package cardgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class CardGui extends JFrame implements ActionListener {
	ArrayList<SpellCards> spellList = new ArrayList<SpellCards>();
	ArrayList<JTextArea> jList = new ArrayList<JTextArea>();
	ArrayList<Integer> repetition = new ArrayList<Integer>(); 
	Random rand = new Random();
	JLabel label;
	JLabel nameLabel;
	String[] names = { "Name", "Usage", "Description", "Requirements" };
	JTextArea description;
	Container contentPane = getContentPane();
	JPanel cardPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JButton drawCard = new JButton("Draw Card");
	JButton ruleBook = new JButton("Rule Book");
	RulesGui rules = new RulesGui();

	public static void main(String[] args) {
		CardGui cg = new CardGui();
		cg.readFile();
		cg.init();

	}

	public void init() {
		for (int i = 0; i < names.length; i++) {
			nameLabel = new JLabel(names[i]);
			nameLabel.setFont(new Font("Garamond", Font.BOLD, 11));
			nameLabel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.red));
			nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			nameLabel.setVerticalAlignment(SwingConstants.TOP);
			
			description = new JTextArea(5, 5);
			description.setEditable(false);
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			description.setFont(new Font("Garamond", Font.BOLD, 11));
			description.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.cyan));
			
			cardPanel.setLayout(new GridLayout(4, 1, 10, 10));
			cardPanel.add(nameLabel);
			cardPanel.add(description);
			jList.add(description);
		}

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ruleBook);
		buttonPanel.add(drawCard);
		
		contentPane.add(cardPanel, BorderLayout.NORTH);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		drawCard.addActionListener(this);
		ruleBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules.init();
			}
		});
		setTitle("Card Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		boolean repeat = false;
		int n;
		/* Checks for repetition */
		do{
			n = rand.nextInt(spellList.size());
			if(repeat){
				repeat = false;
			}
			for(int i : repetition){
				if(n == i){
					repeat = true;
				}
			}
		}while(repeat);
		
		jList.get(0).setText(spellList.get(n).getName());
		jList.get(1).setText(spellList.get(n).getUsage());
		jList.get(2).setText(spellList.get(n).getDescription());
		jList.get(3).setText(spellList.get(n).getRequirements());
		
		repetition.add(n);
		
		/* Recycle deck when all cards are drawn */
		if(repetition.size() == spellList.size()){
			repetition.clear();
		}
	}

	public void readFile() {
		File fileName = new File("src/cardgui/Spell.txt");
		try {
			BufferedReader fr = new BufferedReader(new FileReader(fileName));
			String sLine;
			while ((sLine = fr.readLine()) != null) {
				String[] split = sLine.split("\t");
				setSpellCards(split);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSpellCards(String[] split) {

		SpellCards spellCard = new SpellCards();
		spellCard.setName(split[0]);
		spellCard.setUsage(split[1]);
		spellCard.setDescription(split[2]);
		if (split.length == 4) {
			spellCard.setRequirements(split[3]);
		}
		spellList.add(spellCard);

	}
}
