package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.CheckoutMode
import jetbrains.buildServer.configs.kotlin.v2019_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

class SnapshotBuild(private val snapshotName: String, private val vcsRoot: VcsRoot) : BuildType({
    name = snapshotName

    maxRunningBuilds = 1

    vcs {
        root(vcsRoot)
        checkoutMode = CheckoutMode.MANUAL
    }

    steps {
        script {
            name = "10 sec delay"
            scriptContent = "sleep 10"
        }
    }

    requirements {
        exists("AWS_AGENT")
    }
})

