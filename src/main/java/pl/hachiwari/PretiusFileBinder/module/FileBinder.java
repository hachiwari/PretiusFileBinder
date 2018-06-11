package pl.hachiwari.PretiusFileBinder.module;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import pl.hachiwari.PretiusFileBinder.log.FileLog;
import pl.hachiwari.PretiusFileBinder.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;

public class FileBinder {

    private static final File DIR_HOME = new File("HOME");
    private static final File DIR_DEV = new File("DEV");
    private static final File DIR_TEST = new File("TEST");
    private static final String[] EXTENSIONS = { "jar", "xml" };
    private static final String FILE_LOG_NAME = "count.txt";

    private final Queue<File> newFiles = new LinkedList<>();
    private FileLog fileLog;
    private int countFileInDev, countFileInTest;

    public FileBinder() {
        createDirectoryStructure();
    }

    public boolean checkNewFiles() {
        newFiles.addAll(FileUtils.listFiles(DIR_HOME, EXTENSIONS, true));
        return newFiles.isEmpty();
    }

    public void sort() {
        int count = newFiles.size();
        while(count-- > 0) {
            File file = newFiles.poll();
            if (file != null) {
                moveFileTODirectory(file);
            }
        }
        writeInfoToFileLog();
    }

    private void createDirectoryStructure() {
        try {
            FileUtils.forceMkdir(DIR_HOME);
            FileUtils.forceMkdir(DIR_DEV);
            FileUtils.forceMkdir(DIR_TEST);
            fileLog = new FileLog(new File(DIR_HOME, FILE_LOG_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveFileTODirectory(File file) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("H");
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            int hour = Integer.parseInt(df.format(attr.creationTime().toMillis()));

            switch (FilenameUtils.getExtension(file.getName())) {
                case "jar":
                    if (hour % 2 == 0) {
                        countFileInDev++;
                        Utils.moveAndOverWriteFile(file, DIR_DEV);
                    } else {
                        countFileInTest++;
                        Utils.moveAndOverWriteFile(file, DIR_TEST);
                    }
                    break;
                case "xml":
                    countFileInDev++;
                    Utils.moveAndOverWriteFile(file, DIR_DEV);
                    break;
                default:
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeInfoToFileLog() {
        fileLog.log(toString());
    }

    @Override
    public String toString() {
        return String.format("The number of all files: %d\nThe number of files moved to DEV: %d\nThe number of files moved to TEST: %d",
                (countFileInDev+countFileInTest), countFileInDev, countFileInTest);
    }
}
