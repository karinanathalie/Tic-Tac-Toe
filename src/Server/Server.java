package Server;

import java.util.*;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;

/**
 * This is the Server Class representing the game server that handles client connections.
 * It manages communication between clients, game logic, and broadcasts messages to all connected clients.
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class Server {
	private ServerSocket serverSocket;
	private SharedView sharedView;
	
	private int numPlayer;
	private int row;
	private int col;

	// The set of all the print writers for all the clients, used for broadcast.
	private Set<PrintWriter> writers = new HashSet<>();

    /**
     * This is the constructor to construct a Server object with the specified ServerSocket.
     *
     * @param serverSocket The ServerSocket for handling client connections.
     */
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.numPlayer = 0;
		this.sharedView = new SharedView();
	}

    /**
     * This is the start method.
     * It starts the server and handles client connections using a thread pool.
     */
	public void start() {
		var pool = Executors.newFixedThreadPool(200);
		int clientCount = 1;
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				pool.execute(new Handler(socket));
				System.out.println("Connected to client " + clientCount++);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * This is the Handler class represents a thread that handles communication with a specific client.
     */
	public class Handler implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;

        /**
         * This is the constructor method to construct a Handler object with the specified socket.
         *
         * @param socket The Socket for communication with the client.
         */
		public Handler(Socket socket) {
			this.socket = socket;
		}
		
        /**
         * This is the run method for handling client communication and game logic.
         */
		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream(), true);

				// Add the client to the broadcast list
				writers.add(output);

				while (input.hasNextLine()) {
					var command = input.nextLine();

					System.out.println("Server Received: " + command);

					// Condition 1: Players submitted their name (indicating they join the game)
					if (command.startsWith("Name Submitted")) {
						numPlayer++;

						if ((numPlayer == 1) && (writers.size() < 3)) {
							output.println("Player 1 joined successfully");
						}
						else if ((numPlayer == 2)&& (writers.size() < 3)) {
							for (PrintWriter writer : writers) {
								writer.println("Player 2 joined successfully");
							}
							System.out.println("Server Broadcasted: Player 2 joined successfully");
							numPlayer = 0;
						}
						else {
							numPlayer--;
							output.println("Players are full");
							System.out.println(numPlayer);
						}
					}
					
					// Condition 2: Player exits in the middle of the game
					if (command.startsWith("Player Exits")) {
						System.out.println(numPlayer);
						if (numPlayer == 0) {
							sharedView.restart();
							for (PrintWriter writer : writers) {
								writer.println("Player Exits");
							}
							System.out.println("Server Broadcasted: Player Exits");
						}
					}
					
					// Condition 3: Player moves
					if (command.startsWith("Player 1 pressed the Button")) {
						String[] commands = command.split("\\s+");
						row = Integer.parseInt(commands[7]);
						col = Integer.parseInt(commands[10]);
						
						// Check whether the move is valid
						boolean buttonStatus = sharedView.checkButton(row, col, "Player 1");
						if (buttonStatus == true) {
							for (PrintWriter writer : writers) {
								writer.println("Player 1 successfully pressed the Button at row " + row + " and col "+ col);
							}
							System.out.println("Server Broadcasted: Player 1 successfully pressed the Button at row " + row + " and col "+ col);
							
							// If the move is valid, check whether the move makes the player win
							boolean winningStatus = sharedView.checkWinningCondition("Player 1");
							if (winningStatus == true) {
								sharedView.restart();
								for (PrintWriter writer : writers) {
									writer.println("Player 1 wins");
								}
								System.out.println("Server Broadcasted: Player 1 wins");
							}
							
							// If the move is valid, check whether the move creates a draw condition
							boolean drawStatus = sharedView.checkDrawCondition();
							if (drawStatus == true) {
								sharedView.restart();
								for (PrintWriter writer : writers) {
									writer.println("Draw");
								}
								System.out.println("Server Broadcasted: Draw");
							}
							
						}
						else {
							output.println("Player 1 failed to press the Button");
						}

						
					}
					else if (command.startsWith("Player 2 pressed the Button")) {
						String[] commands = command.split("\\s+");
						row = Integer.parseInt(commands[7]);
						col = Integer.parseInt(commands[10]);
						
						// Check whether the move is valid
						boolean buttonStatus = sharedView.checkButton(row, col, "Player 2");
						if (buttonStatus == true) {
							for (PrintWriter writer : writers) {
								writer.println("Player 2 successfully pressed the Button at row " + row + " and col "+ col);
							}
							System.out.println("Server Broadcasted: Player 2 successfully pressed the Button at row " + row + " and col "+ col);
							
							// If the move is valid, check whether the move makes the player win
							boolean winningStatus = sharedView.checkWinningCondition("Player 2");
							if (winningStatus == true) {
								sharedView.restart();
								for (PrintWriter writer : writers) {
									writer.println("Player 2 wins");
								}
								System.out.println("Server Broadcasted: Player 2 wins");
							}
							
							// If the move is valid, check whether the move creates a draw condition
							boolean drawStatus = sharedView.checkDrawCondition();
							if (drawStatus == true) {
								sharedView.restart();
								for (PrintWriter writer : writers) {
									writer.println("Draw");
								}
								System.out.println("Server Broadcasted: Draw");
							}
							
						}
						else {
							output.println("Player 2 failed to press the Button");
						}
					}
			
					// Condition 3: Player wants to play again
					if(command.startsWith("Player wants to play again")) {
						numPlayer++;
						System.out.println(numPlayer);
						if ((numPlayer == 1) && (writers.size() < 3)) {
							output.println("Player 1 joined successfully");
						}
						else if ((numPlayer == 2) && (writers.size() < 3)) {
							for (PrintWriter writer : writers) {
								writer.println("Player 2 joined successfully");
							}
							System.out.println("Server Broadcasted: Player 2 joined successfully");
							numPlayer = 0;
						}
					}
					
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				
			} finally {
				// Condition 4: Client Disconnected
				if (output != null) {
					System.out.println("Client disconnected");
					writers.remove(output);
				}
			}
		}
	}
}
