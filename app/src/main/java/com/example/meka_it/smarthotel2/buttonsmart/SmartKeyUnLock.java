package com.example.meka_it.smarthotel2.buttonsmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.meka_it.smarthotel.R;
import com.example.meka_it.smarthotel2.main.HomeActivity;


public class SmartKeyUnLock extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_keyunlock);

        ImageButton back = (ImageButton) findViewById(R.id.back);
        ImageButton lock = (ImageButton) findViewById(R.id.lock);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
        lock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SmartKey.class);
                startActivity(i);
            }
        });

    }
}
