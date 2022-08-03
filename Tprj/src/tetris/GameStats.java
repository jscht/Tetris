package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStats extends JPanel {
	// 게임 결과창 클래스
	// 게임오버시 정보들을 가져와서 띄우는데 사용

	ImageIcon retryBtn;

	JLabel title;
	JPanel stats;
	ScoreTablePanel[] scores;
	JPanel scoreSubmit;
	JButton cancelButton;
	JButton playAgainButton;

	JLabel bestTime; // 디비안의 최고기록(시간)을 보여줄 패널
	JLabel finalScore; // 디비안의 최고기록(시간 달성시의 점수)을 보여줄 패널

	static String time; // 프레임에 보여줄 시간
	static String times; // 디비에 보낼 시간
	static int gameScore;

	String sTime;
	int sScore;

	int lines;
	int singles;
	int doubles;
	int triples;

	int tetrises;
	int tetrominos;
	float linesPerMinute;
	float minosPerMinute;

	public GameStats(boolean win, int min, int sec, int milli, 
			int line, int single, int doub, int trip, int tetris,
			int tetros) {

		this.retryBtn = new ImageIcon("./bin/tetris/img/retry.png");

		String hundredth;
		String second;
		String minute;

		if (milli < 10) {
			hundredth = "0" + milli;
		} else
			hundredth = "" + milli;

		if (sec < 10) {
			second = "0" + sec;
		} else
			second = "" + sec;

		if (min < 10) {
			minute = "0" + min;
		} else
			minute = "" + min;

		time = new String(minute + ":" + second + ":" + hundredth);
		times = new String(minute + second + hundredth);
		// 이거를 가지고 시간 저장하는데 쓸 수 있도록 해보자

		lines = line;
		singles = single;
		doubles = doub;
		triples = trip;
		tetrises = tetris;

		gameScore = (singles * 100) + (doubles * 300) + (triples * 500) + (tetrises * 800);

		tetrominos = tetros;

		linesPerMinute = lines / (min + ((float) ((float) sec + ((float) milli / 1000)) / 60));
		minosPerMinute = tetrominos / (min + ((float) ((float) sec + ((float) milli / 1000)) / 60));

		setSize(250, 100);
		setPreferredSize(new Dimension(600, 600));
		setLayout(new FlowLayout());
		setOpaque(false);

		if (win == false) {
			title = new JLabel("Game Over: " + time + "     Score: " + gameScore);
		} else {
			title = new JLabel("Time: " + time + "     Score: " + gameScore);
		}

		title.setForeground(Color.black);
		title.setFont(new Font("Arial Rounded MT", Font.PLAIN, 28));

		stats = new JPanel();
		stats.setLayout(new FlowLayout());
		stats.setOpaque(false);
		stats.setPreferredSize(new Dimension(750, 280));

		scores = new ScoreTablePanel[8];

		scores[0] = new ScoreTablePanel(new Color(128, 128, 128, 148));
		scores[1] = new ScoreTablePanel(new Color(200, 200, 200, 148));
		scores[3] = new ScoreTablePanel(new Color(128, 128, 128, 148));
		scores[2] = new ScoreTablePanel(new Color(200, 200, 200, 148));
		scores[4] = new ScoreTablePanel(new Color(128, 128, 128, 148));
		scores[5] = new ScoreTablePanel(new Color(200, 200, 200, 148));
		scores[7] = new ScoreTablePanel(new Color(128, 128, 128, 148));
		scores[6] = new ScoreTablePanel(new Color(200, 200, 200, 148));

		scores[0].setLabel("Lines: ");
		scores[0].setValue("" + lines);

		scores[1].setLabel("Lines Per Minute: ");
		scores[1].setValue("" + linesPerMinute);

		scores[2].setLabel("Minos Locked Down: ");
		scores[2].setValue("" + tetrominos);

		scores[3].setLabel("Minos Per Minute: ");
		scores[3].setValue("" + minosPerMinute);

		scores[4].setLabel("Singles: ");
		scores[4].setValue("" + singles);

		scores[5].setLabel("Doubles: ");
		scores[5].setValue("" + doubles);

		scores[6].setLabel("Triples: ");
		scores[6].setValue("" + triples);

		scores[7].setLabel("Tetrises: ");
		scores[7].setValue("" + tetrises);

		for (int i = 0; i < scores.length; i++) {
			stats.add(scores[i]);
		}

		// 점수를 기록을 위한 패널
		scoreSubmit = new JPanel();
		scoreSubmit.setOpaque(false);
		scoreSubmit.setLayout(new FlowLayout());
		scoreSubmit.setPreferredSize(new Dimension(400, 250));

		ButtonHandler handler = new ButtonHandler();
		
		bestTime = new JLabel();
		
		TetrisDB.display(GamePanel.stmt);

		try { // 처음 디비에서 값 가져옴 -> 첫판이라면 값이 없다는걸 표현해야함
			if(TetrisDB.rs.next() == false) {
				sTime = "이전 기록 없음.";
				sScore = 0;
				bestTime.setText("Best Time: " + sTime);
			} else {
				sTime = TetrisDB.rs.getString("time");
				sScore = TetrisDB.rs.getInt("score");
				bestTime.setText("Best Time: " + sTime.substring(0, 2) +":"+ sTime.substring(2, 4) +":"+ sTime.substring(4, 6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JLabel newRecord = new JLabel("New Record!");
		newRecord.setFont(new Font("Arial Rounded MT", Font.BOLD, 26));
		newRecord.setForeground(Color.MAGENTA);
		newRecord.setPreferredSize(new Dimension(355, 40));

		if (win == true) {
			// 승리했을때 -> 시간 최고기록 경신했을 때만 실행
			try {
				if (TetrisDB.rs.next() == false) {
					// 첫 기록일 경우
					TetrisDB.insertDB(GamePanel.stmt);
					TetrisDB.display(GamePanel.stmt);
					// 가끔 신기록 아닌데도 신기록 들어갈때가 있던데  뭐지?

					try {
						TetrisDB.rs.next();
						sTime = TetrisDB.rs.getString("time");
						bestTime.setText("Best Time: " + sTime.substring(0, 2) +":"+ sTime.substring(2, 4) +":"+ sTime.substring(4, 6));
						sScore = TetrisDB.rs.getInt("score");
						scoreSubmit.add(newRecord);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					// 기록 비교후 더 나은 시간 기록이면 삭제 후 기록
					int highScore = Integer.parseInt(sTime);
					int userScore = Integer.parseInt(times);
					
					if(userScore <= highScore) { // 신기록일 경우
						TetrisDB.deleteDB(GamePanel.stmt);
						TetrisDB.insertDB(GamePanel.stmt);
						scoreSubmit.add(newRecord);
					}
					
					TetrisDB.display(GamePanel.stmt);

					try {
						TetrisDB.rs.next();
						sTime = TetrisDB.rs.getString("time");
						sScore = TetrisDB.rs.getInt("score");
						// 새로 받아온 시간 적용
						bestTime.setText("Best Time: " + sTime.substring(0, 2) +":"+ sTime.substring(2, 4) +":"+ sTime.substring(4, 6));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		bestTime.setFont(new Font("Arial Rounded MT", Font.BOLD, 26));
		bestTime.setForeground(Color.WHITE);
		bestTime.setPreferredSize(new Dimension(355, 40));

		finalScore = new JLabel();
		finalScore.setText("Score: " + sScore);
		finalScore.setFont(new Font("Arial Rounded MT", Font.BOLD, 26));
		finalScore.setForeground(Color.WHITE);
		finalScore.setPreferredSize(new Dimension(355, 40));

		cancelButton = new JButton(retryBtn);
		cancelButton.setRolloverIcon(retryBtn);
		cancelButton.setBorderPainted(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setContentAreaFilled(false);
		cancelButton.addActionListener(handler);
		cancelButton.setPreferredSize(new Dimension(100, 100));

		scoreSubmit.add(bestTime);
		scoreSubmit.add(finalScore);

		if (win) {
			scoreSubmit.add(cancelButton);
		} else {
			playAgainButton = new JButton(retryBtn);
			playAgainButton.setRolloverIcon(retryBtn);
			playAgainButton.setBorderPainted(false);
			playAgainButton.setFocusPainted(false);
			playAgainButton.setContentAreaFilled(false);
			playAgainButton.addActionListener(handler);
			playAgainButton.setPreferredSize(new Dimension(100, 100));
			scoreSubmit.add(playAgainButton);
		}

		add(title);
		add(stats);
		add(scoreSubmit);

		setVisible(true);

	}

	class ScoreTablePanel extends JPanel {
		Color color;
		JLabel label;
		JLabel value;

		public ScoreTablePanel(Color clr) {
			setLayout(new BorderLayout(5, 5));
			setPreferredSize(new Dimension(250, 60));

			color = clr;
		}

		public void setLabel(String label) {
			this.add(new JLabel("   " + label), BorderLayout.WEST);
		}

		public void setValue(String value) {
			this.add(new JLabel(value + "   "), BorderLayout.EAST);
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(color);
			g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 4, 4);
		}
	}

	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			Tetris.app.removeAll();
			Tetris.stop();
			Tetris.restart();
		}
	}

}
