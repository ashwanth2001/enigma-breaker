package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Rotors extends Item {
	
	private int[][] details;
	private int[] locs;
	private int[] positions;
	
	private String[] numerals;
	Font font;
	
	public Rotors(int x, int y, int width, int height) {
		super(x, y, width, height);
		locs = new int[3];
		positions = new int[3];
		numerals = new String[5];
		
		instantiate();
	}
	
	private void instantiate() {
		int[][] temp = {{4, 10, 12, 5, 11, 6, 3, 16, 21, 25, 13, 19, 14, 22, 24, 7, 23, 20, 18, 15, 0, 8, 1, 17, 2, 9}, 
				{0, 9, 3, 10, 18, 8, 17, 20, 23, 1, 11, 7, 22, 19, 12, 2, 16, 6, 25, 13, 15, 24, 5, 21, 14, 4}, 
				{1, 3, 5, 7, 9, 11, 2, 15, 17, 19, 23, 21, 25, 13, 24, 4, 8, 22, 6, 0, 10, 12, 20, 18, 16, 14}, 
				{4, 18, 14, 21, 15, 25, 9, 0, 24, 16, 20, 8, 17, 7, 23, 11, 13, 5, 19, 6, 10, 3, 2, 12, 22, 1}, 
				{21, 25, 1, 17, 6, 8, 19, 24, 20, 15, 18, 3, 13, 7, 11, 23, 0, 22, 12, 9, 16, 14, 5, 4, 2, 10}
		};
		details = temp;
		
		locs[0] = 0;
		locs[1] = 1;
		locs[2] = 2;
		
		numerals[0] = "I";
		numerals[1] = "II";
		numerals[2] = "III";
		numerals[3] = "IV";
		numerals[4] = "V";
		
		font = new Font("Courier New", 0, 30);

	}
	
	public void updateDim(int x, int y, int width, int height) {
		super.updateDim(x, y, width, height);
		font = new Font("Courier New", 0, width/15);
	}
	
	public int run(int n) {
		turn();
		
		int ret = n;
		for(int i = 0; i<3; i++) {
			ret = details[locs[i]][(ret+positions[i])%26];
		}
		return ret;
	}
	
	public void click(int mX, int mY) {
		int tempx;
		double div = width/7;
		for(int i = 0; i<3; i++) {
			tempx = x+width-(int)(2*div+2*div*i);
			if(mX>=tempx&&mX<=tempx+div && mY>=y+div&&mY<=y+2*div) {
				while(true) {
					locs[i]++;
					locs[i]%=5;
					if(locs[i]!=locs[(i+1)%3]&&locs[i]!=locs[(i+2)%3])
						break;
				}
			}
			else if(mX>=tempx&&mX<=tempx+div && mY>=y+3*div&&mY<=y+7*div/2) {
				positions[i]+=25;
				positions[i]%=26;
			}
			else if(mX>=tempx&&mX<=tempx+div && mY>=y+11*div/2&&mY<=y+6*div) {
				positions[i]++;
				positions[i]%=26;
			}
			
		}
	}
	
	public void setLocs(int[] locs) {
		this.locs = locs;
	}
	
	public void setPos(int[] pos) {
		this.positions = pos;
	}
	
	public void setPos(int pos) {
		double total = Math.pow(26, 3);
		int modPos = (int)(((pos%total)+total)%total);
		positions[0] = modPos%26;
		positions[1] = (modPos/26)%26;
		positions[2] = (int) (modPos/Math.pow(26, 2));
	}
	
	public void turn() {
		positions[0]++;
		if(positions[0]>25) {
			positions[0]%=26;
			positions[1]++;
			if(positions[1]>25) {
				positions[1]%=26;
				positions[2]+=1;
				positions[2]%=26;
			}
		}
	}
	
	public void back() {
		positions[0]+=25;
		positions[0]%=26;
		if(positions[0]==25) {
			positions[1]+=25;
			positions[1]%=26;
			if(positions[1]==25) {
				positions[2]+=25;
				positions[2]%=26;
			}
		}
	}
	
	public int reverseRun(int n) {
		int ret = n;
		int ind = 0;
		for(int i = 2; i>=0; i--) {
			ind = 0;
			while(details[locs[i]][ind]!=ret)
				ind++;
			ret = (ind-positions[i]+26)%26;
		}
		return ret;	
	}
	
	public String toString() {
		String ret = "";
		
		for(int i = 2; i>=0; i--) {
			ret+=numerals[locs[i]];
			ret+=" ";
			ret+=Integer.toString(positions[i]+1);
			ret+=" ";
		}
		
		return ret;
	}
	
	public void draw(Graphics g) {
		drawBack(g);
		for(int i = 0; i<3; i++) {
			drawRotor(g, i);
		}
	}
	
	private void drawBack(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}
	
	private void drawRotor(Graphics g, int loc) {
		g.setFont(font);
		double div = width/7.0;
		int tempx = x+width-(int)(2*div+2*div*loc);
		
		g.setColor(Color.BLACK);
		g.fillRect(tempx, y+(int)div, (int)div, (int)div);
		
		g.setColor(Color.WHITE);
		String num = numerals[locs[loc]];
		int len = 4-num.length();
		g.drawString(num, tempx+(int)(len*9*div/80), y+(int)(div+7*div/11));
		
		g.setColor(Color.WHITE);
		g.fillRect(tempx, y+(int)(3*div), (int)div, (int)(3*div));
		
		g.setColor(Color.BLACK);
		g.drawRect(tempx, y+(int)(3*div), (int)div, (int)(3*div));
		g.drawRect(tempx, y+(int)(4*div), (int)div, (int)div);
		
		g.setColor(Color.BLACK);
		g.drawString(format((positions[loc]+25)%26+1), tempx+(int)(9*div/40), y+(int)(3*div+7*div/11));
		g.drawString(format(positions[loc]+1), tempx+(int)(9*div/40), y+(int)(4*div+7*div/11));
		g.drawString(format((positions[loc]+1)%26+1), tempx+(int)(9*div/40), y+(int)(5*div+7*div/11));
		
		g.setColor(Color.BLACK);
		g.fillRect(tempx, y+(int)(3*div), (int)div, (int)(div/2));
		g.fillRect(tempx, y+(int)(11*div/2), (int)div, (int)(div/2+2));
		
		g.setColor(Color.WHITE);
		int[] xtr = {tempx, (int)(tempx+div), (int)(tempx+div/2)};
		int[] ytr1 = {y+(int)(7*div/2-div/8), y+(int)(7*div/2-div/8), y+(int)(3*div)};
		int[] ytr2 = {y+(int)(11*div/2+div/8), y+(int)(11*div/2+div/8), y+(int)(6*div)};
		g.fillPolygon(xtr, ytr1, 3);
		g.fillPolygon(xtr, ytr2, 3);
	}
	
	private String format(int x) {
		String ret = Integer.toString(x);
		if(x<10) {
			ret = "0" + ret;
		}
		return ret;
	}
}
