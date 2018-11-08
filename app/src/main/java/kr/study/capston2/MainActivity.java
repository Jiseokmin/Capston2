package kr.study.capston2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String userID;
    String what;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imagebtn_chicken = (ImageButton) findViewById(R.id.chicken);


        Intent intent = getIntent();
        userID  = intent.getStringExtra("userID");

        imagebtn_chicken.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent  = new Intent(MainActivity.this,ChickenActivity.class);
                what = "chicken";


                intent.putExtra("userID",userID);
                intent.putExtra("what",what);

                MainActivity.this.startActivity(intent);

            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
    }

    }

//point가 -가 되면 채팅방에 입장 못하도록 하기
//푸쉬 알림
//자동로그인
//개인정보 (로그아웃, 기본적인 정보)
//치킨 말고 다른 채티방 추가
//포인트 마이너스 된 사람들은 '이구역의파토왕은나야!' 방으로


