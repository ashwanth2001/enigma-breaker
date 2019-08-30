package breaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import components.Machine;

public class Decryptor extends JProgressBar{

	private Machine machine;
	private boolean[][] plugStore;
	private int[] plugs;
	private int plugCount, noCount;
	
	private ArrayList<int[]> occ;
	private ArrayList<int[]> occClone;
	private ArrayList<ArrayList<Integer>> occLoc;
	private ArrayList<ArrayList<Integer>> occLocClone;
	private int[] occOpp;
	private int[] occOppClone;
	
	private int[] currSetting;
	
	private ArrayList<int[]> outputPlugs;
	private ArrayList<int[]> outputSettings;
	private ArrayList<int[]> outputCounts;
	
	private String outputText;
	
	private int tempRotorPos;
	private String sub;
	private String input, section, crib;
	private ArrayList<Integer> spaceLocs;
	private int subIndex;
	
	private int progress;
	private int runs;
	
	private String[] numerals;
	
	public Decryptor() {
		machine = new Machine(0, 0, 0, 0);
		
		plugStore = new boolean[26][26];
		plugs = new int[26];
		plugCount = 0;
		noCount = 0;
		progress = 0;
		
		occ = new ArrayList<int[]>();
		occClone = new ArrayList<int[]>();
		occLoc = new ArrayList<ArrayList<Integer>>();
		occLocClone = new ArrayList<ArrayList<Integer>>();
		occOpp = new int[26];
		occOppClone = new int[26];
		
		currSetting = new int[6]; 
		outputSettings = new ArrayList<int[]>();
		outputPlugs = new ArrayList<int[]>();
		outputCounts = new ArrayList<int[]>();
		outputText = "";
		
		spaceLocs = new ArrayList<Integer>();		
		numerals = new String[5];
		
		runs = 0;
		
		numerals[0] = "I";
		numerals[1] = "II";
		numerals[2] = "III";
		numerals[3] = "IV";
		numerals[4] = "V";
	}
	
	public int getProgress() {
		return progress;
	}

	public void run(String in, String sect, String c) {		
		runs++;
		
		input = in.toUpperCase();
		section = sect.toUpperCase();
		crib = c.toUpperCase();
		
		subIndex = input.indexOf(section);
		
		spaceLocs = new ArrayList<Integer>();
		for(int i = 0;; i++) {
			if(input.indexOf(" ")>=0) {
				spaceLocs.add(input.indexOf(" ")+i);
				input = input.substring(0, input.indexOf(" ")) + input.substring(input.indexOf(" ")+1);
			}
			else {
				break;
			}
		}
		
		section = section.replaceAll("//s+","");
		crib = crib.replaceAll("//s+","");
		
		if(subIndex<0) {
			outputText = "INVALID";
			System.out.println("invalid");
			return;
		}
		
		int numPos = section.length() - crib.length() + 1;
		int totalShifts = numPos;
		boolean[] checkPos = new boolean[numPos];
		for(int i = 0; i < numPos; i++) {
			checkPos[i] = true;
			for(int j = 0; j < crib.length(); j++) {
				if(crib.charAt(j) == section.charAt(j + i)) {
					checkPos[i] = false;
					totalShifts--;
					break;
				}
			}
		}
		
		int currShift = 0;
		
		outputSettings = new ArrayList<int[]>();
		outputPlugs = new ArrayList<int[]>();
		outputCounts = new ArrayList<int[]>();
		outputText = "";
		
		shifts: for(int i = 0; i<numPos; i++) {
			if(!checkPos[i]) {
				continue;
			}
			sub = section.substring(i, i+crib.length());
			set();
			currSetting[0] = i;
			
			refs: for(int j = 0; j<2; j++) {
				currSetting[1] = j;
				
				perms: for(int k = 0; k<60; k++) {
					progress = (int)(100*(currShift*120+j*60+k)/(120.0*totalShifts));
					setValue(progress);
					
					int[] locs = setLocs(k);
					currSetting[2] = locs[0];
					currSetting[3] = locs[1];
					currSetting[4] = locs[2];
					
					rotors: for(int m = 0; m<Math.pow(26, 3); m++) {
						reset();
						machine.set(locs, j);
						tempRotorPos = m;
						currSetting[5] = tempRotorPos;
						checkAssume();
					}
				}
			}
			
			currShift++;
		}
		
		printResults();
		setValue(100);
	}
	
