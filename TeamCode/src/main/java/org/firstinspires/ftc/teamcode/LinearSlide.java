package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LinearSlide {

    protected DcMotor linearSlideMotor;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Gamepad gamepad;
    private ElapsedTime runtime = new ElapsedTime();

    // low= the position that the linear slide goes to to pick up cones and/or deposit on ground junctions.
    // this should be at a height where the empty intake can comfortably fit over a stack of five cones.

    // middle= the position for the low and medium junctions
    // this should be at a height where the intake with a cone can comfortably fit over the low junction
    // when the four bar is low, and the medium junction when the four bar is high.

    // high= the position for the high junction
    // this should be at a height where the intake with a cone can comfortably fit over the high junction

    static final int LOW_TARGET_COUNT = 10;
    static final int MIDDLE_TARGET_COUNT = 60;
    static final int HIGH_TARGET_COUNT = 110;
    static final int FIVE_STACK_INTAKE_COUNT = 7;
    static final int TIMEOUT_SECONDS = 10;
    static final double MAXIMUM_SPEED = 0.2;
    static final double ADJUSTMENT_SPEED = 0.1;


    public LinearSlide(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        linearSlideMotor  = hardwareMap.get(DcMotor.class, "CHANGETHIS");
        //use the below line if the motor runs the wrong way!!
        // linearSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        linearSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Linear Slide Starting At",  "%7d",
                linearSlideMotor.getCurrentPosition());

    }

    /**
     * This function moves the linear slide to the height specified in 'position.'
     * It tells the motor to run to a position, then returns from the function.
     * @param position What position the linear slide will go to. There are three positions, low (1),
     * medium (2) and high (3) There are also positions that only used when trying to intake. To descend
     * on a stack of five is -5, on a stack of four is -4, three is -3, etc. Only -5 is currently defined.
     */
    public void setPosition(int position) {
        int targetPosition;
        if (linearSlideMotor.isBusy()) {
            return;
        } else if (position == 1) {
            targetPosition = LOW_TARGET_COUNT;
        } else if (position == 2) {
            targetPosition = MIDDLE_TARGET_COUNT;
        } else if (position == 3) {
            targetPosition = HIGH_TARGET_COUNT;
        } else if (position == -5){
            targetPosition = FIVE_STACK_INTAKE_COUNT;
        } else {
            return;
        }
        linearSlideMotor.setTargetPosition(targetPosition);

        linearSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        linearSlideMotor.setPower(MAXIMUM_SPEED);

        telemetry.addData("Linear slide starting to run to position", position);
    }


    /**
     * Senses whether a button is being pushed, and whether it was being pushed last time you checked.
     * If a button is pushed, it will set the linear slide to a desired height. Push A for low,
     * B or X for medium, and Y for high.
     * Next, if the linear slide motor isn't busy with moving to a position, check if up or down
     * has been pressed on the dpad. If it has been, adjust the linear slide up or down, and if
     * it hasn't, make sure that the motor speed is 0.
     * @param gamepad What gamepad will be used
     */
    private void readGamepad(Gamepad gamepad) {
        if (gamepad.a) {
            setPosition(1);
        } else if (gamepad.x || gamepad.b) {
            setPosition(2);
        } else if (gamepad.y) {
            setPosition(3);
        }

        if (!linearSlideMotor.isBusy()) {
            if (gamepad.dpad_up) {
                if (linearSlideMotor.getCurrentPosition() <= HIGH_TARGET_COUNT) {
                    linearSlideMotor.setPower(ADJUSTMENT_SPEED);
                } else {
                    linearSlideMotor.setPower(0);
                }
            } else if (gamepad.dpad_down) {
                if (linearSlideMotor.getCurrentPosition() >= 5) {
                    linearSlideMotor.setPower(-ADJUSTMENT_SPEED);
                } else {
                    linearSlideMotor.setPower(0);
                }
            } else {
                linearSlideMotor.setPower(0);
            }
        }
    }


    public void loop() {
        if (linearSlideMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            if (!linearSlideMotor.isBusy() || (runtime.seconds() > TIMEOUT_SECONDS)) {
                //checks if a) the motor is done moving to a position, or b) it's been on for way too long
                linearSlideMotor.setPower(0);
                linearSlideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetry.addData("Linear Slide Position is:", linearSlideMotor.getCurrentPosition());
            } else {
                //if the motor is still moving, update the telemetry to show its position and target!
                telemetry.addData("Linear Slide is moving", "%7d of %7d",
                        linearSlideMotor.getCurrentPosition(), linearSlideMotor.getTargetPosition());
            }
        } else {
            telemetry.addData("Linear Slide Position is:", linearSlideMotor.getCurrentPosition());
            readGamepad(gamepad);
        }
    }
}
