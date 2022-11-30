package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Left Side Autonomous", group="Robot")
//@Disabled

/**
 * LeftSideAutonomousPowerPlay
 * Select this opmode if your robot starts on the left of the field relative to your drivers, in position A5 or F2
 **/
public class LeftSideAutonomousPowerPlay extends PowerPlayAutonomous {

    public LeftSideAutonomousPowerPlay() {
        reverseTurnsForLeftSide = -1;
    }
}
