package app.login.com.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import app.login.com.model.User;

/**
 * Created by erdinc on 4/21/16.
 */
public class MyPreferenceManager {

    public static final String TAG = MyPreferenceManager.class.getSimpleName();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "login";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    public MyPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public User getUser() {
        if (sharedPreferences.getString(KEY_USER_ID, null) != null) {
            String id, name, email;
            id = sharedPreferences.getString(KEY_USER_ID, null);
            name = sharedPreferences.getString(KEY_USER_NAME, null);
            email = sharedPreferences.getString(KEY_USER_EMAIL, null);

            User user = new User(id, name, email);
            return user;
        }
        return null;
    }

    public void storeUser(User user){
        editor.putString(KEY_USER_ID,user.getId());
        editor.putString(KEY_USER_NAME,user.getName());
        editor.putString(KEY_USER_EMAIL,user.getEmail());
        editor.commit();
        Log.e(TAG,"User is stored:"+user.getName()+" ,email:"+user.getEmail());
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }


}


















