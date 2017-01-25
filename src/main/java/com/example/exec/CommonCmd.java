package com.example.exec;

import java.nio.charset.Charset;

/**
 * Created by THINK on 2017/1/25.
 */
public class CommonCmd extends AbstractCmd<Integer> {
    public ExitValueHandler createHandler() {
        return new ExitValueHandler();
    }

    protected Charset charset() {
        return Charset.forName("GBK");
    }



    public static void main(String[] args) throws Exception {
        CommonCmd commonCmd = new CommonCmd();
        for (int i = 0; i < 10; i++) {
            Integer ping = commonCmd.excute("ping", "www.qq.com");
            System.out.println(ping);
        }
    }
}