	private void set() {
		occ = new ArrayList<int[]>();
		occLoc = new ArrayList<ArrayList<Integer>>();
		
		for(int j = 0; j<26; j++) {
			occ.add(new int[]{j, 0});
			occLoc.add(new ArrayList<Integer>());
		}
		for(int j = 0; j<crib.length(); j++) {
			occLoc.get(crib.charAt(j)-65).add(j);
			occ.get(crib.charAt(j)-65)[1]++;
			
			occLoc.get(sub.charAt(j)-65).add(j);
			occ.get(sub.charAt(j)-65)[1]++;
		}
		
		Collections.sort(occ, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return b[1]-a[1];
			}
		});
		
		for(int j = 0; j<26; j++) {
			occOpp[occ.get(j)[0]] = j;
		}
	}
	
	private int[] setLocs(int n) {
		int[] ret = new int[3];
		ret[0] = n/12;
		ret[1] = (n%12)/3+1;
		ret[2] = (n%3)+ret[1]+1+(n%3+ret[1]-1)/3;
		ret[1] = (ret[1]+ret[0])%5;
		ret[2] = (ret[2]+ret[0])%5;
		return ret;
	}
	
	private void reset() {
		plugCount = 0;
		noCount = 0;
		plugs = new int[26];
		for(int i = 0; i<26; i++) {
			plugs[i] = -1;
		}
				
		occClone =  new ArrayList<int[]>();
		for(int i = 0; i<occ.size(); i++) {
			occClone.add(new int[0]);
			occClone.set(i, occ.get(i).clone());
		}
		
		occLocClone = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<occLoc.size(); i++) {
			occLocClone.add(new ArrayList<Integer>());
			occLocClone.set(i, (ArrayList<Integer>) occLoc.get(i).clone());
		}
		
		plugStore = new boolean[26][26];
		occOppClone = occOpp.clone();
	}
	
	private void checkAssume() {
		int[] plugsSave = plugs.clone();		
		int plugCountSave = plugCount;
		int noCountSave = noCount;
		int[] occOppCloneSave = occOppClone.clone();
		
		ArrayList<int[]> occCloneSave =  new ArrayList<int[]>();
		ArrayList<ArrayList<Integer>> occLocCloneSave = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i<occClone.size(); i++) {
			occCloneSave.add(new int[0]);
			occCloneSave.set(i, occClone.get(i).clone());
		}
		for(int i = 0; i<occLocClone.size(); i++) {
			occLocCloneSave.add(new ArrayList<Integer>());
			occLocCloneSave.set(i, (ArrayList<Integer>) occLocClone.get(i).clone());
		}
		
		for(int i = 0; i<26; i++) {
			plugs = plugsSave.clone();
			plugCount = plugCountSave;
			noCount = noCountSave;
			occOppClone = occOppCloneSave.clone();
			
			occClone =  new ArrayList<int[]>();
			occLocClone = new ArrayList<ArrayList<Integer>>();
			
			for(int j = 0; j<occCloneSave.size(); j++) {
				occClone.add(new int[0]);
				occClone.set(j, occCloneSave.get(j).clone());
			}
			for(int j = 0; j<occLocCloneSave.size(); j++) {
				occLocClone.add(new ArrayList<Integer>());
				occLocClone.set(j, (ArrayList<Integer>) occLocCloneSave.get(j).clone());
			}
			
			int p = occClone.get(0)[0];
			
			if(!(plugs[p]==i) && (plugs[p]>=0 || plugs[i]>=0)) {
				continue;
			}
						
			if(plugs[p]!=i) {
				if(p==i) {
					noCount++;
				}
				else {
					plugCount+=2;
				}
			}
			
			plugStore[p][i] = true;
			plugStore[i][p] = true;
			plugs[i] = p;
			plugs[p] = i;
			
			checkDerive(0);
		}
	}
	
	private void checkDerive(int index) {
		int n = occClone.get(index)[0];
		int ret = machine.runNoPb(plugs[n], tempRotorPos+occLocClone.get(n).get(0));
		int p = -1;
		
		if(crib.charAt(occLocClone.get(n).get(0))-65 == n) {
			p = sub.charAt(occLocClone.get(n).get(0))-65;
		}
		else {
			p = crib.charAt(occLocClone.get(n).get(0))-65;
		}
		
		if(!(plugs[p]==ret) && (plugs[p]>=0 || plugs[ret]>=0)) {
			return;
		}
		
		if(plugs[p]!=ret) {
			if(p==ret) {
				noCount++;
			}
			else {
				plugCount+=2;
			}
		}
				
		plugStore[p][ret] = true;
		plugStore[ret][p] = true;
		plugs[ret] = p;
		plugs[p] = ret;
		
		occLocClone.get(p).remove(Integer.valueOf(occLocClone.get(n).get(0)));
		occLocClone.get(n).remove(0);
		occClone.get(occOppClone[p])[1]--;
		occClone.get(occOppClone[n])[1]--;

		if(occLocClone.get(n).isEmpty()) {
			Collections.sort(occClone, new Comparator<int[]>() {
				@Override
				public int compare(int[] a, int[] b) {
					return b[1]-a[1];
				}
			});
			
			for(int j = 0; j<26; j++) {
				occOppClone[occClone.get(j)[0]] = j;
			}
			
			for(int i = 0; i<26; i++) {
				if(occClone.get(i)[1]==0)
					break;
				if(plugs[occClone.get(i)[0]]>=0) {
					checkDerive(i);
					return;
				}
			}
			if(occClone.get(0)[1]!=0) {
				checkAssume();
				return;
			}
			if(plugCount>20 || noCount>6) {
				return;
			}
			outputPlugs.add(plugs.clone());
			outputSettings.add(currSetting.clone());
			outputCounts.add(new int[]{plugCount, noCount});
			return;
		}
		else {
			checkDerive(index);
			return;
		}
	}
	
	private void printResults() {
		for(int i = 0; i<outputPlugs.size(); i++) {
			runSolutions(i);
		}
		
		frameSetup();
	}
	
	private void runSolutions(int index) {
		int[] counts = outputCounts.get(index);
		int pairs = (20-counts[0])/2;
		int total = 26-(counts[0]+counts[1]);
		combinations(total, pairs*2, index);
	}
	
	private void combinations(int n, int r, int pIndex) {
		boolean[] ret = new boolean[n];
		combinationUtil(ret, 0, n - 1, 0, r, pIndex);
	}
	
	private void combinationUtil(boolean[] ret, int start, int end, int index, int r, int pIndex) {
		if (index == r) {
			calcSettings(pIndex, ret);
			return;
		}
		
		for (int i = start; i<=end && end-i+1>=r-index; i++) {
			ret[i] = true;
			combinationUtil(ret, i + 1, end, index + 1, r, pIndex);
			ret[i] = false;
		}
	}
	
	private void calcSettings(int index, boolean[] extra) {
		int[] settings = outputSettings.get(index).clone();
		int[] pl = outputPlugs.get(index).clone();
		int[] counts = outputCounts.get(index).clone();
		
		int total = 26-(counts[0]+counts[1]);
		int ind = 0;
		int[] blanks = new int[total];
		for(int i = 0; i<26; i++) {
			if(pl[i]<0) {
				blanks[ind] = i;
				ind++;
			}
		}
		
		int[] tempPlugs = pl.clone();
		int saved = -1;
		for(int i = 0; i<total; i++) {
			if(extra[i]) {
				if(saved>=0) {
					tempPlugs[blanks[saved]] = blanks[i];
					tempPlugs[blanks[i]] = blanks[saved];
				}
				else {
					saved = i;
				}
			}
			else {
				tempPlugs[blanks[i]] = blanks[i];
			}
		}
		
		settings[5]-=(settings[0]+subIndex);
		settings[5]%=(int)Math.pow(26, 3);
		settings[5]+=(int)Math.pow(26, 3);
		settings[5]%=(int)Math.pow(26, 3);
				
		machine.set(new int[]{settings[2], settings[3], settings[4]}, settings[1], settings[5]);
		machine.setPB(tempPlugs);
		
		String ret = "";
		int[] pos = {settings[5]%26, (settings[5]/26)%26, (int)(settings[5]/Math.pow(26, 2))};
		for(int i = 0; i<3; i++) {
			ret+=numerals[settings[i+2]] + " ";
			ret+=pos[i] + " ";
		}
		for(int i = 0; i<26; i++) {
			if(tempPlugs[i]>i) {
				ret+=Character.toString((char)(i+65))+Character.toString((char)(tempPlugs[i]+65));
				ret+=" ";
			}
		}
		
		outputText+=ret + "\t";
		run();
	}
	
	private void run() {
		String ret = "";
		for(int i = 0; i<input.length(); i++) {
			ret+=Character.toString((char)(machine.run(input.charAt(i)-65)+65));
		}
		for(int i = 0; i<spaceLocs.size(); i++) {
			ret = ret.substring(0, spaceLocs.get(i)) + " " + ret.substring(spaceLocs.get(i));
		}
		
		outputText+=ret + "\n";
	}
	
	private void frameSetup() {
		JFrame frame = new JFrame("Ouput " + runs);
		JTextArea textbox = new JTextArea();
		textbox.setLineWrap(true);
		textbox.setText(outputText);

		JScrollPane scrollPane = new JScrollPane(textbox);
		scrollPane.setViewportView(textbox);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		frame.add(scrollPane);
		frame.pack();
		frame.setLocation(0, 0);
		frame.setSize(250, 250);
		frame.setVisible(true);
	}
}
