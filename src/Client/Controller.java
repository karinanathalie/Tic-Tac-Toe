package Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * This is the Controller class responsible for handling user input and communication with the server.
 * It connects the view and the server, updating the UI based on the server's responses.
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class Controller {

	private View view;
	
	private ActionListener submitButtonListener;
	private ActionListener moveButtonListener;
	private ActionListener exitMenuItemListener;

	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	private String Player;

    /**
     * This is the constructor for a new controller with the specified view.
     *
     * @param View The view associated with this controller.
     */
	public Controller(View view) {
		this.view = view;
	}
	
    /**
     * This is the start method 
     * It starts the controller by establishing a connection to the server, setting up listeners,
     * and creating a thread for handling server messages.
     */
	public void start() {
		try {
			this.socket = new Socket("127.0.0.1", 5001);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		submitButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (view.getNameLength() != 0) {
					out.println("Name Submitted");	
				}
			}
		};
		view.getSubmitButton().addActionListener(submitButtonListener);
		
		moveButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JButton pressedButton = (JButton) actionEvent.getSource();
				
				int row = -1;
				int col = -1;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						if (view.getMoveButton(i, j) == pressedButton) {
							row = i;
							col = j;
							break;
						}
					}
				}
				
				if ((row != -1) && (col != -1)){
					out.println(Player + " pressed the Button at row " + row + " and col " + col);
				}	
			}
		};
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				view.getMoveButton(i, j).addActionListener(moveButtonListener);
			}
		}
		
		exitMenuItemListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (view.getNameLength() != 0) {
					out.println("Player Exits");
				}
			}
		};
		view.getExit().addActionListener(exitMenuItemListener);

		// Creates a new Thread for reading server messages
		Thread handler = new ClientHandler(socket);
		handler.start();
	}

    /**
     * This is the inner class ClientHandler representing a thread for handling messages received from the server.
     * It also updates the UI based on server commands.
     */
	class ClientHandler extends Thread {
		private Socket socket;
		private View playerView;
		private int row;
		private int col;

        /**
         * This is the constructor method 
         * It creates a new client handler with the specified socket.
         *
         * @param socket The socket associated with this ClientHandler.
         */
		public ClientHandler(Socket socket) {
			this.socket = socket;
			this.playerView = view;
		}

        /**
         * This is the main run method of the ClientHandler thread.
         * It continuously reads messages from the server and updates the UI accordingly.
         */
		@Override
		public void run() {
			try {
				readFromServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * This is the readFromServer method 
		 * It reads messages from the server and updates the client's UI accordingly.
		 *
		 * @throws Exception If an error occurs while processing server messages.
		 */
		public void readFromServer() throws Exception {
			try {
				while (in.hasNextLine()) {
					var command = in.nextLine();
					System.out.println("Client Received: " + command);
					
					// Condition 1: Player joins
					if (command.startsWith("Player 1 joined successfully")) {
						this.playerView.setPlayer("X");
						Player = "Player 1";
					}
					else if (command.startsWith("Player 2 joined successfully")) {
						if (Player == "Player 1") {
							this.playerView.getTitleLabel().setText("Player 2 has joined. Your turn to move");
							this.playerView.enableMove(); // When Player 2 joins, Player 1 can start to move
						}
						else {
							this.playerView.setPlayer("O");
							Player = "Player 2";
							this.playerView.getTitleLabel().setText("Waiting for your opponent to move");
							this.playerView.disableMove();
						}
					}
					else if (command.startsWith("Players are full")) {
						this.playerView.playersAreFull();
					}
					
					// Condition 2: Player successfully moves (the buttons are pressed)
					if (command.startsWith("Player 1 successfully pressed the Button")) {
						String[] commands = command.split("\\s+");
						row = Integer.parseInt(commands[8]);
						col = Integer.parseInt(commands[11]);
						view.updateButton(row, col, "X");
						if (Player == "Player 1") {
							this.playerView.getTitleLabel().setText("Vaild move, wait for your opponent.");
							this.playerView.disableMove();
						}
						else {
							this.playerView.getTitleLabel().setText("Your opponent has moved, now is your turn.");
							this.playerView.enableMove();
						}
					}
					else if (command.startsWith("Player 2 successfully pressed the Button")) {
						String[] commands = command.split("\\s+");
						row = Integer.parseInt(commands[8]);
						col = Integer.parseInt(commands[11]);
						view.updateButton(row, col, "O");
						if (Player == "Player 2") {
							this.playerView.getTitleLabel().setText("Vaild move, wait for your opponent.");
							this.playerView.disableMove();
						}
						else {
							this.playerView.getTitleLabel().setText("Your opponent has moved, now is your turn.");
							this.playerView.enableMove();
						}
					}
					
					// Condition 3: Player failed to move
					if (command.startsWith("Player 1 failed to press the Button") || command.startsWith("Player 2 failed to press the Button")) {
						this.playerView.getTitleLabel().setText("Invalid move. Please try again.");
					}
					
					// Condition 4: Player exits in the middle of the game
					if (command.startsWith("Player Exits")) {
						view.gameEnds();
					}
					
					// Condition 5: The Game Ends (Player 1 Wins, Player 2 Wins, or Draw)
					String player1PlayAgain = ""; 
					String player2PlayAgain = "";
					
					// Condition 5a : Player 1 wins, Player 2 loses
					if (command.startsWith("Player 1 wins")) {
						view.disableMove();
						if (Player == "Player 1") {
							player1PlayAgain = this.playerView.playerWins();
						}
						if (Player == "Player 2") {
							player2PlayAgain = this.playerView.playerLoses();
						}
					}
					
					// Condition 5b : Player 1 loses, Player 2 wins
					if (command.startsWith("Player 2 wins")) {
						view.disableMove();
						if (Player == "Player 2") {
							player2PlayAgain = this.playerView.playerWins();
						}
						else {
							player1PlayAgain = this.playerView.playerLoses();
						}
					}
					
					// Condition 5c : Draw
					if (command.startsWith("Draw")) {
						view.disableMove();
						if (Player == "Player 1") {
							player1PlayAgain = this.playerView.draw();
						}
						else {
							player2PlayAgain = this.playerView.draw();
						}
					}
					
					if (Player == "Player 1") {
						if (player1PlayAgain == "No") {
							out.println("Player wants to exit");
							this.playerView.PlayerExits();
							Player = "";
						}
						else if (player1PlayAgain == "Yes") {
							this.playerView.restartView();
							Player = "";
							out.println("Player wants to play again");
						}
					}
					
					if (Player == "Player 2") {
						if (player2PlayAgain == "No") {
							out.println("Player wants to exit");
							this.playerView.PlayerExits();
							Player = "";
						}
						else if (player2PlayAgain == "Yes") {
							this.playerView.restartView();
							out.println("Player wants to play again");
							Player = "";
						}
					}
						
					out.flush();
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}

}
