package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity goal = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        RoadRunnerBotEntity aud = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeRedLight())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        aud.runAction(aud.getDrive().actionBuilder(new Pose2d(61.5, 23.5, Math.toRadians(180)))
                        .waitSeconds(299.99)
                        .turn(Math.toRadians(0.01))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
                        .build());

        goal.runAction(goal.getDrive().actionBuilder(new Pose2d(-61.5, 23.5, Math.toRadians(0)))
                .waitSeconds(299.99)
                .turn(Math.toRadians(-0.01))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
//                        .turn(Math.toRadians(90))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(goal)
                .addEntity(aud)
                .start();
    }
}