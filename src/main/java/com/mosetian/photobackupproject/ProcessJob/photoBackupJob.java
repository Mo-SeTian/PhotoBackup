package com.mosetian.photobackupproject.ProcessJob;

import com.mosetian.photobackupproject.controller.photoBackupController;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class photoBackupJob {

    @Value("${photoBackup.folderToZip}")
    private String folderToZip;

    @Value("${photoBackup.targetZipFilePath}")
    private String targetZipFilePath;

    @Value("${photoBackup.password}")
    private String password;

    @Scheduled(cron = "0 0 3 * * 0")
    public void jobMain() throws Exception {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);
        String targetZipFilePathTemp = targetZipFilePath + formattedDate + ".zip"; // 压缩后的ZIP文件路径
        System.out.println("压缩开始 ========>");
        long startTime = System.nanoTime();
        zipFolder(targetZipFilePathTemp, folderToZip, password);
        long endTime = System.nanoTime();
        System.out.println("压缩结束 <========");
        double durationInSeconds = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("压缩总耗时 " + durationInSeconds + " 秒.");
    }

    public void zipFolder(String targetZipFilePath, String folderToZip, String password) throws Exception {
        photoBackupController.zipFolderMain(targetZipFilePath, folderToZip, password);
    }
}
