package breaker;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RunBreaker {
	
	private JFrame frame;
	private JLabel iLabel, sLabel, cLabel;
	private JTextField iField, sField, cField;
	private JButton button;
	
	private Decryptor d;
		
	public RunBreaker() {
		frame = new JFrame("Enigma Decryption");
		iLabel = new JLabel("  Input: ");
		sLabel = new JLabel("Section: ");
		cLabel = new JLabel("   Crib: ");
		iField = new JTextField("WQCIFLQDBTRKQOCAKCCZIZNLMSWAFCIRFJLDVITJVBL", 15);
		sField = new JTextField("WQCIFLQDBTRKQOCAKCCZI", 15);
		cField = new JTextField("ASHWANTHXMURUHATHASAN", 15);
		button = new JButton("Decrypt");
		
		d = new Decryptor();
		d.setValue(0); 
		d.setStringPainted(true); 
		
		button.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){
				String input = iField.getText();
				String section = sField.getText();
				String crib = cField.getText();

				Thread runThread = new Thread(){
					public void run(){
						d.run(input, section, crib);
					}
				};
				runThread.start();
				frame.setVisible(true);
			}
		});
		
		Container contentPane = frame.getContentPane();
		FlowLayout layout = new FlowLayout();
		contentPane.setLayout(layout);
		contentPane.add(iLabel);
		contentPane.add(iField);
		contentPane.add(sLabel);
		contentPane.add(sField);
		contentPane.add(cLabel);
		contentPane.add(cField);
		contentPane.add(button);
		contentPane.add(d); 
	}
		
	public static void main(String[] args) {
		RunBreaker main = new RunBreaker();
		main.setup();
	}
	
	private void setup() {
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.setSize(300, 175);
		frame.setVisible(true);
	}
}
