package org.example.factories.interfaces;

import org.example.exceptions.CreationException;

public interface SingleParamFactory<Product, Param> {
    Product create(Param param) throws CreationException;
}
