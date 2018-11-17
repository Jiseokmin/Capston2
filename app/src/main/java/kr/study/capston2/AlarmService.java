package kr.study.capston2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AlarmService extends BroadcastReceiver {
    private DatabaseReference reference_chicken;
    private DatabaseReference reference_soju;
    private DatabaseReference reference_mic;
    private DatabaseReference reference_basketball;
    private DatabaseReference reference_study;
    private DatabaseReference reference_failer;

    @Override
    public void onReceive(Context context, Intent intent) //24시간 마다 실행
    {
        FirebaseApp.initializeApp(context);
        reference_chicken = FirebaseDatabase.getInstance()
                .getReference().child("chicken").getRef();
        reference_chicken.removeValue();

        reference_soju = FirebaseDatabase.getInstance()
                .getReference().child("soju").getRef();
        reference_soju.removeValue();

        reference_mic = FirebaseDatabase.getInstance()
                .getReference().child("mic").getRef();
        reference_mic.removeValue();

        reference_basketball = FirebaseDatabase.getInstance()
                .getReference().child("basketball").getRef();
        reference_basketball.removeValue();

        reference_study = FirebaseDatabase.getInstance()
                .getReference().child("study").getRef();
        reference_study.removeValue();

        reference_failer = FirebaseDatabase.getInstance()
                .getReference().child("failer").getRef();
        reference_failer.removeValue();

    }
}
