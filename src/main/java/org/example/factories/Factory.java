package org.example.factories;

import org.example.exceptions.CreationException;

public interface Factory<Product> {
    Product create() throws CreationException;
}
