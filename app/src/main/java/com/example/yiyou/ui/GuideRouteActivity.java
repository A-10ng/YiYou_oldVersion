package com.example.yiyou.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Route;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GuideRouteActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private TextView tv_day1;
    private EditText et_day1;
    private ImageButton btn_create;
    private Button btn_save;
    // “+”按钮控件List
    private LinkedList<ImageButton> listIBTNAdd;
    //textView控件List
    private LinkedList<TextView> listTextView;
    // “+”按钮ID索引
    private int btnIDIndex = 1000;
    //edittext控件List
    private LinkedList<EditText> listEditText;
    // “-”按钮控件List
    private LinkedList<ImageButton> listIBTNDel;
    private int iETContentWidth;   // EditText控件宽度
    private int iETContentHeight;  // EditText控件高度
    private int iTVContentWidth;   //textView控件高度
    private int iTVContentHeight;   //textView控件高度
    private int iIBContentWidth;   //imageButton控件边长
    private float fDimRatio = 1.0f; // 尺寸比例（实际尺寸/xml文件里尺寸）
    private Message msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_route);

        findAllViews();

        LitePal.getDatabase();
        final Route route = new Route();
        route.setGuidePhoneNum(Data.getUserPhoneNum());
        route.setTeamName(Data.getTeamName());
        route.setRoute("0");
        route.save();

        listIBTNAdd = new LinkedList<ImageButton>();
        listIBTNDel = new LinkedList<ImageButton>();
        listTextView = new LinkedList<TextView>();
        listEditText = new LinkedList<EditText>();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iETContentWidth = et_day1.getWidth();   // EditText控件宽度
                iETContentHeight = et_day1.getHeight();  // EditText控件高度
                iTVContentWidth = tv_day1.getWidth();   //textView控件高度
                iTVContentHeight = tv_day1.getHeight();   //textView控件高度
                iIBContentWidth = btn_create.getWidth();
                fDimRatio = iETContentWidth / 150;

                addContent(v);
            }
        });
        listIBTNAdd.add(btn_create);
        listIBTNDel.add(null);
        listEditText.add(et_day1);

        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    String content = "";
                    Data.setRoute("none");
                    for (int i = 0; i < listEditText.size(); i++) {
                        if (listEditText.get(i).getText().toString() == null ||
                                listEditText.get(i).getText().toString().equals("")) {
                            Toast.makeText(GuideRouteActivity.this, "请填好相关行程！", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            content += listEditText.get(i).getText().toString() + "_";
                            Data.setRoute(content);
                        }
                    }
                    if (!Data.getRoute().equals("none")) {
                        String[] contens = Data.getRoute().split("_");
                        if (contens.length == listEditText.size()) {
                            List<Route> routes = LitePal.where("guidePhoneNum = ? and route != ? and teamName = ?",
                                    Data.getUserPhoneNum(),"0",Data.getTeamName())
                                    .find(Route.class);
                            if (routes.size() != 0){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(GuideRouteActivity.this);
                                dialog.setTitle("已存在行程，确定要覆盖么？");
                                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LitePal.deleteAll(Route.class,"guidePhoneNum = ? and teamName = ?",
                                                Data.getUserPhoneNum(),Data.getTeamName());
                                        Route route1 = new Route();
                                        route1.setGuidePhoneNum(Data.getUserPhoneNum());
                                        route1.setTeamName(Data.getTeamName());
                                        route1.setRoute(Data.getRoute());
                                        route1.save();
                                        Toast.makeText(GuideRouteActivity.this, "新建成功！", Toast.LENGTH_SHORT).show();
                                        final Intent intent1 = new Intent(GuideRouteActivity.this,MainActivity.class);
                                        Timer timer = new Timer();
                                        TimerTask timerTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                startActivity(intent1);
                                            }
                                        };
                                        timer.schedule(timerTask,1000);
                                    }
                                });
                                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }else {
                                LitePal.deleteAll(Route.class,"guidePhoneNum = ? and teamName = ?",
                                        Data.getUserPhoneNum(),Data.getTeamName());
                                Route route1 = new Route();
                                route1.setGuidePhoneNum(Data.getUserPhoneNum());
                                route1.setTeamName(Data.getTeamName());
                                route1.setRoute(Data.getRoute());
                                route1.save();
                                Toast.makeText(GuideRouteActivity.this, "新建成功！", Toast.LENGTH_SHORT).show();
                                final Intent intent = new Intent(GuideRouteActivity.this,MainActivity.class);
                                Timer timer = new Timer();
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        startActivity(intent);
                                    }
                                };
                                timer.schedule(timerTask,1000);
                            }
                        }
                    }
                }
                return true;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
                thread.start();
            }
        });
    }

    @SuppressWarnings("ResourceType")
    private void addContent(View v) {
        if (v == null)
            return;

        int index = -1;
        for (int i = 0; i < listIBTNAdd.size(); i++) {
            if (listIBTNAdd.get(i) == v) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            //判断点击按钮的下方是否已经存在按钮，是则将按钮从最底部插入
            try {
                if (listIBTNAdd.get(index + 1) != null) {
                    index = listIBTNAdd.size();
                }
            } catch (Exception e) {
                index += 1;
            }


            //开始添加控件

            //创建外围linearlayout控件
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams ILayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            ILayoutlayoutParams.setMargins(0, (int) (fDimRatio * 15), 0, 0);
            layout.setGravity(Gravity.CENTER);
            layout.setLayoutParams(ILayoutlayoutParams);

            //创建内部textView控件
            TextView tvContent = new TextView(this);
            LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams(
                    iTVContentWidth, iTVContentHeight
            );
            tvContent.setText("Day " + (index + 1));
            layout.addView(tvContent);
            listTextView.add(tvContent);

            //创建内部edittext控件
            EditText etContent = new EditText(this);
            LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                    iETContentWidth, iETContentHeight
            );
            try {
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                field.set(etContent, R.drawable.et_cursor_style);
            } catch (Exception e) {

            }
            etParam.setMargins((int) (fDimRatio * 20), 0, 0, 0);
            etContent.setBackgroundResource(R.drawable.edit_register_bg);
            etContent.setLayoutParams(etParam);
            layout.addView(etContent);
            listEditText.add(etContent);

            //创建“+”按钮
            ImageButton btnAdd = new ImageButton(this);
            LinearLayout.LayoutParams btnAddParam = new LinearLayout.LayoutParams(
                    iIBContentWidth,
                    iIBContentWidth
            );
            btnAddParam.setMargins((int) (fDimRatio * 10), 0, 0, 0);
            btnAdd.setLayoutParams(btnAddParam);
            btnAdd.setBackgroundResource(R.drawable.ibcreatestyle);
            btnAdd.setId(btnIDIndex);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addContent(v);
                }
            });
            layout.addView(btnAdd);
            listIBTNAdd.add(index, btnAdd);

            //创建“-”按钮
            ImageButton btnDelete = new ImageButton(this);
            LinearLayout.LayoutParams btnDeleteParam = new LinearLayout.LayoutParams(
                    iIBContentWidth,
                    iIBContentWidth
            );
            btnDeleteParam.setMargins((int) (fDimRatio * 10), 0, 0, 0);
            btnDelete.setLayoutParams(btnDeleteParam);
            btnDelete.setBackgroundResource(R.drawable.ibdeletestyle);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteContent(v);
                }
            });
            layout.addView(btnDelete);
            listIBTNDel.add(index, btnDelete);

            //将layout同它内部的所有控件都放在最外围的linearLayout容器里
            linearLayout.addView(layout);

            btnIDIndex++;
        }
    }

    private void deleteContent(View v) {
        if (v == null)
            return;

        //判断第几个“-”按钮触发了事件
        int index = -1;
        for (int i = 0; i < listIBTNDel.size(); i++) {
            if (listIBTNDel.get(i) == v) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            listIBTNAdd.remove(index);
            listIBTNDel.remove(index);
            listTextView.remove(index - 1);
            listEditText.remove(index);

            linearLayout.removeViewAt(index);
        }

        for (int i = 0; i < listTextView.size(); i++) {
            int num = 2 + i;
            listTextView.get(i).setText("Day " + num);
        }
    }

    private void findAllViews() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_day1 = (TextView) findViewById(R.id.tv_day1);
        et_day1 = (EditText) findViewById(R.id.et_day1);
        btn_create = (ImageButton) findViewById(R.id.btn_create);
        btn_save = (Button) findViewById(R.id.btn_saveroute);
    }
}
