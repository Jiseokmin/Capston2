package kr.study.capston2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity {
    String userID;
    String what;

    private AutoScrollViewPager autoViewPager;

    private TextView txt_id;
    private ListView userlist;  //개인정보를 나타내기 위한 listview
    private ArrayAdapter<String> adapter;
    private ArrayList<String> infor_list = new ArrayList<>();
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();
/////////////////////////////////////////////////////////////////이미지 슬라이더 //////////////////////////////////////////////////////////////////////
        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("https://blogfiles.pstatic.net/MjAxODExMThfMzkg/MDAxNTQyNTMxODk4ODky.IqCZr-deaJOGvOBd_5KmAojBhVQRpyJ4hAuknAIK6k4g.S4gPEoqOeK59dluc4Za-3C751phRsu6F59DJaZTbOQ4g.PNG.37jr/first.PNG");
        data.add("https://blogfiles.pstatic.net/MjAxODExMThfMjQ1/MDAxNTQyNTMxMjYxODYw.wnfK7j8AfnoCYXoxVXQF7B0GJ_qZ3I8a_m3whUzGiDgg.Aebsb20xJ6Zh6A5-f5eiK-zTxqEcywvsVdGBVq_RlMkg.PNG.37jr/second.PNG");
        data.add("https://blogfiles.pstatic.net/MjAxODExMThfMTQ0/MDAxNTQyNTMxMjY2MDk5.VJEVIsm-Oe5gOwPz7ho6cwEx3pC8g0m96FJtZdz91Bwg.AofgIdJRdA3zpY6G2SbLY3NXM9_udAQOFL5orZVxXmcg.PNG.37jr/third.PNG");

        autoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        final ImageButton imagebtn_chicken = (ImageButton) findViewById(R.id.chicken);
        final ImageButton imagebtn_soju = (ImageButton) findViewById(R.id.soju);
        final ImageButton imagebtn_mic = (ImageButton) findViewById(R.id.mic);

        final ImageButton imagebtn_basketball = (ImageButton) findViewById(R.id.basketball);
        final ImageButton imagebtn_soccer = (ImageButton) findViewById(R.id.soccer);
        final ImageButton imagebtn_study = (ImageButton) findViewById(R.id.study);

        final ImageButton imagebtn_movie = (ImageButton) findViewById(R.id.movie);
        final ImageButton imagebtn_meal = (ImageButton) findViewById(R.id.meal);
        final ImageButton imagebtn_failer = (ImageButton) findViewById(R.id.failer);

        userlist=(ListView)findViewById(R.id.main_drawer);
        final View header = getLayoutInflater().inflate(R.layout.main_listview_header, null, false) ;
        userlist.addHeaderView(header) ;

        txt_id = (TextView) findViewById(R.id.main_title);



        final Intent intent = getIntent();
        userID  = intent.getStringExtra("userID");


        txt_id.setText(userID);

        infor_list.add("로그아웃");
        infor_list.add("회원탈퇴");
        adapter=new ArrayAdapter<String>(this,R.layout.simpleitem,infor_list);
        userlist.setAdapter(adapter);

        final DatabaseReference reference_warnRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("point");

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView parent, View v, final int position, long id) {
                if(position == 1) { //로그아웃
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                    editor.clear();
                    editor.commit();
                    finish();
                }

                if(position == 2) {     //회원탈퇴
                    Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                    finish();

                }

            }
        });

        reference_warnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> taskMap = new HashMap<String, Object>();
                String value = dataSnapshot.getValue(String.class); //firebase 에서 DB 가져옴
                final int point = Integer.parseInt(value);

                imagebtn_chicken.setOnClickListener(new View.OnClickListener() {        //치킨 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "chicken";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_soju.setOnClickListener(new View.OnClickListener() {        //소주 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "soju";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_mic.setOnClickListener(new View.OnClickListener() {        //노래방 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "mic";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_basketball.setOnClickListener(new View.OnClickListener() {        //농구 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "basketball";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_soccer.setOnClickListener(new View.OnClickListener() {        //축구 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "soccer";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_movie.setOnClickListener(new View.OnClickListener() {        //영화 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "movie";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

                imagebtn_meal.setOnClickListener(new View.OnClickListener() {        //식사 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "meal";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });
                imagebtn_study.setOnClickListener(new View.OnClickListener() {        //스터디 채팅방 입장

                    @Override
                    public void onClick(View arg0) {

                        if(point >= 0) {
                            Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                            what = "study";


                            intent.putExtra("userID", userID);
                            intent.putExtra("what", what);

                            MainActivity.this.startActivity(intent);
                        }
                        else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("약속 장소에 안나가셨나요?")
                                    .setMessage("여러 명과의 약속을 어겨 '천하제일 파토대회' 만 이용가능합니다.")
                                    .setNegativeButton("돌아가기",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            })

                                    .show();
                        }


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        imagebtn_failer.setOnClickListener(new View.OnClickListener() {        //파토방 채팅방 입장

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MainActivity.this, ChickenActivity.class);
                what = "failer";


                intent.putExtra("userID", userID);
                intent.putExtra("what", what);

                MainActivity.this.startActivity(intent);
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout) ;
        //각각의 버튼을 클릭할때의 수행할것을 정의해 준다.
        switch (item.getItemId()){
            case R.id.person_navigation_drawer:    //drawer 오른쪽으로 펼치기

                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT) ;
                }

        }

        return true;
    }

    public void registerAlarm()
    {
        Log.e("###", "registerAlarm");

        Intent intent = new Intent(this, AlarmService.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 10) {     //아침 10시 마다 실행

            calendar.add(Calendar.DATE, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        am.set(AlarmManager.RTC, calendar.getTimeInMillis(), sender);


    }

}

//푸쉬 알림
//개인정보 추가(회원탈퇴 등)