package org.example.factories.hw3;

import java.io.*;

public class BufferedIOProcessorsFactory implements IOStreamProcessorsFactory{
    @Override
    public Reader createReader(InputStream istream) {
        return new BufferedReader(new InputStreamReader(istream));
    }

    @Override
    public Writer createWriter(OutputStream ostream) {
        return new BufferedWriter(new OutputStreamWriter(ostream));
    }
}
