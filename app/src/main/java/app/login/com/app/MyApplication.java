package app.login.com.app;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import app.login.com.activity.LoginActivity;
import app.login.com.helper.MyPreferenceManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by erdinc on 2/17/16.
 */
public class MyApplication extends Application {

    public static final String TAG=MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private MyPreferenceManager preferenceManager;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(){
        super.onCreate();
        mInstance=this;
    }

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return  requestQueue;
    }

    public MyPreferenceManager getPreferenceManager(){
        if(preferenceManager==null){
            preferenceManager=new MyPreferenceManager(this);
        }
        return  preferenceManager;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    public void logout() {
        preferenceManager.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
