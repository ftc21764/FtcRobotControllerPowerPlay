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
    private int targetPositionCount;

    // low= the position that the linear slide goes to to pick up cones and/or deposit on ground junctions.
    // this should be at a height where the empty intake can comfortably fit over a stack of five cones.

    // middle= the position for the low and medium junctions
    // this should be at a height where the intake with a cone can comfortably fit over the low junction
    // when the four bar is low, and the medium junction when the four bar is high.

    // high= the position for the high junction
    // this should be at a height where the intake with a cone can comfortably fit over the high junction

    static final int PICKUP_POINT_COUNT = 5;
    static final int HIGH_POINT_COUNT = 110; //110? Adjust to same distance from robot as low
    static final int TIMEOUT_SECONDS = 10;
    static final double MAXIMUM_SPEED = 0.99;
    static final double ADJUSTMENT_SPEED = 0.99;


    public SwingArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        swingArmMotor  = hardwareMap.get(DcMotor.class, "four_bar");
        //use the below line if the motor runs the wrong way!!
        swingArmMotor.setDirection(DcMotor.Direction.REVERSE);
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
        if (position == 1) {
            targetPositionCount = PICKUP_POINT_COUNT;

        } else if (position == 2) {
            targetPositionCount = HIGH_POINT_COUNT;
        } else {
            return;
        }
        swingArmMotor.setTargetPosition(targetPositionCount);

        swingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        swingArmMotor.setPower(MAXIMUM_SPEED);

        telemetry.addData("Swing arm starting to run to position", targetPositionCount);
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

        if (gamepad.dpad_right) {
            if (swingArmMotor.getCurrentPosition() <= HIGH_POINT_COUNT) {
                targetPositionCount++;
                swingArmMotor.setTargetPosition(targetPositionCount);
            }
        } else if (gamepad.dpad_left) {
            if (swingArmMotor.getCurrentPosition() <= HIGH_POINT_COUNT) {
                targetPositionCount--;
                swingArmMotor.setTargetPosition(targetPositionCount);
            }
        }
    }


    public void loop() {
        telemetry.addData("Swing Arm Position is:", swingArmMotor.getCurrentPosition());
        readGamepad(gamepad);
    }
}
