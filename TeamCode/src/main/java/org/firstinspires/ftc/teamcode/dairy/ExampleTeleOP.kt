package org.firstinspires.ftc.teamcode.dairy

import dev.frozenmilk.dairy.mercurial.continuations.Continuations.async
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.noop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels.send
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels.sendPoll
import org.firstinspires.ftc.teamcode.dairy.subsystems.DriveTrain
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial
import org.firstinspires.ftc.teamcode.dairy.subsystems.Intake
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Hood
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Turret

@Suppress("UNUSED")
val exampleTeleOP = Mercurial.teleop {
    val driveTrain: DriveTrain = DriveTrain(gamepad1, hardwareMap, telemetry)
    val flywheel: Flywheel = Flywheel(hardwareMap)
    val intake: Intake = Intake(hardwareMap)
    val hood: Hood = Hood(hardwareMap)
    lateinit var turret: Turret

    schedule(
        sequence(
            wait { inLoop },
            exec {
                turret = Turret(hardwareMap, driveTrain.alliance, driveTrain.follower)
            },
            driveTrain.driveTrainAsync,
            exec {

            }
        )
    )

    bindSpawn(
        risingEdge { gamepad1.a },
        send({ Flywheel.Actions.SPIN }, {flywheel.spin.tx})
    )

    bindSpawn(
        risingEdge { gamepad1.b },
        send({ Flywheel.Actions.STOP }, {flywheel.spin.tx})
    )

    bindSpawn(
        risingEdge { gamepad2.dpad_up },
        send({ Hood.MOVING(0.0) }, { hood.spin.tx }) // Note you'd replace your target obviously
    )

    bindSpawn(
        risingEdge { gamepad1.right_trigger > 0.2 },
        send({ Intake.Actions.FORWARD}, {intake.spin.tx})
    )


    bindSpawn(
        risingEdge { gamepad1.left_trigger > 0.2 },
        send({ Intake.Actions.BACK}, {intake.spin.tx})
    )

    bindSpawn(
        risingEdge { gamepad1.left_trigger < 0.2 && gamepad1.right_trigger < 0.2 },
        send({ Intake.Actions.RELEASE}, {intake.spin.tx})
    )
}