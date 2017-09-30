package com.example.meka_it.smarthotel2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meka_it.smarthotel.R;
import com.example.meka_it.smarthotel2.main.HomeActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        Button PortalHome = (Button) findViewById(R.id.btnRegister);
        PortalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPage = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(nextPage);
            }
        });
    }
}
