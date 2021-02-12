package ir.amirsobhan.earthquake.UI;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.amirsobhan.earthquake.R;

public class Font extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Setup persian font
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Vazir.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }
}
