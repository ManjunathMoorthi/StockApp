package com.manjunath.moorthi.stockapp;



import java.util.Comparator;


public class Stock implements Comparable<Stock>{
    private String StockSys;
    private String CompanyName;
    private String Price;
    private String Pricechange;
    private String PercentageChange;
    private String PreviousClose;
    double d;

    public void setPercentageChange(String percentageChange) {
        PercentageChange = percentageChange;
    }

    public void setPricechange(String pricechange) {
        Pricechange = pricechange;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setStockSys(String stockSys) {
        StockSys = stockSys;
    }

    public void setPreviousClose(String previousClose){PreviousClose = previousClose;}

    public String getStockSys() {
        return StockSys;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getPrice() {
        return Price;
    }

    public String getPricechange() {
        return Pricechange;
    }

    public String getPreviousClose(){
        return PreviousClose;
    }

    public String getPercentageChange() {
        return PercentageChange;
    }
    public String toString(){
        return StockSys+CompanyName+Price+Pricechange+PercentageChange;
    }

    @Override
    public int compareTo(Stock o) {
        return 0;
    }
    public static Comparator<Stock> sortit
            = new Comparator<Stock>() {

        @Override
        public int compare(Stock o1, Stock o2) {
            String s=o1.getStockSys().toUpperCase();
            String s1=o2.getStockSys().toUpperCase();
            return s.compareTo(s1);
        }

    };
}
