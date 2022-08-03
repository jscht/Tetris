package tetris;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class HUDPanel extends JPanel {
	// next가방과 상태창을 만드는 레이아웃 설정
	// BagPanel, StatsPanel
	
	public HUDPanel() {
        setLayout( new FlowLayout() );
        setOpaque(false);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension (175, 600);
    }
}
