package com.hypocrite30.path;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path 需要用 Paths 工具类 .get(basePath, relativePath)来获取
 * @Description: NIO 的路径类Path
 * @Author: Hypocrite30
 * @Date: 2021/10/8 21:39
 */
public class PathDemo {
    public static void main(String[] args) {
        //创建path实例
        Path path = Paths.get("d:\\01.txt");
        //创建相对路径，前面是基本路径，后面是相对路径
        //文件夹
        Path projects = Paths.get("d:\\aa", "projects");
        //创建文件
        Path file = Paths.get("d:\\aa", "projects\\002.txt");

        String originalPath = "d:\\aa\\projects\\..\\yygh-project";
        Path path1 = Paths.get(originalPath);
        System.out.println("path1 = " + path1); // d:\aa\projects\..\yygh-project
        //路径标准化
        Path path2 = path1.normalize();
        System.out.println("path2 = " + path2); // d:\aa\yygh-project
    }
}
