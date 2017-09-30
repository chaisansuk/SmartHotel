package com.example.meka_it.smarthotel2.buttonsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.meka_it.smarthotel2.page.MyAdapter;
import com.example.meka_it.smarthotel.R;
import com.example.meka_it.smarthotel2.page.PageChef;
import com.example.meka_it.smarthotel2.page.PageFood1;
import com.example.meka_it.smarthotel2.page.PageFood10;
import com.example.meka_it.smarthotel2.page.PageFood11;
import com.example.meka_it.smarthotel2.page.PageFood12;
import com.example.meka_it.smarthotel2.page.PageFood13;
import com.example.meka_it.smarthotel2.page.PageFood14;
import com.example.meka_it.smarthotel2.page.PageFood3;
import com.example.meka_it.smarthotel2.page.PageFood2;
import com.example.meka_it.smarthotel2.page.PageFood4;
import com.example.meka_it.smarthotel2.page.PageFood5;
import com.example.meka_it.smarthotel2.page.PageFood6;
import com.example.meka_it.smarthotel2.page.PageFood7;
import com.example.meka_it.smarthotel2.page.PageFood8;
import com.example.meka_it.smarthotel2.page.PageFood9;

public class SmartFood extends AppCompatActivity {

    ListView mListView;

    String[] foodname = {"ราคา 75 บาท", "ราคา 60 บาท", "ราคา 275 บาท", "ราคา 115 บาท", "ราคา 60 บาท", "ราคา 150 บาท", "ราคา 60 บาท", "ราคา 60 บาท"
            , "ราคา 275 บาท", "ราคา 75 บาท", "ราคา 90 บาท", "ราคา 75 บาท", "ราคา 155 บาท", "ราคา 155 บาท"};
    String[] foodtitle = {"ฝอยทองพันลูกเต๋า", "ผัดพริก", "กุ้งเผา", "เปาะเปี๊ยะทอด", "ขนมปังทรงเครื่อง", "SET Breakfast", "ผัดไทย", "ส้มตำ"
            , "SET Breakfast2", "ดับเบิ้ลแซลมอน", "กุ้งฟูต้นตำรับ", "มิกซ์บอล", "SET น้ำพริกปลาทู", "ปีกไก่อบซอสสมุนไพร"};
    int[] foodpic = {
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
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
        setContentView(R.layout.activity_smartfood_home);



        mListView = (ListView) findViewById(R.id.listview);

        MyAdapter myAdapter = new MyAdapter(SmartFood.this, foodtitle, foodname, foodpic);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0
                    , View arg1, int arg2, long arg3) {
                Intent intent;
                switch(arg2) {
                    case 0 :
                        intent = new Intent(getApplicationContext()
                                , PageFood1.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(getApplicationContext()
                                , PageFood2.class);
                        startActivity(intent);
                        break;
                    case 2 :
                        intent = new Intent(getApplicationContext()
                                , PageFood3.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        intent = new Intent(getApplicationContext()
                                , PageFood4.class);
                        startActivity(intent);
                        break;
                    case 4 :
                        intent = new Intent(getApplicationContext()
                                , PageFood5.class);
                        startActivity(intent);
                        break;
                    case 5 :
                        intent = new Intent(getApplicationContext()
                                , PageFood6.class);
                        startActivity(intent);
                        break;
                    case 6 :
                        intent = new Intent(getApplicationContext()
                                , PageFood7.class);
                        startActivity(intent);
                        break;
                    case 7 :
                        intent = new Intent(getApplicationContext()
                                , PageFood8.class);
                        startActivity(intent);
                        break;
                    case 8 :
                        intent = new Intent(getApplicationContext()
                                , PageFood9.class);
                        startActivity(intent);
                        break;
                    case 9 :
                        intent = new Intent(getApplicationContext()
                                , PageFood10.class);
                        startActivity(intent);
                        break;
                    case 10 :
                        intent = new Intent(getApplicationContext()
                                , PageFood11.class);
                        startActivity(intent);
                        break;
                    case 11 :
                        intent = new Intent(getApplicationContext()
                                , PageFood12.class);
                        startActivity(intent);
                        break;
                    case 12 :
                        intent = new Intent(getApplicationContext()
                                , PageFood13.class);
                        startActivity(intent);
                        break;
                    case 13 :
                        intent = new Intent(getApplicationContext()
                                , PageFood14.class);
                        startActivity(intent);
                        break;
                }
            }
        });



    }
}
