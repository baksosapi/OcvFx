package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wildan on 7/27/17.
 */
class LogReport {


    BufferedWriter buff;
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_hhmmss");
    File dirlog = new File("./Report");
    File filelog = new File(dirlog, "FaceLog_"+dateFormat.format(date)+".csv" );


    LogReport(){


        String[] header = new String[]{
                "File_Name", "Face_Num"
        };

        try {
            if (!dirlog.exists())
                dirlog.mkdir();
            else
                System.out.println("Directory Exist "+dirlog.getPath());
            if (!filelog.exists())
                filelog.createNewFile();
            buff = new BufferedWriter(new FileWriter(filelog, true));

//            buff.write("FILENAME");
            for (String h : header) {
                buff.write(h);
                buff.write(44);
            }

            buff.write(10);
            buff.close();
//            buff.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void append(String fName, int numFace, String size) {

//        System.out.println("Number : "+numFace);

        try {

            buff = new BufferedWriter(new FileWriter(filelog, true));
            buff.write(fName);
            buff.write(44);
            buff.write((Integer.toString(numFace)));

            buff.write(10);
            buff.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
