package com.example.exec;

/**
 * Created by THINK on 2017/1/25.
 */
public interface HandlerFactory<R> {
    Handler<R> createHandler();
}
