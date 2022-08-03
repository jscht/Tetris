package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Title extends JPanel {
	// 게임 시작시 타이틀을 정의한 클래스
	
	private Image gameTitle;
	private ImageIcon startButton;
	static Tetris tetris;
	
	static JButton startBtn;
	
	static JLabel jl;
	
	public Title() {
		this.gameTitle = new ImageIcon(getClass().getResource("/tetris/img/tetris-logo.png")).getImage();
		this.startButton = new ImageIcon("./bin/tetris/img/ps.png");
		
		setLayout(null);
		
		ButtonHandler handler = new ButtonHandler();
		
		startBtn = new JButton(startButton);
		startBtn.setRolloverIcon(startButton);
		startBtn.setBorderPainted(false);
		startBtn.setFocusPainted(false);
		startBtn.setContentAreaFilled(false);
		startBtn.setPreferredSize(new Dimension(700, 100));
		startBtn.setBounds(60, 450, 700, 100);
		startBtn.addActionListener(handler);
		
		jl = new JLabel("fall puzzle game tetris!!");
		jl.setFont(new Font("Arial Rounded MT", Font.PLAIN, 28));
		jl.setPreferredSize(new Dimension(335, 420));
		jl.setBounds(260, 395, 300, 40);
		
		add(startBtn);
		add(jl);
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(650, 500));
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Tetris.WIDTH, Tetris.HEIGHT);
		g.drawImage(gameTitle, 125, 0, null);
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == startBtn) {
					Tetris.app = new GamePanel();
					add(Tetris.app, BorderLayout.CENTER);
					
					setOpaque(true);
					jl.setVisible(false);
					startBtn.setVisible(false);
					
					Tetris.start();
				
					Tetris.app.repaint();
			}
		}
	}

}
