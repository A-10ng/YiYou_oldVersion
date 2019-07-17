package com.example.yiyou.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Route;

import org.litepal.LitePal;

import java.util.List;


public class TouristRouteActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_route);

        findAllViews();

        List<Route> routes = LitePal.where("guidePhoneNum=? and teamName=? and route!=?",
                Data.getUserPhoneNum(),Data.getTeamName(),"none")
                .find(Route.class);
        if (routes.size() == 0){
            TextView textView = new TextView(this);
            textView.setText("您的导游还未发布行程信息！");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(layoutParams);
            linearLayout.addView(textView);
        }else {
            String[] num = routes.get(0).getRoute().split("_");
            TextView[] Views = new TextView[num.length];
            for (int i = 0; i < num.length; i++) {
                Views[i] = new TextView(this);
                Views[i].setTextSize(20);
                Views[i].setText("Day "+(i+1)+"："+num[i]);
                LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                para.setMargins(0,30,0,0);
                Views[i].setLayoutParams(para);
                linearLayout.addView(Views[i]);
            }
        }
    }

    private void findAllViews() {
        linearLayout = (LinearLayout) findViewById(R.id.activity_tourist_route);
    }
}
