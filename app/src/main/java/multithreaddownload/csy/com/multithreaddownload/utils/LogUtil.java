package multithreaddownload.csy.com.multithreaddownload.utils;

import android.util.Log;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class LogUtil {
    public static boolean isDbug = false;

    public static void d(String tag,String log){
        if(!isDbug){
            return;
        }
        Log.d(tag, log);
    }

    public static void e(String tag,String log){
        if(!isDbug){
            return;
        }
        Log.e(tag, log);
    }
}
