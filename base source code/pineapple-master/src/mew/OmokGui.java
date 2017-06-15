package mew;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class OmokGui extends JFrame{
	private OmokGame game;
	protected int lineNum;
	private OmokBoard gameBoard;

	public OmokGui(int gLineNum, OmokGame game) {
		// TODO Auto-generated constructor stub
		super("NeOP's Super Awesome Omok Game");
		setLayout(new BorderLayout());

		this.game = game;
		this.lineNum = gLineNum++;
		gameBoard = new OmokBoard(this.lineNum, game);
		add(gameBoard, "Center");
		setVisible(true);

		gLineNum *= 30;
		this.setSize(this.getInsets().left + gLineNum, this.getInsets().top + gLineNum);

		setResizable(false);
	}

	public void put(int x, int y){
    gameBoard.put(x, y);
  }

	public void gameEnd(){
		System.out.println("Game End");
		JOptionPane.showMessageDialog(this, (game.whoIsTurnIsIt() == 1 ? "White" : "Black") + " Win!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
	}

}
