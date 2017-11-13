package multithreaddownload.csy.com.downloadlib;

import android.content.Context;
import android.content.Intent;

import multithreaddownload.csy.com.downloadlib.utils.Constant;

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

    public void pauseEnty(Context context,DownloadEnty downloadEnty){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(Constant.KEY_DOWNLOAD_ENTY, downloadEnty);
        intent.setAction(Constant.KEY_DOWNLOAD_PAUSE);
        context.startService(intent);
    }

    public void canselEnty(Context context,DownloadEnty donloadEnty){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(Constant.KEY_DOWNLOAD_ENTY, donloadEnty);
        intent.setAction(Constant.KEY_DOWNLOAD_CANSEL);
        context.startService(intent);
    }

    public void resumEnty(Context context,DownloadEnty downloadEnty){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(Constant.KEY_DOWNLOAD_ENTY, downloadEnty);
        intent.setAction(Constant.KEY_DOWNLOAD_RESUM);
        context.startService(intent);
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
