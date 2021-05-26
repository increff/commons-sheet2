package com.increff.commons.sheet;

public class ChannelStockFile extends AbstractDataFile<ChannelStockRow> {

    public ChannelStockFile() {
        setProgressMonitor(new DataFileMonitor());
        setMaxRows(20_00_0000);
    }

    @Override
    protected String[] getHeaders() {
        return new String[]{"channel", "day", "sku", "qty"};
    }

    @Override
    protected ChannelStockRow read(DataRow r) throws Exception {
        ChannelStockRow o = new ChannelStockRow();
        o.channel = r.getInteger("channel");
        o.day = r.getLocalDate("day");
        o.sku = r.getInteger("sku");
        o.qty = r.getInteger("qty");
        return o;
    }

    @Override
    protected void write(DataRow r, ChannelStockRow o) {
        throw new RuntimeException("Write not implemented for " + ChannelStockRow.class.getName());

    }

}