package assignment1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Board {
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;

    /**
     * Create a Board with the current player turn set.
     */
    public Board() {
        this.loadBoard("Boards/Starter.txt");
    }

    /**
     * Create a Board with the current player turn set and a specified board.
     * @param boardFilePath The path to the board file to import (e.g. "Boards/Starter.txt")
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    public void setTurn(Piece.Type turn) {
        this.turn = turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) { // TODO
        return this.board[coordinate.row][coordinate.col];
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Returns a list of all the cells on the board that contain a Musketeer piece.
     *
     * @return A list of cells containing Musketeer pieces
     */
    public List<Cell> getMusketeerCells() {
        List<Cell> musketeerCells = new ArrayList<Cell>();
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j].hasPiece() && board[i][j].getPiece().getType() == Piece.Type.MUSKETEER) {
                    musketeerCells.add(board[i][j]);
                }
            }
        }
        
        return musketeerCells;
    }

    /**
     * Gets all the Guard cells on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() {
        List<Cell> guardCells = new ArrayList<Cell>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j].hasPiece() && board[i][j].getPiece().getType() == Piece.Type.GUARD) {
                    guardCells.add(board[i][j]);
                }
            }
        }

        return guardCells;
    }


    /**
     * Executes the given move on the board and changes turns at the end of the
     * method.
     * 
     * @param move a valid move
     */
    /**
     * This method moves a piece from one cell to another based on the given move object.
     * If the current turn is MUSKETEER, the method checks if the move is valid and then moves the piece to the new cell.
     * If the current turn is GUARD, the method checks if the move is valid and then moves the piece to the new cell.
     * The turn is then switched to the opposite player.
     *
     * @param move the move object containing the fromCell and toCell coordinates
     */
    public void move(Move move) { // TODO
        if (getTurn() == Piece.Type.MUSKETEER) {
            Coordinate fromCell = move.fromCell.getCoordinate();
            Coordinate toCell = move.toCell.getCoordinate();
            Move moves = new Move(move.fromCell, move.toCell);
            if (isValidMove(moves)) {
                getCell(toCell).setPiece(move.fromCell.getPiece());
                getCell(fromCell).removePiece();
                turn = Piece.Type.GUARD;
            }
            
        } else if (getTurn() == Piece.Type.GUARD) { 

            Coordinate fromCell = move.fromCell.getCoordinate();
            Coordinate toCell = move.toCell.getCoordinate();
            Move moves = new Move(move.fromCell, move.toCell);
            if (isValidMove(moves)) {
                getCell(toCell).setPiece(move.fromCell.getPiece());
                getCell(fromCell).removePiece();
                turn = Piece.Type.MUSKETEER;
            }
        }
    }

    /**
     * Undo the move given.
     * 
     * @param move Copy of a move that was done and needs to be undone. The move
     *             copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    /**
     * Undoes the given move by swapping the pieces in the from and to cells,
     * and updating the turn accordingly. If the current turn is GUARD, the
     * from cell is set to a Musketeer and the to cell is set to a Guard, and
     * the turn is set to MUSKETEER. Otherwise, the from cell is set to a Guard,
     * the to cell is emptied, and the turn is set to GUARD.
     *
     */
    public void undoMove(Move move) { // TODO
        Cell FromCell = this.board[move.fromCell.getCoordinate().row][move.fromCell.getCoordinate().col];
        Cell ToCell = this.board[move.toCell.getCoordinate().row][move.toCell.getCoordinate().col];

        FromCell.setPiece(ToCell.getPiece());
        if (FromCell.getPiece().getType().equals(Piece.Type.MUSKETEER)) {
            ToCell.setPiece(new Guard());
        } else {
            ToCell.setPiece(null);
        }

        if (this.turn.equals(Piece.Type.MUSKETEER)) {
            this.turn = Piece.Type.GUARD;
        } else {
            this.turn = Piece.Type.MUSKETEER;
        }
    }

    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * @param move a move
     * @return     True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) {
        Cell fromCell = move.fromCell;
        Coordinate fromCoordinate = fromCell.getCoordinate();
        Coordinate toCoordinate = move.toCell.getCoordinate();

        int xDifference = Math.abs(fromCoordinate.col - toCoordinate.col);
        int yDifference = Math.abs(fromCoordinate.row - toCoordinate.row);
        if ((xDifference == 0 && yDifference == 1) || (xDifference == 1 && yDifference == 0)) {
            if (isOnBoard(toCoordinate)) {
                return fromCell.getPiece().canMoveOnto(move.toCell);
            }
        }
        return false;
    }

    /**
     * Checks if the given coordinate is within the bounds of the board.
     * @param coordinate the coordinate to check
     * @return true if the coordinate is on the board, false otherwise
     */
    private boolean isOnBoard(Coordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return List of cells that can be moved
     */
    /**
     * Returns a list of all possible cells that the current player can move to in the current turn.
     * If the current player is a musketeer, returns all musketeer cells that have possible destinations.
     * If the current player is a guard, returns all guard cells that have possible destinations.
     *
     * @return a list of all possible cells that the current player can move to in the current turn
     */
    public List<Cell> getPossibleCells() {
        List<Cell> allCellsThisTurn;
        if (getTurn() == Piece.Type.MUSKETEER) {
            allCellsThisTurn = getMusketeerCells();
        } else {
            allCellsThisTurn = getGuardCells();
        }

        List<Cell> possibleCells = new ArrayList<>();
        for (Cell cell : allCellsThisTurn) {
            if (!getPossibleDestinations(cell).isEmpty()) {
                possibleCells.add(cell);
            }
        }

        return possibleCells;
    }

    /**
     * Returns a list of possible destinations for a given cell on the board.
     * A destination is considered valid if it is adjacent to the given cell and the move is valid.
     * 
     * @param fromCell the cell for which to find possible destinations
     * @return a list of adjacent cells that are valid destinations for the given cell
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) {
        List<Cell> destinations = new ArrayList<Cell>();
        int row = fromCell.getCoordinate().row;
        int col = fromCell.getCoordinate().col;

        if (row > 0 && isValidMove(new Move(fromCell, board[row-1][col]))) {
            destinations.add(board[row-1][col]);
        }
        if (row < 4 && isValidMove(new Move(fromCell, board[row+1][col]))) {
            destinations.add(board[row+1][col]);
        }
        if (col > 0 && isValidMove(new Move(fromCell, board[row][col-1]))) {
            destinations.add(board[row][col-1]);
        }
        if (col < 4 && isValidMove(new Move(fromCell, board[row][col+1]))) {
            destinations.add(board[row][col+1]);
        }

        return destinations;
    }

    /**
     * Returns a list of all possible moves that can be made on the board.
     * A move is represented by a pair of cells: the cell from which a piece is moved,
     * and the cell to which the piece is moved.
     *
     * @return a list of all possible moves on the board
     */
    public List<Move> getPossibleMoves() {
        List<Move> movesList = new ArrayList<>();
        List<Cell> possibleCells = this.getPossibleCells();
        for (Cell possibleFromCell: possibleCells) {
            List<Cell> possibleDestinations = this.getPossibleDestinations(possibleFromCell);
            for (Cell toCell : possibleDestinations) {
                Move move = new Move(possibleFromCell, toCell);
                movesList.add(move);
            }
        }
        return movesList;
    }


    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        List<Cell> musketeerCells = getMusketeerCells();

        // Check if all musketeers are in the same row or column
        int row = musketeerCells.get(0).getCoordinate().row;
        int col = musketeerCells.get(0).getCoordinate().col;
        boolean sameRow = true;
        boolean sameCol = true;
        for (Cell cell : musketeerCells) {
            if (cell.getCoordinate().row != row) {
                sameRow = false;
            }
            if (cell.getCoordinate().col != col) {
                sameCol = false;
            }
        }
        if (sameRow || sameCol) {
            winner = Piece.Type.GUARD;
            return true;
        }

        // Check if there are no possible moves for the musketeers
        if (getPossibleCells().isEmpty()) {
            winner = Piece.Type.MUSKETEER;
            return true;
        }

        // If neither condition is met, the game is not over
        return false;
    }

    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    /**
     * Loads a board file from a file path.
     * @param filePath The path to the board file to load (e.g. "Boards/Starter.txt")
     */
    private void loadBoard(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece: pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        scanner.close();
        System.out.printf("Loaded board from %s.\n", filePath);
    }
}
