package com.om.muruga.gotouch;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reg extends AppCompatActivity {
EditText et_imei,et_name,et_contact,et_address;
Spinner sp_distributer;
Button submit;
    String result2;
    ArrayList<String> dropdown2=new ArrayList<>();
    ArrayList<String>dropdown_result=new ArrayList<>();
String imei,name,contact,address;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        api2();
        et_imei=(EditText)findViewById(R.id.et_imei);
        et_name=(EditText)findViewById(R.id.et_name);
        et_contact=(EditText)findViewById(R.id.et_contact);
        et_address=(EditText)findViewById(R.id.et_address);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imei=et_imei.getText().toString();
                name=et_name.getText().toString();
                contact=et_contact.getText().toString();
                address=et_address.getText().toString();
                if(imei.equals("")&&name.equals("")&&contact.equals("")&&address.equals("")){
                    Toast.makeText(Reg.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> map = new LinkedHashMap<>();
                    map.put("imei", "" + imei);

                    map.put("contact_number", "" + contact);

                    map.put("name", "" +name);

                    map.put("address", "" + address);

                    map.put("distributor", "" + result2);
                    JSONObject object = new JSONObject(map);
                    Log.i("post",object.toString());
                    new RequestTask().execute(object.toString());
                }
            }
        });
        sp_distributer=(Spinner)findViewById(R.id.sp_distributor);
        getIMEI();

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
                et_imei.setText(IMEI_Number_Holder);
            }
        }
    }
    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder response = new StringBuilder();
            try {
                String url = Constants.URL+"/v1/gotouch/login/user/saveData";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                Log.i("Info", strings[0]);
                try {
                    OutputStream os = con.getOutputStream();
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                } catch (Exception e) {

                }
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                if (result.equalsIgnoreCase("Success")) {
                    Intent intent=new Intent(Reg.this,MainActivity.class);
                    startActivity(intent);

                }
            }
        }


    }
    public void api2(){
        String url="http://103.91.84.218:9006/v1/gotouch/retailer/getDistributor";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("My dhinesh", "" + response);


                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String value=jsonObject.getString("name");
                        dropdown2.add(id);
                        dropdown_result.add(value);
                        //ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,dropdown1);
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, dropdown_result);
                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        sp_distributer.setAdapter(adapter);
                        sp_distributer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                                        result2 = dropdown2.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView parent) {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Reg.this, "Please Check Connectivity :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                //search_condition




                return map;
            }
        };
        queue.add(request);


    }
}