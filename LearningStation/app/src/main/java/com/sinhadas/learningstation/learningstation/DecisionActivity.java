package com.sinhadas.learningstation.learningstation;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.sinhadas.learningstation.learningstation.service.DecisionService;




public class DecisionActivity extends AppCompatActivity {
    private Button starthrmBtn;
    private Button stophrmBtn;
    private DecisionService decisionService;
    private static final int REQUEST_HRM = 0;
    private DataCallBack mDataCallbck;
    private TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        starthrmBtn = (Button) findViewById(R.id.str_hrm);
        stophrmBtn = (Button) findViewById(R.id.stop_hrm);
        view = (TextView) findViewById(R.id.view);
        mDataCallbck = new DataCallBack() {
            @Override
            public void uiUpdate(float data) {
                view.setText(String.valueOf(data));
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BODY_SENSORS)
                != PackageManager.PERMISSION_GRANTED) {


            requestPermissionForBodySensor();

        }

        starthrmBtn.setOnClickListener(clickListener);
        stophrmBtn.setOnClickListener(clickListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bindDecisionService();
    }


    private void requestPermissionForBodySensor(){

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.BODY_SENSORS},
                REQUEST_HRM);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_HRM){
            Toast.makeText(this, "Permission Granted for HRM ", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         switch(v.getId()){
             case R.id.str_hrm:
                 decisionService.startSensors(mDataCallbck);
                 starthrmBtn.setVisibility(View.GONE);
                 stophrmBtn.setVisibility(View.VISIBLE);
                 break;
             case R.id.stop_hrm:
                 decisionService.stopSensors();
                 starthrmBtn.setVisibility(View.VISIBLE);
                 stophrmBtn.setVisibility(View.GONE);
                 break;
         }
        }
    };

    private void bindDecisionService(){
        Intent intent = new Intent(this, DecisionService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                decisionService =(DecisionService)((DecisionService.LocalBinder)service).getService();


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decision, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
