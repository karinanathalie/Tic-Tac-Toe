package Client;
import javax.swing.SwingUtilities;

/**
 * This is the main class representing the client application for the Tic-Tac-Toe game.
 * It initializes the graphical user interface (GUI) and connects it with the controller.
 * 
 * Notes: Please put Images folder outside src folder in Eclipse
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class Client {

    /**
     * The main method that starts the client application.
     * It initializes the view, controller, and starts the GUI on the Event Dispatch Thread.
     *
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				View view = new View();
				Controller controller = new Controller(view);
				controller.start();
			}
		});
		
	}
	
}
