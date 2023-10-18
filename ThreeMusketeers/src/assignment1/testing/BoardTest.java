package assignment1.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.*;

import assignment1.Board;
import assignment1.Cell;
import assignment1.Coordinate;
import assignment1.Guard;
import assignment1.Move;
import assignment1.Musketeer;
import assignment1.Piece;

public class BoardTest {

    private Board board;
    private Coordinate from;

    @Before
    public void setup() {
        this.board = new Board("Starter.txt");
    }

    @Test
    public void testGetCellAtStarter() {
        for (int i = 0 ; i < 5 ; i++){
            for ( int j = 0; j < 5 ; j ++){
                Cell cell = board.getCell(new Coordinate(i, j));
                Assert.assertNotNull(cell);
                if (i + j == 4 && i % 2 == 0){
                    assertEquals(Piece.Type.MUSKETEER, cell.getPiece().getType());
                }
                else{
                    assertEquals(Piece.Type.GUARD, cell.getPiece().getType());
                }
            }
        }
    }

    @Test
    public void testGetMusketeerCellsAtStarter() {
        // Test if getMusketeerCells returns all musketeer cells on the board
        List<Cell> cells = (board.getMusketeerCells());
        assertEquals(3,cells.size());
        for (Cell cell : cells) {
            assertEquals(Piece.Type.MUSKETEER, cell.getPiece().getType());
        }
    }

    @Test
    public void testGetGuardCells() {
        // Test if getGuardCells returns all guard cells on the board
        List<Cell> cells = (board.getGuardCells());
        assertEquals(22,cells.size());
        for (Cell cell : cells) {
            assertEquals(Piece.Type.GUARD, cell.getPiece().getType());
        }
    }
    @Test
    public void testMoveValidMusketeerMove() {
        // Test if a valid musketeer move is executed correctly
        Cell fromCoordinate = board.getCell(new Coordinate(0, 4)); // Example starting coordinate
        Cell toCoordinate = board.getCell(new Coordinate(1, 4));  // Example destination coordinate
        Move move = new Move(fromCoordinate, toCoordinate);
        board.move(move);
        assertNull(board.getCell(new Coordinate(0, 4)).getPiece()); // Check that the fromCell is now empty
        assertEquals(Piece.Type.MUSKETEER, board.getCell(new Coordinate(1, 4)).getPiece().getType()); // Check that the toCell has a musketeer
        assertEquals(21, board.getGuardCells().size());
        assertEquals(3, board.getMusketeerCells().size());
    }

    @Test
    public void testMoveValidGuardMove() {
        // Test if a valid guard move is executed correctly
        Cell fromCoordinate = board.getCell(new Coordinate(0, 4)); // Example starting coordinate
        Cell toCoordinate = board.getCell(new Coordinate(1, 4));  // Example destination coordinate
        Move move = new Move(fromCoordinate, toCoordinate);
        board.move(move);
        assertNull(board.getCell(new Coordinate(0, 4)).getPiece()); // Check that the fromCell is now empty
        fromCoordinate = board.getCell(new Coordinate(0, 3)); // Example starting coordinate
        toCoordinate = board.getCell(new Coordinate(0, 4));  // Example destination coordinate
        move = new Move(fromCoordinate, toCoordinate);
        board.move(move);
        assertNull(board.getCell(new Coordinate(0, 3)).getPiece()); // Check that the fromCell is now empty
        assertEquals(Piece.Type.MUSKETEER, board.getCell(new Coordinate(1, 4)).getPiece().getType()); // Check that the toCell has a musketeer
        assertEquals(Piece.Type.GUARD, board.getCell(new Coordinate(0, 4)).getPiece().getType());
        assertEquals(21, board.getGuardCells().size());
        assertEquals(3, board.getMusketeerCells().size());
    }

    @Test
    public void testGetPossibleCellsAtStart() {

        List<Cell> possibleCells = board.getPossibleCells();
        assertEquals(3, possibleCells.size());
        assertTrue(possibleCells.containsAll(board.getMusketeerCells()));
    }

    @Test
    public void testPieceCountGameOver() {
        this.board = new Board("Boards/GameOver.txt");
        Assert.assertEquals(3, board.getMusketeerCells().size());
        Assert.assertEquals(4, board.getGuardCells().size());
    }

    @Test
    public void testPieceCountNearEnd() {
        this.board = new Board("Boards/NearEnd.txt");
        Assert.assertEquals(3, board.getMusketeerCells().size());
        Assert.assertEquals(5, board.getGuardCells().size());
    }

    // Tests for moving pieces
    @Test
    public void testMovePiece() {
        Cell cell = board.getCell(new Coordinate(1, 4));
        Assert.assertNotNull(cell.getPiece());

        Coordinate from = new Coordinate(1, 4);
        Coordinate to = new Coordinate(2, 4);

        Cell fromCell = board.getCell(from);
        Cell toCell = board.getCell(to);

        Move move = new Move(fromCell, toCell);
        board.move(move);

        Assert.assertNull(cell.getPiece());
        Assert.assertNotNull(board.getCell(new Coordinate(2, 4)).getPiece());
    }


    @Test
    public void testPossibleMoves() {
        this.board = new Board();
        List<Move> moves = board.getPossibleMoves();
        Assert.assertFalse(moves.isEmpty());
    }

