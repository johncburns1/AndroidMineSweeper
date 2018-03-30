package hu.ait.android.minesweeper.data;

import java.util.Random;

/**
 * Created by johnc on 9/29/2017.
 */

public class MineSweeperModel {

    private static MineSweeperModel mineSweeperModel = null;

    private MineSweeperModel() {
    }

    public static MineSweeperModel getInstace() {

        if (mineSweeperModel == null) {
            mineSweeperModel = new MineSweeperModel();
        }

        return mineSweeperModel;
    }

    public static final int EMPTY = 0;
    public static final int BOMB = 1;
    public static final int FLAGGED_BOMB = 2;
    public static final int DETONATED_BOMB = 3;
    int boardSize = 5;
    private Random rand = new Random();

    private int[][] model = {

            /**
             * This configuration is used for testing
            */
            {1, 0, 1, 0, 1},
            {0, 0, 1, 0, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1}

            /**
             * This configuration is used for the actual game and the board reset
            {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
            {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
            {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
            {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
            {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}
             **/
    };


    public int getFieldContent(int x, int y) {
        return model[x][y];
    }

    public void setFieldContent(int x, int y, int content) {
        model[x][y] = content;
    }

    public int getNeighbors(int x, int y) {

        int num_neighbors = 0;

        // right
        if (y + 1 < boardSize && model[x][y + 1] == BOMB) {
            num_neighbors += 1;
        }
        // left
        if (y - 1 >= 0 && model[x][y - 1] == BOMB) {
            num_neighbors += 1;
        }
        // down
        if (x + 1 < boardSize && model[x + 1][y] == BOMB) {
            num_neighbors += 1;
        }
        // up
        if (x - 1 >= 0 && model[x - 1][y] == BOMB) {
            num_neighbors += 1;
        }
        // down-right
        if (x + 1 < boardSize && y + 1 < boardSize && model[x + 1][y + 1] == BOMB) {
            num_neighbors += 1;
        }
        // down-left
        if (x + 1 < boardSize && y - 1 >= 0 && model[x + 1][y - 1] == BOMB) {
            num_neighbors += 1;
        }
        // up-right
        if (x - 1 >= 0 && y + 1 < boardSize && model[x - 1][y + 1] == BOMB) {
            num_neighbors += 1;
        }
        // up-left
        if (x - 1 >= 0 && y - 1 >= 0 && model[x - 1][y - 1] == BOMB) {
            num_neighbors += 1;
        }

        return num_neighbors;
    }

    //resets and randomizes the game board
    public void resetGame() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                model[i][j] = rand.nextInt(2);
            }
        }
    }

    //check for a win state
    public boolean checkForWin() {
        boolean won = true;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (model[i][j] == BOMB) {
                    won = false;
                }
            }
        }
        return won;
    }

    //print the board
    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(model[i][j] + ", ");
            }
            System.out.println();
        }
    }
}
