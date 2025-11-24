package org.firstinspires.ftc.teamcode.dairy

import dev.frozenmilk.dairy.mercurial.continuations.Continuations
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import dev.frozenmilk.dairy.mercurial.continuations.Fiber
import org.firstinspires.ftc.teamcode.dairy.subsystems.DriveTrain
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial 

@Supress("UNUSED")
class ExampleTeleOP {
    val exampleTeleOP = Mercurial.teleop {
        val driveTrain: DriveTrain = DriveTrain(gamepad1, hardwareMap)
        val flywheel: Flywheel = Flywheel(hardwareMap)
    
        schedule(
            sequence(
                wait { inLoop }, 
                loop(
                    exec {
                        driveTrain.driveContinuation
                        outtake.outtakeContinuation
                    }
                )
            )
        )
    }
}