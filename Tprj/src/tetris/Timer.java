package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Timer extends JPanel {
	// 타이머를 정의한 클래스
	
	int minutes; 
    int seconds; 
    int hundredths; 

    String min; 
    String sec; 
    String hund; 

    String time; 

    public Timer() {
        setLayout(new FlowLayout());
        setSize(getPreferredSize());
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        setOpaque(false);

        min = sec = hund = "00";
        minutes = seconds = hundredths = 0;
        time = min+":"+sec+":"+hund; 
    }

	public void update() {
		hundredths += 5;

		if (hundredths >= 100) {
			hundredths = 0;
			seconds++;
		}
		if (seconds >= 60) {
			seconds = 0;
			minutes++;
		}

		if (hundredths < 10) {
			hund = "0" + hundredths;
		} else
			hund = "" + hundredths;

		if (seconds < 10) {
			sec = "0" + seconds;
		} else
			sec = "" + seconds;

		if (minutes < 10) {
			min = "0" + minutes;
		} else
			min = "" + minutes;

		time = new String(min + ":" + sec + ":" + hund);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(128, 128, 128, 128));
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);

		g.setColor(Color.black);
		g.setFont(new Font("Arial Rounded MT", Font.PLAIN, 27));
		g.drawString(time, 10, 50);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(130, 80);
	}

	@Override
	public String toString() {
		return new String(min + ":" + sec + ":" + hund);
	}
	
}
