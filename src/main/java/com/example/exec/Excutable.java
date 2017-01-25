package com.example.exec;

/**
 * Created by THINK on 2017/1/25.
 */
public interface Excutable<R> {
    R excute(String[] args) throws Exception;
}
