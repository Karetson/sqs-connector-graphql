package com.example.demo.ports.out;

public interface SerializePort<T> {
    void serialize(T input);
}
