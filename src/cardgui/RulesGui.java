package cardgui;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RulesGui extends JFrame {
	JLabel label = new JLabel();

	public void init() {

		label.setText("<html>1. Player(s) with most checkpoint cells win.<br><br>" +
				"2. Only two cells can be on a tile at one time (excluding checkpoints)<br><br>" +
				"3. Able to move right and left on outer ring<br><br>" +
				"4. Checkpoints with 4 cells are immune (to most spells)<br><br>" +
				"5. Fourth cell can be planted on a checkpoint only when all checkpoints have at least one cell<br><br>" +
				"6. Trading is allowed. But players cannot exceed the cell limit<br><br>" +
				"7. If all board cells are gone, you lose your checkpoint cells<br><br>" +
				"8. If your name is Howard, you lose.</html>");
		label.setFont(new Font("Garamond", Font.BOLD, 11));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.TOP);
	
		add(label);
		setTitle("Rule Book");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(280, 320);
		setVisible(true);

	}
}
