package com.mkemp.mvvmtictactoe.view;

import android.os.Bundle;

import com.mkemp.mvvmtictactoe.R;
import com.mkemp.mvvmtictactoe.databinding.ActivityGameBinding;
import com.mkemp.mvvmtictactoe.models.Player;
import com.mkemp.mvvmtictactoe.viewmodel.GameViewModel;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import static com.mkemp.mvvmtictactoe.utilities.StringUtility.isNullOrEmpty;

public class GameActivity extends AppCompatActivity
{
    private static final String GAME_BEGIN_DIALOG_TAG = "game_dialog_tag";
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag";
    private static final String NO_WINNER = "No one";
    private GameViewModel gameViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        promptForPlayers();
    }
    
    public void promptForPlayers() {
        GameBeginDialog dialog = GameBeginDialog.newInstance(this);
        dialog.show(getSupportFragmentManager(), GAME_BEGIN_DIALOG_TAG);
    }
    
    public void onPlayersSet(String player1, String player2) {
        initDataBinding(player1, player2);
    }
    
    private void initDataBinding(String player1, String player2) {
        ActivityGameBinding activityGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        gameViewModel.init(player1, player2);
        activityGameBinding.setGameViewModel(gameViewModel);
        setUpOnGameEndListener();
    }
    
    private void setUpOnGameEndListener() {
        gameViewModel.getWinner().observe(this, this::onGameWinnerChanged);
    }
    
    @VisibleForTesting
    public void onGameWinnerChanged(Player winner) {
        String winnerName = winner == null || isNullOrEmpty(winner.name) ? NO_WINNER : winner.name;
        GameEndDialog dialog = GameEndDialog.newInstance(this, winnerName);
        dialog.show(getSupportFragmentManager(), GAME_END_DIALOG_TAG);
    }
}
