package multithreaddownload.csy.com.downloadlib;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
    private LinkedBlockingQueue<DownloadEnty> mWaitingPool = new LinkedBlockingQueue();
    private DataWhatcher mDataWhatcher = new DataWhatcher() {
        @Override
        public void notifyDataChange(Object data) {
            DownloadEnty mEnty = (DownloadEnty) data;
            switch (mEnty.downloadStatus){
                case downloadcansel:
                    //状态变化,检查下载队列有没有任务待下载
                    cheakTaskWait();
                    break;
                case downloadpause:
                    //状态变化,检查下载队列有没有任务待下载
                    cheakTaskWait();
                    break;
                case downloadcomplete:
                    //下载完成,下载任务移除,检查下载队列有没有任务待下载
                    downloadTasks.remove(mEnty.id);
                    cheakTaskWait();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检查下载队列是否有等待的任务
     * 有就取出执行
     * 需要加个同步,因为多个线程更新状态
     */
    private synchronized void cheakTaskWait() {
        DownloadEnty dowloadEnty = mWaitingPool.poll();
        if (null != dowloadEnty){
            startDownload(dowloadEnty);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataChanger.getInstance().addObserver(mDataWhatcher);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doActions(Intent intent) {
        if (null == intent.getSerializableExtra(Constant.KEY_DOWNLOAD_ENTY)){
            return;
        }
        DownloadEnty downloadEnty = (DownloadEnty) intent.getSerializableExtra(Constant.KEY_DOWNLOAD_ENTY);
        if (Constant.KEY_DOWNLOAD_ADD.equals(intent.getAction())){
            //开始下载
            LogUtil.e("download","===KEY_DOWNLOAD_ADD===");
            addDownload(downloadEnty);
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

    private void addDownload(DownloadEnty downloadEnty) {
        if (downloadTasks.size() >= Constant.MAX_DOWNLOAD_NUM){
            //加入任务队列 此方法通常要优于 add 方法，后者可能无法插入元素，而只是抛出一个异常
            mWaitingPool.offer(downloadEnty);
            downloadEnty.downloadStatus = DownloadEnty.DownloadStatus.downloadWaiting;
            //更新状态,等待下载
            DownloadManager.getInstance().postStatus(downloadEnty);
        }else{
            startDownload(downloadEnty);
        }
    }

    private void resumDownload(DownloadEnty downloadEnty) {
        //恢复也是调用开始方法
        addDownload(downloadEnty);
    }

    private void canselDownload(DownloadEnty downloadEnty) {
        DownloadTask task = downloadTasks.remove(downloadEnty.id);
        if (task!=null){
            task.canselDownload();
        }else{
            mWaitingPool.remove(downloadEnty);
        }
    }

    private void pauseDownload(DownloadEnty downloadEnty) {
        DownloadTask task = downloadTasks.remove(downloadEnty.id);
        if (task!=null){
            task.pauseDownload();
        }else{
            mWaitingPool.remove(downloadEnty);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataChanger.getInstance().deleteObserver(mDataWhatcher);
    }
}
