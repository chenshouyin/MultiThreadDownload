package multithreaddownload.csy.com.multithreaddownload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import multithreaddownload.csy.com.multithreaddownload.utils.LogUtil;

public class SingleTaskActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btStartDownload,btPause,btCansel;
    private DownloadEnty downloadEnty;
    private DownloadManager downloadManager;
    private DataWhatcher dataWhatcher = new DataWhatcher() {

        @Override
        public void notifyDataChange(Object data) {
            downloadEnty = (DownloadEnty) data;
            if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloading){
                LogUtil.e("download","===notifyDataChange===downloading"+downloadEnty.currentLenth);
            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcomplete){
                LogUtil.e("download","===notifyDataChange===downloadcomplete");
            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcansel){
                downloadEnty = null;
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
        setContentView(R.layout.activity_single);
        downloadManager = DownloadManager.getInstance();
        initView();
        LogUtil.isDbug = true;
    }

    private void initView() {
        btStartDownload = (Button) findViewById(R.id.btStartDownload);
        btStartDownload.setOnClickListener(this);

        btPause = (Button) findViewById(R.id.btPause);
        btPause.setOnClickListener(this);

        btCansel = (Button) findViewById(R.id.btCansel);
        btCansel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btStartDownload){
            if (downloadEnty == null){
                downloadEnty = new DownloadEnty();
                downloadEnty.id = 1;
                downloadEnty.fileUrl = "http://api.stay4it.com/uploads/test.jpg";
                downloadManager.addEnty(SingleTaskActivity.this,downloadEnty);
                LogUtil.e("download","===addEnty===");
            }
        }else if (v.getId() == R.id.btPause){
            if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause){
                downloadManager.resumEnty(SingleTaskActivity.this,downloadEnty);
                btPause.setText("暂停下载");
                LogUtil.e("download","===resumEnty===");
            }else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloading){
                downloadManager.pauseEnty(SingleTaskActivity.this,downloadEnty);
                btPause.setText("恢复下载");
                LogUtil.e("download","===pauseEnty===");
            }
        }else if (v.getId() == R.id.btCansel){
            DownloadManager.getInstance().canselEnty(SingleTaskActivity.this,downloadEnty);
            LogUtil.e("download","===addEnty===");
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
