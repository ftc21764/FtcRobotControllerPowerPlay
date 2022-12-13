package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Auto Medium And Park 21764", group="Robot")
@Disabled

public class AutoMediumAndPark extends PowerPlayAutonomous {

    public void runAutonomousProgram() {
        // Step through each leg of the path,
        // Notes:   Reverse movement is obtained by setting a negative distance (not speed)
        //          holdHeading() is used after turns to let the heading stabilize
        //          Add a sleep(2000) after any step to keep the telemetry data visible for review


        //BASE DRIVE PATH:
        //turn around
        // TODO: testing observed that the first driveStraight call doesn't work on the first run, only after a second run would this first call work.
        // TODO: adding this holdHeading call in the hope that it 'wakes up' the robot and the driveStraight command runs on first run.

        intake.pickUpCone();
        driveStraight(DRIVE_SPEED, 37.0, 0.0);
        driveStraight(DRIVE_SPEED, -8.5, 0.0);
        moveToMediumPosition();
        holdHeading(TURN_SPEED, 0.0,2.0);
        turnToHeading(TURN_SPEED, 45.0);
        driveStraight(DRIVE_SPEED, 12.0, 45.0);
        intake.dropCone();
        holdHeading(TURN_SPEED, 45.0, 2.0);
        driveStraight(DRIVE_SPEED, -12.0, 45.0);

        // Whether we start on right or start on left, we want the robot to turn to the right
        // to line up for parking, since the parking squares are ordered 1, 2, 3 from left to right
        // in all cases.
        reverseTurnsForLeftSide = 1; //Undo parking mirroring
        turnToHeading(TURN_SPEED, -90.0);
        moveToGroundPosition();
        holdHeading(TURN_SPEED, -90.0, 1.0);

        //check whether recognition label is null, if not, drive to parking space
        if (recognizer.recognitionLabel == null) {
            //Drive to parking 3
            driveStraight(DRIVE_SPEED, 24.0, -90.0);
        } else if (recognizer.recognitionLabel.startsWith("1")) {
            //Drive to parking 1
            driveStraight(DRIVE_SPEED, -24.0, -90.0);
        } else if (recognizer.recognitionLabel.startsWith("2")) {
            //Stay in parking 2
        } else {
            //Drive to parking 3
            driveStraight(DRIVE_SPEED, 24.0, -90.0);
        }
        turnToHeading(TURN_SPEED,0);
    }
}
