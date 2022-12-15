package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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
    private int targetPositionCount = PICKUP_POINT_COUNT;

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
    static final double UP_MAXIMUM_SPEED = 0.9;
    static final double DOWN_MAXIMUM_SPEED = 0.3;
    static final int ADJUSTMENT_COUNT = 2;
    static final double MOTOR_SCALE_DIFFERENCE = 1.0;
    boolean currentlyRunningToJunction = false;
    boolean isAutonomous;

    public SwingArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean isAutonomous) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        this.isAutonomous = isAutonomous;

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
    }

    public void setPower(double currentPosition) {
        if (targetPositionCount > currentPosition) {
            //if it's going up, it needs to actually set the power xD It won't work if you take this out
            swingArmMotor.setPower(UP_MAXIMUM_SPEED);
            swingArmMotor2.setPower(UP_MAXIMUM_SPEED);
        } else if (!isAutonomous && (currentPosition <= PICKUP_POINT_COUNT + 10)) {
            // set holding power to 0 when teleop mode, but not in auto mode
            swingArmMotor.setPower(0);
            swingArmMotor2.setPower(0);
        } else {
            //We used to have different speeds for up and down, but that didn't seem to be helping. Now we just use UP_MAXIMUM_SPEED for everything.
            swingArmMotor.setPower(UP_MAXIMUM_SPEED);
            swingArmMotor2.setPower(UP_MAXIMUM_SPEED);
        }
    }

    /**
     * Senses whether a button is being pushed.
     * If a button is pushed, it will set the swing arm to a desired height.
     * If a is pushed, it will go to the low position, and the slide will also go to low to prepare to score on ground.
     * If b is pushed, it will go to the low position, and the slide will go to high to prepare to score on low.
     * If x is pushed, it will go to the high position, and the slide will go to low to prepare to score on medium.
     * If y is pushed, it will go to the high position, and the slide will also go to high to prepare to score on high.
     * Next, if the swing arm motor isn't busy with moving to a position, check if the right joystick is being used.
     * If it has been, adjust the swing arm up (joystick up) or down (joystick down),
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

        telemetry.addData("Gamepad right stick/left stick:", "%f %f", gamepad.right_stick_y, gamepad.left_stick_y);
        if (gamepad.right_stick_y > 0.1 || gamepad.right_stick_y < -0.1 ) {
            targetPositionCount = Range.clip((int)(targetPositionCount + ADJUSTMENT_COUNT*-gamepad.right_stick_y), PICKUP_POINT_COUNT, HIGH_POINT_COUNT);
            swingArmMotor.setTargetPosition(targetPositionCount);
            swingArmMotor2.setTargetPosition((int)(targetPositionCount * MOTOR_SCALE_DIFFERENCE));
        } else if (!currentlyRunningToJunction) {
            //This is so that if you let go of the joystick, it immediately stops the arm from moving. Not a bug!!!
            targetPositionCount = swingArmMotor.getCurrentPosition();
        }
    }


    public void loop() {
        double currentPosition = swingArmMotor.getCurrentPosition();
        telemetry.addData("Swing Arm Motor 1 Position is:", swingArmMotor.getCurrentPosition());
        telemetry.addData("Swing Arm Motor 2 Position is:", swingArmMotor2.getCurrentPosition());
        readGamepad(gamepad);
        setPower(currentPosition);
        telemetry.addData("Swing arm target position", targetPositionCount);
        telemetry.addData("SwingArmMotorPower", swingArmMotor.getPower());
        telemetry.addData("SwingArm Currently Running to Junction", currentlyRunningToJunction);

        if (currentlyRunningToJunction) {
            if (!(swingArmMotor.isBusy() || swingArmMotor2.isBusy())) {
                currentlyRunningToJunction = false;
            }
        }

        // Testing the PID loop rule coefficients
        //DcMotorEx samEx1 = (DcMotorEx)swingArmMotor;
        //DcMotorEx samEx2 = (DcMotorEx)swingArmMotor2;
        //int tolerance = samEx1.getTargetPositionTolerance();
        //PIDFCoefficients coeff = samEx1.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);

        //telemetry.addData("Swing Arm Motor 1 PIDF+tolerance", "%f, %f, %f, %f, %d",
        //        coeff.p, coeff.i, coeff.d, coeff.f, tolerance);
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
        // We originally developed this function in an attempt to slow the movement down
        // near the top of the swing arm's movement, since it was crashing into its hard stop.
        // Later on we learned that a motor has its maximum amount of torque when it's run at
        // 50% of its power. So we currently think the best power to use for all movement of
        // the swing arm is 50%. If it violently hits the hard stop in the "up" position, then
        // we need to examine the HIGH_POINT_COUNT and possibly make it lower, so that the
        // PID loop becomes responsible for slowing the movement at the top.
        return UP_MAXIMUM_SPEED;

        //double scaledMaximumSpeed;
        //if (currentPosition < (HIGH_POINT_COUNT / 2.0)) {
        //    return UP_MAXIMUM_SPEED;
        //} else {
        //    scaledMaximumSpeed = Math.sin((currentPosition / (double) HIGH_POINT_COUNT) * 3.14159) * UP_MAXIMUM_SPEED;
        //}
        //return Math.max(0.2, scaledMaximumSpeed);
    }
}
