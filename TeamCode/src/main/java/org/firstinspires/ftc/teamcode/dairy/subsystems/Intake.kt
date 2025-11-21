package org.firstinspires.ftc.teamcode.dairy.subsystems

import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor:DcMotorEx = hardwareMap.dcMotor.get("intakeMotor") as DcMotorEx
}