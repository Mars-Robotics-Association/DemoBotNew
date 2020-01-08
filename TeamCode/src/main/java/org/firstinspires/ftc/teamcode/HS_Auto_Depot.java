package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

/*
Goes forwards to a blue or red line and then brakes
 */

@Autonomous(name = "HS_Auto_Depot", group = "Autonomous")
public class HS_Auto_Depot extends LinearOpMode {

    public SimpleFieldNavigation nav = null;

    private ColorSensor colorSensor;
    private double HueThreshold = 20;
    private double RedHue = 0;
    private double BlueHue = 207;

    @Override
    public void runOpMode() {
        nav = new SimpleFieldNavigation(this);
        nav.Init();

        colorSensor = new ColorSensor(this);
        colorSensor.Init();

        waitForStart();

        nav.Start();
        telemetry.addData("Status", "Initialized");

        //Block: Go forwards to the line
        nav.GoForward(10, 1);
        while (Math.abs(RedHue - colorSensor.returnHue()) > HueThreshold && Math.abs(BlueHue - colorSensor.returnHue()) > HueThreshold)
        {
            nav.Loop();
            telemetry.addData("looping1: ", true);
            telemetry.update();
        }
        nav.StopAll();
        
    }
}
