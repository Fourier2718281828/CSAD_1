package org.example.factories.interfaces;

import org.example.exceptions.CreationException;

public interface DoubleParamFactory<Product, Param1, Param2> {
    Product create(Param1 param1, Param2 param2) throws CreationException;
}
