package com.covid19.comp590;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.maps.GeoApiContext;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


@RequiresApi(api = Build.VERSION_CODES.N)
public class Statistics extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    ImageView back, nofity;
    TextView active, confirmed, deaths, recovered, newconfirmed, locationname, risklevel;
    Button global;
    SliderLayout sliderLayout;
    FloatingActionButton floatbtn;
    Button riskbtn;
    FusedLocationProviderClient fusedLocationClient;
    String riskurl = "https://api.covidactnow.org/v2/state/";
    String riskurl2 =".json?apiKey=afc9aab013284ca090f66bd3be398a6d";
    String realriskurl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        back = (ImageView) findViewById(R.id.dash);
        nofity = (ImageView) findViewById(R.id.notify);
        active = (TextView) findViewById(R.id.active);
        confirmed = (TextView) findViewById(R.id.confirmed);
        deaths = (TextView) findViewById(R.id.deaths);
        recovered = (TextView) findViewById(R.id.recovered);
        newconfirmed = (TextView) findViewById(R.id.newconfirmed);
        global = (Button) findViewById(R.id.button4);
        locationname = (TextView) findViewById(R.id.textView13);
        risklevel = (TextView) findViewById(R.id.textView14);
        riskbtn = (Button) findViewById(R.id.button5);
        locationname.setText("Texas Risk:");
        Thread[] threads = new Thread[2];

        // floatbtn=(FloatingActionButton)findViewById(R.id.floatbtn);
      /*  floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Statistics.this,bot.class);
                startActivity(intent);
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }*/
        threads[0] = new Thread(()-> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION_CODE);
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    System.out.println(location);
                                    getAddress(location);
                                }
                            }
                        });
            } else {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    System.out.println(location);
                                    getAddress(location);
                                }
                            }
                        });
            }
        });
        threads[0].start();



        sliderLayout=(SliderLayout) findViewById(R.id.imageslider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1);
        setSliderViews();
        System.out.println(realriskurl);
        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g=new Intent(Statistics.this,GlobalStatistics.class);
                startActivity(g);
                finish();
            }
        });
        riskbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://covidactnow.org/us/texas-tx/county/harris_county/?s=31751998");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        nofity.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          PopupMenu popup = new PopupMenu(Statistics.this, nofity);
                                          //Inflating the Popup using xml file
                                          popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                                          //registering popup with OnMenuItemClickListener
                                          popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                              public boolean onMenuItemClick(MenuItem item) {
                                                  switch (item.getTitle().toString())
                                                  {
                                                      case "About Developer":
                                                          Toast.makeText(Statistics.this, "Ansar WeiHong\nRice University",
                                                                  Toast.LENGTH_LONG).show();
                                                          break;
                                                  }
                                                  //Toast.makeText(Statistics.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                                  return true;
                                              }
                                          }); popup.show();
                                      }
                                  });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Statistics.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        threads[1] = new Thread(()->{
           OkHttpClient client = new OkHttpClient();

           final Request request = new Request.Builder()
                   .url("https://api.covidactnow.org/v2/country/US.json?apiKey=afc9aab013284ca090f66bd3be398a6d")
                   .get()
                   .addHeader("User-Agent", "android").
                           header("Content-Type", "text/html; charset=utf-8")
                   //HAVE TO REMOVE FOR PRIVACY CONCERN
                   .build();
           final Request request1 = new Request.Builder()
                   .url(realriskurl)
                   .get()
                   .addHeader("User-Agent", "android").
                           header("Content-Type", "text/html; charset=utf-8")
                   //HAVE TO REMOVE FOR PRIVACY CONCERN
                   .build();


           client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   if (response.isSuccessful()) {

                       final String myResponse = response.body().string();
                       //System.out.println(myResponse);

                       Statistics.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               String activ, confirm, newconfirm, death, vaccine;


                               try {

                                   JSONObject obj = new JSONObject(myResponse);

                                   JSONObject actualValue = obj.getJSONObject("actuals");


                                   activ = actualValue.getString("positiveTests");
                                   active.setText(activ);
                                   confirm = actualValue.getString("cases");
                                   confirmed.setText(confirm);
                                   newconfirm = actualValue.getString("newCases");
                                   newconfirmed.setText(newconfirm);
                                   death = actualValue.getString("deaths");
                                   deaths.setText(death);
                                   vaccine = actualValue.getString("vaccinationsCompleted");
                                   recovered.setText(vaccine);



                                /*for (int i = 0; i < userArray.length(); i++) {

                                    JSONObject userDetail = userArray.getJSONObject(i);

                                    activ=userDetail.getString("active");
                                    active.setText(activ);

                                    confirm=userDetail.getString("confirmed");
                                    confirmed.setText(confirm);

                                    newconfirm=userDetail.getString("newconfirmed");
                                    newconfirmed.setText(newconfirm);

                                    death=userDetail.getString("deaths");
                                    deaths.setText(death);

                                    recover=userDetail.getString("recovered");
                                    recovered.setText(recover);

                                }*/
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                   }

               }
           });
           client.newCall(request1).enqueue(new Callback() {
               @Override
               public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
               }

               @Override
               public void onResponse(Call call, Response response) throws IOException {
                   if (response.isSuccessful()) {

                       final String myResponse = response.body().string();
                       //System.out.println(myResponse);

                       Statistics.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               String risk;


                               try {

                                   JSONObject obj = new JSONObject(myResponse);
                                   System.out.println(obj);
                                   JSONObject riskLevel = obj.getJSONObject("riskLevels");
                                   risk = riskLevel.getString("overall");
                                   risklevel.setText(risk);


                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       });
                   }

               }
           });

        });
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threads[1].start();

/*
        try {
            threads[0].join();
            threads[1].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }


    private  void  getAddress(Location location) {
        List<Address> result = null;
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
                String realstate = statelist[1];
                locationname.setText("   "+ realstate + "   Risk:");
                realriskurl = riskurl + realstate +riskurl2;
                System.out.println("11111"+ realriskurl);





            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return result;
    }
    private void setSliderViews() {
        for(int i=0;i<3;i++)
        {
            DefaultSliderView sliderView =new DefaultSliderView(this);

            switch (i)
            {
                case 0:
                    sliderView.setImageDrawable(R.drawable.rice_covid);
                    sliderView.setDescription("Rice University Campus Covid Report Form");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.rice_hashmap);
                    sliderView.setDescription("COMP590 Join us in the fight against COVID-19");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.usa_cdc);
                    sliderView.setDescription("Centers for Disease Control and Prevention");
                    break;


            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI=i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(Statistics.this,"Redirecting... ",Toast.LENGTH_SHORT).show();
                    switch (finalI) {
                        case 0:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://coronavirus.rice.edu/"));
                            startActivity(browserIntent);
                            break;
                        case 1:
                            Intent browserIntent1 = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://hsmap.rice.edu/"));
                            startActivity(browserIntent1);
                            break;
                        case 2:
                            Intent browserIntent2 = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.cdc.gov/"));
                            startActivity(browserIntent2);
                            break;
                    }
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }
}
