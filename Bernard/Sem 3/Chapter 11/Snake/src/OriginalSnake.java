import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class OriginalSnake extends JFrame implements KeyListener
{
	int headCol = 35, headRow = 20,snakeLength = 9, mouseRow, mouseCol, score = 0,counter = 0, playAgain;
	boolean right, left, down, up, winning = true, needMouse = false, done, gameStarted = false;
	JPanel[][] snakeBoard = new JPanel[40][60];
	JLabel scoreLabel = new JLabel("Score:  " + score, JLabel.CENTER);
	int[][] snake = new int[40][60];
	JFrame scoreFrame;

	static OriginalSnake f1;

	public OriginalSnake()
	{
		this.getContentPane().addKeyListener(this);
		this.getContentPane().setFocusable(true);
		this.setLayout(new GridLayout(40,60));
		for(int i = 0; i < snakeBoard.length; i++)
		{
			for(int x = 0; x < snakeBoard[0].length; x++)
			{
				snakeBoard[i][x] = new JPanel();
				//snakeBoard[i][x].setBackground(new Color(255,255,255));
				this.add(snakeBoard[i][x]);
				snake[i][x] = 0;
			}
		}
		for(int i = 27; i < 36; i++)
		{
			counter++;
			snake[20][i] = counter;
		}
		scoreFrame = new JFrame();
		scoreFrame.add(scoreLabel);
		scoreFrame.setTitle("Score");
		scoreFrame.setBounds(915,5,150,100);
		scoreFrame.setVisible(true);
		pickMouse();
		drawBoard();
	}

	public void keyPressed(KeyEvent ke)
	{
		right = false;
		left = false;
		down = false;
		up = false;

		if(ke.getKeyCode() == KeyEvent.VK_S && !gameStarted)
		{
			GameThread t1 = new GameThread();
			t1.start();
			right = true;
			gameStarted = true;
		}
		else if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			right = true;
		}
		else if(ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			left = true;
		}
		else if(ke.getKeyCode() == KeyEvent.VK_UP)
		{
			up = true;
		}
		else if(ke.getKeyCode() == KeyEvent.VK_DOWN)
		{
			down = true;
		}
	}

	public void keyReleased(KeyEvent ke){}
	public void keyTyped(KeyEvent ke){}

	public void pauseThread(int pauseTime)
	{
		try
		{
			Thread.sleep(pauseTime);
		}
		catch(Exception ex){}
	}

	public void pickMouse()
	{
		done = false;
		while(!done)
		{
			mouseRow = (int)(Math.random() * 40);
			mouseCol = (int)(Math.random() * 60);

			if(snake[mouseRow][mouseCol] == 0)
			{
				done = true;
				snake[mouseRow][mouseCol] = -1;
			}
		}
	}

	public void drawBoard()
	{
		for(int i = 0; i < snakeBoard.length; i++)
		{
			for(int x = 0; x < snakeBoard[0].length; x++)
			{
				if(snake[i][x] == snakeLength)
				{
					snakeBoard[i][x].setBackground(new Color(204,153,255));
				}
				else if(snake[i][x] == 1)
				{
					snakeBoard[i][x].setBackground(new Color(125,200,185));
				}
				else if(snake[i][x] > 0)
				{
					snakeBoard[i][x].setBackground(new Color(0,0,0));
				}
				else if(snake[i][x] == -1)
				{
					snakeBoard[i][x].setBackground(Color.lightGray);
				}
				else
				{
					snakeBoard[i][x].setBackground(new Color(0,0,255));
				}
			}
		}
		pauseThread(100);
	}

	public class GameThread extends Thread
	{
		public void run()
		{
			while(winning)
			{
				if(needMouse)
				{
					pickMouse();
					needMouse = false;
				}
				if(right)
				{
					headCol++;
					if(headCol > 59)
					{
						headCol = 0;
					}
					if(snake[headRow][headCol] == snakeLength -1)
					{
						right = false;
						left = true;
					}
				}
				else if(left)
				{
					headCol--;
					if(headCol < 0)
					{
						headCol = 59;
					}
					if(snake[headRow][headCol] == snakeLength -1)
					{
						left = false;
						right = true;
					}
				}
				else if(down)
				{
					headRow++;
					if(headRow > 39)
					{
						headRow = 0;
					}
					if(snake[headRow][headCol] == snakeLength -1)
					{
						down = false;
						up = true;
					}
				}
				else
				{
					headRow--;
					if(headRow < 0)
					{
						headRow = 39;
					}
					if(snake[headRow][headCol] == snakeLength -1)
					{
						up = false;
						down = true;
					}
				}
				if(snake[headRow][headCol] == -1)
				{
					score += 100;
					needMouse = true;
					snakeLength++;
					snake[headRow][headCol] = snakeLength;
					drawBoard();
					scoreLabel.setText("Score:  " + score);
					scoreLabel.invalidate();
					scoreLabel.validate();
					pauseThread(100);
				}
				else if(snake[headRow][headCol] != 0 && snake[headRow][headCol] != snakeLength -1 && snake[headRow][headCol] != snakeLength)
				{

					Font loseFont = new Font("Chiller", Font.BOLD, 40);
					JLabel loseLabel = new JLabel("Game Over!", JLabel.CENTER);
					winning = false;
					loseLabel.setFont(loseFont);
					loseLabel.setForeground(Color.red);
					f1.getContentPane().removeAll();
					f1.getContentPane().repaint();
					f1.getContentPane().setBackground(Color.black);;
					f1.getContentPane().setLayout(new BorderLayout());
					f1.getContentPane().add(loseLabel,BorderLayout.CENTER);
					f1.getContentPane().invalidate();
					f1.getContentPane().validate();

					playAgain = JOptionPane.showConfirmDialog(null,"Do you want to play again", "Play Again", JOptionPane.YES_NO_OPTION);
					if(playAgain == 0)
					{
						f1.dispose();
						f1 = new OriginalSnake();
						f1.setTitle("Snake");
						f1.setBounds(0,0,910,715);
						f1.setVisible(true);
					}
					else
					{
						scoreFrame.dispose();
						System.exit(0);
					}
				}
				else if(snake[headRow][headCol] != snakeLength -1)
				{
					for(int i = 0; i < snake.length; i++)
					{
						for(int j = 0; j < snake[0].length; j++)
						{
							if(snake[i][j] > 0)
							{
								snake[i][j]--;
							}
						}
					}
					snake[headRow][headCol] = snakeLength;
					drawBoard();
				}
			}
		}
	}

	public static void main(String [] args)
	{
		f1 = new OriginalSnake();
		f1.setTitle("Snake");
		f1.setBounds(0,0,910,715);
		f1.setVisible(true);
	}
}