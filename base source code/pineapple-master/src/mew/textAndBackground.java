package Chat;

import java.awt.Color;
import java.awt.Font;

public class textAndBackground {
	private int font;		// 폰트
	private Color BackColor;	// 배경색
	private int size = 12;	// 글자크기
	
	public int getFont(){
		return font;
	}
	
	public void setFont(int font){
		this.font = font;
	}
	
	public Color getBackColor(){
		return BackColor;
	}
	
	public void setBackColor (Color color){
		this.BackColor = color;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
}
