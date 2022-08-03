package tetris;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class PausePanel extends JDialog {
	// 일시정지 다이얼로그를 정의한 클래스 (ESC)
	// 재시작 기능 포함
	
	private JButton retry, mainMenu;
	
	PausePanel() {
		setTitle("PauseDialog");
		setSize(350, 450);
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 150, 30));
		
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		ButtonHandler btnHandler = new ButtonHandler();
		KeyHandler keyHandler = new KeyHandler();
		
		// ESC누르면 창 닫기
		getRootPane().registerKeyboardAction(keyHandler,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		
		JLabel pause = new JLabel("Pause!");
		pause.setFont(new Font("Arial Rounded MT", Font.PLAIN, 30));
		
		JLabel goback = new JLabel("<html><body style='text-align:center;'>Press 'ESC'<br/>to go back</body></html>");
		goback.setFont(new Font("Arial Rounded MT", Font.PLAIN, 20));
		
		JPanel pad = new JPanel();
        pad.setPreferredSize(new Dimension(130, 130));
        pad.setOpaque(false);
        pad.add(pause);
        pad.add(goback);
         
        this.add(pad);
		
		JLabel rt = new JLabel("Retry");
		rt.setFont(new Font("Arial Rounded MT", Font.PLAIN, 15));
		JLabel mme = new JLabel("Main Menu");
		mme.setFont(new Font("Arial Rounded MT", Font.PLAIN, 15));
		
		retry = new JButton();
		retry.add(rt);
		retry.setPreferredSize(new Dimension(130, 50));
		retry.addActionListener(btnHandler);
		
		mainMenu = new JButton();
		mainMenu.add(mme);
		mainMenu.setPreferredSize(new Dimension(130, 50));
		mainMenu.addActionListener(btnHandler);
		
		add(retry);
		
		setVisible(false);
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == retry) {
				Tetris.app.removeAll();
				Tetris.stop();
				Tetris.restart();
				dispose();
				// 속도 제어 필요 -> 쓰레드 풀 문제 였다..
			}	
		}
	}
	
	private class KeyHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			GamePanel.status = GamePanel.Status.PLAYING;
		}
		
	}
	
}
