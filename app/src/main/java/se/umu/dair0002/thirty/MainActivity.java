package se.umu.dair0002.thirty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.umu.dair0002.thirty.classes.RuleSelectionListener;
import se.umu.dair0002.thirty.classes.ThirtyGame;
import se.umu.dair0002.thirty.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //private DiceRoller diceRoller = new DiceRoller(6);
    private ThirtyGame thirtyGame;
    private ArrayList<ImageView> dice = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapterForChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if(savedInstanceState!=null){
            //recover saved data
            thirtyGame = savedInstanceState.getParcelable("ThirtyGame");
        }else{
            thirtyGame = new ThirtyGame();
        }

        CharSequence round = "Round: "+thirtyGame.getRound();
        CharSequence noThrows = "Throws: "+thirtyGame.getThrowCount();
        dataBinding.Round.setText(round);//to keep track of round and throws through rotation of ui
        dataBinding.Throw.setText(noThrows);//to keep track of round and throws through rotation of ui

        //gather dice in array to easily loop and instantiate them
        dice.add(dataBinding.Dice1);
        dice.add(dataBinding.Dice2);
        dice.add(dataBinding.Dice3);
        dice.add(dataBinding.Dice4);
        dice.add(dataBinding.Dice5);
        dice.add(dataBinding.Dice6);
        int i = 0;
        for (ImageView iv:dice) {
            final int tempI = i;//need a final int for lamda func
            iv.setImageResource(thirtyGame.getDice(tempI).getImage());
            iv.setOnClickListener(x->{
                thirtyGame.selectDice(tempI);
                iv.setImageResource(thirtyGame.getDice(tempI).getImage());
            });
            i++;
        }

        dataBinding.buttonRoll.setOnClickListener(x -> {
            thirtyGame.Roll();
            int j = 0;
            for (ImageView iv:dice) {
                iv.setImageResource(thirtyGame.getDice(j).getImage());//update dice pics
                j++;
            }
            if(thirtyGame.getThrowCount()==3) dataBinding.buttonRoll.setEnabled(false); // cant roll more than thrice
            CharSequence t = "Throws: "+thirtyGame.getThrowCount();
            dataBinding.Throw.setText(t);//update "current throw" text
        });

        dataBinding.buttonEndRound.setOnClickListener(x -> {
            dataBinding.buttonRoll.setEnabled(true);//enable rolling again
            int scoreThisRound = thirtyGame.EndRound();
            makeToast(scoreThisRound);
            if(thirtyGame.getRound() == 11){ //means we just ended round 10 i.e. game is done
                EndGame();
            }
            int k = 0;
            for (ImageView iv:dice) {
                iv.setImageResource(thirtyGame.getDice(k).getImage());//update dice pics
                k++;
            }
            changeRuleSelection(dataBinding.spinnerChoices);
            CharSequence t = "Throws: "+thirtyGame.getThrowCount();
            CharSequence r = "Round: "+thirtyGame.getRound();
            dataBinding.Throw.setText(t);//update "current throw" text
            dataBinding.Round.setText(r);//update "current round" text
        });
        //spinner for rule selection
        makeSpinner();
        //dataBinding.spinnerChoices.


    }

    /**
     * finds a rule that is not disabled and chooses it as selection
     * need to be done so that selection changes from a disabled one
     * between rounds
     * @param spinner - spinner with rules
     */
    private void changeRuleSelection(Spinner spinner) {
        int pos = 0;
        for (Integer i:thirtyGame.getKeepRuleAndScore()) {
            if(i == 0){//0 means rule hasn't been used
                //select this rule since it is enabled
                spinner.setSelection(pos);
                return;
            }
            pos++;
        }

    }

    private void makeSpinner() {
        Spinner choices =  findViewById(R.id.spinner_choices);
        /*this.adapterForChoices = ArrayAdapter.createFromResource(this,
                R.array.Thirty_choices, android.R.layout.simple_spinner_item);*/

        String[] rules = new String[]{
                "Low","4","5","6","7","8","9","10","11","12"
        };

        final List<String> values = new ArrayList<String>(Arrays.asList(rules));
        final ArrayAdapter<String> adapterForChoices = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, values){

            //handles so you cant select same rule more than once
            @Override
            public boolean isEnabled(int pos){
                return thirtyGame.getKeepRuleAndScore().get(pos).equals(0);
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(!thirtyGame.getKeepRuleAndScore().get(position).equals(0)) {
                    // Set the disabled item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapterForChoices.setDropDownViewResource(R.layout.spinner_item);
        choices.setAdapter(adapterForChoices);
        choices.setOnItemSelectedListener(new RuleSelectionListener(thirtyGame));
    }

    /**
     * Make a toast to show user points gained from a round
     * @param scoreToDisplay - int with points gained
     */
    private void makeToast(int scoreToDisplay) {
        Context context = getApplicationContext();
        CharSequence text = "You received: "+scoreToDisplay+ " points";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    /**
     * saves the data to be recovered
     * @param outState Bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ThirtyGame", thirtyGame);
    }

    /**
     * Ends the game and starts next activity that will show scores
     */
    private void EndGame(){

        Intent intent = new Intent(this, ScoreScreen.class);
        intent.putExtra("score", thirtyGame.getScore());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putIntegerArrayListExtra("rulesWithScore", thirtyGame.getKeepRuleAndScore());
        resetGame();
        startActivity(intent);
    }

    private void resetGame() {
        thirtyGame.reset();
    }

}