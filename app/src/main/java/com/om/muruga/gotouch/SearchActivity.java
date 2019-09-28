package com.om.muruga.gotouch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.om.muruga.gotouch.Constants.URL;

public class SearchActivity extends AppCompatActivity {
    TextView txt_name, et_contact, et_mobile_model, et_imei, label1, label2, label3,nosearch;
    LinearLayout view1, view2, view3, searchContent;
    EditText et_search;
    ImageButton btn_search;
    String search_text;
    String stringToConvert="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAEEAMMDASIAAhEBAxEB/8QAHwAAAAUFAQEAAAAAAAAAAAAAAAIDBgcBBAUICQoL/8QAQRAAAQQBAwIFAwIFAwIEBAcAAQIDBBEFBhIhADEHEyJBUQgUMhVhCSNCcYEWUpGhsSQlcsEzYoLxChcmorLR4f/EABwBAAEF AQEBAAAAAAAAAAAAAAEAAgQFBgMHCP/EAD8RAAEDAgUCAwYEBAUDBQEAAAECAxEEIQAFEjFBUWEG E3EigZGhsfAUMtHhFSNCwQcWUmLxM5KyJDRTcsJz/9oADAMBAAIRAxEAPwDyoZ8Y3K4R9IzM11TUdT8ONNbhZNCFpKghEd3bCyjavNoIfkGWkEvoX5hWsLgWNKmPrTDnZ2U8ww1vgsKyL70aIlJethLElS2WV+Wpay2gIKXVJ8rg2l15Uy4DyWJYb9JQW3UPhxpwLTYt1KGVpIKkqKlMJQOHEoUV7Ut2AwyuNKfWy0ovvqUEublKKUJDaglsh0g8Jp4ElSioCkAqPndLDLDgJ8xKtJSqEmFEpAMn19o72IMzj2yobL1U0tADakhQckqTKEgkiUwreyQQReQJsGVmytc/a4sOONhIU46UqUtoXfDaC3+SgkqUhqiGz2JUk+HbP3KEFTqVglsK277ICnFFdKK9yVqUAAU7UqVsCVWo4zKKDk10IAUEldgHchYDm4hJUApQUpSEJUFErokcBBS4NPISp5FKBQAtaVqJAKSlJShY2BLVlO0lAtO8UAkl5GhSCmlTOwQCZt026bbT7uMYatcbNc+oz+cJkHfYEHkm8TYyOu0ksHZSlKWpKUFJCmitKgshO0+Yku+k7UlKkqcCWElTtEHq/RtLSdrarQXCUgrQLDiiUqCHAhe`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";
    ProgressDialog progress = null;
    ImageButton img_mob1, img_mob2, img_invoice1, img_invoice2, img_cust_prof1, img_cust_prof2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        byte[] theByteArray = stringToConvert.getBytes();
        show_dialog(theByteArray);
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        txt_name = (TextView) findViewById(R.id.txt_name);
        et_contact = (TextView) findViewById(R.id.et_contact);
        et_mobile_model = (TextView) findViewById(R.id.et_mobile_model);
        et_imei = (TextView) findViewById(R.id.et_imei);
        view1 = (LinearLayout) findViewById(R.id.view1);
        view2 = (LinearLayout) findViewById(R.id.view2);
        view3 = (LinearLayout) findViewById(R.id.view3);
        searchContent = (LinearLayout) findViewById(R.id.searchContent);
        img_mob1 = (ImageButton) findViewById(R.id.img_mob1);
        img_mob2 = (ImageButton) findViewById(R.id.img_mob2);
        img_invoice1 = (ImageButton) findViewById(R.id.img_invoice1);
        img_cust_prof1 = (ImageButton) findViewById(R.id.img_cust_prof1);
        img_invoice2 = (ImageButton) findViewById(R.id.img_invoice2);
        img_cust_prof2 = (ImageButton) findViewById(R.id.img_cust_prof2);
        searchContent = (LinearLayout) findViewById(R.id.searchContent);
        nosearch=(TextView)findViewById(R.id.nosearch);
        searchContent.setVisibility(View.GONE);
        progress = new ProgressDialog(this);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_text = et_search.getText().toString();
                new RequestTask().execute(et_search.getText().toString(),et_search.getText().toString());
            }
        });

    }
                class RequestTask extends AsyncTask<String, String, String> {
                    @Override
                    protected String doInBackground(String... strings) {
                        StringBuilder response = new StringBuilder();
                        try {
                            progress.setTitle("Loading");
                            progress.setMessage("Wait while loading...");
                            progress.setCancelable(false);

                            String url = URL + "/v1/gotouch/login/user/getData/" + search_text;
                            Log.i("url", url);
                            java.net.URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                            con.setRequestMethod("GET");
                            con.setRequestProperty("Content-Type", "application/json");
                            con.setDoOutput(true);

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
                        Log.i("result", result);
                        if (result != null) {
                            try {
                                JSONObject object = new JSONObject(result);
                                searchContent.setVisibility(View.VISIBLE);
                                nosearch.setVisibility(View.GONE);

                                txt_name.setText("");
                                et_contact.setText("");
                                et_mobile_model.setText("");
                                et_imei.setText("");
                                String stringToConvert="";


                                Log.i("obj", object.toString());
//                    String status = object.getString("status");
//                    if (status.equalsIgnoreCase("failed")) {
//                        Toast.makeText(getApplicationContext(), "Invalid user name & password", Toast.LENGTH_SHORT).show();
//                    } else if (status.equalsIgnoreCase("success")) {
//                        Intent i = new Intent(getApplicationContext(), DataActivity.class);
//                        startActivity(i);
//                    }
                                progress.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
    public void show_dialog(byte[] bytes){

        Activity activity = null;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        ListView a_listview ;

        final AlertDialog alertDialog = dialogBuilder.create();
        LayoutInflater factory = LayoutInflater.from(this);
        final View v = factory.inflate(R.layout.alert_image_server, null);
        ImageView imageview;
        imageview=(ImageView)v.findViewById(R.id.image_server);
        byte[] decodedString = Base64.decode(stringToConvert, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageview.setImageBitmap(decodedByte);
        alertDialog.setView(v);
        alertDialog.show();
        alertDialog.setCancelable(true);

    }
            }
