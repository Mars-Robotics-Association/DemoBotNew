package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class UDC_Teleop
{
     SkyStoneBot Bot = null;
     PIDAngleFollower angleFollower = null;

     double BaseDriveSpeedMultiplier = 1;
     double BaseTurnSpeedMultiplier = 1;
     private double DriveSpeedMultiplier = 1 ;
     private double TurnSpeedMultiplier = 1;
     private double DriveAngle;
     private boolean DriveAngleReseted = false;

    private double JoystickThreshold = 0.2;
    private double turnSpeed;

    private int[] MaxMotorPositions = {0,0,0,0};
    private int[] PreviousMotorPositions = {0,0,0,0};
    private int[] TotalMotorClicks = {0,0,0,0};
    boolean FirstRun = true;

    OpMode opmode;
    public boolean headlessMode = false;

    boolean rotatedRobot = false;

    public UDC_Teleop(OpMode thatopmode, boolean rotatedRobot)
    {
        opmode = thatopmode;
        this.rotatedRobot = rotatedRobot;
    }

    public void Init()
    {
        Bot = new SkyStoneBot(opmode, rotatedRobot);
        Bot.Init();
        angleFollower = new PIDAngleFollower();
    }

    public void Start()
    {
        Bot.Start();
    }

    public void Loop()
    {
        Bot.Loop();
    }

    public void UpdateTurnSpeed(double input)
    {
        turnSpeed = input;
    }

    //Reset Gyro if needed

    public void gyroOffset()
    {
        Bot.OffsetGyro();
    }

    //switch between normal and slow modes
    public void fullSpeed()
    {
        DriveSpeedMultiplier = BaseDriveSpeedMultiplier;
        TurnSpeedMultiplier = BaseTurnSpeedMultiplier;
    }
    public void threeFourthsSpeed()
    {
        DriveSpeedMultiplier = BaseDriveSpeedMultiplier*3/4;
        TurnSpeedMultiplier = BaseTurnSpeedMultiplier*3/4;
    }

    public void quarterSpeed()
    {
        DriveSpeedMultiplier = BaseDriveSpeedMultiplier/2;
        TurnSpeedMultiplier = BaseTurnSpeedMultiplier/2;
    }

    public void brake(double power)
    {
        Bot.Brake(power);
        DriveAngleReseted = false;
    }

    public void forthSpeed()
    {
        DriveSpeedMultiplier = BaseDriveSpeedMultiplier/8;
        TurnSpeedMultiplier = BaseTurnSpeedMultiplier/8;
    }

    public void chooseDirection(double rightStickX, double leftStickBaring, double leftStickPower) //Move
    {
        if(!DriveAngleReseted)//reset the target drive angle
        {
            opmode.telemetry.addData("RESETING DRRIVE ANGLE: ", true);
            DriveAngle = Bot.GetRobotAngle();
            DriveAngleReseted = true;
        }

        opmode.telemetry.addData("DRIVE ANGLE: ", DriveAngle);

        //if we need to turn while moving, choose direction and add/subtract that with the target angle
        boolean turnRight = false;
        if (rightStickX > JoystickThreshold)
        {
            turnRight = true;
            DriveAngle -= 6 * Math.abs(rightStickX);
        }
        if (rightStickX <= JoystickThreshold)
        {
            turnRight = false;
            DriveAngle += 6 * Math.abs(rightStickX);
        }

        //Get an offset of the robot so that it can stay on track:
        double offset = angleFollower.GetOffsetToAdd(GetUsableRot(DriveAngle), GetUsableRot(Bot.GetRobotAngle()), 0.01, 0, 0);//gets an offset to keep the robot on track
        opmode.telemetry.addData("Target Angle: ", DriveAngle);
        opmode.telemetry.addData("Current Angle: ", Bot.GetRobotAngle());
        opmode.telemetry.addData("Turn offset: ", offset);

        if (!(offset > 0) && !(offset < 0))
        {
            offset = 0;
        }

        //Make robot move at the angle of the left joystick at the determined speed while applying a turn to the value of the right joystick
        Bot.MoveAtAngleTurning(leftStickBaring, DriveSpeedMultiplier * leftStickPower, turnRight, 0 * TurnSpeedMultiplier, headlessMode, offset);
    }

    public void RawForwards(double speed)
    {
        Bot.RawForwards(speed);
    }

    public void RawRight(double speed)
    {
        Bot.RawRight(speed);
    }

    public void turnRight() //Turn Right
    {
        Bot.RawTurn(true, turnSpeed*TurnSpeedMultiplier/2);
        DriveAngleReseted = false;
    }


    public void turnLeft() //Turn Left
    {
        Bot.RawTurn(false, turnSpeed*TurnSpeedMultiplier/2);
        DriveAngleReseted = false;
    }

    public void stopWheels() //STOP
    {
        Bot.StopMotors();
        Bot.SetBrakePos();
        DriveAngleReseted = false;
    }
   // public void FoundationGrab(double desiredAngle){ Bot.FoundationGrab(desiredAngle); }
    public int getfleftudc(){
        int fleft = Bot.getfleft();
        return fleft;
    }
    public int getfrightudc(){
        int fright = Bot.getfright();
        return fright;
    }
    public int getrleftudc(){
        int rleft = Bot.getrleft();
        return rleft;
    }
    public int getrrightudc(){
        int rright = Bot.getrright();
        return rright;
    }

    private double GetUsableRot(double rot)//
    {
        double newRot = 0;
        if(rot > 180)
        {
            newRot = -360 + rot;
        }
        else if(rot < -180)
        {
            newRot = 360 + rot;
        }
        else
        {
            newRot = rot;
        }
        return newRot;
    }


}
