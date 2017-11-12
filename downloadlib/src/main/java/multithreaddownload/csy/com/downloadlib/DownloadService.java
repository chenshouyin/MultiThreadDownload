package multithreaddownload.csy.com.downloadlib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import multithreaddownload.csy.com.downloadlib.utils.Constant;
import multithreaddownload.csy.com.downloadlib.utils.LogUtil;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadService extends Service{

    private Map<String,DownloadTask> downloadTasks = new HashMap();
    private ExecutorService executorService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doActions(Intent intent) {
        DownloadEnty downloadEnty = (DownloadEnty) intent.getSerializableExtra(Constant.KEY_DOWNLOAD_ENTY);

        if (Constant.KEY_DOWNLOAD_ADD.equals(intent.getAction())){
            //开始下载
            LogUtil.e("download","===KEY_DOWNLOAD_ADD===");
            startDownload(downloadEnty);
        }else if(Constant.KEY_DOWNLOAD_PAUSE.equals(intent.getAction())){
            //暂停下载
            LogUtil.e("download","===KEY_DOWNLOAD_PAUSE===");
            pauseDownload(downloadEnty);
        }else if(Constant.KEY_DOWNLOAD_CANSEL.equals(intent.getAction())){
            //取消下载
            LogUtil.e("download","===KEY_DOWNLOAD_RESUM===");
            canselDownload(downloadEnty);
        }else if(Constant.KEY_DOWNLOAD_RESUM.equals(intent.getAction())){
            //恢复下载
            LogUtil.e("download","===KEY_DOWNLOAD_RESUM===");
            resumDownload(downloadEnty);
        }

    }

    private void resumDownload(DownloadEnty downloadEnty) {
        //恢复也是调用开始方法
        startDownload(downloadEnty);
    }

    private void canselDownload(DownloadEnty downloadEnty) {
        DownloadTask task = downloadTasks.remove(downloadEnty.id);
        if (task!=null){
            task.canselDownload();
        }

    }

    private void pauseDownload(DownloadEnty downloadEnty) {
        DownloadTask task = downloadTasks.remove(downloadEnty.id);
        if (task!=null){
            task.pauseDownload();
        }
    }


    private void startDownload(DownloadEnty downloadEnty) {

        DownloadTask downloadTask = new DownloadTask(downloadEnty);
        downloadTasks.put(downloadEnty.id,downloadTask);
        //downloadTask.startDownload();
        getExecutorService().execute(downloadTask);
    }

    private synchronized ExecutorService getExecutorService(){
        if (executorService == null){
            executorService = Executors.newCachedThreadPool();
        }
        return executorService;
    }


}
