package se.umu.dair0002.thirty.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.material.internal.ParcelableSparseArray;

import se.umu.dair0002.thirty.R;

public class Dice implements Parcelable {
    public int value;
    private boolean saved = false;


    /**
     * Constructor, will roll dice once to have an initial value
     */
    public Dice (){
        RollDice();
    }


    /**
     * Rolls the die and sets the rolled value
     * @return - rolled value
     */
    public int RollDice(){
        value = (int)(Math.random()*6+1);
        return value;
    }

    /**
     * toggle save state
     */
    public void saveDice(){
        saved = !saved;
    }

    /**
     * to get the correct dice image
     * @return image id
     */
    public int getImage(){
        switch (value){
            case 1:
                return saved ? R.drawable.grey1 : R.drawable.white1;
            case 2:
                return saved ? R.drawable.grey2 : R.drawable.white2;
            case 3:
                return saved ? R.drawable.grey3 : R.drawable.white3;
            case 4:
                return saved ? R.drawable.grey4 : R.drawable.white4;
            case 5:
                return saved ? R.drawable.grey5 : R.drawable.white5;
            case 6:
                return saved ? R.drawable.grey6 : R.drawable.white6;
            default:
                return 0;
        }
    }

    /**
     * checks if dice is saved
     * @return boolean
     */
    public boolean isSaved(){
        return saved;
    }

    //Parcelable functions

    @Override
    public int describeContents() {
        return 0;
    }

    public Dice (Parcel in){
        value = in.readInt();
        saved = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
        dest.writeInt(saved? 1:0);
    }

    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {
        public Dice createFromParcel(Parcel in){
            return new Dice(in);
        }

        public Dice[] newArray(int size){
            return new Dice[size];
        }
    };
}
