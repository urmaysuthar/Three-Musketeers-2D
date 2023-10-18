package assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment1.Exceptions.InvalidMoveException;

public class HumanAgent extends Agent {

    public HumanAgent(Board board) {
        super(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    @Override
    public Move getMove() { 
        try {
            Scanner scanner = new Scanner(System.in);

            // Get all possible pieces that can be moved
            List<Cell> possiblePieces = board.getPossibleCells();

            // Convert the possible pieces to a list of strings
            List<String> possiblePiecesString = new ArrayList<String>();
            char col;
            for (int i = 0; i < possiblePieces.size(); i++) {
                col = Utils.convertIntToLetter(possiblePieces.get(i).getCoordinate().col + 1);
                possiblePiecesString.add(String.format("%c%d", col, possiblePieces.get(i).getCoordinate().row + 1));
            }

            // Ask the user to select a piece to move
            String selectedPiece;
            System.out.print("[" + board.getTurn() + "] ");
            System.out.print("Possible moves are ");
            System.out.print(possiblePiecesString.toString());
            System.out.print(". Enter your move: ");
            selectedPiece = scanner.nextLine();

            // Keep asking the user to select a piece until a valid one is selected
            while (!possiblePiecesString.contains(selectedPiece.toUpperCase())) {
                System.out.println(selectedPiece + " is an invalid piece.");
                System.out.print("[" + board.getTurn() + "] ");
                System.out.print("Possible moves are ");
                System.out.print(possiblePiecesString.toString());
                System.out.print(". Enter your move: ");
                selectedPiece = scanner.nextLine();
            }

            // Get the cell corresponding to the selected piece
            Cell fromCell = board.getCell(Utils.parseUserMove(selectedPiece.toUpperCase()));

            // Get all possible destinations for the selected piece
            List<Cell> possibleDestinations = board.getPossibleDestinations(fromCell);

            // Convert the possible destinations to a list of strings
            List<String> possibleDestinationsAsString = new ArrayList<String>();
            for (int i = 0; i < possibleDestinations.size(); i++) {
                col = Utils.convertIntToLetter(possibleDestinations.get(i).getCoordinate().col + 1);
                possibleDestinationsAsString
                        .add(String.format("%c%d", col, possibleDestinations.get(i).getCoordinate().row + 1));
            }

            // Ask the user to select a destination for the selected piece
            String selectedDestination;
            System.out.print("[" + board.getTurn() + "] ");
            System.out.print("Possible Destinations are ");
            System.out.print(possibleDestinationsAsString.toString());
            System.out.print(". Enter your move: ");
            selectedDestination = scanner.nextLine();

            // Keep asking the user to select a destination until a valid one is selected
            while (!possibleDestinationsAsString.contains(selectedDestination.toUpperCase())) {
                System.out.println(selectedDestination + " is an invalid move.");
                System.out.print("[" + board.getTurn() + "]› ");
                System.out.print("Your Possible moves are ");
                System.out.print(possibleDestinationsAsString.toString());
                System.out.print(". Enter your move: ");
                selectedDestination = scanner.nextLine();
            }

            // Get the cell corresponding to the selected destination
            Cell toCell = board.getCell(Utils.parseUserMove(selectedDestination.toUpperCase()));

            // Create and return a Move object with the selected fromCell and toCell
            return new Move(fromCell, toCell);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        return null;
    }

}