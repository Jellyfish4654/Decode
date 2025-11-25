package org.firstinspires.ftc.teamcode.Framework;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Paddle;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Outtake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.SensorColor;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision;

public abstract class BaseOpMode extends LinearOpMode {
    protected Drivetrain drivetrain;
    protected Paddle paddle;
    protected Intake intake;
    protected Outtake outtake;
    protected Spindexer spindexer;

    protected SensorColor colorSensor;
    protected Vision vision;
    public void initHardware() {

        // wheel motors

        DcMotor[] driveMotors = {
                hardwareMap.dcMotor.get("motorFL"),
                hardwareMap.dcMotor.get("motorBL"),
                hardwareMap.dcMotor.get("motorFR"),
                hardwareMap.dcMotor.get("motorBR")};

        drivetrain = new Drivetrain(driveMotors);


        drivetrain.setMotorDirections(new DcMotorSimple.Direction[]{
                DcMotorSimple.Direction.FORWARD, // motorFL
                DcMotorSimple.Direction.REVERSE, // motorBL
                DcMotorSimple.Direction.REVERSE, // motorFR
                DcMotorSimple.Direction.REVERSE  // motorBR
        });

        paddle = new Paddle(hardwareMap.get(Servo.class, "paddleServo"));
        paddle.setPosDown();

        intake = new Intake(hardwareMap.get(DcMotorEx.class, "intakeMotor"));
        intake.off();

        outtake = new Outtake(hardwareMap.get(DcMotorEx.class, "outtakeMotor"));
        outtake.off();

        spindexer = new Spindexer(hardwareMap.get(Servo.class, "spinServo"));
        spindexer.setIn(1);

        colorSensor = new SensorColor (hardwareMap.get(RevColorSensorV3.class, "colorSensor"));

        vision = new Vision(hardwareMap.get(WebcamName.class, "vision"));
    }
}