package th.co.logwatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Constants {

    public static String LOG_TAG = "LOG_TO_TXT";
    public static final String  filepath = "MyFileStorage";
    public static final String timeStamp = new SimpleDateFormat("EEE-dd-MM-yyyy", Locale.ENGLISH).format(Calendar.getInstance().getTime());
    public static final String fileName = "logcat_" + Constants.timeStamp + ".txt";

}
