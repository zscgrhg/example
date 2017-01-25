package com.example.exec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by THINK on 2017/1/25.
 */
public abstract class AbstractCmd<R> implements Excutable<R>, HandlerFactory<R> {


    protected Charset stdoutCharset() {
        return charset();
    }
    protected Charset charset() {
        return Charset.forName("UTF8");
    }
    protected Charset stderrCharset() {
        return charset();
    }


    protected List<String> getCommandLines(String... args) {
        return Arrays.asList(args);
    }

    protected void waitUntilProcessExit(Process process) throws Exception {
        process.waitFor();
    }

    protected Process createProcess(String... args) throws IOException {
        List<String> cmds = new ArrayList<String>();
        cmds.addAll(getCommandLines(args));
        ProcessBuilder pb = new ProcessBuilder(cmds);
        return pb.start();
    }

    public R excute(String... args) throws Exception {
        final BlockingQueue<PMessage> queue = new LinkedBlockingQueue<PMessage>();
        Process process = createProcess(args);
        InputStream errorStream = process.getErrorStream();

        ProcessReader stderrReader =
                new ProcessReader(errorStream, stderrCharset(), false, queue);
        stderrReader.start();
        InputStream inputStream = process.getInputStream();
        ProcessReader stdoutReader =
                new ProcessReader(inputStream, stdoutCharset(), queue);
        stdoutReader.start();
        Publisher<R> publisher = new Publisher<R>(queue, this);
        publisher.start();
        waitUntilProcessExit(process);
        stdoutReader.join();
        stderrReader.join();
        queue.add(new PMessage(PMessage.Key.FINISH, String.valueOf(process.exitValue())));
        publisher.join();
        return publisher.getResult();
    }

}
