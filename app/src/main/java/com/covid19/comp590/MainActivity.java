package com.covid19.comp590;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Serializable {
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    ImageView flags,notify,dash;
    Spinner mSpinner;
    Button stat;
    FusedLocationProviderClient fusedLocationClient;
    String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner=(Spinner)findViewById(R.id.spinner);
        flags =(ImageView)findViewById(R.id.flag);
        stat=(Button)findViewById(R.id.stat);
        notify=(ImageView)findViewById(R.id.notify);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_CODE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                    LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LaunchIntent);
                }
            }, 3000);// 1秒钟后重启应用
        } else {
            System.out.println("11111111");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {

                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                System.out.println("1111111" + location);
                                String tmp =getAddress(location);
                                setState(tmp);
                            }
                        }
                    });
        }

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MainActivity.this,Statistics.class);

                i.putExtra("state", state);


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
    public void setState(String tmp){
        state = tmp;
        System.out.println("现在的地址"+state);
    }
    public String getState(){
        return state;
    }
    private  String  getAddress(Location location) {
        List<Address> result = null;
        String realstate = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                //           val geoApiCtx = GeoApiContext.Builder()
                //                 .apiKey(Util.readGoogleApiKey(location))
                //                .build()
                System.out.println(result.getClass());
                // Toast.makeText(this, "address info："+result.toString(), Toast.LENGTH_LONG).show();
                Log.v("TAG", "address："+result.toString());
                System.out.println("11111"+ result.toString());
                Object[] address1 = result.toArray();
                System.out.println(address1[0].toString());
                String adress = (result.get(0).getAddressLine(0));
                String [] addressarray = adress.split(",");
                System.out.println(addressarray[2]);
                String state = addressarray[2].toString();
                String [] statelist = state.split("\\s+");
                realstate = statelist[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realstate;
    }
}
