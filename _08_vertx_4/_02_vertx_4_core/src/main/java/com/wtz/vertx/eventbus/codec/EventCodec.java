package com.wtz.vertx.eventbus.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class EventCodec<T> implements MessageCodec<T, T> {

    private static final Logger LOG = LoggerFactory.getLogger(EventCodec.class);

    @Override
    public void encodeToWire(Buffer buffer, T event) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(event);
            out.flush();
            byte[] eventBytes = bos.toByteArray();
            buffer.appendInt(eventBytes.length);
            buffer.appendBytes(eventBytes);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Encode event failed.", e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                LOG.error("Encode out stream close failed.", ex);
            }
        }

    }

    @Override
    public T decodeFromWire(int pos, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = pos;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Jump 4 because getInt() == 4 bytes
        byte[] yourBytes = buffer.getBytes(_pos += 4, _pos += length);
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            @SuppressWarnings("unchecked")
            T msg = (T) ois.readObject();
            ois.close();
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Listen failed " + e.getMessage(), e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                LOG.error("Decode in stream close failed.", ex);
            }
        }
    }

    @Override
    public T transform(T event) {
        return event;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
