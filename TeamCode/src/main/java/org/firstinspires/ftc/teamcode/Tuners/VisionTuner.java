package org.firstinspires.ftc.teamcode.Tuners;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Flipper;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;


@TeleOp(name = "Vision Tuner", group = "Test")
public class VisionTuner extends BaseOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {

        boolean togglePressed = false;
        boolean isDetectingTags = true;

        AprilTagDetection tag;
        double[] tagNav;

        ColorBlobLocatorProcessor.Blob blob;
        double[] blobNav;


        waitForStart();

        while (opModeIsActive())
        {
            if (isDetectingTags) {
                telemetry.addLine("Detecting Tags");
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
            }else{
                telemetry.addLine("Detecting Blobs/Artifacts");
                for (Object blobDetection : vision.getGreenArtifacts()){
                    telemetry.addLine();
                    blob = (ColorBlobLocatorProcessor.Blob) blobDetection;
                    telemetry.addData("\tColor: ","Green");
                    blobNav = vision.getArtifactLocation(blob);
                    telemetry.addData("\tApprox. Dist: ",blobNav[0])
                            .addData("Bearing: ",blobNav[1]);

                }
            }

            if (gamepad1.a && !togglePressed){
                isDetectingTags = !isDetectingTags;
                togglePressed = true;
            }else if (!gamepad1.a && togglePressed){
                togglePressed = false;
            }

            telemetry.update();
        }
    }
}