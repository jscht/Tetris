package tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MinoBag {
	// 블럭의 생성과 생성 규칙(7-bag)을 정의한 클래스
	
	Tetromino heldMino;
	List<Tetromino> mainMinoBag;
	List<Tetromino> subMinoBag;
	HashSet<Integer> set;
	List<Integer> list;
	
	public MinoBag() {
		heldMino = null;
		
		mainMinoBag = new ArrayList<Tetromino>();
		subMinoBag = new ArrayList<Tetromino>();
		
		set = new HashSet<>();
		// 순서를 유지하지 않는 정수형 데이터 집합
		
		while (set.size() < 7) { 
			Double d = Math.random() * 7;
			//System.out.println(d);
			set.add(d.intValue());
			// 집합에 삽입시 중복을 허용하지 않는다. 다시 랜덤 값을 뽑아내게 된다.
		}
		list = new ArrayList<>(set);
		
		Collections.shuffle(list); // 섞기
		for(int i=0; i<7; i++) {
			mainMinoBag.add(new Tetromino(list.get(i)+1));
		}
		
		Collections.shuffle(list); // 섞기
		for(int i=0; i<7; i++) {
			subMinoBag.add(new Tetromino(list.get(i)+1));
		}
	}
	
	public void shiftUp() {
		mainMinoBag.add(subMinoBag.get(0));
		mainMinoBag.remove(0);
		subMinoBag.remove(0);
		
		// 한번 생성한 이후로는 계속 subMinobag만 만들어주면 된다
		if(subMinoBag.size()==0) {
			Collections.shuffle(list);
			
			for(int i=0; i<7; i++) {
				subMinoBag.add(new Tetromino(list.get(i)+1));
			}
		}
    }
    
    public Tetromino newMino() {
        return mainMinoBag.get(0);
    }
    
    public void setHeld(Tetromino held) {
        this.heldMino = held; 
    }
    
    public Tetromino getHeld() {
        return this.heldMino; 
    }
	
}
