package com.example.yiyou.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.Team;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.litepal.LitePal;

import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class CreateTeamActivity extends AppCompatActivity {

    private EditText mTeamName;
    private EditText mTravelTime;
    private EditText mTeamIntro;
    private Button mCreateTeam;
    private String TeamName;
    private String TravelTime;
    private String TeamIntro;
    private String TeamCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        findAllViews();

        mCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamName = mTeamName.getText().toString().trim();
                TravelTime = mTravelTime.getText().toString().trim();
                TeamIntro = mTeamIntro.getText().toString().trim();
                if (TeamName.equals("") || TeamName == null){
                    Toast.makeText(CreateTeamActivity.this,"请至少填写队伍名称！",Toast.LENGTH_SHORT).show();
                }else {
                    TeamCode = createTeamCode();
                    Bitmap QRBitmap = createQRCode(TeamCode);

                    LitePal.getDatabase();
                    Team team = new Team();
                    team.setGuidePhoneNum(Data.getUserPhoneNum());
                    team.setTeamName(TeamName);
                    team.setTravelDate(TravelTime);
                    team.setTeamIntro(TeamIntro);
                    team.setTeamCode(TeamCode);
                    team.setTeamatePhoneNum("0");
                    team.setQrCode(RegisterActivity.bitmap2byte(QRBitmap));
                    team.save();
                    Toast.makeText(CreateTeamActivity.this,"创建队伍成功！",Toast.LENGTH_SHORT).show();
                    final Intent intent = new Intent(CreateTeamActivity.this,FinishCrtTeamActivity.class);
                    intent.putExtra("TeamCode",TeamCode);
                    intent.putExtra("QRBitmap",RegisterActivity.bitmap2byte(QRBitmap));
                    intent.putExtra("TeamName",TeamName);
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
        });

    }

    private Bitmap createQRCode(String url) {
        try {
            int w = 480;
            int h = 480;
            if (url == null || url.equals("") || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];

            //下面这里按照二维码的算法，逐个生成二维码的图片
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * w + x] = 0xff000000;
                    } else {
                        pixels[y * w + x] = 0xffffffff;
                    }
                }

            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String createTeamCode() {
        String TeamCode = "";
        for (int i = 0; i < 6; i++) {
            int value = (int) (Math.random() * 26 + 65);
            TeamCode += (char)value;
        }
        List<Team> teams = LitePal.where("teamCode = ?", TeamCode).find(Team.class);
        while (teams.size() >= 1) {
            for (int i = 0; i < 6; i++) {
                int value = (int) (Math.random() * 26 + 65);
                TeamCode += (char)value;
            }
            teams = LitePal.where("teamCode = ?", TeamCode).find(Team.class);
        }
        return TeamCode;
    }

    private void findAllViews() {
        mTeamName = (EditText) findViewById(R.id.edit_teamName);
        mTravelTime = (EditText) findViewById(R.id.edit_travelTime);
        mTeamIntro = (EditText) findViewById(R.id.edit_teamIntro);
        mCreateTeam = (Button) findViewById(R.id.btn_create);
    }
}
