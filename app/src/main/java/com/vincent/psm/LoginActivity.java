package com.vincent.psm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vincent.psm.network_helper.MyOkHttp;

import org.json.JSONException;
import org.json.JSONObject;

import static com.vincent.psm.data.DataHelper.KEY_ACCOUNT;
import static com.vincent.psm.data.DataHelper.KEY_PASSWORD;
import static com.vincent.psm.data.DataHelper.KEY_USER_INFO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText edtAcc, edtPwd;
    private Button btnLogin;
    private ProgressBar prgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        edtAcc = findViewById(R.id.edtAccount);
        edtPwd = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        prgBar = findViewById(R.id.prgBar);

        btnLogin.setOnClickListener(this);
    }

    private void login(String account, String password) {
        MyOkHttp conn = new MyOkHttp(LoginActivity.this, new MyOkHttp.TaskListener() {
            @Override
            public void onFinished(final String result) {
                btnLogin.setVisibility(View.VISIBLE);
                prgBar.setVisibility(View.INVISIBLE);
                Intent it  = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_USER_INFO, result);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        try {
            JSONObject reqObj = new JSONObject();
            reqObj.put(KEY_ACCOUNT, account);
            reqObj.put(KEY_PASSWORD, password);
            conn.execute(getString(R.string.link_login), reqObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                btnLogin.setVisibility(View.INVISIBLE);
                prgBar.setVisibility(View.VISIBLE);
                login(edtAcc.getText().toString(), edtPwd.getText().toString());
                break;
        }
    }
}
;