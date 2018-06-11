package pl.hachiwari.PretiusFileBinder.log;

import java.io.*;

public class FileLog implements ILog {

    private final File file;

    public FileLog(File file) {
        this.file = file;
    }

    @Override
    public void log(String msg) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, false);
            fileWriter.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
