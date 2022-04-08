package com.covid19.comp590;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView flags,notify,dash;
    Spinner mSpinner;
    Button stat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner=(Spinner)findViewById(R.id.spinner);
        flags =(ImageView)findViewById(R.id.flag);
        stat=(Button)findViewById(R.id.stat);
        notify=(ImageView)findViewById(R.id.notify);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, notify);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString())
                        {
                            case "About Developer":
                                Toast.makeText(MainActivity.this, "Ansar Weihong\nRice University",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        return true;
                    }
                }); popup.show();
            }
        });

        mSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                CounrtyData.countryNames));

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                flags.setImageResource(CounrtyData.countryFlag[mSpinner.getSelectedItemPosition()]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Statistics.class);
                startActivity(i);
            }
        });
    }

    public void CallNow(View view) {
        Intent i1 =new Intent(Intent.ACTION_VIEW, Uri.parse("tel:713-348-6000"));
        startActivity(i1);
    }

    public void WhatsappReq(View view) {
        Intent i2 =new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://veoci.com/v/p/dashboard/jnmraf9eqg"));
        startActivity(i2);
    }

    public void sendMesReq(View view) {
        Intent i2 =new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://veoci.com/v/p/dashboard/jnmraf9eqg"));
        startActivity(i2);
    }
}
