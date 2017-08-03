package com.example.admin.w3d3ex02;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final java.lang.String MESSAGE_EXTRA = "com.example.admin.w3d3ex02_MESSAGE_EXTRA";
    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    Handler handler= new Handler(){
    @Override
    public void handleMessage(Message msg) {
        String message= msg.getData().getString(MESSAGE_EXTRA);
        tvresult.setText(message);
    }
};
    public TextView tvresult;
    public EditText eturl;
    public Button btnthread;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_thread:
                thisIsNewThread();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvresult=(TextView)findViewById(R.id.tv_result);
        eturl=(EditText)findViewById(R.id.et_url);
        btnthread=(Button)findViewById(R.id.btn_thread);
        btnthread.setOnClickListener(this);
        thisIsNewThread();
    }
    public void thisIsNewThread() {
        tvresult.setText("");
        if (eturl.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "NOT EMPTY URL", Toast.LENGTH_SHORT).show();

        } else {
            Thread newTread = new Thread() {
                @Override
                public void run() {

                    try {
                        Message message = handler.obtainMessage();
                        Bundle data = new Bundle();

                        URL url = new URL(eturl.getText().toString());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        Log.d(TAG, stringBuilder.toString());
                        data.putString(MESSAGE_EXTRA, stringBuilder.toString());
                        message.setData(data);
                        handler.sendMessage(message);
                    } catch (MalformedURLException mue) {
                        mue.printStackTrace();
                        //Toast.makeText(MainActivity.this,mue.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            };
            newTread.start();
        }
    }
}
