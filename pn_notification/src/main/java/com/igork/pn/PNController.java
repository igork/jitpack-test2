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
import java.util.List;

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
        if (settings!=null) {

            try {
                JSONObject jsonObject = new JSONObject(settings);
                subscribeKey = jsonObject.getString("subscribeKey");
                publishKey = jsonObject.getString("publishKey");

                channelName = jsonObject.getString("channelName");
                setChannelName(channelName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
        /*
        if (settings!=null) {
            JsonElement element = new JsonPrimitive(settings);
            JsonObject result = element.getAsJsonObject();

            element = result.get("subscribeKey");
            String subscribeKey1 = element.getAsString();

            element = result.get("publishKey");
            String publishKey1 = element.getAsString();

            element = result.get("channedlName");
            String channelName1 = element.getAsString();

            if (subscribeKey1 != null && publishKey1 != null) {

                subscribeKey = subscribeKey1;
                publishKey = publishKey1;
                setChannelName(channelName1);
                return true;
            }
        }
        return false;
        */
    }

    public void setChannelName(String channelName){
        if (channelName!=null) {
            this.channelName = channelName;
        }
    }

    public String getChannedName(){
        return this.channelName;
    }

    public void setCredetials(String publishKey,String subscribeKey){
        if (publishKey!=null)
            this.publishKey = publishKey;
        if (subscribeKey!=null)
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

    public List<String> getSubscribedChannels() {
        return pubnub.getSubscribedChannels();
    }

    public void subscribe(String channel){

        if (channel==null){
            return;
        }

        List<String> list = pubnub.getSubscribedChannels();
        if (!list.contains(channel)) {
            list.add(channel);
        }
        try {

            pubnub.addListener(new PNSubscribeCallback(channel, activity));

            pubnub.subscribe()
                    .channels(list)//Arrays.asList(channel))
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

    /*
    max message size 32K
     */

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
