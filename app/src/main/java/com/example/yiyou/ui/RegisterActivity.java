package com.example.yiyou.ui;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.db.User;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_username;
    private EditText edit_phoneNum;
    private RadioButton radio_male;
    private RadioButton radio_female;
    private EditText edit_password;
    private EditText edit_company;
    private RadioButton radio_tourist;
    private RadioButton radio_guide;
    private Button btn_upload_avatar;
    private Button btn_save;
    private TextView click2login;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 0;
    public static final int IDENTITY_TOURIST = 0;
    public static final int IDENTITY_GUIDE = 1;
    private int gender;
    private int identity;
    private String username;
    private String phoneNum;
    private String password;
    private String company;
    public static final int CHOOSE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //取消标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        findAllView();

        setAllOnClickListener();

    }

    private void setAllOnClickListener() {

        btn_upload_avatar.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void findAllView() {

        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_phoneNum = (EditText) findViewById(R.id.edit_phoneNum);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_male);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_company = (EditText) findViewById(R.id.edit_company);
        radio_tourist = (RadioButton) findViewById(R.id.radio_tourist);
        radio_guide = (RadioButton) findViewById(R.id.radio_guide);
        btn_upload_avatar = (Button) findViewById(R.id.btn_upload_avatar);
        btn_save = (Button) findViewById(R.id.btn_save);
        click2login = (TextView) findViewById(R.id.click2login);
    }

    //id为click2login的textView的点击事件
    public void click2loginListener(View v){
        Intent intent1 = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload_avatar:

                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.
                            permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.btn_save:
                username = edit_username.getText().toString();
                password = edit_password.getText().toString();
                company = edit_company.getText().toString();
                phoneNum = edit_phoneNum.getText().toString();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) btn_upload_avatar.getBackground();
                Bitmap current_avatar_bitmap = bitmapDrawable.getBitmap();

                Bitmap upload_avatar_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_upload);
                Bitmap default_avatar_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);


                //判断所选身份
                if (radio_tourist.isChecked()) {
                    identity = IDENTITY_TOURIST;
                } else {
                    identity = IDENTITY_GUIDE;
                }

                //判断所选性别
                if (radio_male.isChecked()) {
                    gender = GENDER_MALE;
                } else {
                    gender = GENDER_FEMALE;
                }

                if (username == null || username.equals("") || password == null || password.equals("")
                        || company == null || company.equals("") || phoneNum == null
                        || phoneNum.equals("") || phoneNum.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    LitePal.getDatabase();
                    User user = new User();
                    List<User> users = LitePal.where("phoneNum = ?",phoneNum)
                            .find(User.class);
                    if (users.size() >= 1){
                        Toast.makeText(this,"您已经注册过该手机号了！",Toast.LENGTH_SHORT).show();
                    }else{
                        if (current_avatar_bitmap.equals(upload_avatar_bitmap)){
                            user.setUsername(username);
                            user.setPhoneNum(phoneNum);
                            user.setGender(gender);
                            user.setPassword(password);
                            user.setCompany(company);
                            user.setIdentity(identity);
                            user.setAvatar(bitmap2byte(default_avatar_bitmap));
                            user.save();
                            Toast.makeText(this,"注册成功，请前往登录！",Toast.LENGTH_SHORT).show();
                        }else {
                            user.setUsername(username);
                            user.setPhoneNum(phoneNum);
                            user.setGender(gender);
                            user.setPassword(md5(password));
                            user.setCompany(company);
                            user.setIdentity(identity);
                            user.setAvatar(bitmap2byte(current_avatar_bitmap));
                            user.save();
                            Toast.makeText(this,"注册成功，请前往登录！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    public static byte[] bitmap2byte(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void openAlbum() {

        Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
        intent2.setType("image/*");
        startActivityForResult(intent2, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4及以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }

    private void handleImageBeforeKitKat(Intent data) {

        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private void handleImageOnKitKat(Intent data) {

        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //picture.setImageBitmap(bitmap);
            btn_upload_avatar.setBackground(new BitmapDrawable(getResources(),bitmap));
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {

        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
