package se.umu.dair0002.thirty.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class ThirtyGame implements Parcelable {

    //Rules the game is built on
    private static final int DICE_NUM = 6;
    private static final int ROUND_NUM = 10;
    private static final int THROW_NUM = 3;

    private ArrayList<Dice> diceList;            // all dice
    private ArrayList<Dice> selectedDice;        // keep track of which dice are saved
    private ArrayList<Integer> keepRuleAndScore; // to keep track of score gained from which rule
    private int throwCount;
    private int round;
    private int score;
    private String selectedRule;

    /**
     * Constructor, initializes the dice and sets all variables to "start"-values
     */
    public ThirtyGame(){
        diceList = new ArrayList<>();
        selectedDice = new ArrayList<>();
        keepRuleAndScore = new ArrayList<Integer>(){
            {
                for (int i = 0; i < 10; i++){
                    add(0);//this will give 10 instances for points, since there are 10 rules
                }
            }
        };
        throwCount = 0;
        round = 1;
        score = 0;
        CreateDice();
    }

    /**
     * Create 6 dice
     */
    private void CreateDice() {
        for(int i = 0; i < DICE_NUM; i++){
            diceList.add(new Dice());
        }
    }

    /**
     * Checks all the rolling rules, rounds, and throws
     */
    public void Roll(){
        if(round > ROUND_NUM) return; // cant roll on round 11

        if(throwCount < THROW_NUM){
            RollDice();
            throwCount++;
            Log.d(TAG, "Roll: "+throwCount);
        }
    }

    /**
     * ends a round
     */
    public int EndRound() {
        int i = CountScore();
        round++;
        throwCount = 0;
        selectedDice.clear();
        for (Dice d:diceList) {
            if(d.isSaved()) { //reset saved dice
                d.saveDice();
            }
        }
        Log.d(TAG, "Roll: "+throwCount); //debug things
        Log.d(TAG, "Roll: score = "+score);
        return i;
    }

    /**
     * Rolls dice that are not saved
     */
    private void RollDice() {
        for (Dice d:diceList) {
            if(!d.isSaved()){
                d.RollDice();
            }
        }
    }

    /**
     * Toggles a dice as saved or not, saved dice is not rolled
     * @param dicePos dice position in the list
     */
    public void selectDice(int dicePos){
        if(throwCount != 0){ //Cannot save a dice before throwing
            diceList.get(dicePos).saveDice();
            manageChosenDice(dicePos);
        }
    }

    /**
     * saves the selected dice to a seperate list
     * if dice already saved --> means we need to remove it
     * @param pos position in diceList array to find the dice chosen
     */
    private void manageChosenDice(int pos) {
        if(diceList.get(pos).isSaved()){
            selectedDice.add(diceList.get(pos));
        }else{
            selectedDice.remove(diceList.get(pos));
        }
    }

    /**
     * Scoring, the user chooses rule and selectedDice to summarize
     * non "low" rules, needs user to save dice in correct "pair order"
     */
    public int CountScore(){
        int count = 0;
        if(selectedRule.equals("Low")){
                for (Dice d: selectedDice) {
                    if(d.value <= 3 /*&& d.isSaved()*/){
                        count += d.value;
                    }
                }
        }else{//make sure user paired correct dice, so that they can't cheat
            int checkRule = PointRules.intToPointRules(Integer.parseInt(selectedRule)).getRule();
            int currentDicePairValue = 0;
            for (Dice d: selectedDice) {
                currentDicePairValue += d.value;
                if(currentDicePairValue == checkRule){
                    count += currentDicePairValue;
                    currentDicePairValue = 0; //reset, check for new pair
                }
            }
        }
        saveRuleAndScore(count);
        score += count;
        return count;
    }

    /**
     * adds score to remember the score gained using certain rules
     * @param score - score to add to certain rule
     */
    private void saveRuleAndScore(int score){
        switch (selectedRule){
            case "Low":
                keepRuleAndScore.set(0, keepRuleAndScore.get(0)+score);
                break;
            case "4":
                keepRuleAndScore.set(1, keepRuleAndScore.get(1)+score);
                break;
            case "5":
                keepRuleAndScore.set(2, keepRuleAndScore.get(2)+score);
                break;
            case "6":
                keepRuleAndScore.set(3, keepRuleAndScore.get(3)+score);
                break;
            case "7":
                keepRuleAndScore.set(4, keepRuleAndScore.get(4)+score);
                break;
            case "8":
                keepRuleAndScore.set(5, keepRuleAndScore.get(5)+score);
                break;
            case "9":
                keepRuleAndScore.set(6, keepRuleAndScore.get(6)+score);
                break;
            case "10":
                keepRuleAndScore.set(7, keepRuleAndScore.get(7)+score);
                break;
            case "11":
                keepRuleAndScore.set(8, keepRuleAndScore.get(8)+score);
                break;
            case "12":
                keepRuleAndScore.set(9, keepRuleAndScore.get(9)+score);
                break;
            default: //should never reach this
                throw new IllegalArgumentException("Something went wrong in saveRuleAndScore");


        }
    }


    //Methods for parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    public ThirtyGame (Parcel in){
        in.readTypedList(diceList, Dice.CREATOR);
        in.readTypedList(selectedDice, Dice.CREATOR);
        //keepRuleAndScore = (ArrayList<Integer>) in.readSerializable();
        in.readList(keepRuleAndScore, Integer.class.getClassLoader());
        throwCount = in.readInt();
        round = in.readInt();
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(diceList);
        dest.writeTypedList(selectedDice);
        dest.writeList(keepRuleAndScore);
        dest.writeInt(throwCount);
        dest.writeInt(round);
        dest.writeInt(score);
    }
    public static final Parcelable.Creator<ThirtyGame> CREATOR = new Parcelable.Creator<ThirtyGame>() {
        public ThirtyGame createFromParcel(Parcel in){
            return new ThirtyGame(in);
        }

        public ThirtyGame[] newArray(int size){
            return new ThirtyGame[size];
        }
    };

    //Some Getters and Setters
    public Dice getDice(int dicePos){
        return diceList.get(dicePos);
    }

    public ArrayList<Integer> getKeepRuleAndScore(){
        return keepRuleAndScore;
    }

    public void setSelectedRule(String selectedRule) {
        this.selectedRule = selectedRule;
    }
    public int getRound() {
        return round;
    }
    public int getScore() {
        return score;
    }
    public int getThrowCount() {
        return throwCount;
    }


    public void reset() {
        keepRuleAndScore = new ArrayList<Integer>(){
            {
                for (int i = 0; i < 10; i++){
                    add(0);
                }
            }
        };
        throwCount = 0;
        round = 1;
        score = 0;
    }
}
