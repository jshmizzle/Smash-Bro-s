package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import model.Goku;
import model.Link;
import model.Mario;
import model.Princess;
import model.Unit;

public class testAnimation {
	private boolean isAnimating=true;
	private AttackPanel wtr;
	public static void main(String[] AWHJEKAIJEGVEE) {
		new testAnimation();

	}
	
	public testAnimation(){
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(600, 200));
		frame.setLocation(new Point(100, 100));
		frame.setLayout(new BorderLayout());
		Unit one = new Goku('C');
		Unit two = new Princess('c');
		wtr = new AttackPanel(one, two);
		wtr.setPreferredSize(new Dimension(1000, 1000));
		wtr.setSize(650, 650);
		wtr.addListener(new AnimationOverListener());
		frame.add(wtr, BorderLayout.CENTER);
		frame.setVisible(true);
		
		AnimationOverListener a=new AnimationOverListener();
//		Timer t=new Timer(1000000, a);
//		t.start();
		
		System.out.println("DONE DONE DONE DONE DONE DONE DONEDONE");
	}
	
	
	
	public class AnimationOverListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			testAnimation.this.wtr.setVisible(false);
		}
		
	}
}
