package com.skykhan.zikirmatik;

/**
 * Created by GÖKHAN on 09/07/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class ZikirmatikActivity extends Activity {

    final ZikirDb db = new ZikirDb(this);

    int sayi = 0;
    int zid = 0;
    ArrayList<HashMap<String, String>> zikirler;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zikirmatik);

        final ListView lstZikir = (ListView) findViewById(R.id.lstZikir);
        final TextView txtZikir = (TextView) findViewById(R.id.txtZikir);
        final EditText txtZikirAdi = (EditText) findViewById(R.id.txtZikirAdi);
        zikirDoldur(lstZikir);
        if (txtZikir.getText().equals("")) {
            txtZikir.setText("0");
        }

        Button btnZikret = (Button) findViewById(R.id.btnZikret);
        btnZikret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sayi = Integer.parseInt(txtZikir.getText().toString()) + 1;
                txtZikir.setText(String.valueOf((sayi)));
                if (zid == 0) {
                    db.zikirEkle(sayi, txtZikirAdi.getText().toString());
                    zid = db.sonKayitGetir();
                } else {
                    db.zikirGuncelle(zid, txtZikirAdi.getText().toString(), sayi);
                }
                zikirDoldur(lstZikir);

            }
        });

        Button btnSifirla = (Button) findViewById(R.id.btnSifirla);
        btnSifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zid != 0) {
                    //db.zikirSil(zid);
                    db.zikirGuncelle(zid, txtZikirAdi.getText().toString(), 0);
                }
                txtZikir.setText("0");
                Toast.makeText(getApplicationContext(), R.string.msjSifirla, Toast.LENGTH_SHORT).show();
                zikirDoldur(lstZikir);
            }
        });

        Button btnSil = (Button) findViewById(R.id.btnSil);
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zid != 0) {
                    db.zikirSil(zid);
                }
                txtZikir.setText("0");
                txtZikirAdi.setText("");
                zid = 0;
                Toast.makeText(getApplicationContext(), R.string.msjSil, Toast.LENGTH_SHORT).show();
                zikirDoldur(lstZikir);
            }
        });

        Button btnGecmis = (Button) findViewById(R.id.btnGecmis);
        btnGecmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecmisAc = new Intent(ZikirmatikActivity.this, ZikirGecmisi.class);
                startActivity(gecmisAc);
            }
        });

        lstZikir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                zid = Integer.parseInt(zikirler.get(position).get("ID").toString());
                txtZikir.setText(zikirler.get(position).get("SAYI"));
                txtZikirAdi.setText(zikirler.get(position).get("ADI"));
            }
        });
    }

    private void zikirDoldur(ListView lstV) {

        zikirler = db.zikirler();
        adapter = new SimpleAdapter(getApplicationContext(), zikirler, android.R.layout.simple_list_item_2,
                new String[]{"ID", "SAYI", "ADI"}, new int[]{android.R.id.text1, android.R.id.text2, android.R.id.text1});
        lstV.setAdapter(adapter);
    }


}




