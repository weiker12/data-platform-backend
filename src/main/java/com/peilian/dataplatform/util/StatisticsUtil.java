package com.peilian.dataplatform.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengshangchao
 */
public class StatisticsUtil {

    /**
     * 将文件读取到list中去
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String> readToList(String filePath) throws IOException {
        List<String> list = new ArrayList<>();
        InputStream is = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine();
        while (StringUtils.isNotBlank(line)) {
            list.add(line);
            line = reader.readLine();
        }
        reader.close();
        return list;
    }

    /**
     * 将list写入文件
     *
     * @param list
     * @param filePath
     * @throws IOException
     */
    public static void listToFile(List<String> list, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        File file = null;
        if(Files.notExists(path)) {
            file = new File(filePath);
        }
        OutputStream os = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        for (Object str : list) {
            writer.write(str.toString());
            writer.newLine();;
        }
        writer.close();
        os.close();
    }

    /**
     * 音频统计
     */
    public static void audioStatistics() {
        String tengxunAudio = "/Users/zhengshangchao/Desktop/腾讯音频.txt";
        String shengwangAudio = "/Users/zhengshangchao/Desktop/声网音频.txt";
        String wangke = "/Users/zhengshangchao/Desktop/完课.txt";
        try {
            List<String> tengxunAudios = readToList(tengxunAudio);
            System.out.println("当天腾讯音频文件的条数为:" + tengxunAudios.size());
            List<String> shengwangAudios = readToList(shengwangAudio);
            System.out.println("当天声网音频文件的条数为：" + shengwangAudios.size());
            List<String> wankeList = readToList(wangke);
            List<String> normalList = (List<String>) CollectionUtils.union(tengxunAudios, shengwangAudios);
            System.out.println("当天正常的音频条数为：" + normalList.size());
            List<String> resultList = (List<String>) CollectionUtils.subtract(wankeList, normalList);
            System.out.println("当天完课音频条数 - 当天正常的音频条数 = 当天异常音频条数：" + resultList.size());
            String filePath = "/Users/zhengshangchao/Desktop/音频异常结果.txt";
            listToFile(resultList, filePath);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频统计
     */
    public static void videoStatistics() {
        String tengxunAudio = "/Users/zhengshangchao/Desktop/腾讯视频.txt";
        String shengwangAudio = "/Users/zhengshangchao/Desktop/声网视频.txt";
        String wangke = "/Users/zhengshangchao/Desktop/完课.txt";
        try {
            List<String> tengxunAudios = readToList(tengxunAudio);
            System.out.println("当天腾讯视频文件的条数为:" + tengxunAudios.size());
            List<String> shengwangAudios = readToList(shengwangAudio);
            System.out.println("当天声网视频文件的条数为：" + shengwangAudios.size());
            List<String> wankeList = readToList(wangke);
            List<String> normalList = (List<String>) CollectionUtils.union(tengxunAudios, shengwangAudios);
            System.out.println("当天正常的视频条数为：" + normalList.size());
            List<String> resultList = (List<String>) CollectionUtils.subtract(wankeList, normalList);
            System.out.println("当天完课视频条数 - 当天正常的视频条数 = 当天异常视频条数：" + resultList.size());
            String filePath = "/Users/zhengshangchao/Desktop/视频异常结果.txt";
            listToFile(resultList, filePath);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String script = "String convert(String percentage) {\n" +
                "\t\timport java.math.BigDecimal;\n" +
                "    BigDecimal compute = new BigDecimal(percentage);\n" +
                "    double result = compute.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();\n" +
                "    result = result * 100;\n" +
                "    return result + \"%\";\n" +
                "}";
        Convert convert = ConvertUtils.convert(script);
        String str = "0.6161";
        String result = convert.convert(str);
        System.out.println(result);
    }


}
