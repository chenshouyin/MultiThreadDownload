package multithreaddownload.csy.com.downloadlib;

import java.io.Serializable;

/**
 * Created by chenshouyin on 2017/10/25.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadEnty implements Serializable{
    public String id;
    public String fileUrl;
    public String fileName;
    public int currentLenth;
    public int totalLenth;
    public enum DownloadStatus{downloading,downloadcansel,downloadcomplete,downloadpause, downloadWaiting, downloadresum}
    public DownloadStatus downloadStatus;



    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }

    /**
     * 重写hascode,用于比较对象是否相等,根据ID
     * @return
     */

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
