package org.firstinspires.ftc.teamcode.Framework;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.teamcode.Framework.Hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Paddle;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Outtake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.PaddleMotor;
import org.firstinspires.ftc.teamcode.Framework.Hardware.SensorColor;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision;

public abstract class BaseOpMode extends LinearOpMode {
    protected Drivetrain drivetrain;
    protected Paddle paddle;
    protected PaddleMotor paddleMotor;
    protected Intake intake;
    protected Outtake outtake;
    protected Spindexer spindexer;
    protected SensorColor colorSensor;
    protected Vision vision;
    protected Controller controller;
    protected IMU imuSensor;

    public final boolean PADDLE_MOTOR = false;
    
    public void initHardware() {

        // Drivetrain Motors (SAME ORDER IN HARDWARE CONFIG)

        DcMotor[] driveMotors = {
                hardwareMap.get(DcMotor.class, "motorFL"),
                hardwareMap.get(DcMotor.class, "motorBL"),
                hardwareMap.get(DcMotor.class, "motorFR"),
                hardwareMap.get(DcMotor.class, "motorBR")};

        drivetrain = new Drivetrain(driveMotors);


        drivetrain.setMotorDirections(new DcMotor.Direction[]{
                DcMotor.Direction.REVERSE, // motorFL
                DcMotor.Direction.REVERSE, // motorBL
                DcMotor.Direction.REVERSE, // motorFR
                DcMotor.Direction.FORWARD  // motorBR
        });
        
        
        // OTHER HARDWARE
        
        // TODO: remove all paddleMotor stuff soon, once confirmed we are not using it
        if(!PADDLE_MOTOR){
            paddle = new Paddle(hardwareMap.get(Servo.class, "paddleServo"));
            paddle.setDown();
        }else{
            paddleMotor = new PaddleMotor(hardwareMap.get(DcMotorEx.class,"paddleMotor"));
            paddleMotor.setDown();
        }

        intake = new Intake(hardwareMap.get(DcMotor.class, "intakeMotor"));
        intake.off();
        
        outtake = new Outtake(
                hardwareMap.get(DcMotor.class, "outtakeMotor"),
                hardwareMap.get(DcMotor.class, "guidingMotor")
        );
        outtake.off();

        spindexer = new Spindexer(hardwareMap.get(Servo.class, "spindexerServo"));
        spindexer.setSlotIn(1);

        colorSensor = new SensorColor (hardwareMap.get(RevColorSensorV3.class, "colorSensor"));

        vision = new Vision(hardwareMap.get(WebcamName.class, "vision"));

        controller = new Controller(gamepad1);

        imuSensor = initializeIMUSensor();
    }
    
    private IMU initializeIMUSensor()
    {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
        return imu;
    }
}