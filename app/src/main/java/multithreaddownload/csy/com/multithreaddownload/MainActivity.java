package multithreaddownload.csy.com.multithreaddownload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import multithreaddownload.csy.com.multithreaddownload.utils.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btStartDownload;

    private DataWhatcher dataWhatcher = new DataWhatcher() {

        @Override
        public void notifyDataChange(Object data) {
            DownloadEnty downloadEnty = (DownloadEnty) data;
            if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloading){
                LogUtil.e("download","===notifyDataChange===downloading"+downloadEnty.currentLenth);
            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcomplete){
                LogUtil.e("download","===notifyDataChange===downloadcomplete");

            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcansel){
                LogUtil.e("download","===notifyDataChange===downloadcansel");

            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause){
                LogUtil.e("download","===notifyDataChange===downloadpause");

            }else{
                LogUtil.e("download","===notifyDataChange===下载进度"+downloadEnty.currentLenth);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceStatu) {
        super.onCreate(savedInstanceStatu);
        setContentView(R.layout.activity_main);

        initView();
        LogUtil.isDbug = true;
    }

    private void initView() {
        btStartDownload = (Button) findViewById(R.id.btStartDownload);
        btStartDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btStartDownload){
            LogUtil.e("download","===点击下载===");
            DownloadEnty downloadEnty = new DownloadEnty();
            downloadEnty.id = "1";
            downloadEnty.fileUrl = "http://192.168.1.9:8080/csy.jpg";
            DownloadManager.getInstance().addEnty(MainActivity.this,downloadEnty);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownloadManager.getInstance().addObserve(dataWhatcher);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DownloadManager.getInstance().removeObserve(dataWhatcher);
    }
}
