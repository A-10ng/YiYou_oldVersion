package com.example.yiyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;

import org.litepal.LitePal;

import java.util.Timer;
import java.util.TimerTask;



public class QuitTeamActivity extends AppCompatActivity {

    private Button btn_sure;
    private Button btn_hesitate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_team);

        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_hesitate = (Button) findViewById(R.id.btn_hesitate);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAll(Team.class,"teamatePhoneNum = ? and teamName = ?",
                        Data.getUserPhoneNum(),Data.getTeamName());
                Toast.makeText(QuitTeamActivity.this,"退队成功！",Toast.LENGTH_SHORT).show();
                Data.setTeamName("none");
                final Intent intent2 = new Intent(QuitTeamActivity.this,Main2Activity.class);
                Timer timer = new Timer();
                TimerTask ts = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intent2);
                    }
                };
                timer.schedule(ts,1000);
            }
        });


        btn_hesitate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuitTeamActivity.this,Tourist_TeamateActivity.class);
                startActivity(intent);
            }
        });
    }
}
