package com.increff.commons.sheet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.*;

public class DataFileTest {

    public static String CHANNELSTOCK_FILE = "/com/increff/commons/sheet/channelstock.tsv.gz";
    public static String CHANNELSTOCK_FILE_ERROR1 = "/com/increff/commons/sheet/channelstock_error1.tsv";
    public static String CHANNELSTOCK_FILE_ERROR2 = "/com/increff/commons/sheet/channelstock_error2.tsv";
    public static String CHANNELSTOCK_FILE_ERROR3 = "/com/increff/commons/sheet/channelstock_error3.tsv";
    public static String CHANNELSTOCK_FILE_ERROR4 = "/com/increff/commons/sheet/channelstock_error4.tsv";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public static InputStream getInputStream(String resource) {
        return DataFileTest.class.getResourceAsStream(resource);
    }

    @Test
    public void testReadSpeed() throws Exception {
        InputStream fileStream = getInputStream(CHANNELSTOCK_FILE);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        ChannelStockFile dataFile = new ChannelStockFile();
        dataFile.read(gzipStream);
        ArrayList<ChannelStockRow> data = dataFile.getData();
        assertEquals( 18_056_161, data.size());
    }

    @Test
    public void testFileWithErrors() throws Exception {
        InputStream fileStream;
        ChannelStockFile dataFile = new ChannelStockFile();
        fileStream = getInputStream(CHANNELSTOCK_FILE_ERROR1);
        dataFile.read(fileStream);
        assertFalse(dataFile.getErrors().isEmpty());
        assertTrue(dataFile.getErrors().stream().map(k -> k.getError()).collect(Collectors.toSet()).contains("For input string: \"C1\""));

        fileStream = getInputStream(CHANNELSTOCK_FILE_ERROR2);
        dataFile.read(fileStream);
        assertFalse(dataFile.getErrors().isEmpty());
        assertTrue(dataFile.getErrors().stream().map(k -> k.getError()).collect(Collectors.toSet()).contains("Record length does not match that of the header"));
        fileStream = getInputStream(CHANNELSTOCK_FILE_ERROR3);
        dataFile.read(fileStream);
        assertTrue(dataFile.getErrors().stream().map(k -> k.getError()).collect(Collectors.toSet()).contains("Text '2019-09-31' could not be parsed: Invalid date 'SEPTEMBER 31'"));

        expectedEx.expect(SheetException.class);
        expectedEx.expectMessage(" are not expected in the file");
        fileStream = getInputStream(CHANNELSTOCK_FILE_ERROR4);
        dataFile.read(fileStream);
        assertTrue(dataFile.getErrors().stream().map(k -> k.getError()).collect(Collectors.toSet()).contains("Invalid column:sku"));
    }

    @Test
    public void testWrite() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Write not implemented for com.increff.commons.sheet.ChannelStockRow");
        ChannelStockFile file = new ChannelStockFile();
        file.write(getDataRow(), new ChannelStockRow());
    }

    public static DataRow getDataRow() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("channel", 0);
        map.put("sku", 1);
        map.put("day", 2);
        map.put("qty", 3);
        DataRow r = new DataRow(map);
        r.setTokens(new String[]{"1", "1", "2018-01-23", "56"});
        return r;

    }

}
