package com.geolock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.location.aravind.getlocation.GeoLocator;
import com.location.aravind.getlocation.TestLib;

public class MainActivity extends AppCompatActivity {

    TextView tvValue;
    Button btGetData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        btGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tvValue.setText(TestLib.test( ""+geoLocator.getLattitude())+ geoLocator.getLongitude() + "\n" + geoLocator.getAddress());
                String test = "test";
                try {
                    GeoLocator geoLocator = new GeoLocator(getApplicationContext(), MainActivity.this);
                    test = geoLocator.getLattitude()+ geoLocator.getLongitude() + "\n" + geoLocator.getAddress();

                } catch (Exception e){
                    e.printStackTrace();
                }
                tvValue.setText(TestLib.test( test ));

            }
        });







    }


    public  void initWidgets(){

        tvValue = findViewById(R.id.tvValue);
        btGetData = findViewById(R.id.btGetData);



    }












}
