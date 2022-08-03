package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class FieldElement { 
	// field 20x10을 구성하는 클래스
	// 필드를 블럭으로 채우는 것과 지우는 것을 담당
	Color color;
	Point coors;

	public FieldElement(Color clr, Point coors) {
		this.color = clr;
		this.coors = coors;
	}

	public FieldElement(FieldElement elem) {
		this.color = new Color(elem.color.getRGB());
		this.coors = new Point(elem.coors.x, elem.coors.y);
	}

	public void draw(Graphics g, int dim) {
		g.setColor(new Color(120, 120, 120));
		g.setColor(color);
		g.fillRect(coors.x * dim + 1, coors.y * dim + 1, dim - 1, dim - 1);
		g.setColor(Color.white);
		g.drawRect(coors.x * dim + 1, coors.y * dim + 1, dim - 2, dim - 2);
		g.setColor(Color.black);
		g.drawRect(coors.x * dim + 1, coors.y * dim + 1, dim - 1, dim - 1);
	}
}
