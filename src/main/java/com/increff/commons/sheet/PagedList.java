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

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Paged list which is a list of lists. Each sub-list's size is less or equal pagesize
 * Why PageList and Why not ArrayList??
 * By Default ArrayList starts with size 10 and every time it gets filled up, it doubles up itself to a new
 * location in memory and copies the content from the previous version. This gets very costly when list size is large
 * Now what this PagedList is doing
 * 1. It is increasing with a much higher block size
 * 2. It is not copying data from previous version to new
 * So by creating sub-lists of bigger block size, PagedList proves itself to be efficient when handling large size data
 */
public class PagedList<T> {

    private int pageSize = 100000;
    private List<T> curPage;

    private List<List<T>> pages;

    public PagedList() {
        pages = new ArrayList<>();
        createNewPage();
    }

    /**
     * Add an element to the Paged List. If current page is full, creates a new page
     * @param t Element to be added
     */
    public void add(T t) {
        if (curPage.size() == pageSize) {
            createNewPage();
        }
        curPage.add(t);
    }

    /**
     * Return a simple list of all elements by merging all pages
     * @return List of all elements
     */
    public ArrayList<T> getAll() {
        ArrayList<T> all = new ArrayList<>(size());
        for (List<T> page : pages) {
            all.addAll(page);
        }
        return all;
    }

    /**
     * @return Total number of pages in Paged List
     */
    public int pageCount() {
        return pages.size();
    }

    /**
     * Create a new page and add to Paged List
     */
    private void createNewPage() {
        curPage = new ArrayList<>(pageSize);
        pages.add(curPage);
    }

    /**
     * Returns total number of elements stored in all pages of the Paged List
     * @return Count of elements
     */
    public int size() {
        int sum = 0;
        for (List<T> page : pages) {
            sum += page.size();
        }
        return sum;
    }

}
