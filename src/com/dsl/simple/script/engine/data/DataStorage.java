package com.dsl.simple.script.engine.data;

import java.util.HashMap;
import java.util.Map;

public class DataStorage
{
    private Map<String, String> data = new HashMap<>();

    public void update(String key, String value)
    {
        data.put(key, value);
    }

    public Map<String, String> getData()
    {
        return data;
    }

    public Map<String, String> mockData()
    {
        Map<String, String> mockData = new HashMap<>();
        mockData.put("PARAM_1", "5");
        mockData.put("PARAM_2", "5");
        mockData.put("PARAM_3", "10");
        mockData.put("PARAM_4", "20");
        mockData.put("PARAM_5", "");

        return mockData;
    }
}
