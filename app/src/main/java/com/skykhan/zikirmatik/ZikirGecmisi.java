package com.skykhan.zikirmatik;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;

/**
 * Created by GÖKHAN on 09/07/2015.
 */

public class ZikirGecmisi extends Activity {

    final ZikirDb db = new ZikirDb(this);
    //private ArrayAdapter<HashMap<String, String>> adapter;
    private SimpleAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gecmis);
        //final GridView gvGecmis = (GridView) findViewById(R.id.gvGecmis);
        final ListView lstGecmis = (ListView) findViewById(R.id.lstGecmis);
        Button btnSorgula = (Button) findViewById(R.id.btnSorgula);
        btnSorgula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // adapter = new ArrayAdapter<HashMap<String, String>>(getApplicationContext(), android.R.layout.simple_spinner_item, db.zikirler());
                //gvGecmis.setAdapter(adapter);
                adapter2 = new SimpleAdapter(getApplicationContext(), db.zikirler(), android.R.layout.simple_list_item_2,
                        new String[]{"ID", "SAYI", "ADI"}, new int[]{android.R.id.text1, android.R.id.text2, android.R.id.text1});
                lstGecmis.setAdapter(adapter2);


            }
        });

        Button btnGeri = (Button) findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent geriGel = new Intent(ZikirGecmisi.this,ZikirmatikActivity.class);
                //startActivity(geriGel);
                finish();
            }
        });


    }
}
