package org.firstinspires.ftc.teamcode.Tuners;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;


@TeleOp(name = "Vision Tuner", group = "Test")
public class VisionTuner extends LinearOpMode
{
    Vision vision;

    @Override
    public void runOpMode() throws InterruptedException
    {
        vision = new Vision(hardwareMap.get(WebcamName.class, "vision"));

        boolean togglePressed = false;
        int mode = 1;

        AprilTagDetection tag;
        double[] tagNav;

        ColorBlobLocatorProcessor.Blob blob;
        double[] blobNav;

        double percievedWidth;
        double distanceDouble=5;
        int distance=5;
        double focalLength=0;
        double focalLengthSum=0;
        double focalLengthAmount=0;

        boolean lockTele = false;

        waitForStart();

        while (opModeIsActive())
        {


            if (!lockTele) {
                if (mode==1) {
                    telemetry.addLine("Mode 1: Detecting Tags");
                    for (Object tagDetection : vision.getTags()){
                        tag = (AprilTagDetection) tagDetection;
                        telemetry.addLine();
                        if(21 <= tag.metadata.id && tag.metadata.id <= 23){
                            telemetry.addData("\tType: ", "Motif");
                            telemetry.addData("\tLabel: ", String.format("%s (%s)",tag.metadata.name, tag.metadata.id));
                        }else if (tag.metadata.id == 20 || tag.metadata.id == 24){
                            tagNav = vision.getTagNav(tag);
                            telemetry.addData("\tType: ", "Team Target");
                            telemetry.addData("\tLabel: ", String.format("%s (%s)",tag.metadata.name, tag.metadata.id));

                            telemetry.addData("\tRange: ",tagNav[0])
                                    .addData("Bearing: ", tagNav[1])
                                    .addData("Elevation", tagNav[2])
                                    .addData("Yaw", tagNav[3]);
                        }else{
                            telemetry.addData("\tType: ", "Unrecognized Tag");
                        }
                    }
                }else if (mode==2){
                    telemetry.addLine("Mode 2: Detecting Blobs/Artifacts");
                    for (Object blobDetection : vision.getGreenArtifacts()){
                        telemetry.addLine();
                        blob = (ColorBlobLocatorProcessor.Blob) blobDetection;
                        telemetry.addData("\tColor: ","Green");
                        blobNav = vision.getArtifactLocation(blob);
                        telemetry.addData("\tApprox. Dist: ",blobNav[0])
                                .addData("Bearing: ",blobNav[1]);

                    }
                    for (Object blobDetection : vision.getPurpleArtifacts()){
                        telemetry.addLine();
                        blob = (ColorBlobLocatorProcessor.Blob) blobDetection;
                        telemetry.addData("\tColor: ","Purple");
                        blobNav = vision.getArtifactLocation(blob);
                        telemetry.addData("\tApprox. Dist: ",blobNav[0])
                                .addData("Bearing: ",blobNav[1]);

                    }
                } else{
                    telemetry.addLine("Mode 3: Focal Length Calibration (Use a purple artifact)");
                    blob = (ColorBlobLocatorProcessor.Blob) vision.getPurpleArtifacts()[0];
                    percievedWidth = blob.getCircle().getRadius()*2;
                    focalLength = (percievedWidth*distance) / 5;

                    focalLengthAmount+=1;
                    focalLengthSum += focalLength;
                    telemetry.addData("Distance (in): ",distance);
                    telemetry.addData("Width (px): ",percievedWidth);
                    telemetry.addData("Focal Length (in): ", focalLength);

                    telemetry.addData("\nAvg. Focal Length (in)", focalLengthSum/focalLengthAmount);

                    if (gamepad1.dpad_left){
                        distanceDouble -= 0.0005;
                        distance = (int) distanceDouble;
                    }else if (gamepad1.dpad_right){
                        distanceDouble += 0.0005;
                        distance = (int) distanceDouble;
                    }
                }

                if (gamepad1.cross && !togglePressed){
                    mode = mode==3? 1 : mode+1;
                    togglePressed = true;
                }else if (!gamepad1.cross && togglePressed){
                    togglePressed = false;
                }


                telemetry.update();
            }

            if(gamepad1.circleWasPressed()){
                lockTele = !lockTele;
            }
        }
    }
}