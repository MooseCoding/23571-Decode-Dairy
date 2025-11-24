package org.firstinspires.ftc.teamcode.dairy.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Hood
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Turret

class Outtake(hardwareMap: HardwareMap) {
    val flywheel = Flywheel(hardwareMap)
    val turret = Turret(hardwareMap)
    val hood = Hood(hardwareMap)

    val outtakeContinuation:Continuation = Continuations.exec {
        
    }.close()
}