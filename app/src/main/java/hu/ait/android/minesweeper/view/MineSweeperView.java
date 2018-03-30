package hu.ait.android.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hu.ait.android.minesweeper.MainActivity;
import hu.ait.android.minesweeper.R;
import hu.ait.android.minesweeper.data.MineSweeperModel;

/**
 * Created by johnc on 9/29/2017.
 */

public class MineSweeperView extends View {

    //scales
    private static final int FLAG_SCALE = 160;
    private static final int BOMB_SCALE = 195;
    private static final int UNTOUCHED_BLOCK_SCALE = 152;

    //offsets for positions
    private static final int NEIGHBOR_OFFSET = 10;
    private static final int FLAG_OFFSET = 5;
    private static final int BOMB_OFFSET = 8;
    private static final int DETONATED_BOMB_OFFSET = -15;
    private static final int NUMBERS_WIDTH_OFFSET = 40;
    private static final int NUMBERS_HEIGHT_OFFSET = 140;

    //win/loss text
    public static final String LOSE = "lose";
    public static final String WIN = "win";

    //bitmaps
    private Bitmap bitmapFlag;
    private Bitmap scaled_bitmapFlag;

    private Bitmap bitmapBomb;
    private Bitmap scaled_bitmapBomb;

    private Bitmap bitmapUntouchedBlock;
    private Bitmap scaled_bitmapUntouchedBlock;

    //paint objects
    Paint paintBg;
    Paint paintLine;
    Paint paintBorder;
    Paint paintNumbers;
    int boardSize = 5;

    public MineSweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //backround
        paintBg = new Paint();
        paintBg.setColor(Color.GRAY);
        paintBg.setStyle(Paint.Style.FILL);

        //lines
        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(5);
        paintLine.setStyle(Paint.Style.STROKE);

        //border
        paintBorder = new Paint();
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStrokeWidth(1);
        paintBorder.setStyle(Paint.Style.STROKE);

        //number of touching bombs
        paintNumbers = new Paint();
        paintNumbers.setColor(Color.RED);
        paintNumbers.setStrokeWidth(50);

        //flag
        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.txtrd_flag);
        scaled_bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, FLAG_SCALE, FLAG_SCALE, false);

        //bomb
        bitmapBomb = BitmapFactory.decodeResource(getResources(), R.drawable.red_bomb);
        scaled_bitmapBomb = Bitmap.createScaledBitmap(bitmapBomb, BOMB_SCALE, BOMB_SCALE, false);

        //untouched block
        bitmapUntouchedBlock = BitmapFactory.decodeResource(getResources(), R.drawable.untouched_block);
        scaled_bitmapUntouchedBlock = Bitmap.createScaledBitmap(bitmapUntouchedBlock, UNTOUCHED_BLOCK_SCALE, UNTOUCHED_BLOCK_SCALE, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);
        drawGameArea(canvas);
        drawPlayers(canvas);
    }

    private void drawGameArea(Canvas canvas) {

        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBorder);

        // vertical lines
        for (int i = 1; i < boardSize; i++) {
            canvas.drawLine(i * (getWidth() / boardSize), 0, i * (getWidth() / boardSize), getHeight(), paintLine);
        }

        // horizontal lines
        for (int i = 1; i < boardSize; i++) {
            canvas.drawLine(0, i * (getHeight() / boardSize), getWidth(), i * (getHeight() / boardSize), paintLine);
        }
    }

    private void drawPlayers(Canvas canvas) {

        //iterate over the board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                //when a bomb has been flagged
                if (MineSweeperModel.getInstace().getFieldContent(i, j) == MineSweeperModel.FLAGGED_BOMB) {
                    canvas.drawBitmap(scaled_bitmapFlag, i * (getHeight() / boardSize) + FLAG_OFFSET, j * (getWidth() / boardSize) + FLAG_OFFSET, null);

                    //untouched square
                } else if (MineSweeperModel.getInstace().getFieldContent(i, j) == MineSweeperModel.EMPTY ||
                        MineSweeperModel.getInstace().getFieldContent(i, j) == MineSweeperModel.BOMB) {
                    canvas.drawBitmap(scaled_bitmapUntouchedBlock, i * (getHeight() / boardSize) + BOMB_OFFSET, j * (getWidth() / boardSize) + BOMB_OFFSET, null);

                    //detonated bomb
                } else if (MineSweeperModel.getInstace().getFieldContent(i, j) == MineSweeperModel.DETONATED_BOMB) {
                    canvas.drawBitmap(scaled_bitmapBomb, i * (getHeight() / boardSize) + DETONATED_BOMB_OFFSET, j * (getWidth() / boardSize) + DETONATED_BOMB_OFFSET, null);

                    //no bomb
                } else if (MineSweeperModel.getInstace().getFieldContent(i, j) >= NEIGHBOR_OFFSET) {
                    int bomb_count = MineSweeperModel.getInstace().getFieldContent(i, j);
                    bomb_count -= NEIGHBOR_OFFSET;
                    canvas.drawText(Integer.toString(bomb_count), i * (getHeight() / boardSize) + NUMBERS_WIDTH_OFFSET, j * (getHeight() / boardSize) + NUMBERS_HEIGHT_OFFSET, paintNumbers);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int tx = ((int) event.getX()) / (getWidth() / boardSize);
        int ty = ((int) event.getY()) / (getHeight() / boardSize);

        //flag mode
        if (((MainActivity) getContext()).getToggleState()) {

            // flag a bomb
            if (MineSweeperModel.getInstace().getFieldContent(tx, ty) == MineSweeperModel.BOMB) {
                MineSweeperModel.getInstace().setFieldContent(tx, ty, MineSweeperModel.FLAGGED_BOMB);
                checkForWin();

                // flag something that is not a bomb
            } else if (MineSweeperModel.getInstace().getFieldContent(tx, ty) == MineSweeperModel.EMPTY) {
                showBombs();
            }
        }

        // try field mode
        else {

            // hit a bomb
            if (MineSweeperModel.getInstace().getFieldContent(tx, ty) == MineSweeperModel.BOMB) {
                showBombs();

                // do not hit a bomb
            } else if (MineSweeperModel.getInstace().getFieldContent(tx, ty) == MineSweeperModel.EMPTY) {
                int bomb_count = MineSweeperModel.getInstace().getNeighbors(tx, ty);
                MineSweeperModel.getInstace().setFieldContent(tx, ty, NEIGHBOR_OFFSET + bomb_count);
            }
        }


        invalidate();
        return super.onTouchEvent(event);
    }

    private void showBombs() {

        //show all of the bombs and tell the user that they lost
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (MineSweeperModel.getInstace().getFieldContent(i, j) == MineSweeperModel.BOMB) {
                    MineSweeperModel.getInstace().setFieldContent(i, j, MineSweeperModel.DETONATED_BOMB);
                    ((MainActivity) getContext()).lostGame();
                    ((MainActivity) getContext()).showWinLossSnackbar(LOSE);
                }
            }
        }
    }

    private void checkForWin() {
        if (MineSweeperModel.getInstace().checkForWin()) {
            ((MainActivity) getContext()).wonGame();
            ((MainActivity) getContext()).showWinLossSnackbar(WIN);
        }
    }

    public void clearBoard() {
        MineSweeperModel.getInstace().resetGame();
        ((MainActivity) getContext()).clearWinLossMessage();
        ((MainActivity) getContext()).resetToggle();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paintNumbers.setTextSize(getHeight() / boardSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }
}
