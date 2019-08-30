package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Keyboard extends Item {
	
	private Color c1, c2, text1, text2, b, back;
	private boolean[] pressed;
	private final int[] board = {16, 22, 4, 17, 19, 24, 20, 8, 14, 15, 0, 18, 3, 5, 6, 7, 9, 10, 11, 25, 23, 2, 21, 1, 13, 12};
	
	private Font font;
	
	public Keyboard(int x, int y, int width, int height, Color c1, Color c2, Color text1, Color text2, Color back) {
		super(x, y, width, height);
		this.c1 = c1;
		this.c2 = c2;
		this.text1 = text1;
		this.text2 = text2;
		this.back = back;
		
		pressed = new boolean[26];
		
		font = new Font("Courier New", 1, 30);
	}
	
	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		font = new Font("Courier New", 1, width/20);
	}
	
	public void pressed(int x, boolean b) {
		if(x>=0) {
			pressed[x] = b;
		}
		else {
			for(int i = 0; i<26; i++) {
				pressed[i] = false;
			}
		}
	}
	
	public void draw(Graphics g) {
		drawBack(g);
		drawKeys(g);
	}
	
	private void drawBack(Graphics g) {
		g.setColor(back);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
	
	private void drawKeys(Graphics g) {
		double div = (int)(width/12.75);

		g.setFont(font);
		g.setColor(c1);
		
		int curr = 0;
		int tempx, tempy;
		for(int i = 0; i<3; i++) {
			for(int j = 0; j<Math.min(10, 11-i*2); j++) {
				tempx = (int)(x+((j+1)*div/4)+j*div+i*div/2);
				tempy = (int)(y+((i+2)*div/4)+i*div);
				if(pressed[board[curr]]) {
					g.setColor(c2);
					g.fillOval(tempx, tempy, (int)div, (int)div);
					g.setColor(text2);
					g.drawString(Character.toString((char)(board[curr]+65)), (int)(tempx+div/3), (int)(tempy+2*div/3));
				}
				else {
					g.setColor(c1);
					g.fillOval(tempx, tempy, (int)div, (int)div);
					g.setColor(text1);
					g.drawString(Character.toString((char)(board[curr]+65)), (int)(tempx+div/3), (int)(tempy+2*div/3));
				}
				g.setColor(Color.BLACK);
				g.drawOval(tempx, tempy, (int)div, (int)div);
				curr++;
			}
		}
	}
}
