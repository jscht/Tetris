package tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JFrame {
	// 테트리스 메인 프레임을 정의하고 시작, 재시작 메소드를 구현한 메소드
	
	public Image lake;
    
    public static int width = 800; 
    public static int height= 750; 
    public static int dim = 30;
    
    public static GamePanel app;
    public static Title titlePanel;

    public static ExecutorService executor;
    
    public Tetris() {
        setLayout(new BorderLayout(0,0));
        setSize(width, height);
        
        titlePanel = new Title();
        add(titlePanel);
        
        JPanel pad = new JPanel();
        pad.setPreferredSize(new Dimension(100, 100));
        pad.setSize(100, 100);
        pad.setOpaque(false);
        
		// Icon
		Image favicon = Toolkit.getDefaultToolkit().getImage("./bin/tetris/img/Ttlogo.PNG");
		this.setIconImage(favicon);
        
        Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        
        setTitle("Tetris !");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public static void start() {
        executor = Executors.newCachedThreadPool();
        executor.execute(app);
        if (GamePanel.status == GamePanel.Status.START) {
        	GamePanel.status = GamePanel.Status.COUNTDOWN;
        	
        	app.requestFocus();
        }
    }
    
    public static void stop() {
        executor.shutdown();
    }
    
    public static void restart() { // 게임오버 후 리트라이        
        if(GamePanel.status == GamePanel.Status.GAMEOVER) {
        	app.resetGame();
        	
        	executor = Executors.newCachedThreadPool();
            executor.execute(app);
            
            app.requestFocus();

            if(GamePanel.status == GamePanel.Status.START) {
            	GamePanel.status = GamePanel.Status.COUNTDOWN; 
               }
            app.repaint();
            app.revalidate();
            
        } else { // retry
        	app.resetGame();
            
            app.repaint();
            app.revalidate();
            
            if(GamePanel.status == GamePanel.Status.START) {
            	GamePanel.status = GamePanel.Status.COUNTDOWN; 
               }
        }
    }
}
