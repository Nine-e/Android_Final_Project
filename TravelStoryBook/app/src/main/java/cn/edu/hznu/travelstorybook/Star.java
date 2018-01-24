package cn.edu.hznu.travelstorybook;

/**
 * Created by del on 2017/12/22.
 */

public class Star {
    private int star_id;
    private int stoty_id;
    private int user_id;

    public Star(int star_id, int stoty_id, int user_id) {
        this.star_id = star_id;
        this.stoty_id = stoty_id;
        this.user_id = user_id;
    }

    public int getStar_id() {
        return star_id;
    }

    public void setStar_id(int star_id) {
        this.star_id = star_id;
    }

    public int getStoty_id() {
        return stoty_id;
    }

    public void setStoty_id(int stoty_id) {
        this.stoty_id = stoty_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}
