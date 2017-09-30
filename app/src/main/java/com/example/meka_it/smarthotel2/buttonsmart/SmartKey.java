package com.example.meka_it.smarthotel2.buttonsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.meka_it.smarthotel.R;
import com.example.meka_it.smarthotel2.main.HomeActivity;


public class SmartKey extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_key);

        ImageButton back = (ImageButton) findViewById(R.id.back);
        ImageButton unlock = (ImageButton) findViewById(R.id.unlock);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
        unlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SmartKeyUnLock.class);
                startActivity(i);
            }
        });


    }
}
