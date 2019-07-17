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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;
import com.example.yiyou.db.User;
import com.example.yiyou.ui.GuideRouteActivity;
import com.example.yiyou.ui.Guide_TeamateActivity;

import org.litepal.LitePal;

import java.util.List;

import static com.example.yiyou.ui.FinishCrtTeamActivity.byte2bitmap;


/**
 * Created by 龙世治 on 2019/3/14.
 */

public class FunctionFragment extends Fragment {

    private View view;
    private TextView click2edit;
    private TextView click2check;
    private ImageView teamate;
    private ImageView guide_route;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_function, container, false);
        click2edit = (TextView) view.findViewById(R.id.click2edit);
        click2check = (TextView) view.findViewById(R.id.click2check);
        teamate = (ImageView) view.findViewById(R.id.teamate);

        teamate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Guide_TeamateActivity.class);
                startActivity(intent);
            }
        });

        guide_route = (ImageView) view.findViewById(R.id.guide_route);
        guide_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuideRouteActivity.class);
                startActivity(intent);
            }
        });

        click2edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final EditText editText_teamIntro = new EditText(getActivity());
                dialog.setTitle("在下方编辑简介").setView(editText_teamIntro);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Team Myteam = new Team();
                        Myteam.setTeamIntro(editText_teamIntro.getText().toString());
                        Myteam.updateAll("teamName = ?", Data.getTeamName());
                        TextView tv_teamIntro = (TextView) view.findViewById(R.id.teamIntro);
                        List<Team> CurrentTeam = LitePal.where("teamName = ?", Data.getTeamName())
                                .limit(1)
                                .find(Team.class);
                        String teamIntro = CurrentTeam.get(0).getTeamIntro();
                        if (teamIntro == null || teamIntro.equals("")) {
                            tv_teamIntro.setText("暂无简介");
                        } else {
                            tv_teamIntro.setText(teamIntro);
                        }
                    }
                });
                dialog.show();
            }
        });

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
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
