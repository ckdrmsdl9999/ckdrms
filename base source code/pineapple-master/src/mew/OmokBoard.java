package omok;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class OmokBoard extends JPanel implements MouseListener{
	protected int lineNum;
  OmokGame game;
	int x0, y0;

	public OmokBoard(int lineNum, OmokGame game){
    this.lineNum = lineNum;
		this.x0 = 0;
		this.y0 = 0;
    this.game = game;
    addMouseListener(this);
	}

	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int i = 0; i<lineNum; i++){
			g.drawLine(x0+30 , 30+y0 + i*30, 30+x0+30*(lineNum-1), 30+y0 + i*30);
			g.drawLine(x0+30 + i*30 ,30+y0, 30+x0+i*30, 30+ y0+30*(lineNum-1));
		}
	}

  public void mousePressed(MouseEvent e){
    if((!game.solo && !game.myTurn) || !game.start){
      return;
    }
		int x, y, outerBoundary = 15 + lineNum * 30;

		if(!game.isItend()){
			x = e.getX() + 15;
			y = e.getY() + 15;

			if(x < 15 || y < 15 || x >= outerBoundary || y >= outerBoundary) {
				return;
			}
			x = (x - (x % 30)) / 30;
			y = (y - (y % 30)) / 30;
			//System.out.println(x + " " + y);
			//System.out.println("a");
			game.put(x, y);
		}
	}

  public void put(int x, int y){
    circleDraw(this.getGraphics(), x, y);
  }

  private void circleDraw(Graphics g,int x,int y){
    x = x * 30 - 15;
    y = y * 30 - 15;
    if(game.whoIsTurnIsIt() == 1){
      g.setColor(Color.WHITE);
    }
    else{
      g.setColor(Color.BLACK);
    }
    g.fillOval(x, y, 30, 30);
  }

  @Override
 public void mouseClicked(MouseEvent e) {
   // TODO Auto-generated method stub

 }

 @Override
 public void mouseEntered(MouseEvent e) {
   // TODO Auto-generated method stub

 }

 @Override
 public void mouseExited(MouseEvent e) {
   // TODO Auto-generated method stub

 }

 @Override
 public void mouseReleased(MouseEvent e) {
   // TODO Auto-generated method stub

 }
}
