package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class SkyStoneBot extends OpMode implements Robot
{
    double RobotAngle = 0;

    DcMotor FrontRight;
    DcMotor FrontLeft;
    DcMotor RearRight;
    DcMotor RearLeft;

    double FrontRightPower = 0;
    double FrontLeftPower = 0;
    double RearRightPower = 0;
    double RearLeftPower = 0;

    IMU imu;

    double TargetRot = 0;
    double CloseEnoughThresholdRot = 5;
    boolean isRotating = false;

    @Override
    public void init()
    {
        imu = new IMU();
        //Get hardware components
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        RearRight = hardwareMap.get(DcMotor.class, "RearRight");
        RearLeft = hardwareMap.get(DcMotor.class, "RearLeft");
    }

    @Override
    public void loop()
    {
        //get robot angle
        RobotAngle = GetRobotAngle();

        if(isRotating)
        {
            if(CheckCloseEnoughRotation())
            {
                StopMotors();
                CalculateWheelSpeeds(RobotAngle);
                //set the powers of the motors
                FrontRight.setPower(FrontRightPower);
                FrontLeft.setPower(FrontLeftPower);
                RearRight.setPower(RearRightPower);
                RearLeft.setPower(RearLeftPower);
                isRotating = false;
            }
        }
    }

    @Override
    public void MoveAtAngle(double angle, double speed)
    {
        TargetRot = angle;
        isRotating = true;
    }

    @Override
    public void RotateTo(double angle, double speed)
    {
        if (angle > RobotAngle) //turn left
        {
            RearLeft.setPower(speed);
            FrontLeft.setPower(speed);
            FrontRight.setPower(speed);
            RearRight.setPower(speed);
        }
        if (angle < RobotAngle) //turn right
        {
            RearLeft.setPower(-speed);
            FrontLeft.setPower(-speed);
            FrontRight.setPower(-speed);
            RearRight.setPower(-speed);
        }
    }

    @Override
    public void RawTurn(boolean right, double speed)
    {
        if (!right) //turn left
        {
            RearLeft.setPower(speed);
            FrontLeft.setPower(speed);
            FrontRight.setPower(speed);
            RearRight.setPower(speed);
        }
        if (right) //turn right
        {
            RearLeft.setPower(-speed);
            FrontLeft.setPower(-speed);
            FrontRight.setPower(-speed);
            RearRight.setPower(-speed);
        }
    }

    @Override
    public void StopMotors()
    {
        FrontRight.setPower(0);
        FrontLeft.setPower(0);
        RearRight.setPower(0);
        RearLeft.setPower(0);
    }

    @Override
    public void CalculateWheelSpeeds(double degrees)
    {
        //Wheel speeds are calculated using cosine with a shift
        FrontRightPower = Math.cos(Math.toRadians(degrees + 45));
        FrontLeftPower = Math.cos(Math.toRadians(degrees - 45));
        RearRightPower = Math.cos(Math.toRadians(degrees - 45));
        RearLeftPower = Math.cos(Math.toRadians(degrees + 45));
    }

    @Override
    public float GetRobotAngle()
    {
        return imu.angles.firstAngle;
    }

    public boolean CheckCloseEnoughRotation()
    {
        if(GetRobotAngle() < TargetRot + CloseEnoughThresholdRot && GetRobotAngle() > TargetRot - CloseEnoughThresholdRot)
        {
            return true;
        }
        else return false;
    }
}
