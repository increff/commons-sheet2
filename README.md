# commons-lang

Commons sheet library is used for creating a structured way to perform file read and write operations and is used in
Imdb library.

## Overview

Commons sheet library is used for creating a structured way to perform file read and write operations and is used in
Imdb library. The file format is a tsv file and the data is stored in the form of a paged list. Every file contains
columns headers with each row having tokens that are tab separated. These tokens can be obtained using the column it
falls under. The columns are internally mapped using integers. Every token can then be parsed into an object of the
required datatype such as

- String
- Integer
- Double
- BigDecimal
- Boolean
- LocalDate

## Usage

### pom.xml

```xml
<dependency>
    <groupId>com.increff.commons</groupId>
    <artifactId>increff-commons-sheet</artifactId>
    <version>${increff-commons-sheet.version}</version>
</dependency>
```

## Key Classes
### DataRow 
The `DataRow` class represents a single row of tabular data along with its corresponding column
headings. The DataRow class has 3 basic data members

1. `String[] tokens`: It represents the actual row contents, stored in order as a list of Strings 
2. `HashMap<String, Integer> columns`: It represents the table's column headings. Each column's heading name is mapped to an index representing the
position of the column 
3. `int rowIndex`: The index of the row within the table 
   
An overview of the primary functions present in DataRow is given below

- `int getIndex(String col)`: Returns the column's position index based on the column's name
- `String[] getTokens()`: Returns list of all the entries within the row 
- `String getValue(int index)`: Returns value in a given column of the DataRow based on its column index 
- `String getValue(String col)`: Returns value in a given column of the DataRow based on its column name
- Other setters/getters for retrieving values from DataRow depending the data type to be retrieved 
  
### PagedList 
A PagedList is a List which loads its data in chunks (pages). The pages themselves are lists with an upper bound on their size.
Thus, PagedList is essentially a list of lists of Objects where the the lower-level lists have a limit on the number of
items they can retain (set to 100000 by default). When a page is filled up, a new page is created to accommodate newer
entries

### AbstractDataFile 
AbstractDataFile is an abstract class that provides the framework for dealing with files such as .csv,
.tsv, or other similar files.It provides functions to read and write such files from a specified Input/Output stream.
Once read, the table data is stored in a PagedList data member called data. The functions provided are

- `void read(InputStream is)`: Read and parse individual lines of data from an input stream and store them in a PagedList.
On reading each line, the content is split up based on a delimiter and converted into a DataRow representation. Each
DataRow is passed into read() method (which has to be overridden by the inheriting class, as per requirements). This
converts the DataRow into an arbitrary Object type that must be subsequently stored in the PagedList 
- `void write(OutputStream os)`: Allows writing the contents of the data PagedList on the requested Output Stream. Each element stored
in the PagedList, is passed to a write() function to convert the element back into a DataRow. The write() method is
implemented in an inheriting class, as per requirement. The return value of the write() call is a DataRow whose tokens
are fetched, concatenated and written to the output stream. 
  
Sub-classes extending AbstractDataFile must implement the following methods.

- Read - `protected abstract T read(DataRow rec) throws Exception` 
- Write - `protected abstract void write(DataRow rec, T t)`
- Get Headers - `protected abstract String[] getHeaders()`

### Example 
An example of the usage of AbstractDataFile is to implement a class to handle AopFile files is given below

```java
public class AopFile extends AbstractDataFile<AopRow> {

    @Override
    protected String[] getHeaders() {
        return new String[] { "channel", "period", "revenue"};
    }

    @Override
    protected AopRow read(DataRow r) throws Exception {
        AopRow o = new AopRow();
        o.channel = r.getInteger("channel");
        o.channel = r.getInteger("channel");
        o.revenue = r.getDouble("revenue");
        o.period = r.getInteger("period");
        return o;
    }

    @Override
    protected void write(DataRow r, AopRow o) {
        throw new RuntimeException("Write not implemented for " + AopRow.class.getName());
    }

} 
```
The above implementation allows the reading/writing of AopFiles as AopRow objects into the PagedList

## License

Copyright (c) Increff

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "
AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.
