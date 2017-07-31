package com.company;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FaceDetector {

    static FaceDetector fd;
    private static String photoName;
    private static CascadeClassifier faceDetector;
    private static String photoPath;
    private static LogReport logReport;
    ClassLoader classLoader = getClass().getClassLoader();
    static File inFile;
    static List<String> list = new ArrayList<>();

    public static void main(String[] args) {
	// write your code here
        logReport = new LogReport();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");

        fd = new FaceDetector();

        String cFile = fd.getFilePath("haarcascade_frontalface_alt.xml");
//        photoPath =  fd.getFilePath("./image");
//        photoPath =  fd.getFilePath("./imageTest");
        photoPath =  fd.getFilePath("./imageTest/10000099.jpg"); // 2
//        photoPath =  fd.getFilePath("./imageTest/15000168.jpg"); // 8
//        photoPath =  fd.getFilePath("./imageTest/10000859.jpg"); // 4
//        photoPath =  fd.getFilePath("/Volumes/MacHD/Users/wildan/Myproject/SID/Images/Client/PhotoTest_051216");

        inFile = new File(photoPath);
        System.out.println("Is Directory : "+inFile.isDirectory());
        faceDetector = new CascadeClassifier(cFile);

//        processFile(photoPath);

        processListFile();


    }

    private static void processListFile() {
        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList.txt")))){

            list = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;
        for (String path : list) {
            processImage(fd.getFilePath("./imageTest/" + path));
            System.out.println("Process "+ i++ +"/"+list.size());
        }

    }

    private static void processFile(String photoPath) {

        if (!inFile.isDirectory()){

            processImage(photoPath);

        } else {

            File[] fileFolder = new File(photoPath).listFiles();

            int i = 0;
            assert fileFolder != null;
            for ( File f : fileFolder ){
                i++;
//                System.out.println(f.getPath());
                processImage(f.toString());

                System.out.println("Process : "+ Integer.toString(i) +" of "+fileFolder.length);
            }
        }

    }

    private static void processImage(String fName) {
//        Mat image = Highgui.imread(fName);
        Mat image = Highgui.imread( fName, Imgproc.COLOR_RGB2GRAY );

        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, 0, new Size(), new Size());

        //
        // https://stackoverflow.com/questions/20801015/recommended-values-for-opencv-detectmultiscale-parameters
        //
        faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, 2, new Size(50, 50), new Size());

        int numFace = faceDetections.toArray().length;

        System.out.println(String.format("Detected %s faces", numFace ));

        int pos = fName.lastIndexOf("/");
        if (pos > 0) {
            fName = fName.substring(pos+1);
        }

        logReport.append(fName, numFace );

        // Create rectangle to existing face
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        // Save Output OriImage+Rectangle
        String filename = "ouput.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);
    }

    private String getFilePath(String fileName) {

//        System.out.println(fileName);

        File file = new File(classLoader.getResource(fileName).getFile());

        return file.getPath();
    }
}
