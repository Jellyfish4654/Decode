package org.firstinspires.ftc.teamcode.Framework.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;

public abstract class BaseAuto extends BaseOpMode {
    // ↓ -------------- ↓ -------------- ↓ AUTO SHOOTING ACTIONS ↓ -------------- ↓ -------------- ↓
    SequentialAction swingPaddle () {
        return new SequentialAction(
                paddle.paddleUp(),
                new SleepAction(0.5),
                paddle.paddleDown(),
                new SleepAction(0.5)
        );
    }

    SequentialAction shootGPP () {
        return new SequentialAction(
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
        );
    }
    SequentialAction shootPGP () {
        return new SequentialAction(
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
        );
    }
    SequentialAction shootPPG () {
        return new SequentialAction(
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
        );
    }
    public class ShootMotif implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            switch (Params.motif) {
                case GPP:
                    Actions.runBlocking(
                            shootGPP()
                    );
                case PGP:
                    Actions.runBlocking(
                            shootPGP()
                    );
                case PPG:
                    Actions.runBlocking(
                            shootPPG()
                    );

            }
            return false;
        }
    }


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
