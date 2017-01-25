package com.example.exec;

/**
 * Created by THINK on 2017/1/25.
 */
public interface Handler<R> {


    void onComplete(int processExitValue);

    void onStderrEnd();

    void onStdoutEnd();

    void receiveError(String line);

    void receive(String line);

    R get();
}
