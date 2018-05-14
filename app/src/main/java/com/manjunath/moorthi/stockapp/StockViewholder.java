package com.manjunath.moorthi.stockapp;

/**
 * Created by akris on 3/3/2018.
 */
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class StockViewholder extends RecyclerView.ViewHolder {
    public TextView t1;
    public TextView t2;
    public TextView p1;
    public TextView p2;
    public TextView p3;
    public TextView p4;
    public StockViewholder(View itemView) {
        super(itemView);
        t1=(TextView)itemView.findViewById(R.id.t1);
        t2=(TextView)itemView.findViewById(R.id.t2);
        p1=(TextView)itemView.findViewById(R.id.p1);
        p2=(TextView)itemView.findViewById(R.id.p2);
        p3=(TextView)itemView.findViewById(R.id.p3);
        p4=(TextView)itemView.findViewById(R.id.p4);
    }
    public void setTheme1(){
        t1.setTextColor(Color.parseColor("#00cc00"));
        t2.setTextColor(Color.parseColor("#00cc00"));
        p1.setTextColor(Color.parseColor("#00cc00"));
        p3.setTextColor(Color.parseColor("#00cc00"));
        p4.setTextColor(Color.parseColor("#00cc00"));
        p2.setTextColor(Color.parseColor("#00cc00"));
        p2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.greenup,0,0);


    }
    public void setTheme2(){
        //#ff0000
        t1.setTextColor(Color.parseColor("#ff0000"));
        t2.setTextColor(Color.parseColor("#ff0000"));
        p1.setTextColor(Color.parseColor("#ff0000"));
        p3.setTextColor(Color.parseColor("#ff0000"));
        p4.setTextColor(Color.parseColor("#ff0000"));
        p2.setTextColor(Color.parseColor("#ff0000"));
        p2.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.reddown,0,0);

    }

    public void setTheme3() {
        t1.setTextColor(Color.parseColor("#ffff33"));
        t2.setTextColor(Color.parseColor("#ffff33"));
        p1.setTextColor(Color.parseColor("#ffff33"));
        p3.setTextColor(Color.parseColor("#ffff33"));
        p4.setTextColor(Color.parseColor("#ffff33"));
        p2.setTextColor(Color.parseColor("#ffff33"));
        p2.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }
}