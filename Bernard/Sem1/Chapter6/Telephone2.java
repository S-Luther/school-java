import java.awt.*;
import java.awt.event.*;
public class Telephone2 extends Frame implements ActionListener
{
	Button[] buttonArray = new Button[13];
	TextField num = new TextField(20);
	Label help = new Label();
	Panel centerPanel = new Panel();

	public Telephone2()
	{
		this.setBackground(Color.red);
		this.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(4,3));
		add(num, BorderLayout.NORTH);
		num.setText(" ");
		num.setEditable(false);
		num.setBackground(Color.red);
		num.setForeground(Color.blue);
		add(centerPanel, BorderLayout.CENTER);
		for(int i = 1; i< buttonArray.length; i++)
		{
			buttonArray[i] = new Button();
			buttonArray[i].setLabel(String.valueOf(i));
			buttonArray[i].setBackground(Color.blue);
			buttonArray[i].setForeground(Color.red);
			buttonArray[i].addActionListener(this);
			centerPanel.add(buttonArray[i]);
		}
		buttonArray[10].setLabel("-");
		buttonArray[11].setLabel("0");
		buttonArray[12].setLabel("#");
		help.setText("Click each button above to dial your number");
		help.setForeground(Color.blue);
		add(help, BorderLayout.SOUTH);

		addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			}
		);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == buttonArray[10])
		{
			String pnum = num.getText();
			num.setText(pnum + "-");
		}
		else if(e.getSource() == buttonArray[11])
		{
			String pnum = num.getText();
			num.setText(pnum + "0");
		}
		else if(e.getSource() == buttonArray[12])
		{
			String pnum = num.getText();
			num.setText(pnum + "#");
		}
		else
		{
			for(int i = 1; i< buttonArray.length; i++)
			{
				if(e.getSource() == buttonArray[i])
				{
					String pnum = num.getText();
					num.setText(pnum + i);
				}
			}
		}
	}

	public static void main(String args[])
	{
	Telephone2 t = new Telephone2();
	t.setVisible(true);
	t.setBounds(50,50,500,600);
	}
}