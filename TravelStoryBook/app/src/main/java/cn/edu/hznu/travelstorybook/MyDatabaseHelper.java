package cn.edu.hznu.travelstorybook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by del on 2017/12/21.
 */

public class MyDatabaseHelper  extends SQLiteOpenHelper{
    public static final String CREATE_STORY = "create table Story ("
            +"story_id integer primary key autoincrement, "
            +"story_title text, "
            +"story_content text, "
            +"story_img_id integer, "
            +"story_date text, "
            +"story_author_id integer, "
            +"story_author_name text, "
            +"story_star_count integer)";
    public static final String CREATE_USER = "create table User ("
            +"user_id integer primary key autoincrement, "
            +"user_number text, "
            +"user_password text, "
            +"user_name text, "
            +"user_sex text, "
            +"user_img_id integer)";
    public static final String CREATE_STAR = "create table Star ("
            +"star_id integer primary key autoincrement, "
            +"story_id integer, "
            +"user_id integer)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORY);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_STAR);
        Log.d("Database","create success");
        //插入初始数据
        addStory(db);
        addUser(db);
        //addStar(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void addStory(SQLiteDatabase db){

        ContentValues values = new ContentValues();
        //第一条数据
        values.put("story_title","印度南部｜山海之间的另一个印度");
        values.put("story_content","印度南部，刚踏上这片土地，你就会感受到这里与北印度的不同：这里有诸多干净、安静的海滨小城，充满欧洲风情；这里的人们普遍身材矮小、头发卷曲，以素食为主，使用不同于北方印地语的源生泰米尔语；漫长的海岸线上点缀着椰林沙滩，至今仍是嬉皮士的乐园……\n" +
                "如果说北方印度文明是由恒河孕育的，那么印度南部的历史，就是由海洋和山地共同书写的。这是与你印象中大不一样的“另一个印度”，快来看看有哪些好玩的地方吧。\n" +
                "印度南部坐拥漫长的海岸线，是印度沟通世界的窗口，东西文明在此交汇，留下了诸多充满异国情调的海滨小城。\n" +
                "本地治里（Pondicherry）曾经是印度最大的法国殖民地，今天仍然以遗留的法国风情而闻名：海滨区的街道不叫Street，而是叫Rue（法语：街道），各种店名也都沿用了法语的称呼：Le Space、Café des Arts等。漫步其间，一栋栋黄色的法式殖民地建筑和探出墙外的花朵都流露着法式的浪漫。这里你可以吃到地道的法餐，体验殖民地贵族的生活。\n");
        values.put("story_img_id",R.drawable.story_img_1);
        values.put("story_date","2017-01-01");
        values.put("story_author_id",1);
        values.put("story_author_name","游记编辑部");
        values.put("story_star_count",0);
        db.insert("Story",null,values);
        values.clear();
        //第二条数据
        values.put("story_title","辟谣｜听说西澳大利亚的粉红湖消失了？");
        values.put("story_content","这几天，西澳埃斯佩兰斯周边的网红景点——粉红湖（Pink Lake）不再粉红一片的消息让很多旅行者十分着急。可同时也有很多人说粉红湖明明还在，并没有消失或变色。为了弄清楚西澳的粉红湖到底怎么了，我们也联系锦囊的在地作者做了一些调查。\n" +
                "结果发现，澳大利亚媒体报道中提到的这个名为“Pink Lake”的粉湖确实变白了，但是这对旅行者来说影响并不大，因为大部分人去的著名粉红湖，并不是这个。\n" +
                "粉色的盐湖在西澳大利亚并不少见，但只有一个湖泊真正被称为“粉湖”——Pink Lake。虽然拥有“粉湖”的名称，但其实这里早就已经名不符实。\n" +
                "这几年湖中可以产生“粉色颜料”——β胡萝卜素的藻类浓度早已不高，其粉色远不如周边的沃登湖（Lake Warden）与希利尔湖（Lake Hillier）明显。只有在气温高、光照强烈的条件下，粉红湖才会呈现出粉色，而一年中的大部分时间内，这个湖泊都成水晶般的白色。如今由于藻类的进一步减少，这个澳大利亚唯一被命名为“粉湖”的地方终于彻底失去了粉色。\n");
        values.put("story_img_id",R.drawable.story_img_2);
        values.put("story_date","2017-01-02");
        values.put("story_author_id",1);
        values.put("story_author_name","游记编辑部");
        values.put("story_star_count",0);
        db.insert("Story",null,values);
        values.clear();
        //第三条数据
        values.put("story_title","去柬埔寨旅行安全吗？");
        values.put("story_content","被誉为东南亚三大佛教建筑奇迹之一的吴哥窟在被重新发现之时曾以“高棉的微笑”震惊世界。如今，每年都有数百万旅行者慕名前往暹粒一睹吴哥窟的风采，旅游业因而成为了柬埔寨最重要的经济支撑。\n" +
                "然而，在这样一个旅游业异常成熟发达的地方，安全隐患依然广泛存在。 为此我们列出下列注意事项，希望大家能够对柬埔寨当地情况有基本的了解，做到安全而负责任地旅行。\n" +
                "为了帮助大家有效解决前往柬埔寨期间有可能遇到的卫生、财物、健康问题，我们建议在收拾行李时带上以下物品：\n" +
                "小面额的美金\n" +
                "于柬埔寨境内美元以及当地货币可以通用，而大部分的小商店都不接受20美金及以上面额的钞票，因此小面额的美金在购买饮料、日用品以及乘坐tuktuk时使用更方便。\n" +
                "足够的换洗衣服\n" +
                "由于旱季的柬埔寨非常炎热，在外游览时会出很多汗，几乎每天要换一件上衣，所以衣服要带足。 长袖的薄外套、长裤、凉鞋、防晒、帽子、雨伞。长袖的薄外套可以用来防晒防风；穿长裤除了可以防蚊虫叮咬以外，上下攀爬寺庙时也比较方便。\n");
        values.put("story_img_id",R.drawable.story_img_3);
        values.put("story_date","2017-02-02");
        values.put("story_author_id",1);
        values.put("story_author_name","游记编辑部");
        values.put("story_star_count",0);
        db.insert("Story",null,values);
        values.clear();
        //第四条数据
        values.put("story_title","日本东北48秘境温泉");
        values.put("story_content","要说泡温泉，众所周知日本温泉大概是北海道登别地狱谷、兵库县的有马温泉，或是群马县的草津温泉，其实日本东北这个还没被过度开发的地区也有很多非常优质的温泉，这些温泉不仅泉质好，周边风景也好，春天可以赏樱花、秋季看遍山红叶，冬季更是可以伴着飘雪泡温泉。\n" +
                "下面介绍一下日本东北的48个秘境温泉，温泉各有特点、排名不分先后，如果有计划前往日本东北旅行，不妨收好，以备不时之需。\n" +
                "青森\n" +
                "药研温泉 藥研溫泉\n" +
                "药研温泉位于恐山北麓，从被茂密的枫树、橡胶树等阔叶树覆盖的大畑川的溪谷中涌出，是疗养温泉。在这里夏季可以在溪流边垂钓，秋季可欣赏遍山红叶，游客络绎不绝。在2公里的上流还有奥药研温泉，这里有“河童温泉”、“夫妇河童温泉”、“秘境河童温泉”三处露天温泉，关于河童温泉还有一段传说，相传恐山的开山始祖，円仁大师曾被河童指点，在温泉中养好了重伤。\n" +
                "十和田湖畔温泉 十和田湖畔温泉\n" +
                "青森县的旧十和田町与秋田县的小坂町共同钻探，终于在平成15年（2003）年开凿出的新泉。温泉周围有十和田湖等观光景点，奥入濑溪流、八甲田山也是极佳的自驾线路。春季新绿与秋季红叶时期人流不断，从湖畔矗立的高村光太郎所作雕像“少女”处眺望景色，宏伟壮观，令人嗟叹。\n");
        values.put("story_img_id",R.drawable.story_img_4);
        values.put("story_date","2017-06-02");
        values.put("story_author_id",1);
        values.put("story_author_name","游记编辑部");
        values.put("story_star_count",0);
        db.insert("Story",null,values);
        values.clear();

