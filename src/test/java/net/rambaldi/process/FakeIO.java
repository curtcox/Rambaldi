package net.rambaldi.process;

import net.rambaldi.process.IO;
import net.rambaldi.process.Timestamp;
import net.rambaldi.process.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class FakeIO implements IO {
    @Override public byte[] serialize(Serializable serializable) { return new byte[0]; }
    @Override public Serializable deserialize(byte[] bytes) { return null; }
    @Override public void write(OutputStream out, Transaction transaction) throws IOException {    }
    @Override public void write(OutputStream out, Timestamp timestamp) throws IOException {                  }
    @Override public void write(OutputStream out, byte[] bytes) throws IOException {           }
    @Override public Transaction readTransaction(byte[] bytes) throws IOException, ClassNotFoundException { return null; }
    @Override public Transaction readTransaction(InputStream in) throws IOException { return null; }
    @Override public Timestamp readTimestamp(InputStream in) throws IOException { return null; }
    @Override public byte[] readBytes(InputStream in) throws IOException { return new byte[0]; }
}
