package org.firstinspires.ftc.teamcode.Framework;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Framework.Hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Flipper;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Outtake;
import org.firstinspires.ftc.teamcode.Framework.Hardware.SensorColor;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Spindexer;

public class BaseOpMode {
    protected Drivetrain drivetrain;
    protected Flipper flipper;
    protected Intake intake;
    protected Outtake outtake;
    protected Spindexer spindexer;

    protected SensorColor colorSensor;

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

        flipper = new Flipper(hardwareMap.get(Servo.class, "flipperServo"));
        flipper.setPosDown();

        intake = new Intake(hardwareMap.get(DcMotorEx.class, "intakeMotor"));
        intake.off();

        outtake = new Outtake(hardwareMap.get(DcMotorEx.class, "outtakeMotor"));
        outtake.off();

        spindexer = new Spindexer(hardwareMap.get(Servo.class, "spinServo"));
        spindexer.setIn(1);

        colorSensor = new SensorColor (hardwareMap.get(RevColorSensorV3.class, "colorSensor"));
    }
}
