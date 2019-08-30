package components;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Machine extends Item {
	
	private Rotors rotors;
	private Reflectors reflectors;
	private Plugboard plugboard;
	private Notepad notepad;
	
	private int state;
	private boolean mouseDown;
	private String code;
	
	private Font font;
	private boolean uButtonDown;
	
	private String[] numerals;
	
	JFrame upFrame;
	
	public Machine(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		rotors = new Rotors(x, y, width, height);
		plugboard = new Plugboard(x, y+height/3, width, 2*height/3);
		reflectors = new Reflectors(x+width/3, y, width/3, height/3);
		notepad = new Notepad(x, y+height/6, width/3, height/6);
		
		mouseDown = false;
		code = "";
		font = new Font("Courier New", 1, 30);
		
		numerals = new String[5];
		
		numerals[0] = "I";
		numerals[1] = "II";
		numerals[2] = "III";
		numerals[3] = "IV";
		numerals[4] = "V";
	}
	
	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		rotors.updateDim(x+width-height/3, y, height/3, height/3);
		reflectors.updateDim(x+width/3, y, width/3, height/3);
		plugboard.updateDim(x, y+height/3, width, 2*height/3);
		notepad.updateDim(x, y+height/6, width/3, height/6);
		
		font = new Font("Courier New", 1, width/30);
	}
	
	public void updateState(int state) {
		this.state = state;
	}
	
	public void updateMouse(boolean mD, int mX, int mY) {
		if(state==1) {
			plugboard.mouseInfo(mD, mX, mY);
		}
		
		if(mD==mouseDown)
			return;
		mouseDown = mD;
		
		if(!mouseDown)
			return;
		
		if(mX>=x&&mX<=x+width/3 && mY>=y+height/6&&mY<=y+height/3) {
			notepad.click();
		}
		else if (mX>=x+width/3&&mX<=x+2*width/3 && mY>=y&&mY<=y+height/3) {
			reflectors.click(mX, mY);
		}
		else if (mX>=x+2*width/3&&mX<=x+width && mY>=y&&mY<=y+height/3) {
			rotors.click(mX, mY);
		}
		else if(state == 1 && mX>=x+39*width/50&&mX<=x+49*width/50 && mY>=y+39*height/50&&mY<=y+39*height/50+height/11) {
			downloadSettings();
		}
		else if(state == 1 && mX>=x+39*width/50&&mX<=x+49*width/50 && mY>=y+39*height/50+height/9&&mY<=y+39*height/50+height/9+height/11) {
			openUpload();
		}
	}
	
	private void downloadSettings() {
		String s = rotors.toString();
		s+=reflectors.toString();
		s+=plugboard.toString();
		notepad.add(s);
		if(!notepad.buttonDown()) {
			notepad.click();
		}
	}
	
	private void openUpload() {
		if(uButtonDown) {
			upFrame.setVisible(true);
			return;
		}
		uButtonDown = true;
		
		upFrame = new JFrame();
		JTextField field = new JTextField(25);
		JButton button = new JButton("Upload");
		
		button.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){
				uploadSettings(field.getText());
				upFrame.setVisible(false);
				uButtonDown = false;
			}
		});
		
		Container contentPane = upFrame.getContentPane();
		FlowLayout layout = new FlowLayout();
		contentPane.setLayout(layout);
		contentPane.add(field);
		contentPane.add(button);
		
		upFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				uButtonDown = false;
			}
		});
		
		upFrame.pack();
		upFrame.setLocation(0, 0);
		upFrame.setSize(450, 60);
		upFrame.setVisible(true);
	}
	
	private void uploadSettings(String s) {
		String text = s.trim();
		String temp;
		
		int[] locs = new int[3];
		int[] pos = new int[3];
		int[] tempPlugs = new int[26];
		int ref;
		
		for(int i = 0; i<3; i++) {
			temp = text.substring(0, text.indexOf(" "));
			
			for(int j = 0; j<5; j++) {
				if(temp.equals(numerals[j])) {
					locs[i] = j;
					break;
				}
			}
			
			text = text.substring(text.indexOf(" ")).trim();
			pos[i] = (Integer.parseInt(text.substring(0, text.indexOf(" ")))+25)%26;
			text = text.substring(text.indexOf(" ")).trim();
		}
		
		ref = text.charAt(0)-66;
		text = text.substring(text.indexOf(" ")).trim();
		
		for(int i = 0; i<26; i++) {
			tempPlugs[i] = -1;
		}
		
		for(int i = 0; i<10; i++) {
			temp = text.substring(0, text.indexOf(" "));
			tempPlugs[temp.charAt(0)-65] = temp.charAt(1)-65;
			tempPlugs[temp.charAt(1)-65] = temp.charAt(0)-65;
			text = text.substring(text.indexOf(" ")).trim() + " ";
		}
		
		set(locs, ref, pos);
		setPB(tempPlugs);
	}
	
	public void space() {
		notepad.add(" ");
	}
	
	public void back() {
		if(notepad.delete()) {
			rotors.back();
		}
	}
	
	public void enter() {
		notepad.add("\n");
	}
	
	public int run(int n) {
		int ret = plugboard.run(n);
		ret = rotors.run(ret);
		ret = reflectors.run(ret);
		ret = rotors.reverseRun(ret);
		ret = plugboard.run(ret);
		
		notepad.add(Character.toString((char)(ret+65)));
		return ret;
	}
	
	public void set(int[] locs, int r, int pos) {
		set(locs, r);
		rotors.setPos(pos);
	}
	
	public void set(int[] locs, int r, int[] pos) {
		set(locs, r);
		rotors.setPos(pos);
	}
	
	public void set(int[] locs, int r) {
		rotors.setLocs(locs);
		reflectors.set(r);
	}
	
	public void setPB(int[] pairs) {
		plugboard.set(pairs);
	}
	
	public int runNoPb(int n, int rotorPos) {
		rotors.setPos(rotorPos);
		int ret = rotors.run(n);
		ret = reflectors.run(ret);
		ret = rotors.reverseRun(ret);
		return ret;
	}
	
	public void draw(Graphics g) {
		if(state==1) {
			plugboard.draw(g);
			drawSettingsButton(g);
		}
		
		rotors.draw(g);
		reflectors.draw(g);
		notepad.draw(g);
	}
	
	private void drawSettingsButton(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x+39*width/50, y+39*height/50, width/5, height/11);
		g.fillRect(x+39*width/50, y+39*height/50+height/9, width/5, height/11);
		
		g.setColor(Color.WHITE);
		g.drawRect(x+39*width/50, y+39*height/50, width/5, height/11);
		g.drawRect(x+39*width/50, y+39*height/50+height/9, width/5, height/11);
		
		g.setFont(font);
		g.drawString("DOWNLOAD", x+4*width/5, y+4*height/5+height/50);
		g.drawString("SETTINGS", x+4*width/5, y+4*height/5+height/20);
		
		g.drawString("UPLOAD", x+4*width/5+width/50, y+4*height/5+height/9+height/50);
		g.drawString("SETTINGS", x+4*width/5, y+4*height/5+height/9+height/20);
	}
}
