package com.om.muruga.gotouch;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.om.muruga.gotouch.Constants.URL;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progress = null;
    EditText userName,password;
    String GET_URL;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
//        Intent intent=new Intent(MainActivity.this,DataActivity.class);
//        startActivity(intent);

        userName = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        progress = new ProgressDialog(this);
        //showalert();
        getIMEI();
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
    }
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public void getIMEI(){
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                Toast.makeText(this, "allow app to read phone state", Toast.LENGTH_SHORT).show();
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            else {
                IMEI_Number_Holder = telephonyManager.getDeviceId();
                Log.i("IMEI",IMEI_Number_Holder);
                Checkeligibility();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void Checkeligibility() {
        GET_URL =  URL+"/v1/gotouch/retailer/checkRetailerExists/"+IMEI_Number_Holder;
        Log.i("url",GET_URL);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("My success", "" + response);


                try {
                    //JSONObject jsonObject=new JSONObject(response);
                    //will receive id when the register is success
                    if(response.equals("Success")){
                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    }
                    else if(response.equals("Failure")){
                        Intent intent=new Intent(MainActivity.this,Reg.class);
                        startActivity(intent);
                    }
                    //************parsing response object**********
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Please check connectivity", Toast.LENGTH_SHORT).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //send your params here

                return map;
            }
        };
        queue.add(request);

    }
    public void api() {
        GET_URL =  URL+"/v1/gotouch/retailer/getSettings";
        Log.i("url",GET_URL);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("My success", "" + response);


                try {
                    //will receive id when the register is success
                    JSONArray js = new JSONArray(response);
                    Type type = new TypeToken<List<Settings>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<Settings> locList = gson.fromJson(response, type);
                    boolean isUpdateAvail=false;
                    String url = "";
                    for(Settings setting : locList){
                        if(setting.getName().equalsIgnoreCase(Constants.APP_VERSION)){
                            if(Constants.VERSION.intValue()<(Integer.parseInt(setting.getValue()))){
                                isUpdateAvail = true;
                            }
                        }else if(setting.getName().equalsIgnoreCase(Constants.APP_VERSION)){
                            url = setting.getValue();
                        }
                    }

                    if(isUpdateAvail){
                        showalert(url);
                    }

                    //************parsing response object**********
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Please check connectivity", Toast.LENGTH_SHORT).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //send your params here

                return map;
            }
        };
        queue.add(request);

    }
    public void showalert(String url){
        Activity activity = null;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Button close;

        final AlertDialog alertDialog = dialogBuilder.create();
        LayoutInflater factory = LayoutInflater.from(this);
        final View vi = factory.inflate(R.layout.alert_update, null);
        String value = "<html><a href=\""+url+"\">Click here to install</a></html>";
        TextView text = (TextView)vi.findViewById(R.id.text);
        text.setText(Html.fromHtml(value));
        text.setMovementMethod(LinkMovementMethod.getInstance());
        alertDialog.setView(vi);
        alertDialog.show();
        alertDialog.setCancelable(false);

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




