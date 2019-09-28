package com.om.muruga.gotouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URI;

import static com.om.muruga.gotouch.Constants.URL;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progress = null;
    EditText userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        progress = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigate(View v){
//        Intent i = new Intent(getApplicationContext(),DataActivity.class);
//        startActivity(i);
        new RequestTask().execute(userName.getText().toString(),password.getText().toString());
    }

    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response = new StringBuilder();
            try {
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false);

                String url = URL+"/v1/gotouch/login/checkLogin";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                JSONObject object = new JSONObject();
                object.put("userName",strings[0]);
                object.put("password",strings[1]);
                Log.i("Info",object.toString());
                try {
                    OutputStream os = con.getOutputStream();
                    byte[] input = object.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }catch (Exception e){

                }
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8")) ;
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
            }catch (Exception e){
                e.printStackTrace();
            }
            return response.toString();
        }

        protected void onPostExecute(String result) {
            Log.i("result",result);
            if(result != null){
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("failed")) {
                        Toast.makeText(getApplicationContext(), "Invalid user name & password", Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("success")) {
                        Intent i = new Intent(getApplicationContext(), DataActivity.class);
                        i.putExtra("userName",userName.getText().toString());
                        startActivity(i);
                    }
                    progress.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }

}


