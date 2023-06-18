package org.example.factories.interfaces;

import org.example.exceptions.CreationException;

public interface Factory<Product> {
    Product create() throws CreationException;
}
