package com.hypocrite30.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * createDirectory：创建文件夹
 * copy(srcPath, desPath, 「StandardCopyOption.REPLACE_EXISTING 是否替换重名文件」)：复制
 * move(srcPath, desPath, 「StandardCopyOption.REPLACE_EXISTING 是否替换重名文件)：剪切或重命名
 * delete：删除文件
 * 递归操作。。。
 * @Description: 文件操作
 * @Author: Hypocrite30
 * @Date: 2021/10/8 21:49
 */
public class FilesDemo {
    public static void main(String[] args) {
        //createDirectory
        Path path = Paths.get("d:\\aa");
        try {
            Path directory = Files.createDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //copy
        Path path1 = Paths.get("d:\\aa\\01.txt");
        Path path2 = Paths.get("d:\\bb\\001.txt");
        try {
            // Path copy = Files.copy(path1, path2);
            Path copy = Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //move
        Path sourcePath = Paths.get("d:\\aa\\01.txt");
        Path destinationPath = Paths.get("d:\\aa\\01test.txt");
        try {
            Files.move(sourcePath, destinationPath);
            // Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            //移动文件失败
            e.printStackTrace();
        }


        //delete
        Path deletePath = Paths.get("d:\\aa\\001.txt");
        try {
            Files.delete(deletePath);
        } catch (IOException e) {
            // 删除文件失败
            e.printStackTrace();
        }

        /**
         * 文件递归操作
         */
        Path rootPath = Paths.get("d:\\aa"); // 根路径
        String fileToFind = File.separator + "002.txt"; // 待查找文件名
        System.out.println(fileToFind);
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileString = file.toAbsolutePath().toString();
                    //System.out.println("pathString = " + fileString);
                    if (fileString.endsWith(fileToFind)) {
                        System.out.println("file found at path: " + file.toAbsolutePath());
                        return FileVisitResult.TERMINATE; // 终止查找
                    }
                    return FileVisitResult.CONTINUE; // 继续查找
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
