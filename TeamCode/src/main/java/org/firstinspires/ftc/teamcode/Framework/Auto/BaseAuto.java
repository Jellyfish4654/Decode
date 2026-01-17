package org.firstinspires.ftc.teamcode.Framework.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;
import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;

public abstract class BaseAuto extends BaseOpMode {
    // ↓ -------------- ↓ -------------- ↓ AUTO SHOOTING ACTIONS ↓ -------------- ↓ -------------- ↓
    public class SpindexerTelemetry implements Action {
        int time = 0;
        SpindexerTelemetry (int t){
            time = t;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            telemetry.addLine(String.format("This time is the %s time.", time));
            telemetry.addLine(String.format("%s: Spindexer slot: %s", spindexer.getCurrentSlot(),time));
            telemetry.addLine(String.format("%s: Slot 1: %s", spindexer.getContents(1),time));
            telemetry.addLine(String.format("%s: Slot 2: %s", spindexer.getContents(2),time));
            telemetry.addLine(String.format("%s: Slot 3: %s", spindexer.getContents(3),time));
            telemetry.update();
            return false;
        }
    }
    
    public Action spindexerTelemetry(int time) { return new SpindexerTelemetry(time); }
    SequentialAction swingPaddle() { return new SequentialAction(
            paddle.paddleUp(),
            new SleepAction(0.3),
            paddle.paddleDown(),
            new SleepAction(0.3),
            spindexer.contentsSet(Artifact.NONE)
    );}

    SequentialAction shootGPP() { return new SequentialAction(
            new ParallelAction(
                    spindexer.greenOut(),
                    outtake.outtakeOnNear(),
                    new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_LONG/1000.0)
            ),
            swingPaddle(),
            spindexer.purpleOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            spindexer.purpleOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            outtake.outtakeOff()
    );}
    SequentialAction shootPGP() { return new SequentialAction(
            new ParallelAction(
                    spindexer.purpleOut(),
                    outtake.outtakeOnNear(),
                    new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_LONG/1000.0)
            ),
            swingPaddle(),
            spindexer.greenOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            spindexer.purpleOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            outtake.outtakeOff()
    );}
    SequentialAction shootPPG() { return new SequentialAction(
            new ParallelAction(
                    spindexer.purpleOut(),
                    outtake.outtakeOnNear(),
                    new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_LONG/1000.0)
            ),
            swingPaddle(),
            spindexer.purpleOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            spindexer.greenOut(),
            new SleepAction(JellyTele.SPIN_OUTTAKE_DELAY_SHORT/1000.0),
            swingPaddle(),
            outtake.outtakeOff()
    );}
    
    public class ShootMotif implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            switch (Params.motif) {
                case GPP:
                    Actions.runBlocking(
                            new ParallelAction(
                                    shootGPP(),
                                    spindexerTelemetry(1)
                            )
                    );
                    break;
                case PGP:
                    Actions.runBlocking(
                            shootPGP()
                    );
                    break;
                case PPG:
                    Actions.runBlocking(
                            shootPPG()
                    );
                    break;

            }
            return false;
        }
    }
    // intake movement constraint
    VelConstraint intakeMovementConstraint = new VelConstraint() {
        @Override
        public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
            return 10;
        }
    };


    // ↓ -------------- ↓ -------------- ↓ EXTRA AUTO ACTIONS ↓ -------------- ↓ -------------- ↓
    public class DetectArtifact implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            Params.Artifact detected = colorSensor.getArtifact();
            spindexer.setContents(detected);
            return spindexer.getContents(spindexer.getCurrentSlot())== Params.Artifact.NONE;
        }
    }
    public Action detectArtifact() { return new DetectArtifact(); }

    public class ScanMotif implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            Params.Motif motif = vision.getObeliskMotif();

            if(motif != null){
                Params.motif = motif;
                return false;
            }

            return true;
        }
    }
    public Action scanMotif() { return new ScanMotif(); }
}