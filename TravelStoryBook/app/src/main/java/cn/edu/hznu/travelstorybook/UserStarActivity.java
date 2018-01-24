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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class UserStarActivity extends AppCompatActivity {
    private List<Story> storyBriefList = new ArrayList<>();
    private int[] storyId= new int[1000];
    private int length;
    private User user;
    private MyDatabaseHelper dbHelper;
    private StoryBriefAdapter adapter;
    private String get_num;

    private ImageButton backBtn;
    private ListView listView;

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
        setContentView(R.layout.activity_user_star);

        listView = (ListView)findViewById(R.id.star_list_view);
        adapter = new StoryBriefAdapter(UserStarActivity.this,R.layout.story_brief_item,storyBriefList);
        backBtn = (ImageButton)findViewById(R.id.user_star_back_button);

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        //deleteData();

        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");
            Log.d("UserStar",get_num);
            //查询数据库
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

            //修改用户发布的游记ListView
            initStoryBrief();
            listView.setAdapter(adapter);
        }

        //返回 按钮点击事件
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserStarActivity.this,UserDetailActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });

        //初始化ListView
        //adapter = new StoryBriefAdapter(UserStarActivity.this,R.layout.story_brief_item,storyBriefList);
        //listView.setAdapter(adapter);
        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Story story = storyBriefList.get(position);
                Intent intent = new Intent(UserStarActivity.this,StoryDetailActivity.class);
                intent.putExtra("user_num",get_num);
                intent.putExtra("story_id",story.getStory_id());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //修改用户发布的游记ListView
        initStoryBrief();
        listView.setAdapter(adapter);
    }

    private void deleteData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //删除Story表中的所有数据
        db.delete("Star",null,null);
    }


    private void initStoryBrief(){
        //查询数据库
        length=0;
        Cursor cursor2;
        storyBriefList.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor1 = db.query("Star",null,"user_id = ? ",new String[]{String.valueOf(user.getUser_id())},null,null,"star_id desc");
        if(cursor1.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int story_id = cursor1.getInt(cursor1.getColumnIndex("story_id"));
                Log.d("UserStar",""+story_id);
                storyId[length++]=story_id;
            }while (cursor1.moveToNext());
        }
        for(int i=0;i<length;i++){
            cursor2 = db.query("Story",null,"story_id = ? ",new String[]{String.valueOf(storyId[i])},null,null,null);
            if(cursor2.moveToFirst()){
                do{
                    //遍历Cursor对象，取出数据并打印
                    int id = cursor2.getInt(cursor2.getColumnIndex("story_id"));
                    String title = cursor2.getString(cursor2.getColumnIndex("story_title"));
                    String content = cursor2.getString(cursor2.getColumnIndex("story_content"));
                    int img_id = cursor2.getInt(cursor2.getColumnIndex("story_img_id"));
                    String date = cursor2.getString(cursor2.getColumnIndex("story_date"));
                    int author_id = cursor2.getInt(cursor2.getColumnIndex("story_author_id"));
                    String author_name = cursor2.getString(cursor2.getColumnIndex("story_author_name"));
                    int star_count = cursor2.getInt(cursor2.getColumnIndex("story_star_count"));
                    Log.d("UserStar Story",title);
                    Log.d("UserStar Story",content);
                    Log.d("UserStar Story",date);
                    Log.d("UserStar Story",author_name);
                    Story story = new Story(id,img_id,title,content,date,author_name,author_id,star_count);
                    storyBriefList.add(story);
                }while (cursor2.moveToNext());
            }
        }

    }
}
