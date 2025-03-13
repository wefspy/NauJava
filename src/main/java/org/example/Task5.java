package org.example;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Task5 {

    public static void main(String[] args) throws InterruptedException {
        String url = "https://onlinetestcase.com/wp-content/uploads/2023/06/1-MB.pdf";
        String savePath = ".";

        FileDownloadTask downloadTask = new FileDownloadTask(url, savePath);
        System.out.println("Начинаем скачивание...");
        downloadTask.start();

        Thread.sleep(3000);
        System.out.println("Останавливаем загрузку...");
        downloadTask.stop();
    }

    public static class FileDownloadTask implements Task {
        private final String fileURL;
        private final String savePath;
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private volatile boolean isStopped = false;

        public FileDownloadTask(String fileURL, String savePath) {
            this.fileURL = fileURL;
            this.savePath = savePath;
        }

        @Override
        public void start() {
            executor.submit(() -> {
                try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                     BufferedOutputStream out = new BufferedOutputStream(fileOutputStream, 1024)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while (!isStopped && (bytesRead = in.read(buffer, 0, 1024)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    if (isStopped) {
                        System.out.println("Загрузка остановлена. Удаление файла...");
                        new File(savePath).delete();
                    } else {
                        System.out.println("Файл успешно загружен: " + savePath);
                    }

                } catch (IOException e) {
                    System.out.println("Ошибка загрузки файла: " + e.getMessage());
                }
            });
        }

        @Override
        public void stop() {
            isStopped = true;
            executor.shutdownNow();
        }
    }

    public interface Task {
        void start();

        void stop();
    }
}
