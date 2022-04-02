package com.covid19.comp590;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.nlopez.smartlocation.SmartLocation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Statistics extends AppCompatActivity {
    ImageView back, nofity;
    TextView active, confirmed, deaths, recovered, newconfirmed, locationname, risklevel;
    Button global;
    SliderLayout sliderLayout;
    FloatingActionButton floatbtn;


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
        locationname.setText("Texas Risk:");






        floatbtn=(FloatingActionButton)findViewById(R.id.floatbtn);
        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Statistics.this,bot.class);
                startActivity(intent);
            }
        });

        sliderLayout=(SliderLayout) findViewById(R.id.imageslider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1);
        setSliderViews();

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent g=new Intent(Statistics.this,GlobalStatistics.class);
                startActivity(g);
                finish();
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
                                                          Toast.makeText(Statistics.this, "Ansar WeiHong\n18ECE045\nRice University",
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
        OkHttpClient client =new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://api.covidactnow.org/v2/country/US.json?apiKey=afc9aab013284ca090f66bd3be398a6d")
                .get()
                .addHeader("User-Agent","android").
                header("Content-Type","text/html; charset=utf-8")
                //HAVE TO REMOVE FOR PRIVACY CONCERN
                .build();
        final Request request1 = new Request.Builder()
                .url("https://api.covidactnow.org/v2/state/TX.json?apiKey=afc9aab013284ca090f66bd3be398a6d")
                .get()
                .addHeader("User-Agent","android").
                        header("Content-Type","text/html; charset=utf-8")
                //HAVE TO REMOVE FOR PRIVACY CONCERN
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful())
                {

                    final String myResponse =response.body().string();
                    //System.out.println(myResponse);

                    Statistics.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String activ,confirm,newconfirm,death,vaccine;


                            try {

                                JSONObject obj = new JSONObject(myResponse);

                                JSONObject actualValue = obj.getJSONObject("actuals");



                                activ = actualValue.getString("positiveTests");
                                active.setText(activ);
                                confirm = actualValue.getString("cases");
                                confirmed.setText(confirm);
                                newconfirm = actualValue.getString("newCases");
                                newconfirmed.setText(newconfirm);
                                death =actualValue.getString("deaths");
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
                if(response.isSuccessful())
                {

                    final String myResponse =response.body().string();
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
