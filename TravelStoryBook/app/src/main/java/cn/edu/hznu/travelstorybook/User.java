package cn.edu.hznu.travelstorybook;

/**
 * Created by del on 2017/12/21.
 */

public class User {

    private int user_id;
    private String user_number;
    private String user_password;
    private String user_name;
    private String user_sex;
    private int user_img_id;

    public User(int user_id, String user_number, String user_password, String user_name, String user_sex, int user_img_id) {
        this.user_id = user_id;
        this.user_number = user_number;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_sex = user_sex;
        this.user_img_id = user_img_id;
    }


    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public int getUser_img_id() {
        return user_img_id;
    }

    public void setUser_img_id(int user_img_id) {
        this.user_img_id = user_img_id;
    }




    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }




    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
