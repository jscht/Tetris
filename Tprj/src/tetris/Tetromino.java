package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import tetris.GamePanel.FieldPanel;

public class Tetromino {
	// 미노(블럭)의 생성과 회전 규칙을 정의한 클래스
	
	Color color; 
    Point[] pMinos;
    
    int id;
    int rotation; // 블럭의 회전 설정
    
    public void rotation() {
		this.rotation = 0;
	}
    
	public Tetromino(Tetromino mino, boolean reset) {
		
		this.id = mino.id;
		this.color = mino.color;

		if (reset)
			this.pMinos = getStartMinos();

		else {
			this.pMinos = new Point[4];
			for (int i = 0; i < 4; i++) {
				pMinos[i] = new Point(mino.pMinos[i].x, mino.pMinos[i].y);
			}
		}
	}
    
    public Tetromino(int id) {   
        this.id = id;
        rotation();
        pMinos = getStartMinos();
    }
    
    public Point[] getStartMinos() {
        pMinos = new Point[4]; 
        
        switch (id) {
            case 1: { // MinoBar
                color = Color.cyan;
                pMinos[0] = new Point(3, 0);
                pMinos[1] = new Point(4, 0);
                pMinos[2] = new Point(5, 0);
                pMinos[3] = new Point(6, 0);
                
                break;
            }
            case 2: { // MinoL
                color = Color.orange;
                pMinos[0] = new Point(3, 0);
                pMinos[1] = new Point(4, 0);
                pMinos[2] = new Point(5, 0);
                pMinos[3] = new Point(5, -1);
                
                break;
            }
            case 3: { // MinoMirroredL
                color = Color.blue;
                pMinos[0] = new Point(3, -1);
                pMinos[1] = new Point(3, 0);
                pMinos[2] = new Point(4, 0);
                pMinos[3] = new Point(5, 0);
                
                break;
            }
            case 4: { // MinoSquared
                color = Color.yellow;
                pMinos[0] = new Point(4, -1);
                pMinos[1] = new Point(5, -1);
                pMinos[2] = new Point(5, 0);
                pMinos[3] = new Point(4, 0);
                
                break;
            }
            case 5: { // MinoS
                color = Color.green;
                pMinos[0] = new Point(3, 0);
                pMinos[1] = new Point(4, 0);
                pMinos[2] = new Point(4, -1);
                pMinos[3] = new Point(5, -1);
                
                break;
            }
            case 6: { // MinoZ
                color = Color.red;
                pMinos[0] = new Point(3, -1);
                pMinos[1] = new Point(4, -1);
                pMinos[2] = new Point(4, 0);
                pMinos[3] = new Point(5, 0);
                
                break;
            }
            case 7: { // MinoT
                color = Color.magenta;
                pMinos[0] = new Point(3, 0);
                pMinos[1] = new Point(4, 0);
                pMinos[2] = new Point(4, -1);
                pMinos[3] = new Point(5, 0);
                
                break;
            }
        }
        
        return pMinos;
    }
    
    public void draw(Graphics g) {
        for(int i=0; i<4; i++) {
            g.setColor(color); 
            g.fillRect(Tetris.dim*pMinos[i].x, Tetris.dim*pMinos[i].y, 
                       Tetris.dim,             Tetris.dim);
            
            g.setColor(Color.white);
            g.drawRect(Tetris.dim*pMinos[i].x, Tetris.dim*pMinos[i].y, 
                       Tetris.dim-1,           Tetris.dim-1);  
            
            g.setColor(Color.black);
            g.drawRect(Tetris.dim*pMinos[i].x, Tetris.dim*pMinos[i].y, 
                       Tetris.dim,             Tetris.dim);
        }
    }
    
    public void drawInBag(Graphics g, int width, int height) {
        int qDim   = Tetris.dim/6*5; 
        int xOffset = (width - (qDim*3))/2;
        int yOffset = (height - (qDim * 2))/2;
        
        switch(id) {
        	case 1: {
        		xOffset += -1*qDim/2+(11);
        		yOffset += -1*qDim/2;
        	}
            case 4: {
                xOffset += -1*qDim/2;
                break;
            }
        }
        
        for(int i=0; i<4; i++) {
            g.setColor(color); 
            g.fillRect(qDim*(pMinos[i].x-3)+xOffset, qDim*(pMinos[i].y+1)+yOffset, 
                       qDim,   
                       qDim);
            
            g.setColor(Color.white);
            g.drawRect(qDim*(pMinos[i].x-3)+xOffset, qDim*(pMinos[i].y+1)+yOffset, 
                       qDim-1,   
                       qDim-1);
            
            g.setColor(Color.black);
            g.drawRect(qDim*(pMinos[i].x-3)+xOffset, qDim*(pMinos[i].y+1)+yOffset, 
                       qDim,                                 
                       qDim);                               
        }
    }
    
