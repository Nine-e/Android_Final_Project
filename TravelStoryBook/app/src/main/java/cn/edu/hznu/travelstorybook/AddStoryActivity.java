package cn.edu.hznu.travelstorybook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class AddStoryActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private String get_num;
    private User user;
    private int t;
    private int img_id = R.drawable.story_img;

    private ImageButton backBtn;
    private ImageButton sendBtn;
    private EditText titleET;
    private EditText contentET;

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
        setContentView(R.layout.activity_add_story);

        backBtn = (ImageButton)findViewById(R.id.add_back_button);
        sendBtn = (ImageButton)findViewById(R.id.add_send_button);
        titleET = (EditText)findViewById(R.id.add_title_edit);
        contentET = (EditText)findViewById(R.id.add_content_edit);

        //创建数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");

        //找到当前用户
        getUser();

        //返回按钮点击事件
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddStoryActivity.this,UserDetailActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });
        //发布按钮点击事件
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("Add send","success");
                String title = titleET.getText().toString();
                String content = contentET.getText().toString();

                if(title.length()>20){
                    Toast.makeText(AddStoryActivity.this, "标题不能超过20字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(title.length()==0 || content.length()==0){
                    Toast.makeText(AddStoryActivity.this, "标题与内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                //获取当前的年月日
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String date=sdf.format(new java.util.Date());

                t = (int)(Math.random()*20);
                if(t%3 == 0){
                    img_id = R.drawable.story_img_01;
                }else if(t%3 == 1){
                    img_id = R.drawable.story_img_02;
                }else if(t%3 == 2){
                    img_id = R.drawable.story_img_03;
                }

                values.put("story_title",title);
                values.put("story_content",content);
                values.put("story_img_id",img_id);
                values.put("story_date",date);
                values.put("story_author_id",user.getUser_id());
                values.put("story_author_name",user.getUser_name());
                values.put("story_star_count",0);
                db.insert("Story",null,values);

                Intent intent = new Intent(AddStoryActivity.this,UserDetailActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);

            }
        });

    }
    private void getUser(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User",null,"user_number = ? ",new String[]{get_num},null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("user_id"));
                Log.d("UserStar",""+id);
                String number = cursor.getString(cursor.getColumnIndex("user_number"));
                Log.d("UserStar number",""+number);
                String password = cursor.getString(cursor.getColumnIndex("user_password"));
                Log.d("UserStar password",""+password);
                String name = cursor.getString(cursor.getColumnIndex("user_name"));
                Log.d("UserStar name",""+name);
                String sex = cursor.getString(cursor.getColumnIndex("user_sex"));
                Log.d("UserStar sex",sex);
                int img_id = cursor.getInt(cursor.getColumnIndex("user_img_id"));
                Log.d("UserStar img_id",""+img_id);
                user = new User(id,number,password,name,sex,img_id);
            }while (cursor.moveToNext());
        }
    }
}
