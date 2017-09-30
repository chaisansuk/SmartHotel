package com.example.meka_it.smarthotel2.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.meka_it.smarthotel.R;

public class PageChef extends AppCompatActivity {

    ListView mListView;

    String[] foodname = {"ราคา 150 บาท", "ราคา 60 บาท", "ราคา 60 บาท", "ราคา 275 บาท", "ราคา 75 บาท", "ราคา 90 บาท", "ราคา 75 บาท", "ราคา 155 บาท", "ราคา 155 บาท"};
    String[] foodtitle = {"SET Breakfast", "ผัดไทย", "ส้มตำ", "SET Breakfast2", "ดับเบิ้ลแซลมอน", "กุ้งฟูต้นตำรับ", "มิกซ์บอล", "SET น้ำพริกปลาทู", "ปีกไก่อบซอสสมุนไพร"};
    int[] foodpic = {
            R.drawable.pic6,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic9,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,
            R.drawable.pic13,
            R.drawable.pic14
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_chef);


        mListView = (ListView) findViewById(R.id.listview);

        MyAdapter myAdapter = new MyAdapter(PageChef.this, foodtitle, foodname, foodpic);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0
                    , View arg1, int arg2, long arg3) {
                Intent intent;
                switch(arg2) {
                    case 0 :
                        intent = new Intent(getApplicationContext()
                                , PageFood6.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(getApplicationContext()
                                , PageFood7.class);
                        startActivity(intent);
                        break;
                    case 2 :
                        intent = new Intent(getApplicationContext()
                                , PageFood8.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        intent = new Intent(getApplicationContext()
                                , PageFood9.class);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(getApplicationContext()
                                , PageFood10.class);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(getApplicationContext()
                                , PageFood11.class);
                        startActivity(intent);
                        break;
                    case 6 :
                        intent = new Intent(getApplicationContext()
                                , PageFood12.class);
                        startActivity(intent);
                        break;
                    case 7 :
                        intent = new Intent(getApplicationContext()
                                , PageFood13.class);
                        startActivity(intent);
                        break;
                    case 8 :
                        intent = new Intent(getApplicationContext()
                                , PageFood14.class);
                        startActivity(intent);
                        break;
                }
            }
        });






    }
}
