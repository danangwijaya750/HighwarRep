package marno.jalan;

/**
 * Created by marno on 3/7/2017.
 */

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://58.145.168.181/fb/updfoto.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";


    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    //static String emailnya=KEY_EMAIL.toString();
    //static String pass=KEY_PASSWORD.toString();
    public static String LOGIN_URL = "http://58.145.168.181/fb/login.php";
    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";
    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


}
