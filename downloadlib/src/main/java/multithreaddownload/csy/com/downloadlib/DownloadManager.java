package multithreaddownload.csy.com.downloadlib;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import multithreaddownload.csy.com.downloadlib.sp.SpUtils;
import multithreaddownload.csy.com.downloadlib.utils.Constant;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadManager  {
    private static DownloadManager downloadManager;

    public Map<String, DownloadEnty> getMapDownLoadEnties() {
        return mapDownLoadEnties;
    }

    private Map<String,DownloadEnty> mapDownLoadEnties = new HashMap();

    public SpUtils getSpUtils() {
        return spUtils;
    }

    private SpUtils spUtils = new SpUtils();
    public synchronized static DownloadManager getInstance(){
        if (downloadManager == null){
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
        DataChanger.getInstance().deleteObserver(dataWhatcher);
    }

    public void postStatus(DownloadEnty downloadEnty){
        //保存下载的每一个任务到内存,同时还要保存到本地,防止应用被强杀数据丢失
        DataChanger.getInstance().notifyDataChange(downloadEnty);
        mapDownLoadEnties.put(downloadEnty.id,downloadEnty);
        spUtils.putBean(downloadEnty.id,downloadEnty);
    }


    public void pauseAll(Context context){
        for (DownloadEnty enty : mapDownLoadEnties.values()) {
            if (enty.downloadStatus == DownloadEnty.DownloadStatus.downloading
                    || enty.downloadStatus == DownloadEnty.DownloadStatus.downloadWaiting){
                //下载中的任务 暂停
                pauseEnty(context,enty);
            }
        }
    }

    public void startAll(Context context){
        for (DownloadEnty enty : mapDownLoadEnties.values()) {
            if (enty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause
                    || enty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause || enty.downloadStatus == null){
                //下载中的任务 开始
                resumEnty(context,enty);
            }
        }
    }
}
