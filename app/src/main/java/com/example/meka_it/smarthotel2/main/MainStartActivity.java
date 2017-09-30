package com.example.meka_it.smarthotel2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meka_it.smarthotel.R;


public class MainStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main);

        Button PortalNext = (Button) findViewById(R.id.start);
        PortalNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPage = new Intent(MainStartActivity.this, HomeActivity.class);
                startActivity(nextPage);
            }
        });



    }


}
