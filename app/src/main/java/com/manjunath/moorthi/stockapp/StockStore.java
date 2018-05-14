package com.manjunath.moorthi.stockapp;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class StockStore extends AsyncTask<String,Void,String>{

    public StockViewer reference;
    private final String stocknameurl="http://d.yimg.com/aq/autoc";
    public ArrayList<String[]> s1=new ArrayList<>();
    public boolean resultcheck=false;

    private static final String TAG = "StockStore";
    public StockStore(StockViewer c){
        reference=c;
    }
    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: came to url");
        Uri.Builder Stocksearch=Uri.parse(stocknameurl).buildUpon();
        Stocksearch.appendQueryParameter("region","US&lang");
        Stocksearch.appendQueryParameter("lang","en-US");
        Stocksearch.appendQueryParameter("query",params[0]);
        String updated=Stocksearch.build().toString();
        StringBuilder jsonline=new StringBuilder();
        try{
            URL value=new URL(updated);
            HttpURLConnection connect=(HttpURLConnection)value.openConnection();
            connect.setRequestMethod("GET");
            InputStream read=connect.getInputStream();
            BufferedReader readit=new BufferedReader(new InputStreamReader(read));
            String emptyline;
            while((emptyline=readit.readLine())!=null){
                Log.d(TAG, "doInBackground: came to add String");
                jsonline.append(emptyline).append('\n');
            }
            Log.d(TAG, "doInBackground: "+jsonline.toString());
        }
        catch (Exception e){
            e.getStackTrace();
        }
        ExtractJson(jsonline.toString());


        return null;
    }
    public void ExtractJson(String s){
        Log.d(TAG, "ExtractJson: Working");
        try{
            Log.d(TAG, "ExtractJson: Working1"+s);
            JSONObject jmain=new JSONObject(s);
            Log.d(TAG, "ExtractJson: Working2.1");
            JSONObject Res=jmain.getJSONObject("ResultSet");
            Log.d(TAG, "ExtractJson: Working2.2");
            JSONArray Result=Res.getJSONArray("Result");
            Log.d(TAG, "ExtractJson: Working2.3");
            Log.d(TAG, "ExtractJson: "+Result.toString());
            if(Result.length()==0){
                Log.d(TAG, "ExtractJson: Came to check true");
                resultcheck=true;
            }
            else {
                Log.d(TAG, "ExtractJson: working3");
                for (int i = 0; i < Result.length(); i++) {
                    JSONObject loopdata = (JSONObject) Result.get(i);
                    if (loopdata.getString("symbol").contains(".")) {
                        Log.d(TAG, "ExtractJson: came cot of unwannted");
                        continue;
                    } else if(loopdata.getString("type").equals("S")){
                        Log.d(TAG, "ExtractJson: " + loopdata.getString("name"));
                        String ss=loopdata.getString("symbol");
                        Log.d(TAG, "ExtractJson: "+loopdata.getString("symbol"));
                        String ss1=loopdata.getString("name");
                        Log.d(TAG, "ExtractJson: came"+ss1);
                        s1.add(new String[]{ss,ss1});
                    }
                }
            }

        }
        catch (Exception e){
            e.getStackTrace();
        }


    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: "+s1.size());
        reference.updateTask1(s1,resultcheck);

    }
}
