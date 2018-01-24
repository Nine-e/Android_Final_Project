package cn.edu.hznu.travelstorybook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private User user;//查找结果
    private MyDatabaseHelper dbHelper;

    private ImageButton backBtn;
    private EditText numberET;
    private EditText passwordET;
    private Button loginBtn;
    private Button signBtn;

    private int number;
    private String password;
    private String number_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_login);

        backBtn = (ImageButton)findViewById(R.id.login_back_button);
        numberET = (EditText)findViewById(R.id.login_number_edit);
        passwordET = (EditText)findViewById(R.id.login_password_edit);
        loginBtn = (Button)findViewById(R.id.login_button);
        signBtn = (Button)findViewById(R.id.login_sign_button);
        user = null;

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        //登录 按钮点击事件
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //获取输入值
                number_str = numberET.getText().toString();
                Log.d("Login num_str",number_str);
                /*try {
                    number= Integer.valueOf(number_str).intValue();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Log.d("Login num",""+number);*/
                password = passwordET.getText().toString();
                Log.d("Login passwordET",password);

                //查询数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("User",null,"user_number = ? ",new String[]{number_str},null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据并打印
                        int id = cursor.getInt(cursor.getColumnIndex("user_id"));
                        Log.d("Login id",""+id);
                        String number = cursor.getString(cursor.getColumnIndex("user_number"));
                        Log.d("Login number",""+number);
                        String password = cursor.getString(cursor.getColumnIndex("user_password"));
                        Log.d("Login password",""+password);
                        String name = cursor.getString(cursor.getColumnIndex("user_name"));
                        Log.d("Login name",""+name);
                        String sex = cursor.getString(cursor.getColumnIndex("user_sex"));
                        Log.d("Login sex",sex);
                        int img_id = cursor.getInt(cursor.getColumnIndex("user_img_id"));
                        Log.d("Login img_id",""+img_id);
                        user = new User(id,number,password,name,sex,img_id);
                    }while (cursor.moveToNext());
                }
                //对比密码，正确登录，错误提示（待做）
                if(user==null){
                    Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
                }else {
                    if (user.getUser_password().equals(password)) {
                        Intent intent = new Intent(LoginActivity.this, UserDetailActivity.class);
                        //传递数据
                        intent.putExtra("user_num", user.getUser_number());
                        Log.d("Login", number_str);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        passwordET.setText("");
                    }
                }

            }
        });

        //返回 按钮点击事件
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,UserDetailActivity.class);
                startActivity(intent);
            }
        });
        //注册 按钮点击事件
        signBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });
    }
}
