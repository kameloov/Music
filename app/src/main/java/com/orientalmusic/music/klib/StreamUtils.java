package com.orientalmusic.music.klib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Created by kumait on 1/16/2016.
 */
public class StreamUtils {

    public static final int BUFFER_SIZE = 4096;

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int readed = 0;
        do {
            readed = inputStream.read(buffer);
            if (readed > 0)
                outputStream.write(buffer, 0, readed);
        } while (readed > 0);
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        copyStream(inputStream, outputStream, BUFFER_SIZE);
    }

    public static byte[] load(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copyStream(inputStream, outputStream, BUFFER_SIZE);
        return outputStream.toByteArray();
    }

    public static String getString(InputStream inputStream, Charset charset) throws IOException {
        byte[] buffer = load(inputStream);
        return new String(buffer, charset);
    }

    public static String getString(InputStream inputStream) throws IOException {
        return getString(inputStream, Charset.forName("utf-8"));
    }

    public static InputStream getStream(String s, Charset charset) {
        return new ByteArrayInputStream(s.getBytes(charset));
    }

    public static InputStream getStream(String s) {
        return new ByteArrayInputStream(s.getBytes(Charset.forName("utf-8")));
    }

    public static byte[] serialize(Serializable object) {
        ObjectOutputStream objectOutputStream = null;
        byte[] result = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                close(objectOutputStream);
            }
        }
        return result;
    }

    public static Object deserialize(byte[] bytes) {
        Object result = null;
        ObjectInputStream objectInputStream = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            result = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                close(objectInputStream);
            }
        }
        return result;
    }
}
