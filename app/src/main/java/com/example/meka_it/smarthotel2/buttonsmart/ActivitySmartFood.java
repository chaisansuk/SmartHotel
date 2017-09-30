package com.example.meka_it.smarthotel2.buttonsmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.meka_it.smarthotel.R;
import com.example.meka_it.smarthotel2.page.MyAdapter;
import com.example.meka_it.smarthotel2.page.PageChef;
import com.example.meka_it.smarthotel2.page.PageFood1;
import com.example.meka_it.smarthotel2.page.PageFood10;
import com.example.meka_it.smarthotel2.page.PageFood11;
import com.example.meka_it.smarthotel2.page.PageFood12;
import com.example.meka_it.smarthotel2.page.PageFood13;
import com.example.meka_it.smarthotel2.page.PageFood14;
import com.example.meka_it.smarthotel2.page.PageFood2;
import com.example.meka_it.smarthotel2.page.PageFood3;
import com.example.meka_it.smarthotel2.page.PageFood4;
import com.example.meka_it.smarthotel2.page.PageFood5;
import com.example.meka_it.smarthotel2.page.PageFood6;
import com.example.meka_it.smarthotel2.page.PageFood7;
import com.example.meka_it.smarthotel2.page.PageFood8;
import com.example.meka_it.smarthotel2.page.PageFood9;
import com.example.meka_it.smarthotel2.page.PageWater1;
import com.example.meka_it.smarthotel2.page.PageWater10;
import com.example.meka_it.smarthotel2.page.PageWater2;
import com.example.meka_it.smarthotel2.page.PageWater3;
import com.example.meka_it.smarthotel2.page.PageWater4;
import com.example.meka_it.smarthotel2.page.PageWater5;
import com.example.meka_it.smarthotel2.page.PageWater6;
import com.example.meka_it.smarthotel2.page.PageWater7;
import com.example.meka_it.smarthotel2.page.PageWater8;
import com.example.meka_it.smarthotel2.page.PageWater9;

public class ActivitySmartFood extends AppCompatActivity {

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

    ListView mListView2;

    String[] foodname2 = {"ราคา 150 บาท", "ราคา 275 บาท", "ราคา 155 บาท", "ราคา 60 บาท", "ราคา 60 บาท",  "ราคา 75 บาท", "ราคา 90 บาท", "ราคา 75 บาท",  "ราคา 155 บาท"};
    String[] foodtitle2 = {"SET Breakfast", "SET Breakfast2", "SET น้ำพริกปลาทู", "ผัดไทย", "ส้มตำ",  "ดับเบิ้ลแซลมอน", "กุ้งฟูต้นตำรับ", "มิกซ์บอล",  "ปีกไก่อบซอสสมุนไพร"};
    int[] foodpic2 = {
            R.drawable.pic6,
            R.drawable.pic9,
            R.drawable.pic13,
            R.drawable.pic7,
            R.drawable.pic8,
            R.drawable.pic10,
            R.drawable.pic11,
            R.drawable.pic12,
            R.drawable.pic14
    };

    ListView mListView3;

    String[] foodname3 = {"ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 15 บาท", "ราคา 20 บาท", "ราคา 10 บาท", "ราคา 10 บาท", "ราคา 35 บาท"};
    String[] foodtitle3 = {"น้ำดื่มเนสท์เล่", "น้ำดื่มเคริสคัล", "น้ำทิพย์", "น้ำดื่มสิงห์", "น้ำดื่มเมิเนเล่", "อิชิตันกรีนที", "โออิชิกรีนที", "Pepsi", "EST", "น้ำผลไม้ปั่น"};
    int[] foodpic3 = {
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
        setContentView(R.layout.activity_smartfood);
        ImageButton back = (ImageButton) findViewById(R.id.back);

        //tabhost
        TabHost tabHost = (TabHost) this.findViewById (R.id.tabhost);
        tabHost.setup ( );

        TabHost.TabSpec tab_recommend = tabHost.newTabSpec ("RECOMMEND");
        tab_recommend.setIndicator ("RECOMMEND");
        tab_recommend.setContent (R.id.recommend);

        tabHost.addTab (tab_recommend);

        TabHost.TabSpec tab_food = tabHost.newTabSpec ("FOOD");
        tab_food.setIndicator ("FOOD");
        tab_food.setContent (R.id.food);

        tabHost.addTab (tab_food);

        TabHost.TabSpec tab_drink = tabHost.newTabSpec ("DRINK");
        tab_drink.setIndicator ("DRINK");
        tab_drink.setContent (R.id.drink);

        tabHost.addTab (tab_drink);

        TabWidget tabWidget = tabHost.getTabWidget ();
        tabWidget.setEnabled (true);


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //click b
        mListView = (ListView) findViewById(R.id.listview);

        MyAdapter myAdapter = new MyAdapter(ActivitySmartFood.this, foodtitle, foodname, foodpic);
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

        mListView2 = (ListView) findViewById(R.id.listview2);
        MyAdapter myAdapter2 = new MyAdapter(ActivitySmartFood.this, foodtitle2, foodname2, foodpic2);
        mListView2.setAdapter(myAdapter2);
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        mListView3 = (ListView) findViewById(R.id.listview3);
        MyAdapter myAdapter3 = new MyAdapter(ActivitySmartFood.this, foodtitle3, foodname3, foodpic3);
        mListView3.setAdapter(myAdapter3);
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
