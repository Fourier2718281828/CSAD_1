package org.example.factories.interfaces;

import org.example.exceptions.CreationException;

public interface TripleParamFactory <Product, Param1, Param2, Param3> {
    Product create(Param1 param1, Param2 param2, Param3 param3) throws CreationException;
}
