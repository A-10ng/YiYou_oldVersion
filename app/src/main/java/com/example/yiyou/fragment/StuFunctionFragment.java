package com.example.yiyou.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;
import com.example.yiyou.db.User;
import com.example.yiyou.ui.TouristRouteActivity;
import com.example.yiyou.ui.Tourist_TeamateActivity;

import org.litepal.LitePal;

import java.util.List;

import static com.example.yiyou.ui.FinishCrtTeamActivity.byte2bitmap;


/**
 * Created by 龙世治 on 2019/3/23.
 */

public class StuFunctionFragment extends Fragment {

    private View view;
    private TextView click2check;
    private ImageView teamate;
    private ImageView route;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stufunction,container,false);

        click2check = (TextView) view.findViewById(R.id.click2check);
        click2check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Team> CurrentTeam = LitePal.where("teamName = ?", Data.getTeamName())
                        .limit(1)
                        .find(Team.class);
                String teamIntro = CurrentTeam.get(0).getTeamIntro();

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("查看队伍简介");
                if (teamIntro == null || teamIntro.equals("")) {
                    dialog.setMessage("暂无简介");
                }
                dialog.setMessage(teamIntro);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        teamate = (ImageView) view.findViewById(R.id.teamate);
        teamate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Tourist_TeamateActivity.class);
                startActivity(intent);
            }
        });

        route = (ImageView) view.findViewById(R.id.route);
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TouristRouteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void updateUI() {
        TextView tv_TeamName = (TextView) view.findViewById(R.id.TeamName);
        tv_TeamName.setText(Data.getTeamName());
        List<Team> CurrentTeam = LitePal.where("teamName = ?", Data.getTeamName())
                .limit(1)
                .find(Team.class);
        List<User> CurrentGuide = LitePal.where("phoneNum = ?", CurrentTeam.get(0).getGuidePhoneNum())
                .find(User.class);

        TextView tv_travelDate = (TextView) view.findViewById(R.id.travelDate);
        TextView tv_teamIntro = (TextView) view.findViewById(R.id.teamIntro);
        TextView tv_guideName = (TextView) view.findViewById(R.id.guideName);
        ImageView iv_guideAvatar = (ImageView) view.findViewById(R.id.guideAvatar);

        String travelDate = CurrentTeam.get(0).getTravelDate();
        String teamIntro = CurrentTeam.get(0).getTeamIntro();
        String guideName = CurrentGuide.get(0).getUsername();

        if (travelDate == null || travelDate.equals("")) {
            tv_travelDate.setText("暂无时间");
        } else {
            tv_travelDate.setText(travelDate);
        }

        if (teamIntro == null || teamIntro.equals("")) {
            tv_teamIntro.setText("暂无简介");
        } else {
            tv_teamIntro.setText(teamIntro);
        }

        iv_guideAvatar.setImageBitmap(byte2bitmap(CurrentGuide.get(0).getAvatar()));
        tv_guideName.setText(guideName);
    }
}
