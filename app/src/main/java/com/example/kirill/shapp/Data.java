package com.example.kirill.shapp;

import java.util.HashMap;

class Data {

    private HashMap<String, Integer> dataList;

    Data() {
        dataList = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            dataList.put(String.valueOf(i), 16);
        }
        for (int i = 5; i < 15; i++) {
            dataList.put(String.valueOf(i), 0);
        }
    }

    public HashMap<String, Integer> getDataList() {
        return dataList;
    }

    public void setDataList(HashMap<String, Integer> dataList) {
        this.dataList = dataList;
    }

}
