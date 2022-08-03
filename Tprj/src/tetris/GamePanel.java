package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
	// 게임에 필요한 각종 기능(클래스, 패널 등)을 모아놓은 패널을 정의한 클래스
	// 이 파일 분석 후 보고서 작성
	
	public static Connection con;
	public static Statement stmt;
	
	public Image backImage; 
    public ImageIcon backIcon;
	
    public enum Status { START, PLAYING, PAUSED, COUNTDOWN, GAMEOVER };
    
    public static Status status;
    
    public GameStats gameStats;
    
    private final int FPS = 25; 
    private int frames;
    
    private int minoDelay;
    private boolean minoDelaying;
    
    private int countDown;
    
    private FieldPanel field;
    private StatsPanel stats; 
    private BagPanel bagPanel;
    private PausePanel pausePanel;
    
    private boolean[] keys; 
    
    private boolean hasHeld;
    
    private MinoBag bag;
    private Tetromino currentMino;
    
    private int linesLeft;
    
    private Ghost ghost;
    
    private int lines, singles, doubles, triples, tetrises, minoCount;
    
    private int comboCount;
    
	public GamePanel() {
		
		setPreferredSize(new Dimension(Tetris.width, Tetris.height));
        setSize(getPreferredSize());
        setLayout(new FlowLayout());
        addKeyListener(new KeyHandler());
        setBackground(null);
        //setFocusable(true);
        setOpaque(false);
        
		backImage = new ImageIcon(getClass().getResource("/tetris/img/pixelBg.png")).getImage();
        Image back = backImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);  
        backIcon = new ImageIcon(back);
        
        try {
			   GamePanel.con = TetrisDB.makeConnection();
			   GamePanel.stmt = con.createStatement(); //sql문장 작성 및 전송
		} catch (SQLException e) {
			   e.printStackTrace();
		   }
        
		resetGame();
	}
	
	public void resetGame() {
		
		lines = singles = doubles = triples = tetrises = minoCount = 0;
		
		comboCount = 0;
        countDown  = 3;
        minoDelay  = 0;
        minoDelaying = false;
        
		status = Status.START;
		
		frames = 0; 
        hasHeld = false;
		
		keys = new boolean[8]; 
        
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
        
        bag = new MinoBag();
        
        linesLeft = 40;
        
        stats = new StatsPanel();
		bagPanel = new BagPanel();
        field = new FieldPanel();
        pausePanel = new PausePanel();
        
        JPanel gameAndBag = new JPanel();
        gameAndBag.setLayout(new BorderLayout(0,0));
        gameAndBag.setOpaque(false);
        
        JPanel game = new JPanel();
        game.setLayout(new BorderLayout(4,4));
        game.setBackground(null);
        game.setOpaque(false);
        game.setBorder( BorderFactory.createLoweredBevelBorder());
        game.add(field, BorderLayout.CENTER);
        
        JPanel pad = new JPanel(); 
        pad.setOpaque(false);
        pad.setBackground(null);
        pad.setPreferredSize(new Dimension(this.getWidth(), 50));
        
        add(pad);
        
        gameAndBag.add(game, BorderLayout.CENTER);
        gameAndBag.add(bagPanel,BorderLayout.EAST);
        gameAndBag.add(stats, BorderLayout.WEST);
       
        add(gameAndBag);
	}
	
	public void getGhostMinos() {
        if (status != Status.PLAYING) {
            return; 
        }
        
        ghost = new Ghost(currentMino);
        
        while(!checkGhostAgainstField(ghost, new Point(0, 1))) {
            ghost.move(new Point(0, 1));
        }
    }
	
	public boolean checkGhostAgainstField(Tetromino mino, Point shift) {
		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].y == 19) {
				return true;
			}

			for (int j = 0; j < field.elements.length; j++) {
				for (int k = 0; k < field.elements[j].length; k++) {
					if (field.elements[j][k] != null && mino.pMinos[i].x == j && mino.pMinos[i].y == k - 1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkMinoAgainstField(Tetromino mino) {
		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].y >= 19) {
				return true;
			}

			for (int j = 0; j < field.elements.length; j++) {
				for (int k = 0; k < field.elements[j].length; k++) {
					if (field.elements[j][k] != null && mino.pMinos[i].x == j && mino.pMinos[i].y == k - 1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean minoOutOfBounds(Tetromino mino) {
		int count = 0;
		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].x <= 9 && mino.pMinos[i].x >= 0) {
				count++;

				if (count == 4) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void checkMinoAgainstBorders(Tetromino mino) {
		if (!minoOutOfBounds(mino)) {
			return;
		}

		int x = 1;
		int y = 0;

		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].x > 9) {
				x = -1;
			}
		}

		Point shift = new Point(x, y);

		while (minoOutOfBounds(mino)) {
			mino.move(shift);
		}
	}
	
	public boolean bottomEmpty(Tetromino mino) {
		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].y == 19)
				return false;

			if (field.elements[mino.pMinos[i].x][mino.pMinos[i].y + 1] == null) {
				return true;
			}
		}
		return false;
	}
	
	public void checkMinoAgainstFieldAndCorrect(Tetromino mino) {
		if (!checkMinoAgainstField(mino)) {
			return;
		}
		// 플로어 킥
		Point shift = new Point(0, -1);

		// int count = 0;
		while (checkMinoAgainstField(mino)) {
			mino.move(shift);
			/*
			 * count++; if (count >= 4) { for(int i=0; i<count; i++) { mino.move(new
			 * Point(0, shift.y*-1)); }
			 * 
			 * mino.reverseRotate();
			 * 
			 * if ((!bottomEmpty(mino) && !checkMinoAgainstMatrix(mino))) {
			 * matrix.minoToMatrixElement(currentMino); setNextMino(); } return; }
			 */
		}
	}
	
	public void checkMoveAgainstField(Tetromino mino, Point shift) {
		for (int i = 0; i < 4; i++) {
			if (mino.pMinos[i].y > 19) {
				currentMino.move(new Point(shift.x * -1, shift.y * -1));
				if (shift.x == 0) {
//					field.minoToFieldElement(mino);
//					setNextMino();
					// 이 설정을 켜면 바닥에 닿자마자 고정이 되어버림
					// 인피니티 딜레이 설정 해제 되어버림
					minoDelaying = true;
					// 멈추고 블럭을 좌표에 고정
				}
				return;
			}

			if (mino.pMinos[i].x > 9 || mino.pMinos[i].x < 0) {
				currentMino.move(new Point(shift.x * -1, shift.y * -1));
			}

			for (int j = 0; j < field.elements.length; j++) {
				for (int k = 0; k < field.elements[j].length; k++) {
					if (field.elements[j][k] != null && mino.pMinos[i].x == j && mino.pMinos[i].y == k) {
						currentMino.move(new Point(shift.x * -1, shift.y * -1));

						if (shift.x == 0) {
//							 field.minoToFieldElement(mino);
//							 setNextMino();
							 // 이 설정을 켜면 바닥에 닿자마자 고정이 되어버림
							 // 인피니티 딜레이 설정 해제 되어버림
							minoDelaying = true;
						}
						return;
					}
				}
			}
		}
	}
	
	public void setNextMino() {
		minoCount++;
		minoDelay = 0;
		minoDelaying = false;

		field.checkLineClears();

		boolean gameOver = false;

		if (linesLeft <= 0) {
			gameOver = true;
		}

		if (gameOver) {
			gameOverMethod(true);
		}
		if (status == Status.PLAYING) {
			currentMino = new Tetromino(bag.newMino(), true);
			bag.shiftUp();
			bagPanel.adjust();
			hasHeld = false;
		}
	}
	
	public void gameOverMethod(boolean wins) {
		for (int j = 0; j < keys.length; j++) {
			keys[j] = false;
		}

		status = Status.GAMEOVER;

		gameStats = new GameStats(wins, stats.timer.minutes, stats.timer.seconds, stats.timer.hundredths, lines,
				singles, doubles, triples, tetrises, minoCount);

		JPanel pad = new JPanel();
		pad.setOpaque(false);
		pad.setBackground(null);
		pad.setPreferredSize(new Dimension(this.getWidth(), 50));

		this.removeAll();
		this.revalidate();
		this.add(pad);
		this.add(gameStats);
		this.repaint();
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(backIcon.getImage(), 0, 0, null);
    }
	
	public void keyUpdate() {
		if (keys[0]) {
			minoDelay = 0;
			minoDelaying = false;
			currentMino.rotate(1, false);

			while ((checkMinoAgainstField(currentMino)) || (minoOutOfBounds(currentMino))) {
				checkMinoAgainstFieldAndCorrect(currentMino);

				checkMinoAgainstBorders(currentMino);
			}
			keys[0] = false;
			// rotate
		}

		if (keys[1]) {
			currentMino.move(new Point(1, 0));
			checkMoveAgainstField(currentMino, new Point(1, 0));
			minoDelaying = false;
			minoDelay = 0;
			keys[1] = false;
			// right move
		}

		if (keys[2]) {
			currentMino.move(new Point(0, 1));
			checkMoveAgainstField(currentMino, new Point(0, 1));
			// softdrop move
		}

		if (keys[3]) {
			currentMino.move(new Point(-1, 0));
			checkMoveAgainstField(currentMino, new Point(-1, 0));
			minoDelaying = false;
			minoDelay = 0;
			keys[3] = false;
			// left move
		}

		if (keys[4]) {
			currentMino.pMinos = ghost.pMinos;
			field.minoToFieldElement(currentMino);
			setNextMino();

			keys[4] = false;
			// harddrop move
		}

		if ((keys[5]) && !hasHeld) {
			if (bag.heldMino != null) {
				Tetromino tempC = new Tetromino(bag.heldMino, true);
				bag.heldMino = new Tetromino(currentMino, true);
				currentMino = new Tetromino(tempC, true);
			} else {
				Tetromino tempC = new Tetromino(currentMino, true);
				bag.heldMino = new Tetromino(tempC, true);

				currentMino = new Tetromino(bag.newMino(), true);

				bag.shiftUp();
				bagPanel.adjust();
			}
			stats.hold.mino = bag.heldMino;
			hasHeld = true;
			// hold
		}

		if (keys[6]) {
			if (status == Status.PLAYING) {
				status = Status.PAUSED;
				pausePanel.setVisible(true);
			}
			// pause
			repaint();
		}

		if (keys[7]) {
			currentMino.reverseRotate();

			while ((checkMinoAgainstField(currentMino)) || (minoOutOfBounds(currentMino))) {
				checkMinoAgainstFieldAndCorrect(currentMino);

				checkMinoAgainstBorders(currentMino);
			}
			keys[7] = false;
			minoDelaying = false;
			minoDelay = 0;
			// reverse rotate
		}
	}
	
	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent event) {
			if (status == Status.PLAYING) {
				if ((event.getKeyCode() == KeyEvent.VK_UP) || 
						(event.getKeyCode() == KeyEvent.VK_X)) {
					keys[0] = true;
					// rotate
				}
				if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
					keys[1] = true;
					// right
				}
				if (event.getKeyCode() == KeyEvent.VK_DOWN) {
					keys[2] = true;
					// softdrop
				}
				if (event.getKeyCode() == KeyEvent.VK_LEFT) {
					keys[3] = true;
					// left
				}
				if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					keys[4] = true;
					// harddrop
				}
				if ((event.getKeyCode() == KeyEvent.VK_SHIFT) || 
						(event.getKeyCode() == KeyEvent.VK_C)) {
					keys[5] = true;
					// hold
				}
				if (event.getKeyCode() == KeyEvent.VK_Z) {
					keys[7] = true;
					// reverse rotate
				}
			}
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
				keys[6] = true;
				// set unset
			}
			keyUpdate();
			getGhostMinos();
			repaint();
		}

		@Override
		public void keyReleased(KeyEvent event) {
			if (status == Status.PLAYING) {
				if ((event.getKeyCode() == KeyEvent.VK_UP) || 
						(event.getKeyCode() == KeyEvent.VK_X)) {
					keys[0] = false;
				}
				if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
					keys[1] = false;
				}
				if (event.getKeyCode() == KeyEvent.VK_DOWN) {
					keys[2] = false;
				}
				if (event.getKeyCode() == KeyEvent.VK_LEFT) {
					keys[3] = false;
				}
				if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					keys[4] = false;
				}
				if ((event.getKeyCode() == KeyEvent.VK_SHIFT) || 
						(event.getKeyCode() == KeyEvent.VK_C)) {
					keys[5] = false;
				}
				if (event.getKeyCode() == KeyEvent.VK_Z) {
					keys[7] = false;
				}
			}
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
				keys[6] = false;
			}
		}
	}
	
	class FieldPanel extends JPanel {
		FieldElement[][] elements;
		
		public FieldPanel() {
			setSize(300, 600);
			setOpaque(false);

			elements = new FieldElement[10][20];

			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 20; j++) {
					elements[i][j] = null;
				}
			}
		}
		
		public void minoToFieldElement(Tetromino mino) {
			for (int i = 0; i < 4; i++) {
				if (mino.pMinos[i].y < 0) {
					gameOverMethod(false);
					return;
				}
				elements[mino.pMinos[i].x][mino.pMinos[i].y] = new FieldElement(mino.color, mino.pMinos[i]);
			}
		}
		
		public void checkLineClears() {
			for (int i = 0; i < 20; i++) {
				int count = 0;
				for (int j = 0; j < 10; j++) {
					if (elements[j][i] != null) {
						count++;
					}
					if (count == elements.length) {
						System.out.println("Line clear at:" + i);
						removeRow(i);
						linesLeft--;
						lines++;
						comboCount++;
						stats.updateLines(); // 게임 중 달성한 업적 느낌?
					}
				}
			}
			if (comboCount == 1) {
				singles++;
			} else if (comboCount == 2) {
				doubles++;
			} else if (comboCount == 3) {
				triples++;
			} else if (comboCount == 4) {
				tetrises++;
			}
			comboCount = 0;
		}     
        
		public void removeRow(int row) {
			for (int i = 0; i < 10; i++) {
				elements[i][row] = null;
			}

			FieldElement[][] temp = new FieldElement[10][20];

			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < 10; j++) {
					if (elements[j][i] != null) {
						temp[j][i] = new FieldElement(elements[j][i]);
					}
				}
			}

			for (int i = row; i >= 0; i--) {
				for (int j = 0; j < 10; j++) {
					elements[j][i] = null;

					if (i != 0) {
						if (temp[j][i - 1] != null) {
							elements[j][i] = new FieldElement(temp[j][i - 1].color,
									new Point(temp[j][i - 1].coors.x, temp[j][i - 1].coors.y + 1));
						}
					}
				}
			}
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			drawBackground(g);

			if (status == Status.PLAYING || status == Status.PAUSED) {

				for (int i = 0; i < elements.length; i++) {
					for (int j = 0; j < elements[i].length; j++) {
						if (elements[i][j] != null)
							elements[i][j].draw(g, Tetris.dim);
					}
				}
				ghost.draw(g);
				currentMino.draw(g);
			}

			else if (status == Status.COUNTDOWN) {
				g.setColor(Color.white);
				g.setFont(new Font("Arial Rounded MT", Font.PLAIN, 50));
				g.drawString("" + countDown, getWidth() / 2, getHeight() / 2);
				// ready? start! 바꿔서 출력하기
			}
		}
        

        public void drawBackground(Graphics g) {
			for (int i = 0; i < elements.length; i++) {
				for (int j = 0; j < elements[i].length; j++) {
					g.setColor(new Color(40, 40, 40, 112));
					g.fillRoundRect(i * Tetris.dim, j * Tetris.dim, Tetris.dim + 1, Tetris.dim + 1, 4, 4);
				}
			}
        }

        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 600);
        }
	}  
	
	class BagPanel extends HUDPanel {
        MinoPanel[] minoPanels;
        
        public BagPanel() {
            this.setLayout(new BorderLayout());
            this.setOpaque(false);
            JPanel nextPanel = new JPanel();
            nextPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            nextPanel.setOpaque(false);
        
            minoPanels = new MinoPanel[5];
            
            JLabel nextLabel = new JLabel("NEXT");
            nextLabel.setFont(new Font("Arial Rounded MT", Font.PLAIN, 27));
            
            for(int i=0; i<5; i++) {
                minoPanels[i] = new MinoPanel(bag.mainMinoBag.get(i));
                
                if (i == 0) {               
                    minoPanels[i].setPreferredSize(new Dimension(125, 130));
                    minoPanels[i].add(nextLabel);
                }
                
                if (i >= 1) {               
                    minoPanels[i].setPreferredSize(new Dimension(110, 95));
                }
                nextPanel.add(minoPanels[i]); 
            }
            
            add(nextPanel, BorderLayout.CENTER);
        }
        
        
		public void adjust() {
			for (int i = 0; i < 5; i++) {
				minoPanels[i].mino = new Tetromino(bag.mainMinoBag.get(i), true);
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(130, 400);
		}
    }
	
	class MinoPanel extends JPanel {
        Tetromino mino; 
        
        public MinoPanel(Tetromino mino) {
            this.mino = mino;
            
            setBackground(null);
            setOpaque(false);
            setBorder(BorderFactory.createLineBorder(Color.darkGray));
            setPreferredSize(new Dimension(70, 60));
        }
        
        public MinoPanel() {
            setBackground(null);
            setPreferredSize(new Dimension(70, 60));
        }
        
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(128,128,128,128));
            g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
            if(mino != null)
               mino.drawInBag(g, this.getWidth(), this.getHeight()+20);
        }
    }
	
	class StatsPanel extends HUDPanel {
		MinoPanel hold;
		JLabel holdLabel;
		
		Timer timer;
		JLabel timerLabel;
		
		JLabel linesLeftLabel;

		public StatsPanel() {
			setLayout(new FlowLayout(FlowLayout.RIGHT));
            setOpaque(false);
            timer = new Timer();
            
            timerLabel = new JLabel("TIME");
            timerLabel.setFont(new Font("Arial Rounded MT", Font.PLAIN, 20));
            timerLabel.setForeground(Color.black);
            timerLabel.setPreferredSize(new Dimension(50, 20));
             
            holdLabel = new JLabel("HOLD");
            holdLabel.setFont(new Font("Arial Rounded MT", Font.PLAIN, 27));
            
            JLabel lines = new JLabel("LINES");
            lines.setForeground(Color.black);
            lines.setFont(new Font("Arial Rounded MT", Font.PLAIN, 20));
            
            
            hold = new MinoPanel();
            hold.setPreferredSize(new Dimension(115, 120));
            hold.setBorder(BorderFactory.createLineBorder(Color.darkGray));

            hold.setOpaque(false);
            hold.add(holdLabel);
            
            this.add(hold);
            
            JPanel pad = new JPanel();
            pad.setPreferredSize(new Dimension(130, 250));
            pad.setOpaque(false);
            
            this.add(pad);
            
            GameElementPanel timeP = new GameElementPanel();
            timeP.setPreferredSize(new Dimension(130, 30));
            timeP.add(timerLabel);
            this.add(timeP);
            
            this.add(timer);
            
            JPanel pad2 = new JPanel();
            pad2.setPreferredSize(new Dimension(110, 7));
            pad2.setOpaque(false);
            this.add(pad2);
            
            
            GameElementPanel lineP = new GameElementPanel(); 
            lineP.setPreferredSize(new Dimension(130, 30));
            lines.setPreferredSize(new Dimension(60, 20));
            
            lineP.add(lines);
            
            
            GameElementPanel linePanel = new GameElementPanel();
            linePanel.setPreferredSize(new Dimension(130, 50));
            linePanel.setOpaque(false);
            
            
            linesLeftLabel = new JLabel(""+linesLeft);
            linesLeftLabel.setFont(new Font("Arial Rounded MT", Font.PLAIN, 27));
            linesLeftLabel.setForeground(Color.black);
            linesLeftLabel.setPreferredSize(new Dimension(30, 35));
            
            linePanel.add(linesLeftLabel);
            
            this.add(lineP);
            this.add(linePanel);
		}

		public void updateLines() {
			linesLeftLabel.setText("" + linesLeft);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(140, 440);
		}
	}

	@Override
	public void run() {
		while(status != Status.GAMEOVER) {
			
			if (status == Status.PLAYING) {
				frames++;
				if (frames % 2 == 0) {
					stats.timer.update();

					if (frames % 4 == 0) {
						stats.repaint();
					}
				}

				if (minoDelaying) {
					minoDelay++;
				}
				
				if (minoDelay == 15) {
					minoDelay = 0;
					minoDelaying = false;
					field.minoToFieldElement(currentMino);
					setNextMino();
				}

				if (frames % 30 == 0) {
					currentMino.move(new Point(0, 1));

					checkMoveAgainstField(currentMino, new Point(0, 1));

					getGhostMinos();

					this.repaint();
				}
			}

			else if (status == Status.START) {

			}

			else if (status == Status.PAUSED) {

			}

			else if (status == Status.COUNTDOWN) {
				frames++;

				if (frames % 40 == 0) {
					countDown--;
					repaint();
				}

				if (countDown == 0) {
					status = Status.PLAYING;

					setNextMino();

					ghost = new Ghost(currentMino);
					getGhostMinos();
				}
			}

			try {
				Thread.sleep(FPS);
			} catch (InterruptedException exception) {
				break;
			}
		}
	}

}
