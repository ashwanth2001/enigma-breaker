package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Reflectors extends Item{

	private int[][] details;
	private int selected;
	
	private Font font1, font2;
	
	public Reflectors(int x, int y, int width, int height) {
		super(x, y, width, height);
		instantiate();
	}
	
	private void instantiate() {
		int[][] temp = {{24, 17, 20, 7, 16, 18, 11, 3, 15, 23, 13, 6, 14, 10, 12, 8, 4, 1, 5, 25, 2, 22, 21, 9, 0, 19}, 
				{5, 21, 15, 9, 8, 0, 14, 24, 4, 3, 17, 25, 23, 22, 6, 2, 19, 10, 20, 16, 18, 1, 13, 12, 7, 11}
		};
		details = temp;
		
		selected = 0;
		font1 = new Font("Courier New", 1, 30);
		font2 = new Font("Courier New", 1, 30);
	}
	
	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		font1 = new Font("Courier New", 1, width/8);
		font2 = new Font("Courier New", 1, width/5);
	}
	
	public void click(int mX, int mY) {		
		if(mX>=x+width/7&&mX<=x+3*width/7 && mY>=y+height/2&&mY<=y+height/2+2*height/7) {
			selected = 0;
		}
		else if(mX>=x+4*width/7&&mX<=x+6*width/7 && mY>=y+height/2&&mY<=y+height/2+2*height/7) {
			selected = 1;
		}
	}
	
	public int run(int n) {
		return details[selected][n];
	}
	
	public void set(int r) {
		selected = r;
	}
	
	public String toString() {
		return Character.toString((char)(selected+66)) + " ";
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x+width/7, y+height/2, 2*width/7, 2*height/7);
		g.drawRect(x+4*width/7, y+height/2, 2*width/7, 2*height/7);
		g.fillRect(x+width/7+selected*3*width/7, y+height/2, 2*width/7, 2*height/7);
		
		g.setFont(font1);
		g.drawString("REFLECTORS", x+2*width/15, y+height/3);
		
		g.setFont(font2);
		if(selected == 0) {
			g.drawString("C", x+2*width/9+3*width/7, y+7*height/10);
			g.setColor(Color.WHITE);
			g.drawString("B", x+2*width/9, y+7*height/10);
		}
		else {
			g.drawString("B", x+2*width/9, y+7*height/10);
			g.setColor(Color.WHITE);
			g.drawString("C", x+2*width/9+3*width/7, y+7*height/10);
		}
	}
}
