package app.login.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import app.login.com.R;
import app.login.com.app.EndPoints;
import app.login.com.app.MyApplication;
import app.login.com.model.User;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import app.login.com.app.MyApplication;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erdinc on 4/21/16.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    private EditText edtEmail, edtPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyApplication.getInstance().getPreferenceManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        setContentView(R.layout.activity_login);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutLogin);
        layoutEmail = (TextInputLayout) findViewById(R.id.layoutEdtEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.layoutEdtPassword);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        edtEmail.addTextChangedListener(new MyTextWatcher(edtEmail));
        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void login() {
        if (!validateEmail() || !validatePassword()) return;

        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response:" + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        JSONObject userObj = obj.getJSONObject("user");
                        User user = new User(userObj.getString("user_id"), userObj.getString("name"),
                                userObj.getString("email"));
                        Log.d(TAG, "Login success");
                        MyApplication.getInstance().getPreferenceManager().storeUser(user);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(coordinatorLayout, "Login failed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
               params.put("name",password);
                params.put("email", email);
             //   params.put("password", password);
                Log.e(TAG, "params:" + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edtEmail:
                    validateEmail();
                    break;
                case R.id.edtPassword:
                    validatePassword();
                    break;
            }


        }
    }

    private boolean validatePassword() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            layoutPassword.setError(getString(R.string.err_valid_email));
            requestFocus(edtPassword);
            return false;
        } else {
            layoutPassword.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = edtEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            layoutEmail.setError(getString(R.string.err_valid_email));
            requestFocus(edtEmail);
            return false;
        } else {
            layoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}





















