/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Administrator
 */
public class Util {

    public static void copyFile(Path source, Path target, CopyOption... options) throws IOException {
        if (target.getParent() != null) {
            target.getParent().toFile().mkdirs();
        }
        Files.copy(source, target, options);
    }

    public static String md5File(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return DigestUtils.md5Hex(bytes);
    }

    public static String md5File(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String md5File(String filePath) {
        try ( FileInputStream input = new FileInputStream(filePath)) {
            return md5File(input);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
