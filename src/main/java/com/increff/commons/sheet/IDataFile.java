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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.function.Function;

public interface IDataFile<T> {
    public String getFileExtension();

    public void setMaxRows(Integer maxRows);

    public void setData(ArrayList<T> data);

    public ArrayList<T> getData();

    public void write(OutputStream os) throws IOException;

    public void read(InputStream is) throws Exception;

    public String[] getPartitioningColumns();

    public <K> Function<T, K> getPartitioningFunction();

    public void setProgressMonitor(IProgressMonitor progress);

}
