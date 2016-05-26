package com.shujushow.shiro.cluster.server.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xinshu on 2016/5/26.
 */
public class SerializableUtil {

    public static String serialize(Session session) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(session);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception exc) {
            throw new RuntimeException("serialize session error", exc);
        }
    }

    public static Session deserialize(String sessionStr) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(sessionStr));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Session) objectInputStream.readObject();
        } catch (Exception exc) {
            throw new RuntimeException("deserialize session error", exc);
        }
    }

}
