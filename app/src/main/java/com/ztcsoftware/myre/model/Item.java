package com.ztcsoftware.myre.model;

public class Item {
    int _id;
    String pN;
    String tZ;
    String dY;

    public Item() {
    }

    public Item(String pN) {
        this.pN = pN;
    }

    public Item(String pN,String tZ,String dY) {
        this.pN = pN;
        this.tZ = tZ;
        this.dY = dY;
    }

    public Item(int _id, String pN) {
        this._id = _id;
        this.pN = pN;
    }

    public String gettZ() {
        return tZ;
    }

    public void settZ(String tZ) {
        this.tZ = tZ;
    }

    public String getdY() {
        return dY;
    }

    public void setdY(String dY) {
        this.dY = dY;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getpN() {
        return pN;
    }

    public void setpN(String pN) {
        this.pN = pN;
    }
}
