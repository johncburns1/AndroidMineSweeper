package hu.ait.android.minesweeper;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.shimmer.ShimmerFrameLayout;

import hu.ait.android.minesweeper.data.MineSweeperModel;
import hu.ait.android.minesweeper.view.MineSweeperView;

public class MainActivity extends AppCompatActivity {


    private TextView win_loss_status;
    private LinearLayout layoutContent;
    private ToggleButton mode_toggle;
    private MineSweeperView mineSweeperView;
    private Button btnClear;
    private ShimmerFrameLayout shimmerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mineSweeperView = (MineSweeperView) findViewById(R.id.msview);          // mine sweeper view
        win_loss_status = (TextView) findViewById(R.id.win_loss_status);        // win/loss status
        layoutContent = findViewById(R.id.layoutcontent);                       // layout content

        //shimmer frame
        shimmerView = findViewById(R.id.shimmer_frame);
        shimmerView.startShimmerAnimation();

        // mode toggle/clear button(s)
        mode_toggle = (ToggleButton) findViewById(R.id.flag_mode_toggle);
        btnClear = (Button) findViewById(R.id.clear_btn);

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mineSweeperView.clearBoard();
            }
        });

        MineSweeperModel.getInstace().printBoard();
    }

    //win/loss message
    public void showWinLossSnackbar(String win_loss) {

        //win
        if (win_loss.equals(MineSweeperView.WIN)) {
            Snackbar.make(layoutContent, R.string.win_status, Snackbar.LENGTH_LONG).setAction(

                    R.string.OK_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, R.string.endgame_message, Toast.LENGTH_SHORT).show();
                        }
                    }
            ).show();
        }

        //loss
        else {
            Snackbar.make(layoutContent, R.string.lose_status, Snackbar.LENGTH_LONG).setAction(

                    R.string.OK_btn, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, R.string.endgame_message, Toast.LENGTH_SHORT).show();
                        }
                    }
            ).show();
        }
    }

    public void lostGame() {
        win_loss_status.setText(R.string.lose_status);
    }

    public void wonGame() {
        win_loss_status.setText(R.string.win_status);
    }

    public void clearWinLossMessage() {
        win_loss_status.setText("");
    }

    public boolean getToggleState() {
        return mode_toggle.isChecked();
    }

    public void resetToggle() {
        if (mode_toggle.isChecked()) {
            mode_toggle.toggle();
        }
    }
}
