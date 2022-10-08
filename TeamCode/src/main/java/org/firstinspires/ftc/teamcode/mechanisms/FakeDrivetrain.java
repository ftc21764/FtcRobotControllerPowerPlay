package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.TekerzBaseOpmode;

public class FakeDrivetrain extends TekerzBaseMechanism {

    // Every mechanism should have TELEOP, AUTO_START, STOP, and DONE in addition to its running states,
    // and should initialize the state variable to START.
    enum State {
        TELEOP,
        AUTO_START,
        STOP,
        DONE,
        PATH_1_0_FWD,
        PATH_1_1_TURN,
        PATH_1_2_APPROACH,
        PATH_1_3_WAIT_FOR_SCORE,
        PATH_1_4_REV,
        PATH_1_5_TURN,
        PATH_1_6_FWD,
        PATH_1_7_TURN,
        PATH_1_8_FWD,
        PATH_1_9_TURN,
        PATH_1_10_INTAKE,
        PATH_1_11_REV,
        PATH_1_12_TURN,
        PATH_1_13_APPROACH,
        PATH_1_14_WAIT_FOR_SCORE,
        PATH_1_15_REV,
        PATH_1_16_TURN,
        PATH_1_17_FWD,
        PATH_1_18_INTAKE,
        PATH_1_19_REV
    }

    State state;

    // Variables that are used for state management in autonomous driving
    double distance;
    double heading;
    double holdTime;
    double error;

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

    public void init_loop() {
        super.init_loop(state.toString());

        robot.telemetry.addData("Example Mechanism Init State", state);

        // If you have any state transitions during the init phase, handle them here
    }

    @Override
    public void start() {
        super.start();
    }

    public void loop() {
        super.loop(state.toString());

        switch(state) {
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
                //driveStraight(4, 0, PATH_1_1_TURN);
                break;
            case PATH_1_1_TURN:
                //turnToHeading();
                break;
            default:
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
}
