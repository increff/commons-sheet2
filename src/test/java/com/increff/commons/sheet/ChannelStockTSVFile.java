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

public class ChannelStockTSVFile extends AbstractTSVFile<ChannelStockRow> {

    public ChannelStockTSVFile() {
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