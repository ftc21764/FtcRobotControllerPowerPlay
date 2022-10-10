package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.TekerzBaseOpmode;

public class FakeDrivetrain extends TekerzBaseMechanism {

    // Every mechanism should have TELEOP, AUTO_START, STOP, and DONE in addition to its running states,
    // and should initialize the state variable to START.
    // The remaining states are intended to assist with replicating the example
    // path followed by RobotAutoDriveByGyro_Linear to work in a state machine.
    enum State {
        TELEOP,
        AUTO_START,
        STOP,
        DONE,
        PATH_1_0_FWD,
        PATH_1_1_TURNR,
        PATH_1_2_FWD,
        PATH_1_3_TURNL,
        PATH_1_4_FWD,
        PATH_1_5_TURNR,
        PATH_1_6_REV
    }

    State state;

    // These constants define the desired driving/control characteristics
    // They can/should be tweaked to suit the specific robot drive train.
    static final double DRIVE_SPEED = 0.4;     // Max driving speed for better distance accuracy.
    static final double TURN_SPEED = 0.2;     // Max Turn speed to limit turn rate
    static final double HEADING_THRESHOLD = 1.0;    // How close must the heading get to the target before moving to next step.
    // Requiring more accuracy (a smaller number) will often make the turn take longer to get into the final position.

    // Declare hardware variables here like this:
    // DcMotor leftMotor;

    @Override
    public void init(TekerzBaseOpmode robot) {
        super.init(robot);

        // Initialize hardware variables here like this:
        // leftMotor = robot.hardwareMap.get(DcMotor.class, "left_motor");

        // Initialize state
        state = (robot.isTeleop() ? State.TELEOP : State.AUTO_START);

    }

    @Override
    public void init_loop() {
        super.init_loop(state.toString());

        robot.telemetry.addData("Example Mechanism Init State", state);

        // If you have any state transitions during the init phase, handle them here
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        super.loop(state.toString());

        switch (state) {
            case TELEOP:
                loop_teleop();
                break;
            case AUTO_START:
                state = State.STOP;
                break;
            case STOP:
                break;
            case DONE:
                break;
            case PATH_1_0_FWD:
                driveStraight(DRIVE_SPEED, 24.0, 0.0);    // Drive Forward 24"

                break;
            case PATH_1_1_TURNR:
                turnToHeading(TURN_SPEED, -45.0);               // Turn  CW to -45 Degrees
                holdHeading(TURN_SPEED, -45.0, 0.5);   // Hold -45 Deg heading for a 1/2 second

                break;
            case PATH_1_2_FWD:
                driveStraight(DRIVE_SPEED, 17.0, -45.0);  // Drive Forward 17" at -45 degrees (12"x and 12"y)
                break;
            case PATH_1_3_TURNL:
                turnToHeading(TURN_SPEED, 45.0);               // Turn  CCW  to  45 Degrees
                holdHeading(TURN_SPEED, 45.0, 0.5);    // Hold  45 Deg heading for a 1/2 second
                break;
            case PATH_1_4_FWD:
                driveStraight(DRIVE_SPEED, 17.0, 45.0);  // Drive Forward 17" at 45 degrees (-12"x and 12"y)
                break;
            case PATH_1_5_TURNR:
                turnToHeading(TURN_SPEED, 0.0);               // Turn  CW  to 0 Degrees
                holdHeading(TURN_SPEED, 0.0, 1.0);    // Hold  0 Deg heading for 1 second
                break;
            case PATH_1_6_REV:
                driveStraight(DRIVE_SPEED, -48.0, 0.0);    // Drive in Reverse 48" (should return to approx. staring position)
                break;
        }
    }

    @Override
    public void stop() {
        // Turn off all mechanisms here like this:
        // leftMotor.setPower(0.0);

        state = State.DONE;
        super.stop();
    }

    private void loop_teleop() {
        // Check robot.gamepad1 and robot.gamepad2 to figure out what to do here
    }

    // **********  HIGH Level driving functions.  ********************

    /**
     * Method to drive in a straight line, on a fixed compass heading (angle), based on encoder counts.
     * Move will stop if either of these conditions occur:
     * 1) Move gets to the desired position
     * 2) Driver stops the opmode running.
     *
     * @param maxDriveSpeed MAX Speed for forward/rev motion (range 0 to +1.0) .
     * @param distance      Distance (in inches) to move from current position.  Negative distance means move backward.
     * @param heading       Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *                      0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                      If a relative angle is required, add/subtract from the current robotHeading.
     */
    public void driveStraight(double maxDriveSpeed, double distance, double heading) {

    }

    /**
     * Method to spin on central axis to point in a new direction.
     * Move will stop if either of these conditions occur:
     * 1) Move gets to the heading (angle)
     * 2) Driver stops the opmode running.
     *
     * @param maxTurnSpeed Desired MAX speed of turn. (range 0 to +1.0)
     * @param heading      Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *                     0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                     If a relative angle is required, add/subtract from current heading.
     */
    public void turnToHeading(double maxTurnSpeed, double heading) {

    }

    /**
     * Method to obtain & hold a heading for a finite amount of time
     * Move will stop once the requested time has elapsed
     * This function is useful for giving the robot a moment to stabilize it's heading between movements.
     *
     * @param maxTurnSpeed Maximum differential turn speed (range 0 to +1.0)
     * @param heading      Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *                     0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                     If a relative angle is required, add/subtract from current heading.
     * @param holdTime     Length of time (in seconds) to hold the specified heading.
     */
    public void holdHeading(double maxTurnSpeed, double heading, double holdTime) {

    }
}