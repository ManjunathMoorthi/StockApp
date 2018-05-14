package com.manjunath.moorthi.stockapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;



public class Stockviewhandler extends RecyclerView.Adapter<StockViewholder>{
    public ArrayList<Stock> viewdata;
    public StockViewer ref;
    private static final String TAG = "Stockviewhandler";
    public Stockviewhandler(ArrayList<Stock> data,StockViewer s){
        viewdata=data;
        ref=s;
    }

    @Override
    public StockViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Came");
        View stockitem= LayoutInflater.from(parent.getContext()).inflate(R.layout.stockcards,parent,false);
        stockitem.setOnLongClickListener(ref);
        stockitem.setOnClickListener(ref);
        return new StockViewholder(stockitem);
    }

    @Override
    public void onBindViewHolder(StockViewholder holder, int position) {
        Stock temp=viewdata.get(position);
        try {
           // String temps = temp.getPricechange().substring(0, 1);
            double value = Double.parseDouble(temp.getPercentageChange());
            double value1 = Double.parseDouble(temp.getPrice());
            double value2 = Double.parseDouble(temp.getPreviousClose());
            double result = value1-value2;
            if(result>0){
                holder.setTheme1();
            }
            else if(result<0){
                holder.setTheme2();
            }
            else{
                holder.setTheme3();
            }
           /* if (temps.equals("+")) {
                holder.setTheme1();
            } else if (temps.equals("-")) {
                holder.setTheme2();
            } else {
                holder.setTheme3();
            }*/
            holder.t1.setText(temp.getStockSys());
            holder.t2.setText(temp.getCompanyName());
            holder.p1.setText(temp.getPrice());
            holder.p3.setText(temp.getPricechange());
            holder.p4.setText("(" + temp.getPercentageChange() + ")");
            Log.d(TAG, "onBindViewHolder: Came and setup the Recycle view");

        }
        catch (Exception e){
            Log.d(TAG, "onBindViewHolder: Error in the Json Data Some are Empty");
            holder.setTheme3();
            holder.t1.setText(temp.getStockSys()+"- Error ");
            holder.t2.setText("Some Data are Empty");
            holder.p1.setText("");
            holder.p3.setText("");
            holder.p4.setText("");

            e.getStackTrace();

        }
    }

    @Override
    public int getItemCount() {
        return viewdata.size();
    }
}
