package Client;
import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the view class representing the graphical user interface (GUI) for the Tic-Tac-Toe game.
 * It contains methods for setting up and updating the UI based on user and server interactions.
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class View {
	
	private JFrame frame;
	
	private JPanel InfoPanel;
	private JPanel BoardPanel;
	private JPanel MainPanel;
	private JPanel NamePanel;

	private JMenuBar menuBar;
	private JMenu control;
	private JMenu help;
	private JMenuItem exit;
	private JMenuItem instruction;
	
	private JLabel title;
	
	private JTextField name_input;
	
	private JOptionPane message;
	
	private JButton submit;
	private JButton[][] boardButtons;
	
	private String player;
	private String name;
	
    /**
     * This is the constructor for a new view
     * It initializes the frame and all GUI components.
     */
	public View() {
		setFrame();
		setMenuBar();
		setInfoPanel();
		setBoardPanel();
		setNamePanel();
	}
	
    /**
     * This is the setFrame method 
     * It sets up the main frame of the GUI.
     */
	public void setFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
		frame.setTitle("Tic Tac Toe");
		
		Container cp = frame.getContentPane();
		cp.setLayout(new BorderLayout());
		
		InfoPanel = new JPanel();
		MainPanel = new JPanel();
		BoardPanel = new JPanel(new GridLayout(3, 3));
		NamePanel = new JPanel();
		
		MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
		cp.add(MainPanel);
		
	}
	
    /**
     * This is the setMenuBar method
     * It sets up the menu bar with control and help options.
     */
	public void setMenuBar() {
		menuBar = new JMenuBar();
		control = new JMenu("Control");
		help = new JMenu("Help");
		exit = new JMenuItem("Exit");
		instruction = new JMenuItem("Instruction");
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("Exit".equals(e.getActionCommand())){
					System.exit(0);
				}
			}
		});
		
		instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = 
						"Some information about the game:\n"
						+ "Criteria for a valid move:\n"
						+ "- The move is not occupied by any mark.\n"
						+ "- The move is made in the player's turn.\n"
						+ "- The move is made within the 3 x 3 board.\n"
						+ "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n"
						+ "- Player 1 wins.\n"
						+ "- Player 2 wins.\n"
						+ "- Draw.";
					
				JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		
		control.add(exit);
		help.add(instruction);
		menuBar.add(control);
		menuBar.add(help);
		
		frame.setJMenuBar(menuBar);
		
	}
	
    /**
     * This is the setInfoPanel method 
     * It sets up the information panel in the GUI.
     */
	public void setInfoPanel() {
		title = new JLabel("Enter your player name...");
		
		InfoPanel.add(title);
		InfoPanel.setPreferredSize(new Dimension(50, 25));
		InfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		MainPanel.add(InfoPanel);
	}
	
	 /**
     * This is the setBoardPanel method 
     * It sets up the game board panel with buttons.
     */
	public void setBoardPanel() {
		boardButtons = new JButton[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boardButtons[i][j] = new JButton();
				boardButtons[i][j].setBorder(new LineBorder(Color.BLACK));
				boardButtons[i][j].setPreferredSize(new Dimension(100, 150));
				BoardPanel.add(boardButtons[i][j]);
				boardButtons[i][j].setEnabled(false);
			}
		}
		BoardPanel.setBackground(Color.WHITE);
		MainPanel.add(BoardPanel);
	}
	
    /**
     * This is the setNamePanel method 
     * It sets up the name input panel.
     */
	public void setNamePanel() {
		name_input = new JTextField(20);
		submit = new JButton("Submit");
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = name_input.getText();
				if (getNameLength() != 0) {
					title.setText("WELCOME " + name);
					frame.setTitle("Tic Tac Toe-Player: "+ name);
					submit.setEnabled(false);
					name_input.setEnabled(false);
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							boardButtons[i][j].setEnabled(true);
						}
					}
				}
			}
		});
		
		NamePanel.add(name_input);
		NamePanel.add(submit);
		
		MainPanel.add(NamePanel);
	}
	
	/**
	 * This is the name length getter method.
	 * It gets the length of the name
	 *
	 * @return Integer name length
	 */
	public int getNameLength() {
		name = name_input.getText();
		return name.length();
	}
	
	/**
	 * This is the submit button getter method.
	 * It gets the submit button.
	 *
	 * @return JButton the submit button.
	 */
	public JButton getSubmitButton() {
		return submit;
	}
	
	/**
	 * This is the method to set the current player.
	 *
	 * @param Player The String indicates the name of the player
	 */
	public void setPlayer(String Player) {
		this.player = Player;
	}
	
	/**
	 * This is the move button getter method.
	 * It gets the move button at the specified row and column.
	 *
	 * @param row The Integer indicates the row index.
	 * @param col The Integer indicates the column index.
	 * 
	 * @return JButton The move button at the specified row and column.
	 */
	public JButton getMoveButton(int row, int col) {
		return boardButtons[row][col];
	}
	
	/**
	 * This is the title label getter method.
	 * It gets the title label.
	 *
	 * @return JLabel The title label.
	 */
	public JLabel getTitleLabel() {
		return title;
	}
	
	/**
	 * This is the exit menu item getter method.
	 * It gets the exit menu item.
	 *
	 * @return JMenuItem The exit menu item.
	 */
	public JMenuItem getExit() {
		return exit;
	} 
	
	/**
	 * This is the method called when the players are already full (2 players).
	 * It displays a message when all player slots are occupied, and exits the program.
	 */
	public void playersAreFull() {
		String message = "The game is currently in progress, and all player slots are currently occupied. Please try again later.";
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	    System.exit(0);
	}
	
	/**
	 * This is the method to enable the board button.
	 * It enables all move buttons on the game board.
	 */
	public void enableMove() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boardButtons[i][j].setEnabled(true);
			}
		}
	}
	
	/**
	 * This is the method to disable the board button.
	 * It disables all move buttons on the game board.
	 */
	public void disableMove() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boardButtons[i][j].setEnabled(false);
			}
		}
	}
	
	/**
	 * This is the updateButton method.
	 * It updates the button at the specified row and column with the player's icon.
	 *
	 * @param row    The Integer indicates the row index.
	 * @param col    The Integer indicates the column index.
	 * @param Player The String indicates the name of the player ("X" or "O").
	 */
	public void updateButton(int row, int col, String Player) {
		System.out.println("Updating button: row=" + row + ", col=" + col + ", Player=" + Player);
		if (Player.equals("X")) {
			ImageIcon icon = new ImageIcon("Images/X.PNG");
			Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		    ImageIcon scaledIcon = new ImageIcon(image);
		    boardButtons[row][col].setIcon(scaledIcon);
		}
		else if (Player.equals("O")) {
			ImageIcon icon = new ImageIcon("Images/O.PNG");
			Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		    ImageIcon scaledIcon = new ImageIcon(image);
		    boardButtons[row][col].setIcon(scaledIcon);
		}
	}
	
	/**
	 * This is the method called when the player wins the game.
	 * It displays a message when the player wins and prompts for a new game.
	 *
	 * @return String "Yes" if the player wants to play again, "No" otherwise.
	 */
	public String playerWins() {
		String message = "Congratulations. You win. Do you want to play again?";
		int option = JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.YES_NO_OPTION);
		
		if (option == JOptionPane.YES_OPTION) {
			return "Yes";
		}
		else {
			return "No";
		}
	}
	
	/**
	 * This is the method called when the player loses the game.
	 * It displays a message when the player loses and prompts for a new game.
	 *
	 * @return String "Yes" if the player wants to play again, "No" otherwise.
	 */
	public String playerLoses() {
		String message = "You lose. Do you want to play again?";
		int option = JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.YES_NO_OPTION);
		
		if (option == JOptionPane.YES_OPTION) {
			return "Yes";
		}
		else {
			return "No";
		}
	}
	
	/**
	 * This is the method called when the neither of the players wins nor loses.
	 * It displays a message when the game is a draw and prompts for a new game.
	 *
	 * @return String "Yes" if the player wants to play again, "No" otherwise.
	 */
	public String draw() {
		String message = "Draw. Do you want to play again?";
		int option = JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.YES_NO_OPTION);
		
		if (option == JOptionPane.YES_OPTION) {
			return "Yes";
		}
		else {
			return "No";
		}
	}
	
	/**
	 * This is the gameEnds method.
	 * It displays a message when the game ends due to a player leaving and exits the program.
	 */
	public void gameEnds() {
		String message = "Game Ends. One of the players left.";
		JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
	
	/**
	 * This is the PlayerExits method.
	 * It exits the program when a player chooses to exit.
	 */
	public void PlayerExits() {
		System.exit(0);
	}
	
	/**
	 * This is the restartView method called when the player chooses to play again.
	 * It restarts the game view by resetting the title and clearing the game board.
	 */
	public void restartView() {		
		title.setText("WELCOME " + name);
		
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
            	boardButtons[i][j].setIcon(null);
                boardButtons[i][j].setEnabled(false);
            }
        }
	}
	
}
