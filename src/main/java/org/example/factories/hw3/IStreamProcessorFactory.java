package org.example.factories.hw3;

import java.io.InputStream;
import java.io.Reader;

public interface IStreamProcessorFactory {
    Reader createReader(InputStream istream);
}
