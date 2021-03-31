package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.Template
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace

object RokuBuildsTemplate : Template({
    name = "Roku Build Parameters"

    failureConditions {
        executionTimeoutMin = 180
    }

    features {
        freeDiskSpace {
            id = "jetbrains.agent.free.space"
            requiredSpace = "100gb"
            failBuild = false
        }
    }

    params {
        param("BVAT_URL", "http://ra-jenkins.corp.roku:8080/job/bvat_master_publisher/buildWithParameters")
        param("env.BVAT_URL", "true")
        param("env.CCACHE_DISABLE", "true")
        param("env.IS_TEAMCITY_BUILD", "true")
        param("env.PATH", "/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin")
        param("PluginList", """""""")
        param("env.PUBLISH_DIR", "%teamcity.build.checkoutDir%/publish")
    }
})
