package tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Ghost extends Tetromino {
	// 고스트를 생성하는 클래스
	// 현 시점에서 softdrop 혹은 harddrop시 테트로미노가 고정될 지점을 미리 보여준다.
	
	public Ghost(Tetromino mino) {
        super(mino, false);
        color = new Color(180, 180, 180, 100);
    }
    
    @Override
    public void draw(Graphics g) {
        for(int i=0; i<4; i++) {
            g.setColor(color);
            g.fillRect(Tetris.dim*pMinos[i].x+1, Tetris.dim*pMinos[i].y+1, Tetris.dim-2, Tetris.dim-2);  
            g.setColor(Color.black);
            g.drawRect(Tetris.dim*pMinos[i].x+1, Tetris.dim*pMinos[i].y+1, Tetris.dim-3, Tetris.dim-3);  
        }
    }
}
