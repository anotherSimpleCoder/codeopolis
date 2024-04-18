package de.htwsaar.esch.Codeopolis.Presentation;

/**
 * The main class that starts the Codeopolis game.
 */
public class Codeopolis {

	/**
     * The main method that starts the Codeopolis game.
     *
     * @param args The command line arguments.
     */
	public static void main(String[] args) {
		UserInterface userInterface= new Dialog();
		userInterface.start();

	}

}
