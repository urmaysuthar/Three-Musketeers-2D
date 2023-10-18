package assignment1;

import java.util.List;

public class BoardEvaluatorImpl implements BoardEvaluator {

/**
 * Calculates a score for the given Board
 * A higher score means the Musketeer is winning
 * A lower score means the Guard is winning
 * Add heuristics to generate a score given a Board
 * @param board Board to calculate the score of
 * @return int Score of the given Board
 */
    @Override
    public double evaluateBoard(Board board) {
        double score = 0;
        score += getNumMusketeersPossibleMovesScore(board);
        score += getGuardDistanceFromMusketeers(board);
        return score;
    }

    /**
     * Calculates the number of possible moves for the Musketeers on the board
     * @param board Board to calculate the score of
     * @return double Score of the number of possible moves for the Musketeers on the board
     */
    private double getNumMusketeersPossibleMovesScore(Board board) {
        List<Cell> musketeerCells = board.getMusketeerCells();
        int numMusketeersCanMove = 0;
        for (Cell musketeerCell: musketeerCells) {
            if (board.getPossibleDestinations(musketeerCell).size() > 0) {
                numMusketeersCanMove += 1;
            }
        }
        return numMusketeersCanMove * -3;
    }

    /**
     * Calculates the distance between the Musketeers and the Guards on the board
     * @param board Board to calculate the score of
     * @return double Score of the distance between the Musketeers and the Guards on the board
     */
    private double getGuardDistanceFromMusketeers(Board board) {
        int score = 0;
        for (Cell musketeerCell: board.getMusketeerCells()) {
            int musketeerRow = musketeerCell.getCoordinate().row;
            int musketeerCol = musketeerCell.getCoordinate().col;
            for (Cell guardCell : board.getGuardCells()) {
                int guardRow = guardCell.getCoordinate().row;
                int guardCol = guardCell.getCoordinate().col;

                score += Math.abs(musketeerRow - guardRow);
                score += Math.abs(musketeerCol - guardCol);
            }
        }
        return score;
    }
    
}
