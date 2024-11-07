package se.umu.dair0002.thirty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import se.umu.dair0002.thirty.databinding.ActivityScoreScreenBinding;

public class ScoreScreen extends AppCompatActivity {
    private int score;
    private ArrayList<Integer> rulesWithScore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityScoreScreenBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_score_screen);
        if (savedInstanceState!=null){ //recover score
            score = savedInstanceState.getInt("score");
            rulesWithScore = savedInstanceState.getIntegerArrayList("rulesWithScore");
        }else { //get score from game
            Intent getScoreIntent = getIntent();
            score = getScoreIntent.getIntExtra("score", 0);
            rulesWithScore = getScoreIntent.getIntegerArrayListExtra("rulesWithScore");
        }
        showRuleScores(dataBinding);
        dataBinding.totalScore.setText(String.valueOf(score));
        dataBinding.playAgain.setOnClickListener(x -> {
            //Intent intent = new Intent(this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();//handle so that backstack doesnt get "spammed" while using "play again"
            //startActivity(intent);
        });
    }

    /**
     * save data
     * @param outState Bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("score", score);
        outState.putIntegerArrayList("rulesWithScore", rulesWithScore);
    }

    /**
     * place correct score on a textview to display
     * @param db ActivityScoreScreenBinding to access textviews
     */
    private void showRuleScores(ActivityScoreScreenBinding db){
        db.textViewLow4.setText(String.valueOf(rulesWithScore.get(0).intValue()));
        db.textViewFour4.setText(String.valueOf(rulesWithScore.get(1).intValue()));
        db.textViewFive4.setText(String.valueOf(rulesWithScore.get(2).intValue()));
        db.textViewSix4.setText(String.valueOf(rulesWithScore.get(3).intValue()));
        db.textViewSeven4.setText(String.valueOf(rulesWithScore.get(4).intValue()));
        db.textViewEight4.setText(String.valueOf(rulesWithScore.get(5).intValue()));
        db.textViewNine4.setText(String.valueOf(rulesWithScore.get(6).intValue()));
        db.textViewTen4.setText(String.valueOf(rulesWithScore.get(7).intValue()));
        db.textViewEleven4.setText(String.valueOf(rulesWithScore.get(8).intValue()));
        db.textViewTwelve4.setText(String.valueOf(rulesWithScore.get(9).intValue()));
    }
}