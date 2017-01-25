package com.example.exec;

import java.util.concurrent.BlockingQueue;

/**
 * Created by THINK on 2017/1/25.
 */
public class Publisher<R> extends Thread {
    private final BlockingQueue<PMessage> queue;
    private final HandlerFactory<R> handlerFactory;
    private R r;

    public Publisher(BlockingQueue<PMessage> queue, HandlerFactory<R> handlerFactory) {
        this.queue = queue;
        this.handlerFactory = handlerFactory;
    }

    public R getResult() {
        return this.r;
    }

    private synchronized void publish(R r) {
        this.r = r;
    }

    @Override
    public void run() {
        boolean isInterrupted = isInterrupted();
        Handler<R> handler = handlerFactory.createHandler();
        boolean complete = false;
        while (!complete) {
            try {
                PMessage poll = queue.take();
                PMessage.Key key = poll.key;
                String line = poll.line;
                switch (key) {
                    case STDERR:
                        handler.receiveError(line);
                        break;
                    case STDOUT:
                        handler.receive(line);
                        break;
                    case STDERR_END:
                        handler.onStderrEnd();
                        break;
                    case STDOUT_END:
                        handler.onStdoutEnd();
                        break;
                    case FINISH:
                        handler.onComplete(Integer.parseInt(line.trim()));
                        complete = true;
                }
            } catch (InterruptedException e) {
                isInterrupted = true;
            } finally {
                if (queue.isEmpty() && isInterrupted) {
                    complete = true;
                }
            }
        }
        publish(handler.get());
    }
}
