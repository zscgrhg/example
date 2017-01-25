package com.example.exec;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 * Created by THINK on 2017/1/25.
 */
public class ProcessReader extends Thread {
    private final InputStream in;
    private final Charset charset;
    private final PMessage.Key key;
    private final PMessage.Key end_key;
    private final BlockingQueue<PMessage> queue;

    public ProcessReader(InputStream in,
                         Charset charset,
                         BlockingQueue<PMessage> queue) {
        this(in, charset, true, queue);
    }

    public ProcessReader(InputStream in,
                         Charset charset,
                         boolean isStdout,
                         BlockingQueue<PMessage> queue) {
        this.in = in;
        this.charset = charset;
        if (isStdout) {
            this.key = PMessage.Key.STDOUT;
            this.end_key = PMessage.Key.STDOUT_END;
        } else {
            this.key = PMessage.Key.STDERR;
            this.end_key = PMessage.Key.STDERR_END;
        }

        this.queue = queue;
    }


    @Override
    public void run() {

        Scanner scanner = new Scanner(in, charset.name());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            queue.add(new PMessage(key, line));
        }
        queue.add(new PMessage(end_key, null));
    }
}
