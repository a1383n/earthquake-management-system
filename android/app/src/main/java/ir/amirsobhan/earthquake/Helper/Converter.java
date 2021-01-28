package ir.amirsobhan.earthquake.Helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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

    public static float magToBitmap(double mag){
        if (mag >= 4.0) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (mag <= 3.9 && mag > 2.9) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }

    public static long toTimestampLong(int time){
        return Long.parseLong(time +"000");
    }

    /**
     * A one color image.
     * @param width
     * @param height
     * @param color
     * @return A one color image with the given width and height.
     */
    public static Bitmap colorToBitmap(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }
}
