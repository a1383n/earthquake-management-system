package ir.amirsobhan.earthquake.Helper;

import android.graphics.Color;

public class Converter {
    public static String toFaNum(String faNumbers) {
        String[][] mChars = new String[][]{
                {"0", "۰"},
                {"1", "۱"},
                {"2", "۲"},
                {"3", "۳"},
                {"4", "۴"},
                {"5", "۵"},
                {"6", "۶"},
                {"7", "۷"},
                {"8", "۸"},
                {"9", "۹"}
        };
        for (String[] num : mChars) {
            faNumbers = faNumbers.replace(num[0], num[1]);
        }
        return faNumbers;
    }

    public static int magToColor(double mag) {
        if (mag >= 4.0) {
            return Color.parseColor("#FF0000");
        } else if (mag <= 3.9 && mag > 2.9) {
            return Color.parseColor("#FFA500");
        } else {
            return Color.parseColor("#008000");
        }
    }

}
