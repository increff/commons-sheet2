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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

public class FileSanityTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public static String IMG_FILE = "/com/increff/commons/sheet/cat.jpeg";
    public static String FILE = "/com/increff/commons/sheet/store.tsv";
    public static String INCOMPLETE_FILE = "/com/increff/commons/sheet/store_incomplete_record.tsv";


    public static InputStream getInputStream(String resource) {
        return FileSanityTest.class.getResourceAsStream(resource);
    }

    public static Set<String> getHeaders() {
        return new HashSet<>(Arrays.asList("channel", "store", "store_code", "city"));
    }

    @Test
    public void testRead() throws Exception {
        TsvFile reader = new TsvFile();
        reader.setHeaders(getHeaders());
        InputStream is = getInputStream(FILE);
        reader.read(is);
    }

    @Test
    public void testReadImage() throws Exception {
        expectedEx.expect(SheetException.class);
        TsvFile reader = new TsvFile();
        reader.setHeaders(getHeaders());
        InputStream is = getInputStream(IMG_FILE);
        reader.read(is);
    }

    @Test
    public void testReadExtraHeaders() throws Exception {
        TsvFile reader = new TsvFile();
        reader.setHeaders(getHeaders());
        InputStream is = getInputStream(INCOMPLETE_FILE);
        reader.read(is);
        assertTrue(reader.getErrors().stream().map(k -> k.getError()).collect(Collectors.toSet()).contains("Record length does not match that of the header"));
    }

}
