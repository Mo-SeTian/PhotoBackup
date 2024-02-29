package com.mosetian.photobackupproject.controller;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/backup")
public class photoBackupController {

    @Value("${photoBackup.folderToZip}")
    private String folderToZip;

    @Value("${photoBackup.targetZipFilePath}")
    private String targetZipFilePath;

    @Value("${photoBackup.password}")
    private String password;

    //手动触发备份
    @GetMapping("/start")
    public String photoBackupHandle() {
        System.out.println("接收到手动备份请求,开始进行备份!");
        new Thread(() -> {
            try {
                jobMain();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("手动备份结束!");
        }).start();
        return "手动触发成功,开始备份!";
    }

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
        zipFolderMain(targetZipFilePath, folderToZip, password);
    }

    public static void zipFolderMain(String targetZipFilePath, String folderToZip, String password) throws Exception {
        try {
            // 创建ZipParameters实例并配置压缩级别、压缩方法和加密方法
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionLevel(CompressionLevel.NORMAL); // 设置压缩级别
            zipParameters.setCompressionMethod(CompressionMethod.DEFLATE); // 设置压缩方法
            zipParameters.setEncryptFiles(true); // 启用文件加密
            zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD); // 设置加密方法
            // 创建ZipFile实例并设置输出的ZIP文件路径
            ZipFile zipFile = new ZipFile(targetZipFilePath, password.toCharArray());
            // 设置zipFile为在后台线程运行
            zipFile.setRunInThread(true);
            // 将文件夹添加到zip文件
            zipFile.addFolder(new File(folderToZip), zipParameters);
            // 获取进度监视器并查询进度
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

            while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                // 每500毫秒打印一次进度信息
                System.out.printf("压缩进度: %d%%%n", progressMonitor.getPercentDone());
                Thread.sleep(500);
            }

            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
                System.out.println("文件夹压缩成功.");
            } else {
                throw new Exception("文件夹压缩遇到错误: " + progressMonitor.getException());
            }
        } catch (Exception e) {
            System.err.println("文件夹压缩失败,错误原因为: " + e);
            throw new Exception("文件压缩失败!");
        }
    }

}
