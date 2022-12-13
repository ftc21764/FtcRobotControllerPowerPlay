package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class SignalSleeveRecognizer {
    /*
     * Specify the source for the Tensor Flow Model.
     * If the TensorFlowLite object model is included in the Robot Controller App as an "asset",
     * the OpMode must to load it using loadModelFromAsset().  However, if a team generated model
     * has been downloaded to the Robot Controller's SD FLASH memory, it must to be loaded using loadModelFromFile()
     * Here we assume it's an Asset.    Also see method initTfod() below .
     */
    //private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite"; //FIRST's Model
    private static final String TFOD_MODEL_ASSET = "21764 PowerPlay Model v4.3.tflite"; //NEW MODEL - TEST
    //private static final String TFOD_MODEL_ASSET = "21764_Model_v3.1_3.tflite"; //LATEST MODEL - about 7/10, more distance variety
    //private static final String TFOD_MODEL_ASSET = "21764_Model_v7.tflite"; //SECOND TO LAST MODEL - about 9/10, less distance variety
    //private static final String TFOD_MODEL_ASSET = "model_21764_v2.tflite"; //BACKUP - First Competition

    private static final String[] LABELS = {
            "1_tri",
            "2_gear",
            "3_dog"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AX6Atj//////AAABmQrjkgPTik2EgPJ10KqHivRUj752wUBoq7KTrZ9SAOotj3UHK8Xp/uDooJjeA2lhggy53aTdaagR4UhAzRon3CdXOkvppRHZt5+yEmhNpaKDJeVTt157XhK75NON/YXxW4hPYgj6V2O6Rkl6wpwL7k3Dhu0oVfSOp0pahdXaZEcu/+kMJO8Oqul6DOL9XvIMT1BZIGRcuNVsB0NcRyi5qUmOQfogLCTnAeOaJp5wOPtLpc1+gF8FsrTZhh3mqWNcxpWaTGi2NzSUgJWryN8aRN17Dgy8cfHZW5xpcb1qVlAGPpspjA/A/DOpX2BIwyfEXBDUxmnLdMZnZLi95tsnQ4PA/h+o4nIpLJwPEXc4TtOd";

    /**
     * vuforia is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * tfod is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Recognition bestRecognition = null;
    private double latestConfidence = 0.0;
    private final double THRESHOLD = 50.0; //Change based on testing
    public String recognitionLabel = null;

    public SignalSleeveRecognizer(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();


        /*
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         */
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can increase the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0/9.0);
        }

    }

    public void scan() {

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                //telemetry.addData("# Objects Detected", updatedRecognitions.size());

                // step through the list of recognitions and display image position/size information for each one
                // Note: "Image number" refers to the randomized image orientation/number
                for (Recognition recognition : updatedRecognitions) {
                    //double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                    //double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                    //double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                    //double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                    //telemetry.addData(""," ");
                    //telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                    //telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                    //telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);

                    //if (bestRecognition == null || (recognition.getConfidence() > bestRecognition.getConfidence())) {
                        //bestRecognition = recognition;
                        //recognitionLabel = bestRecognition.getLabel();
                    //}
                    if (recognition.getConfidence() * 100 >= THRESHOLD) {
                        recognitionLabel = recognition.getLabel();
                        latestConfidence = recognition.getConfidence() * 100;
                    }
                }
            }
            telemetry.addData("Latest Recognition: ", recognitionLabel);
            telemetry.addData("Recognition Confidence: ", "%.0f", latestConfidence);
        }
    }


    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
