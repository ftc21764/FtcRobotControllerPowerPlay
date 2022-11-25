package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    protected DcMotor intakeMotor;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final Gamepad gamepad;
    private ElapsedTime runtime = new ElapsedTime();
    static final int AUTO_RUN_SECONDS = 2;
    static final double MAX_SPEED = 0.5;
    Gamepad currentGamepad = new Gamepad();
    Gamepad previousGamepad = new Gamepad();


    public Intake(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.gamepad = gamepad;
        intakeMotor  = hardwareMap.get(DcMotor.class, "intake"); //Define hardware
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void runIntake(double speed) {
        speed = Range.clip(speed, -1, 1);
        runtime.reset();
        intakeMotor.setPower(speed);
    }

    public void dropCone() {
        runIntake(-MAX_SPEED);
    }

    public void pickUpCone() {
        runIntake(MAX_SPEED);
    }

    public void loop() {
        if (intakeMotor.isBusy()) {
            if (runtime.seconds() > AUTO_RUN_SECONDS) {
                //checks if a) the motor is done moving to a position, or b) it's been on for way too long
                intakeMotor.setPower(0);
            }
        } else {
            readGamepad(gamepad);
        }
        telemetry.addData("Intake Power:", intakeMotor.getPower());
    }

    private void readGamepad(Gamepad gamepad) {
        try {
            previousGamepad.copy(currentGamepad);
            currentGamepad.copy(gamepad);
        } catch (RobotCoreException e) {
            // Ignore this exception - it shouldn't happen
        }

        if (!intakeMotor.isBusy()) {
            if (gamepad.right_trigger > 0) {
                intakeMotor.setPower(MAX_SPEED);
            } else if (gamepad.left_trigger > 0) {
                intakeMotor.setPower(-MAX_SPEED);
            } else {
                intakeMotor.setPower(0);
            }
        }

        if (currentGamepad.right_bumper && !previousGamepad.right_bumper) {
            //hi
            if (intakeMotor.isBusy()) {
                intakeMotor.setPower(0);
            } else {
                intakeMotor.setPower(MAX_SPEED);
            }
        }

        if (currentGamepad.left_bumper && !previousGamepad.left_bumper) {
            if (intakeMotor.isBusy()) {
                intakeMotor.setPower(0);
            } else {
                intakeMotor.setPower(-MAX_SPEED);
            }
        }
    }

}
