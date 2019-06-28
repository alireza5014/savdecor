package ir.savdecor.omid.savdecor.Utilities;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import ir.savdecor.omid.savdecor.Fragment.NewRequestFragment;
import ir.savdecor.omid.savdecor.R;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

/**
 * Created by alireza on 2018/10/22.
 */

public final class Helper  {

//    public static String basUrl = "http://192.168.43.72/savdecor/public/";
    public static String basUrl = "http://192.168.43.218/savdecor/public/";
//    public static String basUrl = "http://www.savdecor.ir/";
    public static String socketURL = "http://192.168.43.72:8005";
    public static String getVersionApp = "1.0.0";
    public static String PACKAGE_NAME = "com.example.alireza.dalang";
    public static String BAZAAR_LINK = "https://cafebazaar.ir/app/" + PACKAGE_NAME;
    public static String MYKET_LINK = "https://myket.ir/app/" + PACKAGE_NAME;
    public static String IRANAPPS_LINK = "http://iranapps.ir/app/" + PACKAGE_NAME;
    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    public static String getUserToken(Context context) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("user_token", "");


    }


    public static void message(Context context, String message) {

        new StyleableToast
                .Builder(context)
                .text(message)
                .cornerRadius(5)
                .font(R.font.iransansmobile)
                .textColor(Color.WHITE)
                .backgroundColor(Color.GRAY)
                //   .iconEnd(R.drawable.voice)
                .show();

        //    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


    }
    public static String CreateRandomString(int string) {

        Random random;
        random = new Random();
        String RandomAudioFileName = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    public static String CreateRandomInt(int string) {

        Random random;
        random = new Random();
        String RandomAudioFileName = "123456789";
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }


    public static boolean internetIsConnected() {
//return  true;
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    public static Boolean is_register(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("is_register", false);
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("user_id", 0);
    }

    public static String getFullName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("fname", "")+" "+preferences.getString("lname", "");
    }

    public static String getMobile(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("mobile","");
    }

    public static void Logout(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", "0");
        editor.putBoolean("is_register", false);
        editor.apply();
        editor.commit();


    }


    public static String currentShamsiDate() {
        PersianDate pdate = new PersianDate();
        PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
        return pdformater1.format(pdate);//1396/05/20
    }

    public static String currentShamsiDate(String format) {
        PersianDate pdate = new PersianDate();
        PersianDateFormat pdformater1 = new PersianDateFormat(format);

        return pdformater1.format(pdate);//1396/05/20
    }




    public static String currentShamsiTime() {
        PersianDate pdate = new PersianDate();
        PersianDateFormat pdformater1 = new PersianDateFormat("H:i:s");
        return pdformater1.format(pdate);// 14:12:12
    }

    public static String currentDate() {
        String pattern = "Y-m-d H:i:s";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return  date;
    }



}