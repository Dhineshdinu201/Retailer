package com.om.muruga.gotouch;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    private TextView mTextMessage;
    private static final int pic_id = 123;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              /*  case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
               case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true; */
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    }

    public void openCamera(View v){
        Intent camera_intent
                = new Intent(MediaStore
                .ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);

    }

}
