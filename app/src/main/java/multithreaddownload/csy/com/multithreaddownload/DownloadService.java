package multithreaddownload.csy.com.multithreaddownload;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import multithreaddownload.csy.com.multithreaddownload.utils.Constant;
import multithreaddownload.csy.com.multithreaddownload.utils.LogUtil;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadService extends Service{
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
            downloadEnty.downloadStatus = DownloadEnty.DownloadStatus.downloading;
            DownloadManager.getInstance().postStatus(downloadEnty);
            LogUtil.e("download","===KEY_DOWNLOAD_ADD===");
            for(int i=0;i<1024*100;i+=1024){
                downloadEnty.currentLenth += 1024;
                DownloadManager.getInstance().postStatus(downloadEnty);
            }
            downloadEnty.downloadStatus = DownloadEnty.DownloadStatus.downloadcomplete;
            DownloadManager.getInstance().postStatus(downloadEnty);
        }

    }


}
