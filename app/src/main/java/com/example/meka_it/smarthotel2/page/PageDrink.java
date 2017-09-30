package com.example.meka_it.smarthotel2.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.meka_it.smarthotel.R;

public class PageDrink extends AppCompatActivity {

    ListView mListView;

    String[] foodname = {"ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 15 บาท", "ราคา 20 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 35 บาท"};
    String[] foodtitle = {"น้ำดื่มเนสท์เล่", "น้ำดื่มเคริสคัล", "น้ำทิพย์", "น้ำดื่มสิงห์", "น้ำดื่มเมิเนเล่", "อิชิตันกรีนที", "โออิชิกรีนที", "Pepsi", "EST", "น้ำผลไม้ปั่น"};
    int[] foodpic = {
            R.drawable.pic15,
            R.drawable.pic16,
            R.drawable.pic17,
            R.drawable.pic18,
            R.drawable.pic19,
            R.drawable.pic20,
            R.drawable.pic21,
            R.drawable.pic22,
            R.drawable.pic23,
            R.drawable.pic24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_drink);



        mListView = (ListView) findViewById(R.id.listview);

        MyAdapter myAdapter = new MyAdapter(PageDrink.this, foodtitle, foodname, foodpic);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0
                    , View arg1, int arg2, long arg3) {
                Intent intent;
                switch(arg2) {
                    case 0 :
                        intent = new Intent(getApplicationContext()
                                , PageWater1.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(getApplicationContext()
                                , PageWater2.class);
                        startActivity(intent);
                        break;
                    case 2 :
                        intent = new Intent(getApplicationContext()
                                , PageWater3.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        intent = new Intent(getApplicationContext()
                                , PageWater4.class);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(getApplicationContext()
                                , PageWater5.class);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(getApplicationContext()
                                , PageWater6.class);
                        startActivity(intent);
                        break;
                    case 6 :
                        intent = new Intent(getApplicationContext()
                                , PageWater7.class);
                        startActivity(intent);
                        break;
                    case 7 :
                        intent = new Intent(getApplicationContext()
                                , PageWater8.class);
                        startActivity(intent);
                        break;
                    case 8 :
                        intent = new Intent(getApplicationContext()
                                , PageWater9.class);
                        startActivity(intent);
                        break;
                    case 9 :
                        intent = new Intent(getApplicationContext()
                                , PageWater10.class);
                        startActivity(intent);
                        break;
                }
            }
        });



    }
}
