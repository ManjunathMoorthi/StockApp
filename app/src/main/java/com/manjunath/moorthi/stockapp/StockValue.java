package com.manjunath.moorthi.stockapp;



import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


public class StockValue extends AsyncTask<String,Void,String> {
    public StockViewer reference;
    private final String stocknameurl="https://api.iextrading.com/1.0/stock/";
    public HashMap<String,String> parsedata=new HashMap<>();
    private String res="no";
    public String dec;

    public boolean resultcheck=false;
    public Stock stock=new Stock();
    private static final String TAG = "StockValue";

    public StockValue(StockViewer c)
    {
        reference=c;
    }

    @Override
    protected String doInBackground(String... params) {
        dec=params[2];
        Log.d(TAG, "doInBackground: came to value "+params[0]+params[1]);
        String updated = stocknameurl + params[0] + "/quote";
        StringBuilder jsonline=new StringBuilder();
        try{


           URL value=new URL(updated);
            HttpURLConnection connect=(HttpURLConnection)value.openConnection();
            connect.setRequestMethod("GET");
            InputStream read=connect.getInputStream();
            BufferedReader readit=new BufferedReader(new InputStreamReader(read));
            String emptyline;
            while((emptyline=readit.readLine())!=null){
                jsonline.append(emptyline).append('\n');
            }

        }
        catch (Exception e){
            e.getStackTrace();
        }
        ExtractJson(jsonline.toString(),params[1]);

        return res;
    }
    public void ExtractJson(String s,String s1){
        try{
            if(s.equals("httpserver.cc: Response Code 400")){
                res="yes";

            }
            else {
                Log.d(TAG, "ExtractJson: "+s1);

                JSONObject loopdata =new JSONObject(s);



                    stock.setStockSys(loopdata.getString("symbol"));
                    stock.setPrice(loopdata.getString("latestPrice"));
                    stock.setPricechange(loopdata.getString("change"));
                    stock.setPercentageChange(loopdata.getString("changePercent"));
                    stock.setPreviousClose(loopdata.getString("previousClose"));

                    Log.d(TAG, "ExtractJson: "+loopdata.getString("symbol"));
                    stock.setCompanyName(s1);


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }



    @Override
    protected void onPostExecute(String s) {
        if(res.equals("yes")){
            reference.Nostock();
        }else {
            reference.updateTask2(stock,dec);
        }

    }

}