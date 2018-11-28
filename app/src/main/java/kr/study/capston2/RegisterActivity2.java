package kr.study.capston2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class RegisterActivity2 extends AppCompatActivity {
/////////////////////////이메일 입력 받는 엑티비티 AuthActivity 로 메일 값 전달 기능
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("회원 가입");
        setContentView(R.layout.activity_register2);

        final EditText editText = (EditText) findViewById(R.id.et_email) ;        //메일 아이디 부분

        TextView textView1 = (TextView) findViewById(R.id.tv_email) ;       //메일 주소 부분
        textView1.setText("@cau.ac.kr") ;

        Button btn2 = (Button) findViewById(R.id.btn_confirm);              //버튼 누르면 해당메일로 메일 보냄

        final String str_rand =  getRandomString(7);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);     //툴바 생성
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //뒤로 가기 버튼 클릭하면 MainActivity 로 이동
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(RegisterActivity2.this,LoginActivity.class);
                RegisterActivity2.this.startActivity(intent);
                finish();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                final String mailID  = editText.getText().toString();
                final GMailSender sender = new GMailSender("wltjrala45@gmail.com", "dlthfud1");


                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response); //특정 response를 실행 했을 때 결과 값이 담김
                            boolean  success = jsonResponse.getBoolean("success");

                            if (success) {      //이메일이 중복되지 않는 경우 인증 메일 보냄!!
                                new AsyncTask<Object, Object, Void>() {
                                    @Override public Void doInBackground(Object... arg) {
                                        try {
                                            sender.sendMail("인증 문자 입니다!",
                                                    str_rand,
                                                    "wltjrala45@gmail.com",     //보내는 사람
                                                    mailID+"@cau.ac.kr");       //받는 사람
                                        } catch (Exception e) {
                                            Log.e("SendMail", e.getMessage(), e);
                                        }
                                        return null;}
                                }.execute();
                                Intent intent=new Intent(RegisterActivity2.this,AuthActivity.class); //인증 문자 입력을 위한 Auth 엑티비티로 이동
                                intent.putExtra("ran_num",str_rand);
                                intent.putExtra("mail",mailID);

                                startActivity(intent);
                                finish();

                            }
                            else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity2.this);
                                    builder.setMessage("존재하는 이메일 입니다!")
                                            .setNegativeButton("다시 시도", null)
                                            .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };

                MailCheck mailCheck = new MailCheck(mailID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity2.this);
                queue.add(mailCheck);


            }
        });
    }

    private static String getRandomString(int length)           //랜덤 문자 만드는 함수
    {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] ="a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z".split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }
}
