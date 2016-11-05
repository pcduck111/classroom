package com.example.champ.myclass;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etDisplay;
    private EditText etReUser;
    private EditText etPass;
    private EditText etPassCon;
    private Button btnReRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etDisplay = (EditText) findViewById(R.id.etDisplay);
        etReUser = (EditText) findViewById(R.id.etReUser);
        etPass = (EditText) findViewById(R.id.etPass);
        etPassCon = (EditText) findViewById(R.id.etPassCon);
        btnReRegis = (Button) findViewById(R.id.btnReRegis);
        setListener();
    }

    private void setListener() {
        btnReRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkedittext()) {

                    new Register(etReUser.getText().toString() ,
                            etPass.getText().toString() ,
                            etPassCon.getText().toString() ,
                            etDisplay.getText().toString()).execute();
                } else {
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean checkedittext() {

        String username = etReUser.getText().toString();
        String password = etPass.getText().toString();
        String passwordConfirm = etPassCon.getText().toString();
        String displayName = etDisplay.getText().toString();

        if (username.isEmpty())
            return false;

        if (password.isEmpty())
            return false;

        if (passwordConfirm.isEmpty())
            return false;

        if (!password.equals(passwordConfirm))
            return false;

        if (displayName.isEmpty())
            return false;
        else
            return true;
    }

    private class Register extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;
        private String passwordcon;
        private String displarName;

        public Register(String username, String displarName, String password, String passwordcon) {
            this.username = username;
            this.displarName = displarName;
            this.password = password;
            this.passwordcon = passwordcon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();
            Request request; //request คือส่งไป
            Response response; // rasponse คือส่งค่ากลับไป

            RequestBody requestBody = new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .add("passowrd_con",passwordcon)
                    .add("display_name",displarName)
                    .build();

            request = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requestBody)
                    .build();
            try {


                response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    return  response.body().string();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject rootObj = new JSONObject(s);

                if(rootObj.has("result")){
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    if(resultObj.getInt("result")==1){
                        Toast . makeText(RegisterActivity.this , resultObj.getString("result_desc"),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast . makeText(RegisterActivity.this , resultObj.getString("result_desc"),Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
