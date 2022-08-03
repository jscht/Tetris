package tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GameElementPanel extends JPanel {
	// line과 time 패널 만드는데 사용한 클래스
	
	public GameElementPanel() {
        setBackground(null);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(100,100,100,128));
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
    }
}