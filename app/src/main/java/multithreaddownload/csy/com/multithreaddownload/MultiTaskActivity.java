package multithreaddownload.csy.com.multithreaddownload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import multithreaddownload.csy.com.multithreaddownload.utils.LogUtil;

public class MultiTaskActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private List<DownloadEnty> downloadEntys;
    private DownloadManager downloadManager;
    private DownloadManagerAdapter downloadAdapter;

    private DownloadEnty downloadEnty;
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
        setContentView(R.layout.activity_multi);
        downloadManager = new DownloadManager();
        downloadEntys = new ArrayList<>();
        for (int i=0;i<10;i++){
            DownloadEnty downloadEnty = new DownloadEnty();
            downloadEnty.id = i;
            downloadEnty.fileUrl = "http://api.stay4it.com/uploads/test.jpg";
            //加入下载
            downloadManager.addEnty(MultiTaskActivity.this,downloadEnty);
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


    class DownloadManagerAdapter extends BaseAdapter{

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
            View view = LayoutInflater.from(MultiTaskActivity.this).inflate(R.layout.item_listview_activity_multi,null);

            return view;
        }
    }



}
