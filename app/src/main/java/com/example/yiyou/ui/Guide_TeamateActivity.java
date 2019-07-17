package com.example.yiyou.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;
import com.example.yiyou.db.User;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.example.yiyou.ui.FinishCrtTeamActivity.byte2bitmap;


public class Guide_TeamateActivity extends AppCompatActivity {

    private TextView teamName;
    private TextView teamNum;
    private TextView teamCode;
    private Button btn_teamCode;
    private Button btn_cancelteam;
    private ImageView QRCodeBigpic;
    private List<Team> currentTeam;
    private List<Team> NoPeopleTeam;
    private Dialog dialog;
    private LinearLayout activity_guide__teamate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide__teamate);

        findAllViews();

        activity_guide__teamate = (LinearLayout) findViewById(R.id.activity_guide__teamate);
        List<Team> teams = LitePal.where("teamName = ? and teamatePhoneNum != ?", Data.getTeamName(), "0")
                .find(Team.class);
        NoPeopleTeam = LitePal.where("teamName = ?",
                Data.getTeamName())
                .limit(1)
                .find(Team.class);
        if (teams.size() != 0) {
            List<User> users = LitePal.where("phoneNum = ?",teams.get(0).getTeamatePhoneNum())
                    .find(User.class);
            Button btn[] = new Button[teams.size()];
            for (int i = 0; i < teams.size(); i++) {
                btn[i] = new Button(this);
                btn[i].setId(1000 + i);
                btn[i].setBackgroundResource(R.drawable.btn_team);
                btn[i].setText("游客"+(i+1)+"    "+"姓名："+users.get(i).getUsername()+"    手机号："+
                        users.get(i).getPhoneNum());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                activity_guide__teamate.addView(btn[i], layoutParams);
            }

            Log.i("guideteamate>>>",Data.getTeamName());
            teamName.setText(Data.getTeamName());

            Log.i("teamName:", Data.getTeamName());

            currentTeam = LitePal.where("teamName = ?", Data.getTeamName())
                    .limit(1)
                    .find(Team.class);
            List<Team> currentTeams = LitePal.where("teamName = ? and teamatePhoneNum != ?", Data.getTeamName(), "0")
                    .find(Team.class);

            teamNum.setText(currentTeams.size() + "");
            teamCode.setText(currentTeam.get(0).getTeamCode());

            btn_teamCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(Guide_TeamateActivity.this, R.style.AppTheme);
                    QRCodeBigpic = new ImageView(Guide_TeamateActivity.this);
                    QRCodeBigpic.setImageBitmap(byte2bitmap(currentTeam.get(0).getQrCode()));
                    dialog.setContentView(QRCodeBigpic);
                    dialog.show();

                    final String path = Environment.getExternalStorageDirectory().getPath() + "/teamCode_" +
                            currentTeam.get(0).getTeamCode() + ".jpg";
                    //大图的点击事件（点击会让他消失）
                    QRCodeBigpic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    //大图的长按监听事件
                    QRCodeBigpic.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //弹出的“保存图片”的dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(Guide_TeamateActivity.this);
                            builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveCroppedImage(((BitmapDrawable) QRCodeBigpic.getDrawable()).getBitmap(), path);
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });
                }
            });
        }else{
            teamName.setText(Data.getTeamName());
            teamNum.setText((NoPeopleTeam.size()-1) + "");
            teamCode.setText(NoPeopleTeam.get(0).getTeamCode());
            btn_teamCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = new Dialog(Guide_TeamateActivity.this, R.style.AppTheme);
                    QRCodeBigpic = new ImageView(Guide_TeamateActivity.this);
                    QRCodeBigpic.setImageBitmap(byte2bitmap(NoPeopleTeam.get(0).getQrCode()));
                    dialog.setContentView(QRCodeBigpic);
                    dialog.show();

                    final String paths = Environment.getExternalStorageDirectory().getPath() + "/teamCode_" +
                            NoPeopleTeam.get(0).getTeamCode() + ".jpg";
                    //大图的点击事件（点击会让他消失）
                    QRCodeBigpic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    //大图的长按监听事件
                    QRCodeBigpic.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //弹出的“保存图片”的dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(Guide_TeamateActivity.this);
                            builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveCroppedImage(((BitmapDrawable) QRCodeBigpic.getDrawable()).getBitmap(), paths);
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });
                }
            });
        }

        btn_cancelteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Guide_TeamateActivity.this);
                dialog.setTitle("您真的要删除该队伍么？");
                dialog.setPositiveButton("狠心删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll(Team.class,"teamName = ?",Data.getTeamName());
                        Toast.makeText(Guide_TeamateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        Data.setTeamName("none");
                        Intent intent = new Intent(Guide_TeamateActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("再考虑一下下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void saveCroppedImage(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            Toast.makeText(Guide_TeamateActivity.this, "该路径为目录路径！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            Toast.makeText(Guide_TeamateActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Guide_TeamateActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void findAllViews() {
        teamName = (TextView) findViewById(R.id.teamName);
        teamNum = (TextView) findViewById(R.id.teamNum);
        teamCode = (TextView) findViewById(R.id.teamCode);
        btn_teamCode = (Button) findViewById(R.id.btn_teamCode);
        btn_cancelteam = (Button) findViewById(R.id.btn_cancelteam);
    }
}
