package multithreaddownload.csy.com.multithreaddownload;

/**
 * Created by chenshouyin on 2017/10/30.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadTask implements Runnable {

    private boolean isPaused = false;
    private boolean isCanseled = false;
    private DownloadEnty downloadEnty;

    public DownloadTask(DownloadEnty downloadEnty) {
        this.downloadEnty = downloadEnty;
    }

    /**
     * 开始下载
     */
    public void startDownload() {
        downloadEnty.downloadStatus = DownloadEnty.DownloadStatus.downloading;
        DownloadManager.getInstance().postStatus(downloadEnty);

        for(int i=0;i<1024*100;i+=1024){
            if (isCanseled || isPaused){
                //更新取消或暂停状态
                downloadEnty.downloadStatus = isCanseled ? DownloadEnty.DownloadStatus.downloadcansel:DownloadEnty.DownloadStatus.downloadpause;
                DownloadManager.getInstance().postStatus(downloadEnty);
                return;
            }
            downloadEnty.currentLenth += 1024;
            //更新现在进度
            DownloadManager.getInstance().postStatus(downloadEnty);
        }
        //下载完成,更新状态
        downloadEnty.downloadStatus = DownloadEnty.DownloadStatus.downloadcomplete;
        DownloadManager.getInstance().postStatus(downloadEnty);
    }

    public void resumDownload() {
    }

    public void canselDownload() {
        isCanseled = true;
    }

    public void pauseDownload() {
        isPaused = true;
    }

    @Override
    public void run() {
        startDownload();
    }
}