    public void move(Point shift) {
         for(int i=0; i<4; i++) {
             pMinos[i].x+=shift.x;
             pMinos[i].y+=shift.y;
         }
    }
    
    public void rotate(int n, boolean chk) {  // 1 들어옴
    	boolean isChk = false;
    	isChk = chk;
    	this.rotation++;
    	
        switch (id) {
            case 1: { // MinoBar
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
                		pMinos[0].x += 2*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 2*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-2*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-2*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 1*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
                		pMinos[0].x += 2*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 2*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-2*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-2*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 1*n;
                	}
            	}
            	
                break;
            }

            case 2: { // MinoL
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 0*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 0*n;
                	}
            	}
            	
                break;
            }
            case 3: { // MinoMirroredL
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
                		pMinos[1].x += 1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n; 
                        pMinos[3].y += 1*n;
                        pMinos[0].x += 2*n;
                        pMinos[0].y += 0*n;
                	} else if(this.rotation==2) {
                		pMinos[1].x += 1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n; 
                        pMinos[3].y +=-1*n;
                        pMinos[0].x += 0*n;
                        pMinos[0].y += 2*n;
                	} else if(this.rotation==3) {
                		pMinos[1].x +=-1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n; 
                        pMinos[3].y +=-1*n;
                        pMinos[0].x +=-2*n;
                        pMinos[0].y += 0*n;
                	} else if(this.rotation==0) {
                		pMinos[1].x +=-1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n; 
                        pMinos[3].y += 1*n;
                        pMinos[0].x += 0*n;
                        pMinos[0].y +=-2*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
                		pMinos[1].x += 1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n; 
                        pMinos[3].y += 1*n;
                        pMinos[0].x += 2*n;
                        pMinos[0].y += 0*n;
                	} else if(this.rotation==1) {
                		pMinos[1].x += 1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n; 
                        pMinos[3].y +=-1*n;
                        pMinos[0].x += 0*n;
                        pMinos[0].y += 2*n;
                	} else if(this.rotation==2) {
                		pMinos[1].x +=-1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n; 
                        pMinos[3].y +=-1*n;
                        pMinos[0].x +=-2*n;
                        pMinos[0].y += 0*n;
                	} else if(this.rotation==3) {
                		pMinos[1].x +=-1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n;
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n; 
                        pMinos[3].y += 1*n;
                        pMinos[0].x += 0*n;
                        pMinos[0].y +=-2*n;
                	}
            	}
            	
                break;
            }
            case 4: { // MinoSquared
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 0*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 0*n;
                	}
            	}
            	
            	break;
            }
            case 5: { // MinoS
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 0*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y += 2*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-2*n;
                        pMinos[3].y += 0*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 0*n;
                        pMinos[3].y +=-2*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 2*n;
                        pMinos[3].y += 0*n;
                	}
            	}
            	
                break;
            }
            
            case 6: { // MinoZ
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
            			pMinos[0].x += 2*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y += 2*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-2*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y +=-2*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 1*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
            			pMinos[0].x += 2*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y += 2*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y += 1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-2*n;
                        pMinos[0].y += 0*n;
                        pMinos[1].x +=-1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x += 0*n;
                        pMinos[0].y +=-2*n;
                        pMinos[1].x += 1*n;
                        pMinos[1].y +=-1*n;
                        pMinos[2].x += 0*n; 
                        pMinos[2].y += 0*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 1*n;
                	}
            	}
            	break;
            }
            	
            case 7: { // MinoT
            	
            	this.rotation = this.rotation % 4;
            	// 처음에 1으로 들어옴 0~3 반복
            	
            	if(isChk==false) {
            		if(this.rotation==1) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==0) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 1*n;
                	}
            	} else {
            		n = -1;
            		if(this.rotation==0) {
            			pMinos[0].x += 1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y += 1*n;
                	} else if(this.rotation==1) {
                		pMinos[0].x += 1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y += 1*n;
                        pMinos[3].x +=-1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==2) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y += 1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x +=-1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y +=-1*n;
                	} else if(this.rotation==3) {
                		pMinos[0].x +=-1*n;
                        pMinos[0].y +=-1*n;
                        pMinos[1].x += 0*n;
                        pMinos[1].y += 0*n;
                        pMinos[2].x += 1*n; 
                        pMinos[2].y +=-1*n;
                        pMinos[3].x += 1*n;
                        pMinos[3].y += 1*n;
                	}
            	}
            	
            	break;
            }
        }
    }
    
    public void reverseRotate() {
        this.rotation -= 2;
        if(this.rotation == -2) this.rotation = 2;
        rotate(1, true);
    }
    
}
