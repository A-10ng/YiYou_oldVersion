package com.example.yiyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;
import com.example.yiyou.ui.JoinTeamActivity;
import com.example.yiyou.ui.Main2Activity;
import com.example.yiyou.ui.MainActivity;

import org.litepal.LitePal;

import java.util.List;


/**
 * Created by 龙世治 on 2019/3/23.
 */

public class StuHomeFragment extends Fragment{

    private View view;
    private Button btn_change2guide;
    private LinearLayout linearLayout;
    private Button joinTeam;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stuhome,container,false);
        btn_change2guide = (Button) view.findViewById(R.id.change2guide);
        btn_change2guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setTeamName("none");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        joinTeam = (Button) view.findViewById(R.id.btn_joinTeam);
        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setTeamName("none");
                Intent intent = new Intent(getActivity(), JoinTeamActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        final List<Team> cteams = LitePal.where("teamatePhoneNum = ?", Data.getUserPhoneNum())
                .order("id desc")
                .find(Team.class);
        if (cteams.size() != 0) {
            Button btn[] = new Button[cteams.size()];
            for (int i = 0; i < cteams.size(); i++) {
                btn[i] = new Button(getActivity());
                btn[i].setId(1000 + i);
                btn[i].setBackgroundResource(R.drawable.btn_team);
                btn[i].setText(cteams.get(i).getTeamName());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700, 120);
                layoutParams.setMargins(0, 65, 0, 0);
                linearLayout.addView(btn[i], layoutParams);
            }
            for (int j = 0; j < cteams.size(); j++) {
                btn[j].setTag(j);
                btn[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();
                        Data.setTeamName(cteams.get(i).getTeamName());

                        //更新功能页面的信息
                        Main2Activity activity = (Main2Activity) getActivity();
                        NoScrollViewPager viewPager = (NoScrollViewPager) activity.findViewById(R.id.mViewPager);
                        StuFunctionFragment functionFragment = (StuFunctionFragment)getActivity().getSupportFragmentManager()
                                .findFragmentByTag("android:switcher:"+R.id.mViewPager+":1");
                        functionFragment.updateUI();
                        viewPager.setCurrentItem(1);

                    }
                });
            }
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
