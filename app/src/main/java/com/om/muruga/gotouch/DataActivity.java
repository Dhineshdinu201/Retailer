package com.om.muruga.gotouch;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {
    ImageButton button,mobile2Btn,invoiceBtn1,invoiceBtn2,proofBtn1,proofBtn2;
    int TAKE_PHOTO_CODE = 0;
    String userName="";
    Button btn_search;
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static final String REVISION_NO_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    boolean mobile1 = false, mobile2 = false, invoice1 = false, invoice2 = false, custProof1 = false, custProof2 = false;
    String mobile1Str = "", mobile2Str = "", invoice1Str = "", invoice2Str = "", custProof1Str = "", custProof2Str = "";
    EditText custName, contactNo, number, imei, mobileModel,et_price,et_activation;
    TextView pack;
    String pack_selected;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        et_price=(EditText)findViewById(R.id.et_price);

        et_activation=(EditText)findViewById(R.id.et_activation);
        pack=(TextView)findViewById(R.id.pack);

        et_price.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                try {
                    price=Integer.parseInt(et_price.getText().toString());
                    if(price!=0) {
                        if (price <= 10000 && price >= 4000) {
                            pack.setText("1299");
                            pack_selected = "1299";
                        } else if (price > 10000 && price <= 25000) {
                            pack.setText("1999");
                            pack_selected = "1999";
                        }
                        else {
                            pack.setText("No pack available");
                        }
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(DataActivity.this, "Please insert the price", Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        btn_search=(Button)findViewById(R.id.search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!checkPermission()) {
            requestPermission();
        }
        try {
            userName = getIntent().getStringExtra("UserName");
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/goTouch/";
        File newdir = new File(dir);
        newdir.mkdirs();

        custName = (EditText) findViewById(R.id.et_name);
        contactNo = (EditText) findViewById(R.id.et_contact);
        mobileModel = (EditText) findViewById(R.id.et_mobile_model);
        imei = (EditText) findViewById(R.id.et_imei);

        /**MOBILE**/
        button = (ImageButton) findViewById(R.id.img_mob1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mobile1 = true;

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });


        mobile2Btn = (ImageButton) findViewById(R.id.img_mob2);
        mobile2Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mobile2 = true;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
        /**MOBILE**/

        /**INVOICE**/
        invoiceBtn1 = (ImageButton) findViewById(R.id.img_invoice1);
        invoiceBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                invoice1 = true;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });


        invoiceBtn2 = (ImageButton) findViewById(R.id.img_invoice2);
        invoiceBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                invoice2 = true;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
        /**INVOICE**/

        /**CUST PROOF**/

        proofBtn1 = (ImageButton) findViewById(R.id.img_cust_prof1);
        proofBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                custProof1 = true;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });


        proofBtn2 = (ImageButton) findViewById(R.id.img_cust_prof2);
        proofBtn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                custProof2 = true;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("type", "mobile1");
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
        /**CUST PROO**/


        /**SUBMIT EVENT*/

        Button submitBtn = (Button) findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(custName.getText().toString().isEmpty()&&contactNo.getText().toString().isEmpty()&&mobileModel.getText().toString().isEmpty()&&imei.getText().toString().isEmpty()){

                }
                if (validate(custName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(contactNo.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Contact No", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(mobileModel.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile Model", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(imei.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter IMEI", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(mobile1Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Mobile photo 1", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(mobile2Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Mobile photo 2", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(invoice1Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Invoice copy 1", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(invoice2Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Invoice copy 2", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(custProof1Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Customer proof 1", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validate(custProof2Str)) {
                    Toast.makeText(getApplicationContext(), "Please Capture Customer proof 2", Toast.LENGTH_SHORT).show();
                    return;
                }

                Data data = new Data();
                data.setName(custName.getText().toString());
                data.setContact_number(contactNo.getText().toString());
                data.setMobile_model(mobileModel.getText().toString());
                data.setImei(imei.getText().toString());
                data.setImg_mob1(mobile1Str);
                data.setImg_mob2(mobile2Str);
                data.setImg_invoice1(invoice1Str);
                data.setImg_invoice2(invoice2Str);
                data.setImg_cust_prof1(custProof1Str);
                data.setImg_cust_prof2(custProof2Str);


                Map<String, String> map = new LinkedHashMap<>();
                map.put("name", "" + custName.getText().toString());

                map.put("contact_number", "" + contactNo.getText().toString());

                map.put("mobile_model", "" + mobileModel.getText().toString());

                map.put("imei", "" + imei.getText().toString());

                map.put("activationCode", "" + et_activation.getText().toString());

                map.put("price", "" + et_price.getText().toString());

                map.put("pack", "" + pack_selected);

                map.put("img_mob1", "" + mobile1Str);

                map.put("img_mob2", "" + mobile2Str);

                map.put("img_invoice1", "" + invoice1Str);

                map.put("img_invoice2", "" + invoice2Str);

                map.put("img_cust_prof1", "" + custProof1Str);

                map.put("img_cust_prof2", "" + custProof2Str);

                map.put("userName",userName);
                JSONObject object = new JSONObject(map);

                new RequestTask().execute(object.toString());
            }
        });

        /**SUBMIT EVENT*/
    }

    private boolean validate(String val) {
        if (!"".equals(val)) {
            return false;
        }
        return true;
    }


    public static String getRevisionNumber() {
        DateFormat dateFormat = new SimpleDateFormat(REVISION_NO_DATE_TIME_FORMAT);
        String date = dateFormat.format(new Date());
        return Long.valueOf(date).toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                if (mobile1) {

                    mobile1Str = encodeImage(imageBitmap);
                    Log.i("b",mobile1Str);
                    button.setImageResource(R.drawable.correct);
                    button.setBackgroundColor(Color.TRANSPARENT);
                } else if (mobile2) {
                    mobile2Str = encodeImage(imageBitmap);
                    mobile2Btn.setImageResource(R.drawable.correct);
                    mobile2Btn.setBackgroundColor(Color.TRANSPARENT);
                } else if (invoice1) {
                    invoice1Str = encodeImage(imageBitmap);
                    invoiceBtn1.setImageResource(R.drawable.correct);
                    invoiceBtn1.setBackgroundColor(Color.TRANSPARENT);
                } else if (invoice2) {
                    invoice2Str = encodeImage(imageBitmap);
                    invoiceBtn2.setImageResource(R.drawable.correct);
                    invoiceBtn2.setBackgroundColor(Color.TRANSPARENT);
                } else if (custProof1) {
                    custProof1Str = encodeImage(imageBitmap);
                    proofBtn1.setImageResource(R.drawable.correct);
                    proofBtn1.setBackgroundColor(Color.TRANSPARENT);
                } else if (custProof2) {
                    custProof2Str = encodeImage(imageBitmap);
                    proofBtn2.setImageResource(R.drawable.correct);
                    proofBtn2.setBackgroundColor(Color.TRANSPARENT);
                }
                mobile1 = false;
                mobile2 = false;
                invoice1 = false;
                invoice2 = false;
                custProof1 = false;
                custProof2 = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void revIcon(){
        button.setImageResource(R.drawable.cam);
        button.setBackgroundResource(R.color.darkergray);
        mobile2Btn.setImageResource(R.drawable.cam);
        mobile2Btn.setBackgroundResource(R.color.darkergray);
        invoiceBtn1.setImageResource(R.drawable.cam);
        invoiceBtn1.setBackgroundResource(R.color.darkergray);
        invoiceBtn2.setImageResource(R.drawable.cam);
        invoiceBtn2.setBackgroundResource(R.color.darkergray);
        proofBtn1.setImageResource(R.drawable.cam);
        proofBtn1.setBackgroundResource(R.color.darkergray);
        proofBtn2.setImageResource(R.drawable.cam);
        proofBtn2.setBackgroundResource(R.color.darkergray);
    }
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
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
                if (result.equalsIgnoreCase("success")) {
                    custName.setText("");
                    contactNo.setText("");
                    imei.setText("");
                    mobileModel.setText("");
                    mobile1Str = "";
                    mobile2Str = "";
                    invoice1Str = "";
                    invoice2Str = "";
                    custProof1Str = "";
                    custProof2Str = "";
                    revIcon();
                }
            }
        }


    }



}
