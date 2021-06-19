package com.example.mythread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.mythread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public HandlerThread handlerThread;
    public  Handler handler;
    public static final int MESSAGE_COUNT_DOWN = 100;
    private static final int MESSAGE_DONE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();;
        handler = new Handler(handlerThread.getLooper());

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {

                switch (msg.what) {
                    case MESSAGE_COUNT_DOWN:
                        binding.tvTime.setText(String.valueOf(msg.arg1));
                        break;
                    case MESSAGE_DONE:
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();

                }

            }
        };

        binding.btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_count:
                        doCountDown();
                        break;
                    default:
                        break;
                }
            }

            private void doCountDown()
            {
                // luồng phụ không thay đổi main thread
                Thread thread = new Thread(new Runnable() {


                    @Override
                    public void run() {

                        int time = 10;

                        do {
                            time--;

                            Message message = new Message();
                            message.what = MESSAGE_COUNT_DOWN;
                            message.arg1 = time;
                            handler.sendMessage(message);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }while (time>0);

                        handler.sendEmptyMessage(MESSAGE_DONE);

                    }
                });
                thread.start();

            }
        });

    }
}