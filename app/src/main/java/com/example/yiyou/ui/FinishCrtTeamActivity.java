package com.example.yiyou.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.User;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class FinishCrtTeamActivity extends AppCompatActivity {

    private TextView txt_guideName;
    private TextView txt_teamName_teamCode;
    private ImageView QRCode;
    private String guideName;
    private Button btn_finish;
    private Dialog dialog;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_crt_team);

        findAllViews();

        Intent intent = getIntent();
        String TeamName = intent.getStringExtra("TeamName");
        String TeamCode = intent.getStringExtra("TeamCode");
        byte[] QRByte = intent.getByteArrayExtra("QRBitmap");

        try {
            List<User> user = LitePal.where("phoneNum = ?", Data.getUserPhoneNum()).find(User.class);
            guideName = user.get(0).getUsername();
        } catch (Exception e) {
            guideName = "获取不到";
        }

        txt_guideName.setText("导游（" + guideName + "）");
        txt_teamName_teamCode.setText(TeamName + "（" + TeamCode + "）");
        QRCode.setImageBitmap(byte2bitmap(QRByte));

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setTeamName("none");
                Intent intent = new Intent(FinishCrtTeamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final String path = Environment.getExternalStorageDirectory().getPath() + "/teamCode_" + TeamCode + ".jpg";
        //点击显示大图，长按保存
        //对话框显示大图
        dialog = new Dialog(FinishCrtTeamActivity.this, R.style.AppTheme);
        mImageView = getImageView();
        dialog.setContentView(mImageView);

        //大图的点击事件（点击会让他消失）
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //大图的长按监听事件
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(FinishCrtTeamActivity.this);
                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCroppedImage(((BitmapDrawable) mImageView.getDrawable()).getBitmap(), path);
                    }
                });
                builder.show();
                return true;
            }
        });

        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

    }

    private void saveCroppedImage(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            Toast.makeText(FinishCrtTeamActivity.this, "该路径为目录路径！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            Toast.makeText(FinishCrtTeamActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(FinishCrtTeamActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private ImageView getImageView() {
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置padding
        iv.setPadding(20, 20, 20, 20);
        //imageview设置图片
        iv.setImageBitmap(((BitmapDrawable) QRCode.getDrawable()).getBitmap());
        return iv;
    }

    public static Bitmap byte2bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void findAllViews() {
        txt_guideName = (TextView) findViewById(R.id.guideName);
        txt_teamName_teamCode = (TextView) findViewById(R.id.teamName_teamCode);
        QRCode = (ImageView) findViewById(R.id.qrCode);
        btn_finish = (Button) findViewById(R.id.finish);
    }
}
