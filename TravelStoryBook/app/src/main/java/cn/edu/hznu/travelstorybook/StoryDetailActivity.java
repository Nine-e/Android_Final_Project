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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StoryDetailActivity extends AppCompatActivity {
    private Story story;//查找结果
    private MyDatabaseHelper dbHelper;
    boolean isStar = false;
    boolean isStared = false;
    int story_id;
    private String get_num;
    private User user;
    private User author;
    private int star_id;

    private ImageButton closeBtn;
    private ImageButton starBtn;
    private ImageView story_img;
    private TextView title;
    private TextView content;
    private ImageView author_img;
    private TextView author_name;

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
        setContentView(R.layout.activity_story_detail);

        Log.d("Author","onCreate");

        story_img = (ImageView)findViewById(R.id.story_detail_img);
        title = (TextView)findViewById(R.id.story_detail_title);
        content = (TextView)findViewById(R.id.story_detail_content);
        author_img = (ImageView)findViewById(R.id.story_detail_author_img);
        author_name = (TextView)findViewById(R.id.story_detail_author_name);
        closeBtn = (ImageButton)findViewById(R.id.story_detail_close_button);
        starBtn = (ImageButton)findViewById(R.id.story_detail_star_button);

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");
        story_id = intent.getIntExtra("story_id",0);
        Log.d("StoryDetail",""+story_id);

        //判断该文章是否已被当前用户收藏
        if(get_num!=null){
            getUser();
            getStared();
            if (isStared){
                starBtn.setActivated(true);
                isStar = true;
            }
        }

        //关闭按钮点击事件
        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(isStar && get_num!=null && !isStared)
                    addStar();
                if (!isStar && get_num!=null && isStared)
                    deleteStar();
                finish();
            }
        });
        //收藏按钮点击事件
        starBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                 if (!isStar) {
                   /*if (get_num == null) {
                        Toast.makeText(StoryDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    } else {
                        starBtn.setActivated(true);
                        isStar = true;
                        addStar();
                    }*/
                     if (get_num == null) {
                         Toast.makeText(StoryDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                     } else {
                         starBtn.setActivated(true);
                         isStar = true;
                     }
                } else{
                        starBtn.setActivated(false);
                        isStar = false;
                }
            }
        });
        //初始化界面
        //查找
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Story",null,"story_id = ? ",new String[]{""+story_id},null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("story_id"));
                Log.d("StoryDetail",""+id);
                String title = cursor.getString(cursor.getColumnIndex("story_title"));
                Log.d("StoryDetail",""+title);
                String content = cursor.getString(cursor.getColumnIndex("story_content"));
                Log.d("StoryDetail",""+content);
                int img_id = cursor.getInt(cursor.getColumnIndex("story_img_id"));
                String date = cursor.getString(cursor.getColumnIndex("story_date"));
                int author_id = cursor.getInt(cursor.getColumnIndex("story_author_id"));
                Log.d("Author id",author_id+"");
                String author_name = cursor.getString(cursor.getColumnIndex("story_author_name"));
                int star_count = cursor.getInt(cursor.getColumnIndex("story_star_count"));
                story = new Story(id,img_id,title,content,date,author_name,author_id,star_count);
            }while (cursor.moveToNext());
        }
        getAuthor();
        Log.d("StoryDetail",""+story.getStory_id());
        Log.d("StoryDetail",""+story.getStory_title());
        story_img.setImageResource(story.getStory_img_id());
        title.setText(story.getStory_title());
        content.setText(story.getStory_content());
        author_img.setImageResource(author.getUser_img_id());
        author_name.setText(story.getStory_author());
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
    private void getAuthor(){
        Log.d("Author","ing");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("User",null,"user_id = ? ",new String[]{String.valueOf(story.getStory_author_id())},null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("user_id"));
                Log.d("Author",""+id);
                String number = cursor.getString(cursor.getColumnIndex("user_number"));
                Log.d("Author number",""+number);
                String password = cursor.getString(cursor.getColumnIndex("user_password"));
                Log.d("Author password",""+password);
                String name = cursor.getString(cursor.getColumnIndex("user_name"));
                Log.d("Author name",""+name);
                String sex = cursor.getString(cursor.getColumnIndex("user_sex"));
                Log.d("Author sex",sex);
                int img_id = cursor.getInt(cursor.getColumnIndex("user_img_id"));
                Log.d("Author img_id",""+img_id);
                author = new User(id,number,password,name,sex,img_id);
            }while (cursor.moveToNext());
        }
        if(author==null){
            Log.d("Author","null");
        }
    }
    private void getStared(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        isStared = false;
        Cursor cursor = db.query("Star",null,"user_id = ? ",new String[]{String.valueOf(user.getUser_id())},null,null,null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("story_id"));
                star_id = cursor.getInt(cursor.getColumnIndex("star_id"));
                Log.d("StoryDetail",""+id);
                if (id == story_id){
                    isStared = true;
                    break;
                }

            }while (cursor.moveToNext());
        }
    }
    private void addStar(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("story_id",story_id);
        values.put("user_id",user.getUser_id());
        db.insert("Star",null,values);
        values.clear();

        int starCount = story.getStory_star_count()+1;
        values.put("story_star_count",starCount);
        db.update("Story",values,"story_id = ?",new String[]{String.valueOf(story_id)});
    }
    private void deleteStar(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Star","star_id = ?",new String[]{String.valueOf(star_id)});

        ContentValues values = new ContentValues();
        int starCount = story.getStory_star_count()-1;
        values.put("story_star_count",starCount);
        db.update("Story",values,"story_id = ?",new String[]{String.valueOf(story_id)});
    }
}
