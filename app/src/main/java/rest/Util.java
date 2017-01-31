package rest;

import android.os.Build;

/**
 * Created by abhinav on 28-07-2016.
 */
public class Util {
    public static boolean isLollipopOrGreater() {
        return Build.VERSION.SDK_INT >= 21 ? true : false;
    }
    public static boolean isJellyBeanOrGreater(){
        return Build.VERSION.SDK_INT>=16?true:false;
    }
}
