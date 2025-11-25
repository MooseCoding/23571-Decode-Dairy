package org.firstinspires.ftc.teamcode.dairy

import dev.frozenmilk.dairy.mercurial.continuations.Continuations.sequence
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.loop
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.exec
import dev.frozenmilk.dairy.mercurial.continuations.Continuations.wait
import dev.frozenmilk.dairy.mercurial.continuations.channels.Channels.send
import org.firstinspires.ftc.teamcode.dairy.subsystems.DriveTrain
import dev.frozenmilk.dairy.mercurial.ftc.Mercurial
import org.firstinspires.ftc.teamcode.dairy.subsystems.Alliance
import org.firstinspires.ftc.teamcode.dairy.subsystems.Intake
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Hood
import org.firstinspires.ftc.teamcode.dairy.subsystems.outtake.Turret

@Suppress("UNUSED")
val exampleAuto = Mercurial.autonomous {
    val driveTrain: DriveTrain = DriveTrain(gamepad1, hardwareMap, telemetry)
    val flywheel: Flywheel = Flywheel(hardwareMap)
    lateinit var turret: Turret
    val intake: Intake = Intake(hardwareMap)
    val hood:Hood = Hood(hardwareMap)

    schedule(
        sequence(
            wait { inInit },
            loop(
                exec {

                }
            ),
            wait { inLoop },
            exec {
                turret = Turret(hardwareMap, driveTrain.alliance, driveTrain.follower)
            },
            loop(
                exec {
                    send({ Turret.Actions.SPIN }, {turret.spin.tx})
                }
            )
        )
    )

    bindSpawn(
        risingEdge { gamepad1.a },
        exec {
            driveTrain.alliance = Alliance.RED
        }
    )

    bindSpawn(
        risingEdge { gamepad1.b },
        exec {
            driveTrain.alliance = Alliance.BLUE
        }
    )
}