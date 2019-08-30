package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Plugboard extends Item {

	private int[] pairs;
	private int clicked;
	private int[][] locs;
	private boolean mouseDown;
	int mouseX, mouseY;
	private Font font;
	
	private final int[] board = {16, 22, 4, 17, 19, 24, 20, 8, 14, 15, 0, 18, 3, 5, 6, 7, 9, 10, 11, 25, 23, 2, 21, 1, 13, 12};
	
	public Plugboard(int x, int y, int width, int height) {
		super(x, y, width, height);
		pairs = new int[26];
		locs = new int[26][4];
		setPairs();
		clicked = -1;
		mouseX = 0;
		mouseY = 0;
		font = new Font("Courier New", 1, 30);
	}
	
	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		font = new Font("Courier New", 1, width/20);
	}
	
	public void mouseInfo(boolean mD, int mX, int mY) {
		mouseX = mX;
		mouseY = mY;
		if(!mouseDown&&mD) {
			click();
			mouseDown = true;
		}
		else if(mouseDown&&!mD) {
			unclick();
			mouseDown = false;
		}
	}
	
	public void set(int[] pairs) {
		this.pairs = pairs;
	}
	
	private void click() {
		for(int i = 0; i<26; i++) {
			if(pairs[i]>=0) {
				if(mouseX>=locs[i][0]&&mouseX<=locs[i][0]+locs[i][2] && mouseY>=locs[i][1]&&mouseY<=locs[i][1]+locs[i][3]) {
					clicked = i;
					break;
				}
			}
		}
	}
	
	private void unclick() {
		for(int i = 0; i<26; i++) {
			if(pairs[i]<0) {
				if(mouseX>=locs[i][0]&&mouseX<=locs[i][0]+locs[i][2] && mouseY>=locs[i][1]&&mouseY<=locs[i][1]+locs[i][3]) {
					pairs[i] = pairs[clicked];
					pairs[pairs[clicked]] = i;
					pairs[clicked] = -1;
					clicked = -1;
					break;
				}
			}
		}
		clicked = -1;
	}
	
	private void setPairs() {
		for(int i = 0; i<20; i++) {
			pairs[i] = i+1-2*(i%2);
		}
		for(int i = 20; i<26; i++) {
			pairs[i] = -1;
		}
	}
	public int run(int n) {
		if(pairs[n] == -1) {
			return n;
		}
		else {
			return pairs[n];
		}
	}
	
	public String toString() {
		String ret = "";
		for(int i = 0; i<26; i++) {
			if(pairs[i]>i) {
				ret+=Character.toString((char)(i+65))+Character.toString((char)(pairs[i]+65));
				ret+=" ";
			}
		}
		return ret;
	}
	
	public void draw(Graphics g) {
		drawBack(g);
		drawConnections(g);
		drawPlugs(g);
	}
	
	private void drawBack(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);
	}
	
	private void drawPlugs(Graphics g) {
		double div = (int)(width/12.75);

		g.setFont(font);
		
		int curr = 0;
		int tempx = 0, tempy = 0;
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<Math.min(10, 11-i*2); j++) {
				tempx = (int)(x+((j+1)*div/4)+j*div+i*div/2);
				tempy = (int)(y+((i+1.5)*div/4)+i*div+i*height/6);
				
				g.setColor(Color.WHITE);
				g.drawString(Character.toString((char)(board[curr]+65)), (int)(tempx+div/3), (int)(tempy+2*div/3));
				g.drawOval(tempx, tempy, (int)div, (int)div);
				
				if(board[curr] == clicked) {
					int[] temploc = {(int)(mouseX-div/4), (int)(mouseY-7*div/12), (int)(div/2), (int)(7*div/6)};
					locs[board[curr]] = temploc;
					
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(locs[board[curr]][0], locs[board[curr]][1], locs[board[curr]][2], locs[board[curr]][3]);
					g.setColor(Color.BLACK);
					g.drawRect(locs[board[curr]][0], locs[board[curr]][1], locs[board[curr]][2], locs[board[curr]][3]);
					
					g.setColor(Color.WHITE);
					g.drawOval((int)(tempx+div/3), (int)(tempy+4*div/3), (int)(div/3), (int)(div/3));
					g.drawOval((int)(tempx+div/3), (int)(tempy+6*div/3), (int)(div/3), (int)(div/3));
				}
				else if(pairs[board[curr]]>=0) {
					int[] temploc = {(int)(tempx+div/4), (int)(tempy+5*div/4), (int)(div/2), (int)(7*div/6)};
					locs[board[curr]] = temploc;
					
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(locs[board[curr]][0], locs[board[curr]][1], locs[board[curr]][2], locs[board[curr]][3]);
					g.setColor(Color.BLACK);
					g.drawRect(locs[board[curr]][0], locs[board[curr]][1], locs[board[curr]][2], locs[board[curr]][3]);
				}
				else {
					int[] temploc = {(int)(tempx+div/4), (int)(tempy+5*div/4), (int)(div/2), (int)(7*div/6)};
					locs[board[curr]] = temploc;
					
					g.setColor(Color.WHITE);
					g.drawOval((int)(tempx+div/3), (int)(tempy+4*div/3), (int)(div/3), (int)(div/3));
					g.drawOval((int)(tempx+div/3), (int)(tempy+6*div/3), (int)(div/3), (int)(div/3));
				}
				
				
				curr++;
			}
		}
	}
	
	private void drawConnections(Graphics g) {
		g.setColor(Color.WHITE);
		for(int i = 0; i<26; i++) {
			if(pairs[i]>i) {
				g.drawLine(locs[i][0]+locs[i][2]/2, locs[i][1]+locs[i][3]/2, locs[pairs[i]][0]+locs[pairs[i]][2]/2, locs[pairs[i]][1]+locs[pairs[i]][3]/2);
			}
		}
	}
}
