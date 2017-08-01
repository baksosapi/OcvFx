package com.company;

import org.apache.commons.lang3.ArrayUtils;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.IOException;
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
    private static String photoPath;
    private static LogReport logReport;
    private static int allZeroFace = 0;
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

        String cFile = fd.getFilePath("haarcascade_frontalface_alt.xml");
//        photoPath =  fd.getFilePath("./image");
//        photoPath =  fd.getFilePath("./image/lena.png");
//        photoPath =  fd.getFilePath("./imageTest");
        photoPath =  fd.getFilePath("./imageTestOSorigin");
//        photoPath =  fd.getFilePath("./imageTestOS");
//        photoPath =  fd.getFilePath("./imageTestOS/93001571.jpg");
//        photoPath =  fd.getFilePath("./imageTest/10000099.jpg"); // 2
//        photoPath =  fd.getFilePath("./imageTest/15000168.jpg"); // 8
//        photoPath =  fd.getFilePath("./imageTest/10000859.jpg"); // 4
//        photoPath =  fd.getFilePath("/Volumes/MacHD/Users/wildan/Myproject/SID/Images/Client/PhotoTest_051216");

        inFile = new File(photoPath);
//        System.out.println("Is Directory : "+inFile.isDirectory());
        faceDetector = new CascadeClassifier(cFile);

        processFile(photoPath);

        System.out.println("Undetected Face = "+allZeroFace);
//        processListFile();


    }

    private static void processListFile() {
        try (Stream<String> stream = Files.lines(Paths.get(fd.getFilePath("fileList.txt")))){

            list = stream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0;
        for (String path : list) {
            processImage(fd.getFilePath("./imageTest/" + path), 0, 0, 0);
            System.out.println("Process "+ i++ +"/"+list.size());
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

                    processImage(f.toString(), 1, 0, 0 );

//                System.out.println("Process : "+f.getName() +" - "+ Integer.toString(i) +" of "+fileFolder.length);
                }
                System.out.println("Undetected Face = "+allZeroFace);

//            }

            }
            

    }

    private static void processImage(String fName, double a, int b, int c) {
//        Mat image = Highgui.imread(fName);
        Mat image = Highgui.imread( fName, Imgproc.COLOR_RGB2GRAY );

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections, 1.05, 3, 0, new Size(), new Size());

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
        String[] faceSizes = new String[]{};
        String[] faceBuff = new String[0];
        //        System.out.println(String.format("Detected %s faces", numFace ));
        if (numFace > 0) {
            for (int i = 0; i < numFace; i++){
                faceSizes = new String[numFace];
                System.out.println(numFace);
                System.out.println(faceDetections.toArray()[i].size().toString());
                faceSizes[i] = faceDetections.toArray()[i].size().toString();
                faceBuff = ArrayUtils.addAll(faceBuff, faceSizes[i]);

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

//        System.out.println(Arrays.toString(faceDetections.toArray()));

//        saveOutputImage(fName, image, faceDetections);
    }

    private static void saveOutputImage(String fName, Mat image, MatOfRect faceDetections) {

        // Create rectangle to existing face
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        // Save Output OriImage+Rectangle
//        File outDir = new File("");
        String filename = fName+"_o.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);

    }

    private String getFilePath(String fileName) {

//        System.out.println(fileName);

        File file = new File(classLoader.getResource(fileName).getFile());

        return file.getPath();
    }
}
