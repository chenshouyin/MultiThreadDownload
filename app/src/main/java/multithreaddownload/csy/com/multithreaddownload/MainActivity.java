package multithreaddownload.csy.com.multithreaddownload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btMuilty,btSingle;

    @Override
    protected void onCreate(Bundle savedInstanceStatu) {
        super.onCreate(savedInstanceStatu);
        setContentView(R.layout.activity_main);

    }

    private void initView() {
        btMuilty = (Button) findViewById(R.id.btMuilty);
        btMuilty.setOnClickListener(this);

        btSingle = (Button) findViewById(R.id.btSingle);
        btSingle.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == btSingle){
            Intent intent = new Intent(MainActivity.this,SingleTaskActivity.class);
            startActivity(intent);
        }else if (v == btMuilty){
            Intent intent = new Intent(MainActivity.this,MultiTaskActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
