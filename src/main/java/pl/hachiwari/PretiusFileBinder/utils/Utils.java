package pl.hachiwari.PretiusFileBinder.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Utils {

    public static void moveFile(File file, File destination) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File %s is not exists", file));
        }

        if (!destination.exists() || !destination.isDirectory()) {
            throw new FileNotFoundException(String.format("Destination '%s' is not exists or not directory", destination));
        }

        try {
            FileUtils.moveFileToDirectory(file, destination, true);
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    public static void moveAndOverWriteFile(File file, File destination) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File %s is not exists", file));
        }

        if (!destination.exists() || !destination.isDirectory()) {
            throw new FileNotFoundException(String.format("Destination '%s' is not exists or not directory", destination));
        }

        File oldFile = new File(destination, file.getName());

        if (oldFile.exists()) {
            oldFile.delete();
        }

        try {
            FileUtils.moveFileToDirectory(file, destination, true);
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}
