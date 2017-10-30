package multithreaddownload.csy.com.multithreaddownload;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by chenshouyin on 2017/10/25.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public abstract class DataWhatcher implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadEnty){
            notifyDataChange(data);
        }
    }

    public abstract void notifyDataChange(Object data);
}