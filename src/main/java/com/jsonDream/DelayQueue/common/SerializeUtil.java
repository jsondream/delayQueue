package com.jsonDream.DelayQueue.common;


import java.io.*;

/**
 * <p>
 * java序列化工具类
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 15/12/24
 */
public class SerializeUtil {
    /**
     * 序列化一个对象
     *
     * @param object
     * @return
     * @throws
     */
    public static byte[] serialize(Serializable object) {
        byte[] bytes = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            bytes = baos.toByteArray();
            //            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 反序列化一个对象
     *
     * @param bytes
     * @return
     * @throws
     */
    public static Object unSerialize(byte[] bytes) {

        Object object = null;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }
}
