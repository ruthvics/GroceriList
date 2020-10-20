package com.ruthvicsai.sivakumar.grocerilist;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

public class BarcodeItem implements Parcelable {
    private String barcodeValue;
    private String name;
    private boolean isWebsite;


    /*
    constructor which sets
    barcodeValue = "no-barcode"
    name = "no-name"
     */
    public BarcodeItem()
    {
        barcodeValue = "no-barcode";
        name = "no-name";
    }

    /*
    constructor which sets
    barcodeValue = "no-barcode"
    name = "no-name"
    */
    public BarcodeItem(String s)
    {
        barcodeValue = s;
        name = "no-name";
    }

    /*
    constructor which sets both values
    to passed in values
     */
    public BarcodeItem(String mName, String result)
    {
        name = mName;
        barcodeValue = result;
    }

    protected BarcodeItem(Parcel in) {
        barcodeValue = in.readString();
        name = in.readString();
        isWebsite = in.readByte() != 0;
    }

    public static final Creator<BarcodeItem> CREATOR = new Creator<BarcodeItem>() {
        @Override
        public BarcodeItem createFromParcel(Parcel in) {
            return new BarcodeItem(in);
        }

        @Override
        public BarcodeItem[] newArray(int size) {
            return new BarcodeItem[size];
        }
    };


    public String getBarcodeValue()
    {
        return barcodeValue;
    }

    public String getBarcodeName(){ return name;}

    public void setBarcode(String item)
    {
        barcodeValue = item;
    }

    public void setName(String mName)
    {
        name = mName;
    }


    public boolean checkWebsite()
        {
        try {
            new URL(barcodeValue).toURI();
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcodeValue);
        dest.writeString(name);
        dest.writeByte((byte) (isWebsite ? 1 : 0));
    }

}
