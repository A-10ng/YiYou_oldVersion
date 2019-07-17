package com.example.yiyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yiyou.R;
import com.example.yiyou.data.Data;
import com.example.yiyou.db.User;

import org.litepal.LitePal;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import static com.example.yiyou.ui.RegisterActivity.md5;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_phoneNum;
    private EditText edit_password;
    private Button btn_login;
    private Button btn_register;
    private String phoneNum;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //取消标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        findAllView();

        setAllClickListener();
    }

    private void setAllClickListener() {

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private void findAllView() {

        edit_phoneNum = (EditText) findViewById(R.id.edit_phoneNum);
        edit_password = (EditText) findViewById(R.id.edit_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                phoneNum = edit_phoneNum.getText().toString().trim();
                password = md5(edit_password.getText().toString().trim());
                User user = new User();
                List<User> users = LitePal.where("phoneNum = ?",phoneNum)
                        .find(User.class);
                if (users.size() >= 1){
                    if (users.get(0).getPassword().equals(password)){
                        //将用户的手机号存进Data类中，方便调用
                        Data.setUserPhoneNum(phoneNum);
                        Toast.makeText(this,"登录成功！",Toast.LENGTH_SHORT).show();
                        final Intent intent1 = new Intent(this,MainActivity.class);
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intent1);
                                finish();
                            }
                        };
                        timer.schedule(timerTask,1000);
                    }else {
                        Toast.makeText(this,"密码不正确！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"您还未注册过该手机号了！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
