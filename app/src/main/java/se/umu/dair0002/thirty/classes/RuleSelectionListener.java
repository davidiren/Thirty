package se.umu.dair0002.thirty.classes;

import android.view.View;
import android.widget.AdapterView;

public class RuleSelectionListener implements AdapterView.OnItemSelectedListener {
    ThirtyGame thirtyGame;

    /**
     * Constructor, needs an instance of the game to send which rule has been selected
     * @param tg - ThirtyGame
     */
    public RuleSelectionListener(ThirtyGame tg){
        thirtyGame = tg;
    }


    //Onclick Override, sets the selectedRule variable in the game instance
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedRule = parent.getItemAtPosition(position).toString();
        thirtyGame.setSelectedRule(selectedRule);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
