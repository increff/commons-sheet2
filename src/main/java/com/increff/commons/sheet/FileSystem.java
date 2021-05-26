package com.increff.commons.sheet;

import java.io.*;

/*
 * ImDb Filesystem to read / write files
 */
public class FileSystem {

    private File baseDir;

    public FileSystem(File baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * Write contents of a IDataFile implementation object onto an output stream at specified location 'filePath'
     * @param filePath Path to output stream
     * @param sheet IDataFile implementation from which to fetch data for writing
     */
    public <T> void write(String filePath, IDataFile<T> sheet) throws Exception {
        OutputStream os = getOutputStream(filePath);
        sheet.write(os);
        os.close();
    }

    /**
     * Read data from a specified input stream onto a IDataFile implementation
     * @param filePath Location from which to read data
     * @param sheet IDataFile implementation on which to read the data
     */
    public synchronized <T> void read(String filePath, IDataFile<T> sheet) throws Exception {
        // Load popularityList
        FileInputStream is = getInputStream(filePath);
        sheet.read(is);
        is.close();
    }

    /**
     * Return a File object corresponding to specified path. If path pre-exists, deletes the file and re-creates
     * @param filePath Path to file for output stream
     * @return FileOutputStream object (non-append mode)
     */
    private FileOutputStream getOutputStream(String filePath) throws FileNotFoundException {
        File file = getFile(filePath);
        if (file.exists()) {
            file.delete();
        }
        return new FileOutputStream(file, false);
    }

    /**
     * Return file a FileInputStream for reading
     * @param filePath Path to input stream file
     * @return FileInputStream object (if exists)
     */
    private FileInputStream getInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(getFile(filePath));
    }

    private File getFile(String fileName) {
        return new File(baseDir, fileName);
    }

}
