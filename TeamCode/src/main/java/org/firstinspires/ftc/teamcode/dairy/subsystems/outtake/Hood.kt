package org.firstinspires.ftc.teamcode.dairy.subsystems.outtake

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Hood(hardwareMap: HardwareMap) {
    val servo:Servo = hardwareMap.servo.get("hoodServo")


}