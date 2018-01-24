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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {
    private List<Story> storyBriefList = new ArrayList<>();
    private User user;
    private MyDatabaseHelper dbHelper;
    private StoryBriefAdapter adapter;
    private boolean isOnline = false;
    private int get_id = -1;
    private String get_num;

    private ImageView user_detail_img;
    private TextView user_detail_name;
    private Button user_detail_button;
    private ImageButton backBtn;
    private View starLayout;
    private Button addBtn;
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
        setContentView(R.layout.activity_user_detail);

        user_detail_img = (ImageView)findViewById(R.id.user_detail_img);
        user_detail_name = (TextView)findViewById(R.id.user_detail_name);
        user_detail_button = (Button)findViewById(R.id.user_detail_button);
        listView = (ListView)findViewById(R.id.user_list_view);
        isOnline = false;

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

       /* if(getIntent()!=null) {
            Intent intent = getIntent();
            //get_id = intent.getIntExtra("user_id",-1);
            //Log.d("UserDetail getId", ""+get_id);
            get_num = intent.getStringExtra("user_num");
            Log.d("UserDetail getNum", ""+get_num);
            //isOnline = true;
        }*/
        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");
        if(get_num!=null){
            Log.d("UserDetail getNum", ""+get_num);
            isOnline = true;
        }

        //初始化ListView
        //initStoryBrief();
        init();
        adapter = new StoryBriefAdapter(UserDetailActivity.this,R.layout.story_brief_item,storyBriefList);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);

        //返回 按钮点击事件
        backBtn = (ImageButton)findViewById(R.id.user_detail_back_button);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailActivity.this,MainActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });

        //登录/退出 按钮点击事件（未完善）
        user_detail_button = (Button)findViewById(R.id.user_detail_button);
        user_detail_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDetailActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        //我的收藏 LinearLayout点击事件
        starLayout = (LinearLayout)findViewById(R.id.user_detail_star_layout);
        starLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isOnline){
                    Toast.makeText(UserDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(UserDetailActivity.this, UserStarActivity.class);
                    intent.putExtra("user_num", get_num);
                    startActivity(intent);
                }
            }
        });

        //添加游记 按钮点击事件
        addBtn = (Button)findViewById(R.id.add_story_button);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isOnline){
                    Toast.makeText(UserDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(UserDetailActivity.this, AddStoryActivity.class);
                    intent.putExtra("user_num", get_num);
                    startActivity(intent);
                }
            }
        });

        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Story story = storyBriefList.get(position);
                Intent intent = new Intent(UserDetailActivity.this,StoryDetailActivity.class);
                intent.putExtra("user_num",get_num);
                intent.putExtra("story_id",story.getStory_id());
                startActivity(intent);
            }
        });

        init();
    }
    private void initStoryBrief(){
        /*for (int i=0;i<5;i++){
            Story story = new Story(i,R.drawable.story_img,
                    "年末 | 12月活动推荐",
                    "2017年的最后一个月，哪些演出值得一看，哪些展览不容错过？",
                    "2017-12-12","游记编辑部",10+i);
            storyBriefList.add(story);
        }*/
        storyBriefList.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询Story表中作者为当前用户的所有数据
        Cursor cursor = db.query("Story",null,"story_author_id = ?",new String[]{String.valueOf(user.getUser_id())},null,null,"story_id desc");
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据并打印
                int id = cursor.getInt(cursor.getColumnIndex("story_id"));
                String title = cursor.getString(cursor.getColumnIndex("story_title"));
                String content = cursor.getString(cursor.getColumnIndex("story_content"));
                int img_id = cursor.getInt(cursor.getColumnIndex("story_img_id"));
                String date = cursor.getString(cursor.getColumnIndex("story_date"));
                int author_id = cursor.getInt(cursor.getColumnIndex("story_author_id"));
                String author_name = cursor.getString(cursor.getColumnIndex("story_author_name"));
                int star_count = cursor.getInt(cursor.getColumnIndex("story_star_count"));
                Log.d("SearchActivity Story",title);
                Log.d("SearchActivity Story",content);
                Log.d("SearchActivity Story",date);
                Log.d("SearchActivity Story",author_name);
                Story story = new Story(id,img_id,title,content,date,author_name,author_id,star_count);
                storyBriefList.add(story);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    //计算ListView高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            Log.d("MainActivity","item:"+listItem.getMeasuredHeight());
            totalHeight += listItem.getMeasuredHeight();
            Log.d("MainActivity","total"+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        Log.d("MainActivity","params:"+params.height);
        listView.setLayoutParams(params);
    }

    //初始化页面
    private void init(){
        if(!isOnline){
            user_detail_img.setImageResource(R.drawable.user_detail_unlogin);
            user_detail_name.setText("点击登陆，体验更多");
            user_detail_button.setText("登录");
            storyBriefList.clear();
            listView.setVisibility(View.GONE);
        }else{
            //查询数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("User",null,"user_number = ? ",new String[]{get_num},null,null,null);
            if(cursor.moveToFirst()){
                do{
                    //遍历Cursor对象，取出数据并打印
                    int id = cursor.getInt(cursor.getColumnIndex("user_id"));
                    Log.d("UserDetail id",""+id);
                    String number = cursor.getString(cursor.getColumnIndex("user_number"));
                    Log.d("UserDetail number",""+number);
                    String password = cursor.getString(cursor.getColumnIndex("user_password"));
                    Log.d("UserDetail password",""+password);
                    String name = cursor.getString(cursor.getColumnIndex("user_name"));
                    Log.d("UserDetail name",""+name);
                    String sex = cursor.getString(cursor.getColumnIndex("user_sex"));
                    Log.d("UserDetail sex",sex);
                    int img_id = cursor.getInt(cursor.getColumnIndex("user_img_id"));
                    Log.d("UserDetail img_id",""+img_id);
                    user = new User(id,number,password,name,sex,img_id);
                }while (cursor.moveToNext());
            }
            //修改用户信息内容
            user_detail_img.setImageResource(user.getUser_img_id());
            user_detail_name.setText(user.getUser_name());
            user_detail_button.setText("退出");
            //修改用户发布的游记ListView
            initStoryBrief();
        }
    }
}
