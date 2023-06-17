package org.example.factories.hw3;

import java.io.OutputStream;
import java.io.Writer;

public interface OStreamParamFactory {
    Writer createWriter(OutputStream ostream);
}
