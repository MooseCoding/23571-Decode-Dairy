package org.firstinspires.ftc.teamcode.dairy

import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import dev.frozenmilk.dairy.mercurial.ftc.MercurialOpMode
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import dev.frozenmilk.dairy.mercurial.continuations.Fiber
import org.firstinspires.ftc.teamcode.dairy.subsystems.DriveTrain

class ExampleTeleOP(context:MercurialOpMode.Context): MercurialOpMode(context) {
    val flywheel: Flywheel = Flywheel(hardwareMap)
    val driveTrain:DriveTrain = DriveTrain(gamepad1,hardwareMap)

    init {

        schedule(
            sequence(
                wait {inLoop},
                exec {
                    driveTrain.driveFiber
                }
            )
        )


    }
}