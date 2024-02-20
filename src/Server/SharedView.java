package Server;

import Client.View;

/**
 * This is the SharedView class representing the shared game state between multiple clients.
 * It manages the game board, checks button presses, winning conditions, draw conditions, and game restarts.
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class SharedView {
	private int board[][];
	
    /**
     * This is the constructor method to constructs a SharedView object, initializing the game board.
     */
	public SharedView() {
		board = new int[3][3];
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            board[i][j] = 0;
	        }
	    }
	}
	
    /**
     * This is the checkButton method.
     * It checks if a button can be pressed at the specified row and column for the given player.
     * It marks the button on the board if available.
     *
     * @param row    The Integer indicating the row of the button.
     * @param col    The Integer indicating the column of the button.
     * @param Player The String indicating the player attempting to press the button.
     * 
     * @return boolean true if the button was successfully pressed, false otherwise.
     */
	public synchronized boolean checkButton(int row, int col, String Player) {
		if(board[row][col] == 0) {
			if (Player.equals("Player 1")) {
				board[row][col] = 1;
			} 
			else if (Player.equals("Player 2")) {
				board[row][col] = 2;
			}
		}
		else {
			return false;
		}
		return true;
	}
	
    /**
     * This is the checkWinningCondition method.
     * It checks if the specified player has achieved a winning condition on the game board.
     *
     * @param Player The String indicating the player to check for winning condition.
     * 
     * @return boolean true if the player has won, false otherwise.
     */
	public synchronized boolean checkWinningCondition(String Player) {
		int item = 0;
		if (Player.equals("Player 1")) {
			item = 1;
		} 
		else if (Player.equals("Player 2")) {
			item = 2;
		}
		 
		// Check horizontally
		int count = 0;
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            if (board[i][j] == item) {
	            	count++;
	            }
	            if (count == 3) {
	            	return true;
	            }
	            if (j == 2) {
	            	count = 0;
	            }
	        }
	    }

		// Check vertically
		count = 0;
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            if (board[j][i] == item) {
	            	count++;
	            }
	            if (count == 3) {
	            	return true;
	            }
	            if (j == 2) {
	            	count = 0;
	            }
	        }
	    }
		
		// Check diagonally
		count = 0;
	    for (int i = 0; i < 3; i++) {
            if (board[i][i] == item) {
            	count++;
            }
            if (count == 3) {
            	return true;
            }
	    }
	    
	    count = 0;
	    for (int i = 0; i < 3; i++) {
            if (board[i][2-i] == item) {
            	count++;
            }
            if (count == 3) {
            	return true;
            }
	    }
	    
		return false;
	}
	
    /**
     * This is the checkDrawCondition method.
     * It checks if the game has ended in a draw.
     *
     * @return boolean true if the game is a draw, false otherwise.
     */
	public synchronized boolean checkDrawCondition() {
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	        	if (board[i][j] == 0) {
	        		return false;
	        	}
	        }
	    }
	    return true;
	}

    /**
     * This is the restart method.
     * It restarts the game by resetting the game board.
     */
	public synchronized void restart() {
		board = new int[3][3];
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            board[i][j] = 0;
	        }
	    }
	}
}