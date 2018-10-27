package kr.study.capston2;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
///////////////////////회원 가입 엑티비티
public class RegisterActivity extends AppCompatActivity {
    boolean add ;         //회원가입 정보를 db에 추가할지 말지 정하는 변수

    private String userGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("회원 가입");
        setContentView(R.layout.activity_register);


        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passWordText);
        final EditText passwordText2 = (EditText)findViewById(R.id.passWordText2);
        final EditText nameText = (EditText)findViewById(R.id.nameText);
        final EditText ageText = (EditText)findViewById(R.id.ageText);

        final String usermail;

      final   RadioGroup genderGroup = (RadioGroup)findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();
       userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

        @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton genderButton = (RadioButton)findViewById(i);
                    userGender = genderButton.getText().toString();

        }
        });


        Intent intent=new Intent(this.getIntent());
        usermail=intent.getStringExtra("mail");    //// 넘어온 메일아이디 받기


        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String userID = idText.getText().toString();
                final int userAge = Integer.parseInt(ageText.getText().toString());
                 final String userPassword = passwordText.getText().toString();
                 final String userPassword2 = passwordText2.getText().toString();
                final String userMail = usermail;



                  String userName = nameText.getText().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response); //특정 response를 실행 했을 때 결과 값이 담김
                            boolean  success = jsonResponse.getBoolean("success");
                            boolean  duplicate = jsonResponse.getBoolean("duplicate");


                            if(!userPassword.equals(userPassword2)) {
                                success = false;
                            }

                            if (success && duplicate) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);  //성공 할 경우 알람 뜨게함
                                builder.setMessage("회원 등록에 성공했습니다")
                                        .setPositiveButton("확인", null)
                                        .create().show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); //회원 가입 성공 할 경우 로그인 액티비티로 이동
                                RegisterActivity.this.startActivity(intent);
                                add = true;
                            }
                            else {

                                if (!userPassword.equals(userPassword2)) {      //비밀번호 일치하지 않는 경우
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("비밀번호가 일치하지 않습니다!")
                                            .setNegativeButton("다시 시도", null)
                                            .create().show();

                                } else if(!duplicate){      //아이디가 중복되는경우
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("존재하는 아이디입니다!")
                                            .setNegativeButton("다시 시도", null)
                                            .create().show();
                                }
                                add = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                };


                    RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userPassword2,userName,userMail,userAge,userGender, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);

            }
        });
    }

}
///Db에 gender 추가 , onstop 부분 작성