package com.om.muruga.gotouch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Reg extends AppCompatActivity {
EditText et_imei,et_name,et_contact,et_address;
Spinner sp_distributer;
Button submit;
String imei,name,contact,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
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
                    Toast.makeText(Reg.this, imei + name + contact + address, Toast.LENGTH_SHORT).show();
                }
            }
        });
        sp_distributer=(Spinner)findViewById(R.id.sp_distributor);
        String[]dept={"Alpha","Beta","Cupcake","Donut","Eclair"};
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,dept);
        sp_distributer.setAdapter(adapter);
        sp_distributer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Toast.makeText(Reg.this, "Alpha", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Reg.this, "Beta", Toast.LENGTH_SHORT).show();

                        break;
                    case 2:
                        Toast.makeText(Reg.this, "Cupcake", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Reg.this, "Donut", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(Reg.this, "Eclair", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

    }
}