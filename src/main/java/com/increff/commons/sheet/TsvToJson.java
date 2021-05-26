package com.increff.commons.sheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TsvToJson {

    public static ArrayList<HashMap<String, Object>> tsvToJson(File f) throws Exception {
        TsvFile tsvFile = new TsvFile();
        FileInputStream in = new FileInputStream(f);
        tsvFile.read(in);
        tsvFile.getHeaders();
        ArrayList<DataRow> list = tsvFile.getData();

        ArrayList<HashMap<String, Object>> values = new ArrayList<>();
        for (DataRow d : list) {
            Set<String> columns = d.getColumns().keySet();
            HashMap<String, Object> map = new HashMap<>();
            for (String s : columns) {
                map.put(s, d.getValue(s));
            }
            values.add(map);
        }
        in.close();
        return values;
    }

}
