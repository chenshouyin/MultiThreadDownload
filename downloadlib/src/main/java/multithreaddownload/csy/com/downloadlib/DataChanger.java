package multithreaddownload.csy.com.downloadlib;

/**
 * Created by chenshouyin on 2017/10/25.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

import java.util.Observable;

/**
 * Created by chenshouyin on 2017/10/25.
 *
 * 观察者继承自Observable 里面已经默认有以下方法
 * addObserver
 * deleteObserver
 * notifyObservers()
 * notifyObservers(Object arg)
 * deleteObservers()
 * setChanged()
 * clearChanged()
 * hasChanged()
 * countObservers()
 */

public class DataChanger extends Observable{

    /**
     * 对外提供一个单列引用,用于注册和取消注册监听
     */
    private static DataChanger mDataChanger;

    public static synchronized DataChanger getInstance(){
        if (null == mDataChanger){
            mDataChanger = new DataChanger();
        }
        return mDataChanger;
    }

    public void notifyDataChange(DownloadEnty mDownloadEnty){
        //TODO 关联查看Java源码
        //Marks this <tt>Observable</tt> object as having been changed
        setChanged();
        //通知观察者 改变的内容 也可不传递具体内容 notifyObservers()
        notifyObservers(mDownloadEnty);
    }
}