package com.covid19.comp590;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GlobalStatistics extends AppCompatActivity implements Serializable {
    ImageView back,gnotify;
    TextView active,confirmed,deaths,recovered,newconfirmed;
    Button usa;
    SliderLayout sliderLayout;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_statistics);
        Intent  intent = getIntent();
        state = (String) intent.getSerializableExtra("state");
        System.out.println(state);
        back =(ImageView)findViewById(R.id.gimageView5);
        gnotify=(ImageView)findViewById(R.id.gnotify);
        active=(TextView)findViewById(R.id.gactive);
        confirmed=(TextView)findViewById(R.id.gconfirmed);
        deaths=(TextView)findViewById(R.id.gdeaths);
        recovered=(TextView)findViewById(R.id.grecovered);
        newconfirmed=(TextView)findViewById(R.id.gnewconfirmed);
        usa=(Button)findViewById(R.id.gbutton3);

        sliderLayout=(SliderLayout) findViewById(R.id.imageslider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1);
        setSliderViews();

        usa.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent in=new Intent(GlobalStatistics.this,Statistics.class);
                in.putExtra("state",state);
                startActivity(in);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(GlobalStatistics.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        gnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(GlobalStatistics.this,gnotify);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString())
                        {
                            case "About Developer":
                                Toast.makeText(GlobalStatistics.this, "Ansar WeiHong\nCOMP 590",
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        return true;
                    }
                }); popup.show();
            }
        });


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url("https://covid-193.p.rapidapi.com/history?country=all")
                .get()
                .addHeader("X-RapidAPI-Host", "covid-193.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "2dabede6b2mshbe6402cce5ccb87p12e237jsnfbce635cacb9")
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

                    GlobalStatistics.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String activ,confirm,newconfirm,death,recover;


                            try {
                                JSONObject obj = new JSONObject(myResponse);
                                JSONArray  obj3 = obj.getJSONArray("response");

                                JSONObject obj1 = obj3.getJSONObject(0);
                                JSONObject obj2 = obj1.getJSONObject("cases");
                                JSONObject obj4 = obj1.getJSONObject("deaths");
                                activ=obj2.getString("active");
                                confirm=obj2.getString("total");
                                newconfirm=obj2.getString("new");
                                death=obj4.getString("total");
                                recover=obj2.getString("recovered");
                                recovered.setText(recover);
                                deaths.setText(death);
                                newconfirmed.setText(newconfirm);
                                confirmed.setText(confirm);
                                active.setText(activ);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }
    private void setSliderViews(){
        for(int i=0;i<1;i++)
        {
            DefaultSliderView sliderView =new DefaultSliderView(this);

            switch (i)
            {
                case 0:
                    sliderView.setImageDrawable(R.drawable.global_map);
                    sliderView.setDescription("You Can See The Covid Through The Map");
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            final int finalI=i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText( GlobalStatistics.this,"Redirecting... ",Toast.LENGTH_SHORT).show();
                    switch (finalI) {
                        case 0:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://coronavirus.app/map"));
                            startActivity(browserIntent);
                            System.out.println(" 11111 ");
                            break;

                    }
                }
            });
            sliderLayout.addSliderView(sliderView);
        }
    }
}
