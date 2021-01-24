package ir.amirsobhan.earthquake.UI;

import android.app.Application;

import ir.amirsobhan.earthquake.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Font extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Setup persian font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
