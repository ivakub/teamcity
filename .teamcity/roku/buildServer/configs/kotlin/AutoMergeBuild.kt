package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.AbsoluteId
import jetbrains.buildServer.configs.kotlin.v2019_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2019_2.ParameterRef
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.finishBuildTrigger

data class AutoMerge(
        val name : String,
        val clientPath : String,
        val snapshotID : String,
        val triggerID : String
)

class AutoMergeBuild(automergeData : AutoMerge) : BuildType({
    templates(AbsoluteId("FirmwareActive_930_AutoMerge"))
    name = automergeData.name

    val snapshotID = automergeData.snapshotID
    val vcsNum = ParameterRef("dep.$snapshotID.build.vcs.number").toString()
    val clientMapping = ParameterRef("dep.$snapshotID.vcsroot.client-mapping").toString()
    val buildCounter = ParameterRef("dep.$snapshotID.build.counter").toString()

    params {
        param("VCSNumFromSnapshot", vcsNum)
        param("ClientMappingFromSnapshot", clientMapping)
        param("BldNumFromSnapshot", buildCounter)
        param("automerge_tgt_path", automergeData.clientPath)
    }

    triggers {
        finishBuildTrigger {
            enabled = true
            buildType = AbsoluteId(automergeData.triggerID).absoluteId
        }
    }

    dependencies {
        artifacts(AbsoluteId(automergeData.triggerID)) {
            buildRule = lastSuccessful()
            artifactRules = ".teamcity/properties/build.finish.properties.gz"
        }
    }
})
