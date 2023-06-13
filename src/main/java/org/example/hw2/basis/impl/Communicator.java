package org.example.hw2.basis.impl;

import org.example.hw2.basis.Receiver;
import org.example.hw2.basis.Sender;

public interface Communicator extends Receiver, Sender, AutoCloseable {
}
