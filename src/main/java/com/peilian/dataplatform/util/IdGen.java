package com.peilian.dataplatform.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 以ip后16位掩码作为nodeId, 理论节点数为65536个。足以在k8s这样的环境内保证产生的ID全局唯一。
 * 时间 37 bit,10毫秒产生一批,以北京时间 cst 2011/1/1 1:1:1为标准差，87年左右,id可以持续到 cst 2098/2/7 14:45:30
 * 每10毫秒可以产生 2**9, 512个id.
 * 潜在乱序:在同10毫秒内，不同的节点上产生的id不是严格递增的。
 * total 64 bit
 * * <pre>{@code
 *  * +------+----------------------+----------------+-----------+
 *  * | sign |     delta seconds    | worker node id | sequence  |
 *  * +------+----------------------+----------------+-----------+
 *  * | bit(0)       38bits              16bits         9bits  |
 *  * +------+----------------------+----------------+-----------+
 *  原始  1bit          41bits              10bits         12bits
 *  * }</pre>
 * *
 */
@Slf4j
public class IdGen {
    public static final int NODE_SHIFT = 16;
    public static final int SEQ_SHIFT = 9;
    public static final int MAX_NODE = 65535;
    //    public static final int MAX_SEQUENCE = 131071;
    public static final int MAX_SEQUENCE = 511;

    public static String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    //北京时间 2011/1/1 1:1:1 CST 精确到10毫秒
    private final long stand = 129381486100L;
    private int sequence;
    private long referenceTime;
    private int sequenceIndex;
    private int node;

    /**
     * A snowflake is designed to operate as a singleton instance within the context of a node.
     * If you deploy different nodes, supplying a unique node id will guarantee the uniqueness
     * of ids generated concurrently on different nodes.
     *
     * @param node This is an id you use to differentiate different nodes.
     */
    public IdGen(int node) {
        if (node < 0 || node > MAX_NODE) {
            throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
        }
        this.node = node;
    }

    public IdGen(int node, short sequenceIndex) {
        if (node < 0 || node > MAX_NODE) {
            throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
        }
        if (sequenceIndex < 0 || sequenceIndex > MAX_SEQUENCE) {
            throw new IllegalArgumentException(String.format("rand seq must be between %s and %s", 0, MAX_SEQUENCE));
        }
        this.node = node;
        this.sequenceIndex = sequenceIndex;
    }

    public IdGen() {
        try {
            byte[] ipLast = getIpLastBytes(2);
            this.node = getNode(ipLast);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 对外提供获取Id的方法
     *
     * @return
     */
    public static long getId() {
        return idWork.INSTANCE.next();
    }


    public static String getHexString() {
        return Long.toHexString(idWork.INSTANCE.next());
    }

    /**
     * 获取62进制字符表示的snowflake id,用于生成短字符串
     *
     * @return
     */
    public static String get62Str() {
        return encode62(idWork.INSTANCE.next());
    }

    public static int getNode(byte[] bytes) {
        if (bytes == null) {
            return -1;
        }
        int len = bytes.length;
        if (bytes.length > 4 || bytes.length < 1) {
            return -1;
        }
        byte[] tmp = new byte[4];
        System.arraycopy(bytes, 0, tmp, 4 - len, len);
        return tmp[3] & 0xFF |
                (tmp[2] & 0xFF) << 8 |
                (tmp[1] & 0xFF) << 16 |
                (tmp[0] & 0xFF) << 24;
    }

    public static String getIp(long id) {
        short node = (short) ((id >> SEQ_SHIFT) & 0x0000FFFF);
        byte[] byteArray = new byte[2];
        byteArray[0] = (byte) (node & 0xFF);
        byteArray[1] = (byte) (node >> 8 & 0xFF);
        int ip3 = byteArray[1] & 0xFF;
        int ip4 = byteArray[0] & 0xFF;
        return "" + ip3 + "." + ip4;
    }

    /**
     * 获取ip地址后n个byte
     *
     * @param n n=1/2/3/4
     * @return byte[] null表示异常
     */
    public static byte[] getIpLastBytes(int n) {
        if (n < 1 || n > 4) {
            throw new IllegalArgumentException("n must be between 1 and 4");
        }
        try {
            byte[] tmp = InetAddress.getLocalHost().getAddress();
            int len = 0;
            if (tmp != null) {
                len = tmp.length;
            }
            if (len != 4) {
                throw new RuntimeException("获取本地ip地址失败,或者本地ip非ipv4");
            } else {
                byte[] rs = new byte[n];
                System.arraycopy(tmp, len - n, rs, 0, n);
                return rs;
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("获取本地ip地址失败");
        }
    }

    public static int getNode(long id) {
        id = id >> SEQ_SHIFT & 0xFFFF;
        return (int) id;
    }

    public static String encode62(long num) {
        if (num < 1) {
            throw new IllegalArgumentException("num must be greater than 0.");
        }
        StringBuilder sb = new StringBuilder();
        for (; num > 0; num /= 62) {
            sb.append(ALPHABET.charAt((int) (num % 62)));
        }
        return sb.toString();
    }

    private static long decode62(String str) {
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("str must not be empty.");
        }
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(62, i);
        }
        return result;
    }

    /**
     * Generates a k-ordered unique 64-bit integer. Subsequent invocations of this method will produce
     * increasing integer values.
     *
     * @return The next 64-bit integer.
     */
    public long next() {
        long currentTime;
//        currentTime= (currentTime-currentTime%100)/100;
        long counter;
        synchronized (this) {
            currentTime = System.currentTimeMillis() / 10;
            if (currentTime < referenceTime) {
                throw new RuntimeException(String.format("Last referenceTime %s is after reference time %s", referenceTime, currentTime));
            } else if (currentTime > referenceTime) {
                this.sequence = sequenceIndex;
            } else {
                if (this.sequence < IdGen.MAX_SEQUENCE) {
                    this.sequence++;
                } else {
                    //溢出处理
                    currentTime = tilNextSeconds(referenceTime);
                    sequence = 0;
                }
            }
            counter = this.sequence;
            referenceTime = currentTime;
        }
        return (currentTime - stand) << NODE_SHIFT << SEQ_SHIFT | (long) (node) << SEQ_SHIFT | counter;
    }

    /**
     * 阻塞到下10毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextSeconds(long lastTimestamp) {
        long timestamp = System.currentTimeMillis() / 10;
//        timestamp= (timestamp-timestamp%100)/100;
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis() / 10;
//            timestamp= (timestamp-timestamp%100)/100;
        }
        return timestamp;
    }

    public Date getDate(long id) {
        long tenMs = (id >> NODE_SHIFT >> SEQ_SHIFT) + stand;
        return new Date(tenMs * 10);
    }

    /**
     * 使用单列模式提供一个唯一实例
     *
     * @author yancy
     */
    private static class idWork {
        private static final IdGen INSTANCE = new IdGen();
    }

}
