package cn.edu.hznu.travelstorybook;

import android.content.ContentValues;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Story> storyBriefList = new ArrayList<>();
    //private String[] data = {"Apple","bananana"};
    private MyDatabaseHelper dbHelper;
    private String get_num;
    private StoryBriefAdapter adapter;
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

        setContentView(R.layout.activity_main);

        adapter = new StoryBriefAdapter(MainActivity.this,R.layout.story_brief_item,storyBriefList);
        listView = (ListView)findViewById(R.id.main_list_view);

        //创建数据库
        dbHelper = new MyDatabaseHelper(this,"StoryBook.db",null,1);
        dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        get_num = intent.getStringExtra("user_num");

        //deleteData();
        //addData();

        //搜索按钮点击事件
        ImageButton searchBtn = (ImageButton)findViewById(R.id.main_search_button);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });

        //‘我的’按钮点击事件
        ImageButton mineBtn = (ImageButton)findViewById(R.id.main_mine_button);
        mineBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserDetailActivity.class);
                intent.putExtra("user_num",get_num);
                startActivity(intent);
            }
        });

        //初始化ListView
        //initStoryBrief();
        queryData();
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);

        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Story story = storyBriefList.get(position);
                Intent intent = new Intent(MainActivity.this,StoryDetailActivity.class);
                //传递数据
                intent.putExtra("user_num",get_num);
                intent.putExtra("story_id",story.getStory_id());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        queryData();
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }

    private void queryData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        storyBriefList.clear();
        //查询Story表中的所有数据
        Cursor cursor = db.query("Story",null,null,null,null,null,"story_star_count desc");
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
                Log.d("MainActivity Story",title);
                Log.d("MainActivity starCount",star_count+"");
                Story story = new Story(id,img_id,title,content,date,author_name,author_id,star_count);
                storyBriefList.add(story);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    private void deleteData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //删除Story表中的所有数据
        db.delete("Story",null,null);
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
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        Log.d("MainActivity","params:"+params.height);
        listView.setLayoutParams(params);
        }
}
