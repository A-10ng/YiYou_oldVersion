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
import com.example.yiyou.ui.CreateTeamActivity;
import com.example.yiyou.ui.Main2Activity;
import com.example.yiyou.ui.MainActivity;

import org.litepal.LitePal;

import java.util.List;


/**
 * Created by 龙世治 on 2019/3/14.
 */

public class HomeFragment extends Fragment {

    private Button btn_crtTeam;
    private LinearLayout linearLayout;
    private View view;
    private Button btn_change;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        btn_change = (Button) view.findViewById(R.id.change2tourist);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setTeamName("none");
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent);
            }
        });

        btn_crtTeam = (Button) view.findViewById(R.id.btn_crtTeam);
        btn_crtTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateTeamActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        final List<Team> teams = LitePal.where("guidePhoneNum = ? and teamatePhoneNum = ?", Data.getUserPhoneNum(), "0")
                .order("id desc")
                .find(Team.class);
        if (teams.size() != 0) {
            Button btn[] = new Button[teams.size()];
            for (int i = 0; i < teams.size(); i++) {
                btn[i] = new Button(getActivity());
                btn[i].setId(1000 + i);
                btn[i].setBackgroundResource(R.drawable.btn_team);
                btn[i].setText(teams.get(i).getTeamName());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700, 120);
                layoutParams.setMargins(0, 65, 0, 0);
                linearLayout.addView(btn[i], layoutParams);
            }
            for (int j = 0; j < teams.size(); j++) {
                btn[j].setTag(j);
                btn[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();
                        Data.setTeamName(teams.get(i).getTeamName());

                        //更新功能页面的信息
                        MainActivity activity = (MainActivity) getActivity();
                        NoScrollViewPager viewPager = (NoScrollViewPager) activity.findViewById(R.id.mViewPager);
                        FunctionFragment functionFragment = (FunctionFragment)getActivity().getSupportFragmentManager()
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
