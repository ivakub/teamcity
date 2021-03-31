package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.CheckoutMode
import jetbrains.buildServer.configs.kotlin.v2019_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule

data class BuildAll(
        val name : String,
        val vcsRoot : VcsRoot,
        val scheduleTriggerHour : Int,
        val scheduleTriggerMinute : Int
)

class BuildAllBuild(buildallData : BuildAll, dependency : BuildType) : BuildType({
    name = buildallData.name

    buildNumberPattern = "${dependency.depParamRefs.buildNumber}"

    vcs {
        root(buildallData.vcsRoot)
        checkoutMode = CheckoutMode.MANUAL
    }

    dependencies {
        snapshot(dependency) {}
    }

    // A negative scheduleTriggerHour means don't create the schedule trigger.
    if (buildallData.scheduleTriggerHour > 0) {
        triggers {
            schedule {
                enabled = true
                schedulingPolicy = daily {
                    hour = buildallData.scheduleTriggerHour
                    minute = buildallData.scheduleTriggerMinute
                    timezone = "America/Los_Angeles"
                }
                branchFilter = ""
                triggerBuild = always()
            }
        }
    }

    requirements {
        exists("AWS_AGENT")
    }
})

