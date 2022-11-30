package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwingArm {

    protected DcMotor swingArmMotor;
    protected DcMotor swingArmMotor2;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Gamepad gamepad;
    private final ElapsedTime runtime = new ElapsedTime();
    private int targetPositionCount;

    // low= the position that the linear slide goes to to pick up cones and/or deposit on ground junctions.
    // this should be at a height where the empty intake can comfortably fit over a stack of five cones.

    // middle= the position for the low and medium junctions
    // this should be at a height where the intake with a cone can comfortably fit over the low junction
    // when the four bar is low, and the medium junction when the four bar is high.

    // high= the position for the high junction
    // this should be at a height where the intake with a cone can comfortably fit over the high junction

    static final int PICKUP_POINT_COUNT = 3;
    static final int HIGH_POINT_COUNT = 233; //110? Adjust to same distance from robot as low
    //static final int TIMEOUT_SECONDS = 10;
    static final double UP_MAXIMUM_SPEED = 0.8;
    static final double DOWN_MAXIMUM_SPEED = 0.3;
    static final int ADJUSTMENT_COUNT = 2;
    static final double MOTOR_SCALE_DIFFERENCE = 1.0;
    boolean currentlyRunningToJunction = false;

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

        swingArmMotor2  = hardwareMap.get(DcMotor.class, "four_bar_two");
        //use the below line if the motor runs the wrong way!!
        swingArmMotor2.setDirection(DcMotor.Direction.REVERSE);
        swingArmMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        swingArmMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        swingArmMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Swing Arm Motor 1 Starting At",  "%7d",
                swingArmMotor.getCurrentPosition());
        telemetry.addData("Swing Arm Motor 2 Starting At",  "%7d",
                swingArmMotor2.getCurrentPosition());

    }

    /**
     * This function moves the swing arm to the height specified in 'position.'
     * It tells the motor to run to a position, then returns from the function.
     * @param position What position the swing arm will go to. There are two positions, low (1),
     * and high (2)
     */
    public void setPosition(int position) {
        double currentPosition = swingArmMotor.getCurrentPosition();
        if (position == 1) {
            targetPositionCount = PICKUP_POINT_COUNT;

        } else if (position == 2) {
            targetPositionCount = HIGH_POINT_COUNT;
        } else {
            return;
        }
        swingArmMotor.setTargetPosition(targetPositionCount);
        swingArmMotor2.setTargetPosition((int)(targetPositionCount * MOTOR_SCALE_DIFFERENCE));

        swingArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swingArmMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();
        if (targetPositionCount > currentPosition) {
            swingArmMotor.setPower(scaleUpMaximumSpeed(currentPosition));
            swingArmMotor2.setPower(scaleUpMaximumSpeed(currentPosition));
        } else {
            swingArmMotor.setPower(DOWN_MAXIMUM_SPEED);
            swingArmMotor2.setPower(DOWN_MAXIMUM_SPEED);
        }
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
            currentlyRunningToJunction = true;
        } else if (gamepad.b || gamepad.y) {
            setPosition(2);
            currentlyRunningToJunction = true;
        }

        if (gamepad.right_stick_y != 0) {
            targetPositionCount = Range.clip((int)(targetPositionCount + ADJUSTMENT_COUNT*-gamepad.right_stick_y), PICKUP_POINT_COUNT, HIGH_POINT_COUNT);
            swingArmMotor.setTargetPosition(targetPositionCount);
            swingArmMotor2.setTargetPosition((int)(targetPositionCount * MOTOR_SCALE_DIFFERENCE));
        } else if (!currentlyRunningToJunction) {
            targetPositionCount = swingArmMotor.getCurrentPosition();
        }
    }


    public void loop() {
        telemetry.addData("Swing Arm Motor 1 Position is:", swingArmMotor.getCurrentPosition());
        telemetry.addData("Swing Arm Motor 2 Position is:", swingArmMotor2.getCurrentPosition());
        readGamepad(gamepad);
        telemetry.addData("Swing arm target position", targetPositionCount);

        if (currentlyRunningToJunction) {
            if (!(swingArmMotor.isBusy() || swingArmMotor2.isBusy())) {
                currentlyRunningToJunction = false;
            }
        }
    }

    public void initLoop() {
        telemetry.addData("Swing Arm Motor 1 Position is:", swingArmMotor.getCurrentPosition());
        telemetry.addData("Swing Arm Motor 2 Position is:", swingArmMotor2.getCurrentPosition());
    }

    public void setBrakeMode(boolean b) {
        if (b) {
            swingArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            swingArmMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            swingArmMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            swingArmMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public double scaleUpMaximumSpeed(double currentPosition) {
        double scaledMaximumSpeed;
        if (currentPosition < (HIGH_POINT_COUNT / 2)) {
            return UP_MAXIMUM_SPEED;
        } else {
            scaledMaximumSpeed = Math.sin((currentPosition / (double) HIGH_POINT_COUNT) * 3.14159) * UP_MAXIMUM_SPEED;
        }
        return Math.max(0.1, scaledMaximumSpeed);
    }
}