    @Test
    public void testAllGuardPieces() {
        this.board = new Board();

        Coordinate[] guardCoords = {
                new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3),
                new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(1, 2), new Coordinate(1, 3), new Coordinate(1, 4),
                new Coordinate(2, 0), new Coordinate(2, 1),                  new Coordinate(2, 3), new Coordinate(2, 4),
                new Coordinate(3, 0), new Coordinate(3, 1), new Coordinate(3, 2), new Coordinate(3, 3), new Coordinate(3, 4),
                new Coordinate(4, 1), new Coordinate(4, 2), new Coordinate(4, 3), new Coordinate(4, 4)
        };

        for (Coordinate coord : guardCoords) {
            Piece piece = board.getCell(coord).getPiece();
            Assert.assertTrue("Expected GUARD at " + coord + " but found " + piece.getType(), Piece.Type.GUARD == piece.getType());
        }
    }

    @Test
    public void testAllMusketeerPieces() {
        this.board = new Board();

        Coordinate[] musketeerCoords = {
                new Coordinate(0, 4),
                new Coordinate(2, 2),
                new Coordinate(4, 0)
        };

        for (Coordinate coord : musketeerCoords) {
            Piece piece = board.getCell(coord).getPiece();
            Assert.assertTrue("Expected MUSKETEER at " + coord + " but found " + piece.getType(), Piece.Type.MUSKETEER == piece.getType());
        }
    }

    @Test
    public void canMusketeerMove(){
        Musketeer piece = new Musketeer();
        Cell cell = new Cell(new Coordinate(0,0));
        cell.setPiece(new Guard());
        assertEquals(true, piece.canMoveOnto(cell));
        cell.setPiece(new Musketeer());
        assertEquals(false, piece.canMoveOnto(cell));
        cell.setPiece(null);
        assertEquals(false,piece.canMoveOnto(cell));
    }

    @Test
    public void canGuardMove(){
        Guard piece = new Guard();
        Cell cell = new Cell(new Coordinate(0,0));
        cell.setPiece(new Guard());
        assertEquals(false, piece.canMoveOnto(cell));
        cell.setPiece(new Musketeer());
        assertEquals(false, piece.canMoveOnto(cell));
        cell.setPiece(null);
        assertEquals(true,piece.canMoveOnto(cell));
    }

    @Test
    public void boardTest(){
        Board board = new Board();
        assertEquals(22, board.getGuardCells().size());
        assertEquals(3, board.getMusketeerCells().size());
        board = new Board("Boards/NearEnd.txt");
        assertEquals(board.getTurn(), Piece.Type.MUSKETEER);
        assertEquals(board.getWinner(), null);
        assertEquals(5, board.getGuardCells().size());
        assertEquals(3, board.getMusketeerCells().size());
        board = new Board("Boards/GameOver.txt");
        assertEquals(board.getTurn(), Piece.Type.GUARD);
        assertEquals(4, board.getGuardCells().size());
        assertEquals(3, board.getMusketeerCells().size());
    }

    @Test
    public void testUndoMoveWithOneMove() {
        Move move = new Move(board.getCell(new Coordinate(2, 2)), board.getCell(new Coordinate(2, 1)));
        Move copy = new Move( move);
        board.move(move);
        board.undoMove(copy);

        // Assert that the board state is back to the initial state
        assertEquals(Piece.Type.MUSKETEER, board.getCell(new Coordinate(2, 2)).getPiece().getType());
        assertEquals(Piece.Type.GUARD, board.getCell(new Coordinate(2, 1)).getPiece().getType());

        // Assert that the turn is correct
        assertEquals(Piece.Type.MUSKETEER, board.getTurn());
    }

    @Test
    public void testUndoMoveWithMultipleMoves() {
        Board board = new Board();
        Move move1 = new Move(board.getCell(new Coordinate(2, 2)), board.getCell(new Coordinate(2, 1)));
        Move move2 = new Move(board.getCell(new Coordinate(2, 3)), board.getCell(new Coordinate(2, 2)));
        Move copy1 = new Move(move1);
        Move copy2 = new Move(move2);

        board.move(move1);
        board.move(move2);

        board.undoMove(copy2);
        board.undoMove(copy1);

        assertEquals(Piece.Type.MUSKETEER, board.getCell(new Coordinate(2, 2)).getPiece().getType());
        assertEquals(Piece.Type.GUARD, board.getCell(new Coordinate(2, 1)).getPiece().getType());
    }

    @Test
    public void testIsValidMove() {
        Board board = new Board("Starter.txt");

        // Test valid moves
        Move validMove1 = new Move(board.getCell(new Coordinate(0, 0)), board.getCell(new Coordinate(1, 1)));
        assertFalse(board.isValidMove(validMove1));

        Move validMove2 = new Move(board.getCell(new Coordinate(3, 3)), board.getCell(new Coordinate(2, 2)));
        assertFalse(board.isValidMove(validMove2));

        // Test invalid moves
        Move invalidMove1 = new Move(board.getCell(new Coordinate(0, 0)), board.getCell(new Coordinate(3, 3)));
        assertFalse(board.isValidMove(invalidMove1));

        Move invalidMove2 = new Move(board.getCell(new Coordinate(1, 1)), board.getCell(new Coordinate(2, 4)));
        assertFalse(board.isValidMove(invalidMove2));
    }
    

}

