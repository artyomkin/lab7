package common;

import java.io.*;

public class Serializer {
    public static byte[] serialize(Object obj) throws IOException {

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(b);
        out.writeObject(obj);
        return b.toByteArray();

    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException{
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(b);
        return in.readObject();
    }
}
