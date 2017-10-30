package multithreaddownload.csy.com.multithreaddownload;

import android.content.Context;
import android.content.Intent;

import multithreaddownload.csy.com.multithreaddownload.utils.Constant;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadManager  {
    private static DownloadManager downloadManager;
    public synchronized static DownloadManager getInstance(){
        if (null == null){
            downloadManager = new DownloadManager();
        }
        return downloadManager;
    }

    /**
     * 添加下载任务
     * @param context
     * @param downloadEnty
     */
    public void addEnty(Context context,DownloadEnty downloadEnty){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(Constant.KEY_DOWNLOAD_ENTY, downloadEnty);
        intent.setAction(Constant.KEY_DOWNLOAD_ADD);
        context.startService(intent);
    }

    public void pauseEnty(){

    }


    public void addObserve(DataWhatcher dataWhatcher){
        DataChanger.getInstance().addObserver(dataWhatcher);
    }

    public void removeObserve(DataWhatcher dataWhatcher){
        DataChanger.getInstance().addObserver(dataWhatcher);
    }

    public void postStatus(DownloadEnty downloadEnty){

        DataChanger.getInstance().notifyDataChange(downloadEnty);
    }

}
