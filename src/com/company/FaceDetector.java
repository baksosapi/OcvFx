package com.company;

import org.apache.commons.lang3.ArrayUtils;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FaceDetector {

    static FaceDetector fd;
    private static String photoName;
    private static CascadeClassifier faceDetector;
    private static CascadeClassifier eyesDetector;
    private static String photoPath;
    private static LogReport logReport;
    private static int allZeroFace = 0;
    private static int allZeroEyes = 0;
    ClassLoader classLoader = getClass().getClassLoader();
    static File inFile;
    static List<String> list = new ArrayList<>();
    static Size faceSize;
    public static void main(String[] args) {
	// write your code here
        logReport = new LogReport();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");

        fd = new FaceDetector();

        String detFaces = fd.getFilePath("haarcascade_frontalface_alt.xml");
        String detEyes = fd.getFilePath("haarcascade_eye.xml");
//        photoPath =  fd.getFilePath("./image");
//        photoPath =  fd.getFilePath("./image/lena.png");
//        photoPath =  fd.getFilePath("./imageTest");
//        photoPath =  fd.getFilePath("./imageTestOSorigin");
//        photoPath =  fd.getFilePath("./imageTestOS");
//        photoPath =  fd.getFilePath("./imageTestOSorigin/15000168.jpg"); // 709
//        photoPath =  fd.getFilePath("./imageTestOSorigin/97003104.jpg"); // 709
        photoPath =  fd.getFilePath("./imageTestOSorigin/93005077.jpg"); // 112
//        photoPath =  fd.getFilePath("./imageTestOSorigin/97003519.jpg"); // 112
//        photoPath =  fd.getFilePath("./imageTestOSorigin/11001806.jpg");
//        photoPath =  fd.getFilePath("./imageTestOSorigin/12000190.jpg");
//        photoPath =  fd.getFilePath("./imageTestOSorigin/13000938.jpg");
//        photoPath =  fd.getFilePath("./imageTestOS/93001571.jpg");
//        photoPath =  fd.getFilePath("./imageTest/10000099.jpg"); // 2
//        photoPath =  fd.getFilePath("./imageTest/15000168.jpg"); // 8
//        photoPath =  fd.getFilePath("./imageTest/10000859.jpg"); // 4
//        photoPath =  fd.getFilePath("/Volumes/MacHD/Users/wildan/Myproject/SID/Images/Client/PhotoTest_051216");

        inFile = new File(photoPath);
//        System.out.println("Is Directory : "+inFile.isDirectory());

        faceDetector = new CascadeClassifier(detFaces);
        eyesDetector = new CascadeClassifier(detEyes);

//        processFile(photoPath);

        // Get List file from Text
        // List of Files from "txt"
        processListFile();

        System.out.println("======================================");
        System.out.println("Final Undetected Face = "+ allZeroFace);
        System.out.println("Final Undetected Eyes = "+ allZeroEyes);

    }

    private static void processListFile() {
//        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList.txt")))){
//        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList23.txt")))){
//        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList800.txt")))){
        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList5.txt")))){
//        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileUndetected.txt")))){
//        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileFaceSize.txt")))){

            list = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }


        int i = 0;
        for (String path : list) {
            i++;
//            System.out.println(path);
            processImage(fd.getFilePath("./imageTestOSorigin/" + path), 0, 0, 0);

            System.out.println("Process "+ i +"/"+ list.size());
//            System.out.println("Undetected Face = "+ allZeroFace);
        }

    }

    private static void processFile(String photoPath) {

        if (!inFile.isDirectory()){

            processImage(photoPath, 0, 0, 0);

        } else {

            File[] fileFolder = new File(photoPath).listFiles();

            int i = 0;
            assert fileFolder != null;

//            for (int j = 1; j < 10; j++) {
                allZeroFace = 0;
                for ( File f : fileFolder ){
                    i++;

//                System.out.println(f.getPath());
//                    double scaleImage = j*0.1;
//                    processImage(f.toString(), scaleImage, 0, 0 );

                    processImage(f.toString(), 0, 0, 0 );
                    System.out.println("Process : "+ i +"/"+ fileFolder.length);
                    System.out.println("Undetected Face = "+ allZeroFace);

//                System.out.println("Process : "+f.getName() +" - "+ Integer.toString(i) +" of "+fileFolder.length);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
                System.out.println("Undetected Face = "+ allZeroFace);

//            }

            }
            

    }

    private static void processImage(String fName, double a, int b, int c) {
//        Mat image = Highgui.imread(fName);
        Mat image = Highgui.imread( fName, Imgproc.COLOR_RGB2GRAY );

        MatOfRect faceDetections = new MatOfRect();
        MatOfRect eyesDetections = new MatOfRect();

//        System.out.println(fName);

        faceDetector.detectMultiScale(image, faceDetections, 1.05, 3, 0, new Size(116, 116), new Size());
        eyesDetector.detectMultiScale(image, eyesDetections);

        //
        // https://stackoverflow.com/questions/20801015/recommended-values-for-opencv-detectmultiscale-parameters
        //
        a = 1.01;
        b = 2;
        c = 0;
//        System.out.println(a);

//        faceDetector.detectMultiScale(image, faceDetections);
//        faceDetector.detectMultiScale(image, faceDetections, a, b, c, new Size(100, 100), new Size());
//        faceDetector.detectMultiScale(image, faceDetections, 1+a, b, c, new Size(50, 50), new Size());
//        faceDetector.detectMultiScale(image, faceDetections, 1.1, 3, 2, new Size(50, 50), new Size());

        int numFace = faceDetections.toArray().length;
        int numEyes = eyesDetections.toArray().length;

        String[] faceSizes, eyeSizes;
        String[] faceBuff = new String[0];
        String[] eyesBuff = new String[0];

        //        System.out.println(String.format("Detected %s faces", numFace ));
        if (numFace > 0) {
            for (int i = 0; i < numFace; i++){
                faceSizes = new String[numFace];
//                System.out.println(numFace);
//                System.out.println(faceDetections.toArray()[i].size().toString());
//                faceSizes[i] = Double.toString(faceDetections.toArray()[i].size().width);
                faceSizes[i] = faceDetections.toArray()[i].size().toString();
                faceBuff = ArrayUtils.addAll(faceBuff, faceSizes[i]);

//                Array facePoints[i] = faceDetections.

//                String[] both = ArrayUtils.addAll(faceSizes[i], faceDetections.toArray()[i].size().toString());
//
//  System.out.println(i+" - "+Arrays.toString(faceSizes));
            }
        } else {

            allZeroFace++;

        }

        int pos = fName.lastIndexOf("/");
        if (pos > 0) {
            fName = fName.substring(pos+1);
        }

//        logReport.append(fName, numFace, faceDetections.size().toString(), faceSize.toString() );
        logReport.append(fName, numFace, Arrays.toString(faceBuff));
//        System.out.println("Face Size: "+faceSize.toString());
        System.out.println("Face Size: "+ Arrays.toString(faceBuff));

//        System.out.println(Arrays.toString(faceDetections.toArray()));

        MatOfRect[] det = new MatOfRect[2];
        det[0] = faceDetections;
        det[1] = eyesDetections;
//        saveOutputImage(fName, image, faceDetections);
        saveOutputImage(fName, image, det);

    }

//    private static void saveOutputImage(String fName, Mat image, MatOfRect faceDetections) {
    private static void saveOutputImage(String fName, Mat image, MatOfRect[] mor) {

        // Create rectangle to existing face

        ArrayList<Integer> faceList =  new ArrayList<>();
        for (Rect rect : mor[0].toArray()) {

            faceList.add(rect.width);

            System.out.println(fName+" - "+ rect.x);

            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        for (Rect rect : mor[1].toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(255, 0, 0), 2);
            Core.putText(image, "Eye", new Point(rect.x, rect.y - 5), 1, 2, new Scalar(0, 0, 255));

        }

//        Imgproc.putText(image, "Eye", new Point(rect.x,rect.y-5), 1, 2, new Scalar(0,0,255));
//        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
//                new Scalar(200, 200, 100),2);


        // Save Output OriImage+Rectangle
//        File outDir = new File("");
        String filename = fName+"_o.png";
//        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);

    }

    private String getFilePath(String fileName) {

//        System.out.println(classLoader.getResource(fileName));

        File file = new File(classLoader.getResource(fileName).getFile());

        return file.getPath();
    }

    private void circlePoi(){

        // Get list face
        // Get list eyes
    }
}
