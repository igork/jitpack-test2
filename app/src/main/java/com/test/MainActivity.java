package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igork.pn.GeoLocator;
import com.igork.pn.TestLib;

import com.igork.pn.PNController;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvValue;
    Button btGetData;
    PNController pnc;
    AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        activity = this;


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

                String test2 = "test2";
                try {
                    pnc = new PNController(activity);

                    pnc.subscribe();

                    JSONObject obj = new JSONObject("{a:bb}");
                    pnc.publish(obj);

                    test2 = pnc.toString();
                } catch (Exception e){
                    e.printStackTrace();
                }

                tvValue.setText(TestLib.test( test + " 2222:" + test2));


            }
        });







    }


    public  void initWidgets(){

        tvValue = findViewById(R.id.tvValue);
        btGetData = findViewById(R.id.btGetData);



    }












}
