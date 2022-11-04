package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwingArm {

    protected DcMotor swingArmMotor;
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
    static final int HIGH_TARGET_COUNT = 110;
    static final int TIMEOUT_SECONDS = 10;
    static final double MAXIMUM_SPEED = 0.2;
    static final double ADJUSTMENT_SPEED = 0.1;


    public SwingArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        swingArmMotor  = hardwareMap.get(DcMotor.class, "CHANGETHIS");
        //use the below line if the motor runs the wrong way!!
        // linearSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        swingArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        swingArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        swingArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Swing Arm Starting At",  "%7d",
                swingArmMotor.getCurrentPosition());

    }

    /**
     * This function moves the swing arm to the height specified in 'position.'
     * It tells the motor to run to a position, then returns from the function.
     * @param position What position the swing arm will go to. There are two positions, low (1),
     * and high (2)
     */
    public void setPosition(int position) {
        int targetPosition;
        if (swingArmMotor.isBusy()) {
            return;
        } else if (position == 1) {
            targetPosition = LOW_TARGET_COUNT;
        } else if (position == 2) {
            targetPosition = HIGH_TARGET_COUNT;
        } else {
            return;
        }
        swingArmMotor.setTargetPosition(targetPosition);

        swingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        swingArmMotor.setPower(MAXIMUM_SPEED);

        telemetry.addData("Swing arm starting to run to position", targetPosition);
    }


    /**
     * Senses whether a button is being pushed.
     * If a button is pushed, it will set the swing arm to a desired height. Push A (ground/pickup)
     * or X (low junction) for low position, or B (medium junction) or Y (high junction) for high position.
     * Next, if the swing arm motor isn't busy with moving to a position, check if left or right
     * has been pressed on the dpad. If it has been, adjust the swing arm up (dpad right) or down (dpad left),
     * and if it hasn't, make sure that the motor speed is 0.
     * @param gamepad What gamepad will be used
     */
    private void readGamepad(Gamepad gamepad) {
        if (gamepad.a || gamepad.x) {
            setPosition(1);
        } else if (gamepad.b || gamepad.y) {
            setPosition(2);
        }

        if (!swingArmMotor.isBusy()) {
            if (gamepad.dpad_right) {
                if (swingArmMotor.getCurrentPosition() <= HIGH_TARGET_COUNT) {
                    swingArmMotor.setPower(ADJUSTMENT_SPEED);
                } else {
                    swingArmMotor.setPower(0);
                }
            } else if (gamepad.dpad_left) {
                if (swingArmMotor.getCurrentPosition() >= 5) {
                    swingArmMotor.setPower(-ADJUSTMENT_SPEED);
                } else {
                    swingArmMotor.setPower(0);
                }
            } else {
                swingArmMotor.setPower(0);
            }
        }
    }


    public void loop() {
        if (swingArmMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            if (!swingArmMotor.isBusy() || (runtime.seconds() > TIMEOUT_SECONDS)) {
                //checks if a) the motor is done moving to a position, or b) it's been on for way too long
                swingArmMotor.setPower(0);
                swingArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetry.addData("Swing Arm Position is:", swingArmMotor.getCurrentPosition());
            } else {
                //if the motor is still moving, update the telemetry to show its position and target!
                telemetry.addData("Swing Arm is moving", "%7d of %7d",
                        swingArmMotor.getCurrentPosition(), swingArmMotor.getTargetPosition());
            }
        } else {
            telemetry.addData("Swing Arm Position is:", swingArmMotor.getCurrentPosition());
            readGamepad(gamepad);
        }
    }
}
