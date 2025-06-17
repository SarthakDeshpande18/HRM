package SRMS.FinalProject.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

//public class DatasetDownloader {
//    public static String downloadDataset(String url) {
//        String path = "D:/HRM/datasets/decision_tree_data.csv";
//        try (InputStream in = new URL(url).openStream();
//             FileOutputStream out = new FileOutputStream(path)) {
//            byte[] buffer = new byte[4096];
//            int len;
//            while ((len = in.read(buffer)) != -1) {
//                out.write(buffer, 0, len);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to download dataset: " + e.getMessage());
//        }
//        return path;
//    }
//}


public class DatasetDownloader {
    public static String downloadDataset(String urlStr) {
        String path = "E:/HRM/datasets/decision_tree_data.csv";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // <== This is IMPORTANT

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(path)) {
                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to download dataset: " + e.getMessage());
        }

        return path;
    }
}