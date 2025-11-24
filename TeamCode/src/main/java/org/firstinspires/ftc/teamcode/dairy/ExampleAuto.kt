package org.firstinspires.ftc.teamcode.dairy

import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import org.firstinspires.ftc.teamcode.dairy.subsystems.DriveTrain
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial 

@Supress("UNUSED")
class ExampleAuto {
    val exampleAuto = Mercurial.autonomous {
        val driveTrain: DriveTrain = DriveTrain(gamepad1, hardwareMap, telemetry)
    }
}