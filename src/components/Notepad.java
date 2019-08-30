package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Notepad extends Item {

	private boolean buttonDown;
	private Font font;

	private JFrame frame;
	private JTextArea textbox;
	private JScrollPane scrollPane;

	public Notepad(int x, int y, int width, int height) {
		super(x, y, width, height);
		buttonDown = false;
		font = new Font("Courier New", 2, 30);

		instantiate();
	}

	private void instantiate() {
		frame = new JFrame("Notepad");

		textbox = new JTextArea();
		textbox.setLineWrap(true);

		scrollPane = new JScrollPane(textbox);
		scrollPane.setViewportView(textbox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		frame.add(scrollPane);
		frame.pack();
		frame.setLocation(0, 0);
		frame.setSize(250, 250);
		frame.setVisible(false);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				buttonDown = false;
			}
		});
	}

	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		font = new Font("Courier New", 2, width/5);
	}

	public void add(String s) {
		textbox.setText(textbox.getText() + s);
	}

	public boolean delete() {
		String s = textbox.getText();
		textbox.setText(s.substring(0, Math.max(0, s.length()-1)));
		if (s.substring(Math.max(0, s.length()-1)).equals(" ")
				|| s.substring(Math.max(0, s.length()-1)).equals("")) {
			return false;
		}
		return true;
	}

	public void click() {
		buttonDown = !buttonDown;
		if (buttonDown) {
			frame.setVisible(true);
		} else {
			frame.setVisible(false);
		}
	}
	
	public boolean buttonDown() {
		return buttonDown;
	}

	public void draw(Graphics g) {
		g.setFont(font);

		if (buttonDown) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x, y, width, height);
			g.setColor(Color.WHITE);
			g.drawString("NOTEPAD", x+width/12, y+5*height/8);
		} else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x, y, width, height);
			g.setColor(Color.DARK_GRAY);
			g.drawString("NOTEPAD", x+width/12, y+5*height/8);
		}

		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);

	}
}
