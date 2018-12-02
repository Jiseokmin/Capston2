package kr.study.capston2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
   private  String userID;
    private Switch sw_message;
    private Switch sw_vibrate;
    private Switch sw_popup;

    String push_message;
    String push_popup;
    String push_vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("설정");
        setContentView(R.layout.activity_setting);

        sw_message = (Switch) findViewById(R.id.sw_message);
        sw_vibrate = (Switch) findViewById(R.id.sw_vibrate);
        sw_popup = (Switch) findViewById(R.id.sw_popup);

        final SharedPreferences message = getSharedPreferences("setting", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor setting_push = message.edit();

        push_message = message.getString("message","true");
        push_popup = message.getString("popup","true");
        push_vibrate = message.getString("vibrate","true");

        if(push_message.equals("true")) {       ////전체 푸시 알림
            sw_message.setChecked(true);
        }
        else {

            sw_message.setChecked(false);
        }
/////////////////////////////////////////////////////////////////////////////////
        if(push_popup.equals("true")) {         ///// 팝업 알림
            sw_popup.setChecked(true);
        }
        else {

            sw_popup.setChecked(false);
        }
/////////////////////////////////////////////////////////////////////////////////
        if(push_vibrate.equals("true")) {       ////진동 알림
            sw_vibrate.setChecked(true);
        }
        else {

            sw_vibrate.setChecked(false);
        }
///////////////////////////////////     알림 설정    /////////////////////////////////////////////////////////////////////////////////////

        sw_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {        ////전체 알림 스위치 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {        //// 알림 안 받는 경우
                    sw_message.setChecked(false);
                    push_message = "false";

                    setting_push.putString("message", "false");
                    setting_push.commit();

                }

                else if (isChecked) {       ////알림 받는 경우
                    sw_message.setChecked(true);
                    push_message = "true";

                    setting_push.putString("message", "true");
                    setting_push.commit();
                }
            }
        });


        sw_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  ////팝업 알림 스위치 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {        //// 알림 안 받는 경우
                    sw_popup.setChecked(false);
                    push_popup = "false";

                    setting_push.putString("popup", "false");
                    setting_push.commit();

                }

                else if (isChecked) {       ////알림 받는 경우
                    sw_popup.setChecked(true);
                    push_popup = "true";

                    setting_push.putString("popup", "true");
                    setting_push.commit();
                }
            }
        });


        sw_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  ////진동 알림 스위치 설정
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {        //// 알림 안 받는 경우
                    sw_vibrate.setChecked(false);
                    push_vibrate = "false";

                    setting_push.putString("vibrate", "false");
                    setting_push.commit();

                }

                else if (isChecked) {       ////알림 받는 경우
                    sw_vibrate.setChecked(true);
                    push_vibrate = "true";

                    setting_push.putString("vibrate", "true");
                    setting_push.commit();
                }
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        userID  = intent.getStringExtra("userID");





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //뒤로 가기 버튼 클릭하면 MainActivity 로 이동
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("userID", userID);
                SettingActivity.this.startActivity(intent);
                finish();
            }
        });
    }
}