        //第五条数据
        values.put("story_title","这位一百多年前的大师最爱也是它");
        values.put("story_content","每到年末，全球色彩权威潘通（Pantone）都会公布下一年的年度颜色。不论你喜不喜欢，这个颜色都会成为下一年时尚界、设计界挥之不去的色彩，占据整整一年的头条。\n" +
                "2018 年，潘通公布的年度颜色是紫外光色（Ultra Violet），色号为18-3838。消息一出，时尚界哀鸿遍野：要把紫色穿得好看，真非凡人所能驾驭。\n" +
                "人人皆知莫奈是印象派的创始人之一，甚至都画派以他的作品《日出•印象》命名。但少有人他很少在作品中使用黑色，其作品中的深色阴影几乎都用紫色替代。加上花朵、雪景等等，紫色在莫奈的画笔下转化成一曲曲组歌。\n" +
                "在其他早期画家早亡或改变绘画技法时，莫奈是唯一一个终身坚持实践、拓展印象派光影技法和理论的艺术家。他和他的画派改变了传统有明确阴影变化和轮廓线的画法，可以说是当之无愧的色彩大师。今天我们就来看一看，莫奈笔下的紫色是什么样的。\n" +
                "自1883年起，莫奈定居Giverny。“干草堆系列”主要完成于1890-1891年间，表现了莫奈花园附近田地里的干草堆在一天不同时刻、一年不同季节、在不同光影作用下的不同的色彩状况。\n" +
                "这一系列作品分层清晰，画面上一半均分成天空和树林、远山，下一半是地面，干草堆占据画面中心位置，打在地上的阴影也成了光影变化的重要体现。\n" +
                "每一层的景物在光影和彼此的作用下呈现出不同的色彩。苏格兰国家美术馆收藏了多幅冬季雪景下的干草堆画作，在这一系列作品中莫奈对不同紫色的运用发挥到了极致。\n");
        values.put("story_img_id",R.drawable.story_img_5);
        values.put("story_date","2017-12-10");
        values.put("story_author_id",1);
        values.put("story_author_name","游记编辑部");
        values.put("story_star_count",0);
        db.insert("Story",null,values);

        Log.d("Database","add story success");
    }
    private void addUser(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        //第一条数据
        values.put("user_number","13588220684");
        values.put("user_password","123456");
        values.put("user_name","游记编辑部");
        values.put("user_sex","unknow");
        values.put("user_img_id",R.drawable.user_unknow);
        db.insert("User",null,values);
        Log.d("Database","add user success");
    }
    private void addStar(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        //第一条数据
        values.put("story_id","1");
        values.put("user_id","1");
        db.insert("Star",null,values);
        values.clear();
        //第二条数据
        /*values.put("story_id","2");
        values.put("user_id","1");
        db.insert("Star",null,values);*/
        Log.d("Database","add star success");
    }

}
