package org.firstinspires.ftc.teamcode.Framework.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Params;

@Config
@Autonomous(name = "Red Goal", preselectTeleOp = "JellyTele")
public class RedGoalAuto extends BaseOpMode {
    //poses
    Pose2d scanPose;
    Pose2d shootPose;
    Pose2d gatePose;
    Pose2d firstPose;
    Pose2d secondPose;
    Pose2d thirdPose;
    
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        Pose2d initialPose = new Pose2d(-61.5, 23.5, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Params.alliance = Params.Alliance.RED;
        
        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        
        TrajectoryActionBuilder moveToScan;
        scanPose = null; //pos1
        
        TrajectoryActionBuilder moveToShootPreload;
        shootPose = null; //pos2
        
        TrajectoryActionBuilder openGate;
        gatePose = null; //pos3
        
        TrajectoryActionBuilder collectFirst;
        firstPose = null; //pos4
        
        TrajectoryActionBuilder moveToShootFirst;
        
        TrajectoryActionBuilder collectSecond;
        secondPose = null; //pos5
        
        TrajectoryActionBuilder moveToShootSecond;
        
        TrajectoryActionBuilder collectThird;
        thirdPose = null; //pos6
        
        TrajectoryActionBuilder moveToShootThird;
    }
}