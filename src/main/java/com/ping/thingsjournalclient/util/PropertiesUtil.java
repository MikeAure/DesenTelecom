package com.ping.thingsjournalclient.util;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil {

    public static String getValue(String key) throws FileNotFoundException {
        Properties prop = new Properties();
        InputStream fileDataStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("server.properties");
        if (fileDataStream == null) {
            throw new FileNotFoundException("server.properties not found");
        }
//                BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(mapFile.toPath()));
//                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
//        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fileDataStream));
//        InputStream in = new FileInputStream("./src/main/resources/server.properties");
        try {
            prop.load(fileDataStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String) prop.get(key);
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(PropertiesUtil.getValue("dbUrl"));
    }
}
