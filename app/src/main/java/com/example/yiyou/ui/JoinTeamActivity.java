package com.example.yiyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;

import org.litepal.LitePal;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class JoinTeamActivity extends AppCompatActivity {

    private EditText teamcode;
    private Button btn_sure;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        teamcode = (EditText) findViewById(R.id.edit_teamcode);
        btn_sure = (Button) findViewById(R.id.sure);
        btn_cancel = (Button) findViewById(R.id.cancel);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_teamcode = teamcode.getText().toString();
                if (txt_teamcode == null || txt_teamcode.equals("")){
                    Toast.makeText(JoinTeamActivity.this,"请输入队伍码！",Toast.LENGTH_SHORT).show();
                }else{
                    List<Team> team = LitePal.where("teamCode = ?",txt_teamcode)
                            .find(Team.class);
                    if (team.size() < 1){
                        Toast.makeText(JoinTeamActivity.this,"您的队伍码不正确！",Toast.LENGTH_SHORT).show();
                    }else{
                        Team teams = new Team();
                        teams.setTeamIntro(team.get(0).getTeamIntro());
                        teams.setTeamatePhoneNum(Data.getUserPhoneNum());
                        teams.setGuidePhoneNum(team.get(0).getGuidePhoneNum());
                        teams.setTeamCode(team.get(0).getTeamCode());
                        teams.setQrCode(team.get(0).getQrCode());
                        teams.setTravelDate(team.get(0).getTravelDate());
                        teams.setTeamName(team.get(0).getTeamName());
                        teams.save();
                        Toast.makeText(JoinTeamActivity.this,"加入队伍成功！",Toast.LENGTH_SHORT).show();
                        final Intent intent = new Intent(JoinTeamActivity.this,Main2Activity.class);
                        Timer timer = new Timer();
                        TimerTask ts = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        };
                        timer.schedule(ts,1000);
                    }
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinTeamActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
