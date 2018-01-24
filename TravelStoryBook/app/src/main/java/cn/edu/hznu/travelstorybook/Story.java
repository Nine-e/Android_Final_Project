package cn.edu.hznu.travelstorybook;

/**
 * Created by del on 2017/12/13.
 */

public class Story {
    private int story_id;
    private int story_img_id;
    private String story_title;
    private String story_content;
    private String story_date;
    private String story_author;
    private int story_author_id;
    private int story_star_count;

    public int getStory_star_count() {
        return story_star_count;
    }

    public void setStory_star_count(int story_star_count) {
        this.story_star_count = story_star_count;
    }

    public Story(int story_id, int story_img_id, String story_title, String story_content, String story_date, String story_author, int story_author_id, int story_star_count) {
        this.story_id = story_id;
        this.story_img_id = story_img_id;
        this.story_title = story_title;
        this.story_content = story_content;
        this.story_date = story_date;
        this.story_author = story_author;
        this.story_author_id = story_author_id;
        this.story_star_count = story_star_count;
    }

    public int getStory_id() {
        return story_id;
    }

    public int getStory_img_id() {
        return story_img_id;
    }

    public String getStory_title() {
        return story_title;
    }

    public String getStory_content() {
        return story_content;
    }

    public String getStory_date() {
        return story_date;
    }

    public String getStory_author() {
        return story_author;
    }

    public int getStory_author_id() {
        return story_author_id;
    }

}
