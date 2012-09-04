package net.rambaldi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class StreamCopierThread extends Thread {

    final InputStream in;
    final OutputStream out;

    StreamCopierThread(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            for (;;) {
                byte[] bytes = new byte[1];
                int read = in.read(bytes);
                if (read>0) {
                    out.write(bytes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
