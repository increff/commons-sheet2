package com.increff.commons.sheet;

import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

public class DemoParquetFile extends AbstractParquetFile<DemoParquetRow>{

    @Override
    protected Type[] getSchema(){
        return new Type[]{
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT32, "day", OriginalType.DATE),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT32, "sku"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.DOUBLE, "revenue"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT32, "channel"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.DOUBLE, "discount"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT64, "mrp"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.INT64, "price_bucket"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BOOLEAN, "enabled"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BOOLEAN, "is_online"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BINARY, "style_code"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BINARY, "seasons"),
                new PrimitiveType(Type.Repetition.OPTIONAL, PrimitiveType.PrimitiveTypeName.BINARY, "store_codes")
        };
    }

    @Override
    protected DemoParquetRow read(GenericRecord r) {
        DemoParquetRow o = new DemoParquetRow();
        o.day = (LocalDate) r.get("day");
        o.channel = (int) r.get("channel");
        o.sku = (Integer) r.get("sku");
        o.revenue = (double) r.get("revenue");
        o.discount = (Double) r.get("discount");
        o.mrp = (long) r.get("mrp");
        o.price_bucket = (Long) r.get("price_bucket");
        o.enabled = (boolean) r.get("enabled");
        o.is_online = (Boolean) r.get("is_online");
        o.style_code = (String) r.get("style_code");
        o.seasons = new HashSet<>(Arrays.asList(((String) r.get("seasons")).split("#")));
        o.store_codes = Arrays.asList(((String) r.get("store_codes")).split("#"));
        return o;
    }

    @Override
    protected void write(GenericRecord r, DemoParquetRow o) {
        r.put("day", o.day);
        r.put("channel", o.channel);
        r.put("sku", o.sku);
        r.put("revenue", o.revenue);
        r.put("discount", o.discount);
        r.put("mrp", o.mrp);
        r.put("price_bucket", o.price_bucket);
        r.put("enabled", o.enabled);
        r.put("is_online", o.is_online);
        r.put("style_code",o.style_code);
        r.put("seasons", String.join("#", o.seasons));
        r.put("store_codes", String.join("#", o.store_codes));
    }
}
