package com.igork.pn;

import android.app.Activity;
import android.content.Context;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PNController {

    static PubNub pubnub;

    String channelName; // = "demo_tutorial";
    String subscribeKey;
    String publishKey;

    Context context;
    Activity activity;

    public PNController(Activity activity){

        this.activity = activity;
        this.context = activity.getApplicationContext();

        String settings = getDefaultSettings();
        if (parseSettings(settings)) {
            initPN();
        }

    }


    private void initPN(){
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(subscribeKey);
        pnConfiguration.setPublishKey(publishKey);
        pubnub = new PubNub(pnConfiguration);
    }

    private boolean parseSettings(String settings){
        JsonElement element = new JsonPrimitive(settings);
        JsonObject result = element.getAsJsonObject();

        element = result.get("subscribeKey");
        subscribeKey = element.getAsString();

        element = result.get("publishKey");
        publishKey = element.getAsString();

        element = result.get("channedlName");
        channelName = element.getAsString();

        if( subscribeKey!=null && publishKey!=null){
            return true;
        } else {
            return false;
        }

    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
    }

    public String getChannedName(){
        return this.channelName;
    }

    public void setCredetials(String publishKey,String subscribeKey){
        this.publishKey = publishKey;
        this.subscribeKey = subscribeKey;
        initPN();
    }

    private String getDefaultSettings(){

        return getSettingsFromFile("pn_services.json");

    }

    public String getSettingsFromFile(String fileName){

        try {
            return getSettingsFromFile(context.getAssets().open(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public String getSettingsFromFile(InputStream is){

        String json;

        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        //Log.e("data", json);
        return json;
    }

    private static String getDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);

    }

    public void subscribe(){
        try {

            pubnub.addListener(new PNSubscribeCallback(channelName, activity));

            pubnub.subscribe()
                    .channels(Arrays.asList(channelName))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int num =0;

    public void test(){

        JsonObject position = new JsonObject();
        //position.addProperty("text", "Hello From Java SDK");
        position.addProperty("igor", getDate() + " some data " + num);

        pubnub.publish()
                .message(position)
                .channel("pubnub_onboarding_channel")
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // handle response
                    }
                });

    }

    public void publish(JSONObject data){
        pubnub.publish()
                .message(data)
                .channel(channelName)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status)
                    {
                        System.out.println("On Response" + result.toString());
                    }
                });
    }

}
