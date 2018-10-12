package kr.study.capston2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    String ran_num;
    String mailid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent intent=new Intent(this.getIntent());
        ran_num=intent.getStringExtra("ran_num");
        mailid=intent.getStringExtra("mail");


        Button btn = (Button) findViewById(R.id.bt_ok);
       final  EditText editText = (EditText)findViewById(R.id.et_rannum) ;



        btn.setOnClickListener(new View.OnClickListener() {

        @Override
            public void onClick(View v) {
                final String num  = editText.getText().toString();      //현재 엑티비티에서 입력한 문자

                if(num.equals(ran_num)) {

                    Intent intent=new Intent(AuthActivity.this,RegisterActivity.class);
                    //intent.putExtra("mail",mailid);

                    startActivity(intent);
                    finish();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                    builder.setMessage("로그인 실패")
                            .setNegativeButton("다시 시도",null)
                            .create()
                            .show();
                }

            }
        });
    }
}
////////////////////해야될거
////////////////////3분 타이머 걸기
////////////////////비밀번호 암호화 하기
////////////////////회원가입 할 때 아이디 중복 등 예외처리