package com.increff.commons.sheet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public interface IDataFile<T> {

    public void setData(ArrayList<T> data);

    public ArrayList<T> getData();

    public void write(OutputStream os) throws IOException;

    public void read(InputStream is) throws Exception;

    public void setProgressMonitor(IProgressMonitor progress);

}
