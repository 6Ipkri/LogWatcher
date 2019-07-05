package th.co.logwatcher;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHelper {

    static File myExternalFile;

    //Read file txt from directory to return string
    public static String ReadFile(Context context){
        String line = null;

        try {
            File myExternalFile = new File(context.getExternalFilesDir(Constants.filepath), Constants.fileName);
            FileInputStream fileInputStream = new FileInputStream (myExternalFile);
            if(fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + System.getProperty("line.separator"));
                }
                fileInputStream.close();
                line = stringBuilder.toString();

                bufferedReader.close();
            }
        }
        catch(FileNotFoundException ex) {
            Log.d(Constants.LOG_TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(Constants.LOG_TAG, ex.getMessage());
        }
        return line;
    }

    // zip txt file
    public static void zip(Context context) throws IOException {

        File file = new File(String.valueOf(context.getExternalFilesDir(Constants.filepath)));
        File[] list = file.listFiles();

        String zipFile = context.getExternalFilesDir(Constants.filepath) + "/ZIP_" + Constants.timeStamp + ".zip" ;
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));

        try {
            int BUFFER_SIZE = 6 * 1024;
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < list.length; i++) {
                File f = list[i];
                if (f.getName().contains(".txt")){
                    myExternalFile = new File(context.getExternalFilesDir(Constants.filepath), f.getName());
                    FileInputStream fi = new FileInputStream(myExternalFile);
                    origin = new BufferedInputStream(fi, BUFFER_SIZE);
                    try {
                        ZipEntry entry = new ZipEntry(f.getName().substring(f.getName().lastIndexOf("/") + 1));
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                            out.write(data, 0, count);
                        }
                    } finally {
                        origin.close();
                    }
                }
            }
        } finally {
            out.close();
        }
    }


}