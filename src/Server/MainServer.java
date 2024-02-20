package Server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the MainServer representing the main server application for handling client connections.
 * It initializes the server socket, starts the server, and handles shutdown gracefully.
 * 
 * @author karinanathalie
 * @version 1.0
 * @since 2023-11-25
 */
public class MainServer {
	
    /**
     * This is the main method to start the server.
     *
     * @param args Command line arguments 
     * @throws IOException If an I/O error occurs when creating the ServerSocket.
     */
	public static void main(String[] args) throws IOException {
		System.out.println("Server is runnning...");
		
		Runtime.getRuntime().addShutdownHook(new Thread (new Runnable() {
			public void run() {
				System.out.println("Server stopped.");
			}
		}));
	
		try (var listener = new ServerSocket(5001)){
			// Create and start the server
			Server server = new Server(listener);
			server.start();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
