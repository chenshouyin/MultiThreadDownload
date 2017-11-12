package multithreaddownload.csy.com.multithreaddownload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import multithreaddownload.csy.com.downloadlib.DataWhatcher;
import multithreaddownload.csy.com.downloadlib.DownloadEnty;
import multithreaddownload.csy.com.downloadlib.DownloadManager;
import multithreaddownload.csy.com.downloadlib.utils.LogUtil;


public class MultiTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private List<DownloadEnty> downloadEntys;
    private DownloadManager downloadManager;
    private DownloadManagerAdapter downloadAdapter;
    private DownloadEnty downloadEnty;
    private DataWhatcher dataWhatcher = new DataWhatcher() {

        @Override
        public void notifyDataChange(Object data) {
            downloadEnty = (DownloadEnty) data;
            //重写了对象的hashCode,根据ID比较
            final int id = downloadEntys.indexOf(downloadEnty);
            if (id!=-1) {
                //子线程回调回来的
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadEntys.set(id, downloadEnty);
                        downloadAdapter.notifyDataSetChanged();
                    }
                });
            }
            if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloading) {
                LogUtil.e("download", "===notifyDataChange===downloading"+"=="+downloadEnty.fileName+"==" + downloadEnty.currentLenth);
            } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcomplete) {
                LogUtil.e("download", "===notifyDataChange===downloadcomplete"+"=="+downloadEnty.fileName+"==" );
            } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcansel) {
                downloadEnty = null;
                LogUtil.e("download", "===notifyDataChange===downloadcansel"+"=="+downloadEnty.fileName+"==");
            } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause) {
                LogUtil.e("download", "===notifyDataChange===downloadpause"+"=="+downloadEnty.fileName+"==");
            } else {
                LogUtil.e("download", "===notifyDataChange===下载进度" +"=="+downloadEnty.fileName+"==" + downloadEnty.currentLenth);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceStatu) {
        super.onCreate(savedInstanceStatu);
        setContentView(R.layout.activity_multi);
        initView();
        LogUtil.isDbug = true;

        downloadManager = new DownloadManager();
        downloadEntys = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DownloadEnty downloadEnty = new DownloadEnty();
            downloadEnty.id = ""+i;
            downloadEnty.fileName = "下载任务" + i;
            downloadEnty.fileUrl = "http://api.stay4it.com/uploads/test.jpg";
            downloadEntys.add(downloadEnty);
        }
        downloadAdapter = new DownloadManagerAdapter();
        listView.setAdapter(downloadAdapter);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);

    }

    @Override
    public void onClick(View v) {

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


    class DownloadManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return downloadEntys.size();
        }

        @Override
        public Object getItem(int position) {
            return downloadEntys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MultiTaskActivity.this).inflate(R.layout.item_listview_activity_multi, null);
            final DownloadEnty enty = downloadEntys.get(position);
            TextView tvNameAndState = (TextView) view.findViewById(R.id.tvNameAndState);
            TextView tvProgress = (TextView) view.findViewById(R.id.tvProgress);

            tvNameAndState.setText(enty.fileName + ":" + enty.downloadStatus);
            tvProgress.setText(enty.currentLenth + "/" + enty.totalLenth);
            final Button btDownload = (Button) view.findViewById(R.id.btDownload);
            if (null == enty.downloadStatus) {
                btDownload.setText("开始下载");
            } else if (DownloadEnty.DownloadStatus.downloading == enty.downloadStatus) {
                btDownload.setText("暂停下载");
            }else if (DownloadEnty.DownloadStatus.downloadpause == enty.downloadStatus) {
                btDownload.setText("恢复下载");
            }
            btDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (null == enty.downloadStatus) {
                        downloadManager.addEnty(MultiTaskActivity.this, enty);
                        btDownload.setText("暂停下载");
                    } else if (DownloadEnty.DownloadStatus.downloading == enty.downloadStatus) {
                        downloadManager.pauseEnty(MultiTaskActivity.this, enty);
                        btDownload.setText("暂停下载");
                    }else if (DownloadEnty.DownloadStatus.downloadpause == enty.downloadStatus) {
                        downloadManager.resumEnty(MultiTaskActivity.this, enty);
                        btDownload.setText("恢复下载");
                    }
                }
            });
            return view;
        }
    }


}
