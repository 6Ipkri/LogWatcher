package th.co.logwatcher;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GetLogAsyncTask extends AsyncTask<Void, Void, String> {
    Context context;
    File myExternalFile;

    GetLogAsyncTask(Context context ) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myExternalFile = new File(context.getExternalFilesDir(Constants.filepath), Constants.fileName);

        // Read all .txt and compare day in week if same day then delete last week.
        String currentDay = new SimpleDateFormat("EEE", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        File file = new File(String.valueOf(context.getExternalFilesDir(Constants.filepath)));
        File[] list = file.listFiles();
        for (File f : list){
            if(f.getName().contains(currentDay)){
                if (!f.getName().equals(Constants.fileName)) {
                    f.delete();
                }
            }else if(f.getName().contains(".zip")){
                f.delete();
            }
        }
    }

    //Read log in .txt and current log then append to String and save to .txt file
    @Override
    protected String doInBackground(Void... params) {

        StringBuilder log = new StringBuilder();
        String oldLog = FileHelper.ReadFile(context);
        if (oldLog != null){
            log.append(oldLog);
        }
        String fileSaved = null;

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line+"\n");
            }
        } catch (IOException e) {
            return null;
        }

        if (log != null && log.length() > 0) {
            fileSaved = writeToFile(log.toString());
        }

        return fileSaved;
    }

    protected void onPostExecute(String result) {

        if (result != null) {
            try {
                Runtime.getRuntime().exec("logcat -c");

                Toast.makeText(context,
                        context.getString(R.string.toast_saved_1) + " " + result + " " + context.getString(R.string.toast_saved_2),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context,
                    context.getString(R.string.toast_error_saving),
                    Toast.LENGTH_SHORT).show();
        }
    }

    // put String to .txt
    private String writeToFile(String text) {

        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(text.getBytes());
            fos.close();
            Log.wtf(Constants.LOG_TAG , "writeToFile success");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myExternalFile.getName();
    }
}
