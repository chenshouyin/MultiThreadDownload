package multithreaddownload.csy.com.multithreaddownload;

import java.io.Serializable;

/**
 * Created by chenshouyin on 2017/10/25.
 * 我的博客:http://blog.csdn.net/e_inch_photo
 * 我的Github:https://github.com/chenshouyin
 */

public class DownloadEnty implements Serializable{
    public int id;
    public String fileUrl;
    public int currentLenth;
    public int totalLenth;
    public enum DownloadStatus{downloading,downloadcansel,downloadcomplete,downloadpause}
    public DownloadStatus downloadStatus;
}
