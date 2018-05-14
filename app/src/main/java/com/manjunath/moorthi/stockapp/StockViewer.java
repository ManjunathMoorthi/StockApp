package com.manjunath.moorthi.stockapp;



import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Collections;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class StockViewer extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView r1;
    private ArrayList<Stock> maindata;
    private Stockviewhandler adapter;
    private SwipeRefreshLayout s1;
    private static final String TAG = "StockViewer";
    private String Url = "http://www.marketwatch.com/investing/stock/";
    boolean working = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: working");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_viewer);
        r1 = (RecyclerView) findViewById(R.id.R1);
        maindata = new ArrayList<Stock>();
        adapter = new Stockviewhandler(maindata, this);
        r1.setLayoutManager(new LinearLayoutManager(this));
        s1 = (SwipeRefreshLayout) findViewById(R.id.s1);
        r1.setAdapter(adapter);
        SymbolList.getInstance(this).setupDb();
        s1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                onResume();
                s1.setRefreshing(false);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater f = getMenuInflater();
        f.inflate(R.menu.addmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.m1:
                Log.d(TAG, "onOptionsItemSelected:Came ");
                userinputDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        final int i = r1.getChildLayoutPosition(v);
        Stock s = maindata.get(i);
        String Urlu = Url + s.getStockSys();
        Intent i1 = new Intent(Intent.ACTION_VIEW);
        i1.setData(Uri.parse(Urlu));
        startActivity(i1);


    }

    @Override
    protected void onDestroy() {
        SymbolList.getInstance(this).shutdown();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (isConnected()) {
            maindata.clear();
            ArrayList<String[]> dbdata = SymbolList.getInstance(this).Load();
            for (String[] s : dbdata) {
                StockValue create = new StockValue(this);
                create.execute(s[0], s[1], "load");
            }

        } else {
            noconnection();
        }
        super.onPostResume();
    }

    //User input Dialog
    public void userinputDialog() {
        Log.d(TAG, "userinputDialog: came ");
        final StockViewer app = this;
        if (isConnected()) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);

            builder.setView(et);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Log.d(TAG, "onClick: Came to ok button");
                    StockStore listaction = new StockStore(app);
                    listaction.execute(et.getText().toString());
                }
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });


            builder.setMessage("Please enter a value:");
            builder.setTitle("Select Stock");

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {

            noconnection();
        }
    }

    public void noconnection() {
        Log.d(TAG, "noconnection: came");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        Log.d(TAG, "noconnection: came1");
        builder.setMessage("App Requires Internet ");
        builder.setTitle("No Network");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Task1
    public void updateTask1(ArrayList<String[]> s, boolean r) {
        final String[] arraylist = new String[s.size()];

        if (r) {
            Nostock();
        } else {
            Log.d(TAG, "updateTask1: came to else");
            final StockViewer t = this;
            for (int i = 0; i < s.size(); i++) {
                arraylist[i] = s.get(i)[0] + "-" + s.get(i)[1];
                Log.d(TAG, "updateTask1: " + s.get(i)[0] + "-" + s.get(i)[1]);
            }
            if (arraylist.length == 0) {
                Nostock();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select a Stock");
                Log.d(TAG, "updateTask1: came before crash");
                builder.setItems(arraylist, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MakeSelection(arraylist[which]);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        }

    }

    public void Nostock() {
        Log.d(TAG, "updateTask1: came to no stock");
        AlertDialog.Builder userinput = new AlertDialog.Builder(this);
        userinput.setMessage("No Stock Found");
        userinput.setMessage("Stock Symbol is incorrect ");
        userinput.setPositiveButton("close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog d = userinput.create();
        d.show();

    }

    private void MakeSelection(String s) {
        String[] ss = s.split("-");
        ArrayList<String[]> sd = SymbolList.getInstance(this).Load();
        boolean a = false;
        for (String[] m : sd) {
            if (m[0].equals(ss[0])) {
                Log.d(TAG, "MakeSelection: came" +m[0]);
                alreadyExsist();
                a = true;
            }
        }
        if (a) {
            //DO nothing
        } else {
            StockValue d = new StockValue(this);
            d.execute(ss[0], ss[1], "add");
            Log.d(TAG, "MakeSelection: Came here before crash" + ss[0] + ss[1]);
        }

    }


    public void alreadyExsist() {
        AlertDialog.Builder userinput = new AlertDialog.Builder(this);
        userinput.setMessage(" Stock Duplication");
        userinput.setMessage("Stock Symbol is already Present ");
        userinput.setPositiveButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog d = userinput.create();
        d.show();

    }


    public void updateTask2(Stock ss, String decision) {
        if (decision.equals("load")) {
            maindata.add(ss);
            Collections.sort(maindata, Stock.sortit);
            adapter.notifyDataSetChanged();
        } else if (decision.equals("add")) {
            maindata.add(ss);
            Collections.sort(maindata, Stock.sortit);
            Log.d(TAG, "updateTask2: " + ss.getCompanyName());
            SymbolList.getInstance(this).addentry(ss);
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public boolean onLongClick(View v) {
        final StockViewer v1 = this;
        final int i = r1.getChildLayoutPosition(v);
        AlertDialog.Builder deleteit = new AlertDialog.Builder(this);
        deleteit.setMessage("Do you want to Delete the Stock?");
        deleteit.setTitle("Delete");
        deleteit.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SymbolList.getInstance(v1).Deleteentery(maindata.get(i).getStockSys());
                maindata.remove(i);
                adapter.notifyDataSetChanged();
            }

        });
        deleteit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//Do nothing
            }
        });
        AlertDialog deletealret = deleteit.create();
        deletealret.show();

        return true;
    }
}