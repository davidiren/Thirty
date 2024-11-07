package se.umu.dair0002.thirty.classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.Optional;

/**
 * Only used to keep track of score gotten
 * when used 'x' rule
 */
public enum PointRules {
    low(3),
    four(4),
    five(5),
    six(6),
    seven(7),
    eight(8),
    nine(9),
    ten(10),
    eleven(11),
    twelve(12);

    private final int rule;

    PointRules(int rule){
        this.rule = rule;
    }

    public int getRule() {
        return rule;
    }

    /**
     * to be able to use an int (3-12) as a way to make the enum
     * @param i - rule in int form
     * @return - PointRules
     */
    public static PointRules intToPointRules(int i){
        switch (i){
            case 3:
                return PointRules.low;
            case 4:
                return PointRules.four;
            case 5:
                return PointRules.five;
            case 6:
                return PointRules.six;
            case 7:
                return PointRules.seven;
            case 8:
                return PointRules.eight;
            case 9:
                return PointRules.nine;
            case 10:
                return PointRules.ten;
            case 11:
                return PointRules.eleven;
            case 12:
                return PointRules.twelve;
            default:
                throw new IllegalArgumentException(i + " Is not a valid argument"); // should never get here
        }
    }
}
