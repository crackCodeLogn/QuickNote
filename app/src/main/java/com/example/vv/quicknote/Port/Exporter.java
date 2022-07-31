package com.example.vv.quicknote.Port;

import android.os.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static com.example.vv.quicknote.constants.Constants.EXPORT_FILE_NAME;
import static com.example.vv.quicknote.constants.Constants.EXPORT_FOLDER_NAME;

/**
 * @author : Vivek
 * @version : 1.0
 * @lastMod : 30-12-2017
 * @since : 30-12-2017
 */

public class Exporter {
    public boolean export(String data) {
        final File mydir = new File(Environment.getExternalStorageDirectory() + "/" + EXPORT_FOLDER_NAME);
        if (!mydir.exists() && !mydir.mkdir()) return false;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(mydir, EXPORT_FILE_NAME)))) {
            bw.write(data);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }
}