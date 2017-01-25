package com.example.exec;

/**
 * Created by THINK on 2017/1/25.
 */
public class ExitValueHandler implements Handler<Integer> {
    private volatile int exitValue = 0;

    public void onComplete(int procesExitValue) {
        exitValue = procesExitValue;
    }

    public void onStderrEnd() {

    }

    public void onStdoutEnd() {

    }

    public void receiveError(String line) {

    }

    public void receive(String line) {
        System.out.println(line);
    }

    public Integer get() {
        return exitValue;
    }
}
