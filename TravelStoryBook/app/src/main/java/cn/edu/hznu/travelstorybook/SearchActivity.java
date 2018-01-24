package cn.edu.hznu.travelstorybook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Story> storyBriefList;
    private MyDatabaseHelper dbHelper;
    private StoryBriefAdapter adapter;
    private String get_num;

    private EditText searchET;
    private TextView hotTV;
    private LinearLayout hotLL;
    private ListView listView;
    private Button cancelBtn;

    private Button hotButton1;
    private Button hotButton2;
    private Button hotButton3;

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
        setContentView(R.layout.activity_search);
        //初始化结果列表
        storyBriefList = new ArrayList<>();

        //连接数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        searchET = (EditText)findViewById(R.id.search_editText);
        hotTV = (TextView)findViewById(R.id.search_hot_text);
        hotLL = (LinearLayout)findViewById(R.id.search_hot_linearLayout);
        listView = (ListView)findViewById(R.id.search_list_view);
        cancelBtn = (Button)findViewById(R.id.search_cancel_button);

        hotButton1 = (Button)findViewById(R.id.search_hot_button1);
        hotButton2 = (Button)findViewById(R.id.search_hot_button2);
        hotButton3 = (Button)findViewById(R.id.search_hot_button3);

        adapter = new StoryBriefAdapter(SearchActivity.this,R.layout.story_brief_item,storyBriefList);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");

        //热门搜索按钮点击事件
        hotButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = hotButton1.getText().toString();
                searchET.setText(text);
                //隐藏热门搜索
                hotTV.setVisibility(View.GONE);
                hotLL.setVisibility(View.GONE);
                //数据库查询
                storyBriefList.clear();
                initStoryBrief(text);
                //显示结果
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                Log.d("seatchActivity list","listView visible");
            }
        });
        hotButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = hotButton2.getText().toString();
                searchET.setText(text);
                //隐藏热门搜索
                hotTV.setVisibility(View.GONE);
                hotLL.setVisibility(View.GONE);
                //数据库查询
                storyBriefList.clear();
                initStoryBrief(text);
                //显示结果
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                Log.d("seatchActivity list","listView visible");
            }
        });
        hotButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String text = hotButton3.getText().toString();
                searchET.setText(text);
                //隐藏热门搜索
                hotTV.setVisibility(View.GONE);
                hotLL.setVisibility(View.GONE);
                //数据库查询
                storyBriefList.clear();
                initStoryBrief(text);
                //显示结果
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                Log.d("seatchActivity list","listView visible");
            }
        });

        //设置“取消”按钮点击事件
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this,MainActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });

        //设置输入框“回车键”监听事件
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //隐藏热门搜索
                    hotTV.setVisibility(View.GONE);
                    hotLL.setVisibility(View.GONE);
                    //数据库查询
                    //storyBriefList = new ArrayList<>();
                    storyBriefList.clear();
                    //Story story = new Story(999,R.drawable.story_img,"test","test","test","test",999);
                   // storyBriefList.add(story);
                    String key = searchET.getText().toString();
                    initStoryBrief(key);
                    //显示结果
                    //StoryBriefAdapter adapter = new StoryBriefAdapter(SearchActivity.this,R.layout.story_brief_item,storyBriefList);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                    Log.d("seatchActivity list","listView visible");
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }
                    return true;
                }
                return false;
            }
        });

        //String key = searchET.getText().toString();
        //initStoryBrief(key);

        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Story story = storyBriefList.get(position);
                Intent intent = new Intent(SearchActivity.this,StoryDetailActivity.class);
                //传递数据
                intent.putExtra("user_num",get_num);
                intent.putExtra("story_id",story.getStory_id());
                startActivity(intent);
            }
        });

    }
    private void initStoryBrief(String key){
        /*for (int i=0;i<5;i++){
            Story story = new Story(i,R.drawable.story_img,
                    "年末 | 12月活动推荐",
                    "2017年的最后一个月，哪些演出值得一看，哪些展览不容错过？",
                    "2017-12-12","游记编辑部",10+i);
            Log.d("MainActivity",story.getStory_author());
            storyBriefList.add(story);
        }*/
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询Story表中标题含有关键字的所有数据
        Cursor cursor = db.query("Story",null,"story_title like ?",new String[]{"%"+key+"%"},null,null,"story_id desc");
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
}
