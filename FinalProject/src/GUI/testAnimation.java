package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import model.Goku;
import model.Sonic;
import model.Unit;

public class testAnimation {
	public static void main(String[] AWHJEKAIJEGVEE) {
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(600, 200));
		frame.setLocation(new Point(100, 100));
		frame.setLayout(new BorderLayout());
		Unit one = new Sonic('C');
		Unit two = new Goku('g');
		AttackPanel wtr = new AttackPanel(one, two);
		wtr.setPreferredSize(new Dimension(1000, 1000));
		wtr.setSize(650, 650);
		frame.add(wtr, BorderLayout.CENTER);
		frame.setVisible(true);

	}
}
