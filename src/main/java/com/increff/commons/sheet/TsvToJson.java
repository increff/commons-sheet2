/*
 * Copyright (c) 2021. Increff
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
