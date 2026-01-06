package org.firstinspires.ftc.teamcode.Framework.Auto.Autons;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;

@Config
@Autonomous(name = "Blue Goal", preselectTeleOp = "JellyTele")
public class BlueGoalAuto extends BaseOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //build actions & trajectories here

        waitForStart();
        if (!isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction()
        );

    }
}