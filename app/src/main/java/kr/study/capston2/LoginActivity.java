package kr.study.capston2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
/////////////////////////////로그인 activity
public class LoginActivity extends AppCompatActivity {
    private String loginId, loginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button) findViewById(R.id.loginButtion);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);

        if (loginId != null && loginPwd != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", loginId);
            startActivity(intent);
            finish();
        }
        else if (loginId == null && loginPwd == null){

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = idText.getText().toString();
                    String userPassword = passwordText.getText().toString();

                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId", userID);
                    autoLogin.putString("inputPwd", userPassword);
                    //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                    autoLogin.commit();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {       //로그인 할 때 아이디와 패스워드를 입력하면 메인 화면으로 넘어감
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    String userID = jsonResponse.getString("userID");
                                    //String userPassword = jsonResponse.getString("userPassword");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userID", userID);
                                    //  intent.putExtra("userPassword",userPassword);
                                    LoginActivity.this.startActivity(intent);
                                    finish();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("아이디 또는 비밀번호가 일치하지 않습니다!")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    };
                    LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            });
    }



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity2.class);
                LoginActivity.this.startActivity(registerIntent);       //회원가입 버튼 누르면 RegisterActivity 로 이동
            }
        });



    }

}
