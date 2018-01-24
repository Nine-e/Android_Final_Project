package cn.edu.hznu.travelstorybook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private User user;

    private ImageButton closeBtn;
    private Button signBtn;
    private EditText numberET;
    private EditText passwordET;
    private EditText sureET;
    private EditText nameET;

    private RadioGroup sexRG;
    private RadioButton boyRB;
    private RadioButton girlRB;
    private RadioButton unknowRB;
    private String sex="";
    private int img_id;

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
        setContentView(R.layout.activity_sign);

        closeBtn = (ImageButton)findViewById(R.id.sign_close_button);
        signBtn = (Button)findViewById(R.id.sign_button);
        numberET = (EditText)findViewById(R.id.sign_number_edit);
        passwordET = (EditText)findViewById(R.id.sign_password_edit);
        sureET = (EditText)findViewById(R.id.sign_sure_password_edit);
        nameET = (EditText)findViewById(R.id.sign_name_edit);

        sexRG = (RadioGroup)findViewById(R.id.sign_radioGroup);
        boyRB = (RadioButton)findViewById(R.id.sign_boy_radio);
        girlRB = (RadioButton)findViewById(R.id.sign_girl_radio);
        unknowRB = (RadioButton)findViewById(R.id.sign_unknow_radio);

        user = null;

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        //单选按钮点击事件
        sexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if(checkedId == boyRB.getId()){
                    sex = boyRB.getText().toString();
                    img_id = R.drawable.user_boy;
                }else if(checkedId == girlRB.getId()){
                    sex = girlRB.getText().toString();
                    img_id = R.drawable.user_girl;
                }else if(checkedId == unknowRB.getId()){
                    sex = unknowRB.getText().toString();
                    img_id = R.drawable.user_unknow;
                }
            }
        });

        //关闭按钮点击事件
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //注册按钮点击事件
        signBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String number = numberET.getText().toString();
                String password = passwordET.getText().toString();
                String sure = sureET.getText().toString();
                String name = nameET.getText().toString();
                user = null;

                if(number.equals(null)){
                    Toast.makeText(SignActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("User",null,"user_number = ? ",new String[]{number},null,null,null);
                    if(cursor.moveToFirst()){
                        do{
                            //遍历Cursor对象，取出数据并打印
                            int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));

                            String user_number = cursor.getString(cursor.getColumnIndex("user_number"));
                            Log.d("Sign number",user_number);

                            String user_password = cursor.getString(cursor.getColumnIndex("user_password"));

                            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));

                            String user_sex = cursor.getString(cursor.getColumnIndex("user_sex"));

                            int user_img_id = cursor.getInt(cursor.getColumnIndex("user_img_id"));

                            user = new User(user_id,user_number,user_password,user_name,user_sex,user_img_id);
                        }while (cursor.moveToNext());
                    }
                    if(user!=null){
                        Toast.makeText(SignActivity.this, "手机号已存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(password.length()==0){
                    Toast.makeText(SignActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(sure)){
                    Toast.makeText(SignActivity.this, "请再次确认密码", Toast.LENGTH_SHORT).show();
                    passwordET.setText("");
                    sureET.setText("");
                }else if(name.length()==0){
                    Toast.makeText(SignActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }else if(sex.length()==0){
                    Toast.makeText(SignActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put("user_number",number);
                    values.put("user_password",password);
                    values.put("user_name",name);
                    values.put("user_sex",sex);
                    values.put("user_img_id",img_id);
                    db.insert("User",null,values);

                    Intent intent = new Intent(SignActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
