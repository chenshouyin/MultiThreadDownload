package multithreaddownload.csy.com.multithreaddownload;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import multithreaddownload.csy.com.downloadlib.sp.SpUtils;
import multithreaddownload.csy.com.downloadlib.utils.LogUtil;


public class MultiTaskActivity extends Activity implements View.OnClickListener {

    private ListView listView;
    private List<DownloadEnty> downloadEntys;
    private DownloadManager downloadManager;
    private DownloadManagerAdapter downloadAdapter;
    private DownloadEnty downloadEnty;
    private DataWhatcher dataWhatcher = new DataWhatcher() {

        @Override
        public void notifyDataChange(Object data) {
            dealWithChange(data);
        }
    };

    /**
     * 更新状态,需要加个同步,因为子线程中notify的
     *
     * @param data
     */
    private synchronized void dealWithChange(Object data) {
        downloadEnty = (DownloadEnty) data;
        //重写了对象的hashCode,根据ID比较
        final int id = downloadEntys.indexOf(downloadEnty);
        if (id!=-1) {
//            //保存下载状态
//            if (null != downloadEntys){
//                SpUtils.getInstance(MultiTaskActivity.this).putBean(MultiTaskActivity.class.getSimpleName(),downloadEntys);
//            }
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
            LogUtil.e("download",downloadEnty.fileName + "===downloading"+"=="+downloadEnty.fileName+"==" + downloadEnty.currentLenth+"=="+downloadEnty.fileUrl);
        } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcomplete) {
            LogUtil.e("download",downloadEnty.fileName + "===ndownloadcomplete");
        } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadcansel) {
            downloadEnty = null;
            LogUtil.e("download",downloadEnty.fileName + "===downloadcansel");
        } else if (downloadEnty.downloadStatus == DownloadEnty.DownloadStatus.downloadpause) {
            LogUtil.e("download",downloadEnty.fileName + "===downloadpause");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceStatu) {
        super.onCreate(savedInstanceStatu);
        setContentView(R.layout.activity_multi);
        initView();
        LogUtil.isDbug = true;

        ActionBar actionBar = getActionBar();
        actionBar.show();
        DownloadManager.getInstance().addObserve(dataWhatcher);
        downloadManager = new DownloadManager();
         //取出保存的下载状态
        if (null != SpUtils.getInstance(MultiTaskActivity.this).getBean(MultiTaskActivity.class.getSimpleName())){
            downloadEntys = (List<DownloadEnty>) SpUtils.getInstance(MultiTaskActivity.this).getBean(MultiTaskActivity.class.getSimpleName());
        }else{
            downloadEntys = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                DownloadEnty downloadEnty = new DownloadEnty();
                downloadEnty.id = "" + i;
                downloadEnty.fileName = "下载任务" + i;
                downloadEnty.fileUrl = "http://api.stay4it.com/uploads/test.jpg";
                downloadEntys.add(downloadEnty);
                //保存待下载的任务到内存
                DownloadManager.getInstance().getMapDownLoadEnties().put(downloadEntys.get(i).id,downloadEnty);
            }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
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

            btDownload.setTextColor(Color.WHITE);
            if (null == enty.downloadStatus) {
                btDownload.setText("开始下载");
            } else if (DownloadEnty.DownloadStatus.downloading == enty.downloadStatus) {
                btDownload.setText("暂停下载");
                btDownload.setTextColor(getColor(R.color.colorAccent));
            }else if (DownloadEnty.DownloadStatus.downloadpause == enty.downloadStatus) {
                btDownload.setText("恢复下载");
            }else if (DownloadEnty.DownloadStatus.downloadcomplete == enty.downloadStatus) {
                btDownload.setText("已下载");
            }else if (DownloadEnty.DownloadStatus.downloadWaiting == enty.downloadStatus) {
                btDownload.setText("等待下载");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().removeObserve(dataWhatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuStart) {
            //全部开始
            DownloadManager.getInstance().startAll(MultiTaskActivity.this);
            return true;
        }
        if (item.getItemId() == R.id.menuPause) {
            //全部暂停
            DownloadManager.getInstance().pauseAll(MultiTaskActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
