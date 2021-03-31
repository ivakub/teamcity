import jetbrains.buildServer.configs.kotlin.v10.toExtId
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import roku.buildServer.configs.kotlin.*

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {
    // Provide values here for all variables used to create this project
    val thisBranchName = "THIS"
    val srcBranchName = "SOURCE"
    val snapshotBuildName = "$thisBranchName Branch Snapshot"
    val buildallName = "$thisBranchName Branch BuildAll"

    val clientPath = "//depot/firmware/release/$thisBranchName"
    val vcsRoot = RokuVCS(
            "$thisBranchName VCS",
            """${clientPath}/... //team-city-agent/..."""
    )

    val firmwareProject = FirmwareProject(
            // The following (projectPath) setting must be unique for every project
	    //   To make it obvious, standard practice is to start the ID with "DSL_".
            "DSL_FirmwareTools_KotlinDSL_FirmwareExample",
            "8", "7", "6", "4000",
            vcsRoot
    )

    // A negative value for scheduleTriggerHour creates the BuildAll configuration without a schedule trigger.
    val buildAll = BuildAll(
            buildallName,
            vcsRoot,
            -1,
            0
    )

    val autoMerge = AutoMerge(
            "$thisBranchName Branch MergeFrom $srcBranchName Branch",
            clientPath,
            "FirmwareActive_Main_DevBranchSnapshot",
            "FirmwareActive_Main_DevBranchBryan980"
    )

    // List the platforms from roku.buildServer.configs.kotlin.FirmwarePlatforms.kt to include in the project
    val projectPlatforms = arrayOf(
            platformAmarillo1080,
            platformAmarillo4k,
            platformAustin,
            platformBandera,
            platformBriscoe,
            platformBryan,
            platformCamden,
            platformCooper,
            platformElPaso2k,
            platformFruitland4k,
            platformGilbert,
            platformGilbert4k,
            platformLongview,
            platformMalone,
            platformMidland,
            platformNemo,
            platformReno,
            platformRoma
    )

    // Create all build configurations based on variables defined above.
    val snapshotBuild = SnapshotBuild(snapshotBuildName, vcsRoot)
    buildType(snapshotBuild)

    val buildAllBuild = BuildAllBuild(buildAll, snapshotBuild)
    buildType(buildAllBuild)

    val autoMergeBuild = AutoMergeBuild(autoMerge)
    autoMergeBuild.paused = true
    buildType(autoMergeBuild)

    val uiKitProject = UiKitProject("uikit")

    for (platform in projectPlatforms) {
        val thisPlatform = FirmwareBuild(thisBranchName, platform, firmwareProject, snapshotBuild, buildAllBuild)
        buildType(thisPlatform)
        // This is an example of how to pause an individual platform
        // Uncommenting the following two lines will cause the Bryan platform to be paused
//        if (platform.name == "Bryan") {
//            thisPlatform.paused = true
//        }
    }

    // Create supporting resources
    vcsRoot(vcsRoot)
    template(MainDockerTemplate)
    template(RokuBuildsTemplate)
    subProjects(uiKitProject)

    params {
        param("teamcity.ui.settings.readOnly","true")
    }
}

data class FirmwareProject(
        val projectPath: String,
        val buildVerMajor: String,
        val buildVerMinor: String,
        val buildVerDot: String,
        val buildRange: String,
        val vcsRoot: VcsRoot
)

// This class defines the common settings for all of the platforms listed above.
inner class FirmwareBuild(private val branchName : String,
                          private val platformSettings: FirmwarePlatform,
                          private val projectSettings: FirmwareProject,
                          private val dependency: BuildType,
                          private val buildBuildAll: BuildType) : BuildType({
    val platform = platformSettings.name
    val projectVersion = "${projectSettings.buildVerMajor}.${projectSettings.buildVerMinor}.${projectSettings.buildVerDot}.${projectSettings.buildRange}"

    id("${projectSettings.projectPath}_${platform}$projectVersion".toExtId())
    this.name = "$branchName Branch - $platform.$projectVersion"
    templates(RokuBuildsTemplate, MainDockerTemplate)

    paused = true
    allowExternalStatus = true
    buildNumberPattern = "%BuildCounter%"
    maxRunningBuilds = 4

    vcs {
        root(projectSettings.vcsRoot)
        checkoutMode = CheckoutMode.ON_AGENT
        cleanCheckout = true
    }

    dependencies {
        snapshot(dependency) {}
    }

    steps {
        // Overwrites build step in template
        script {
            name = "Build w/ Docker"
            id = "RUNNER_492"
            scriptContent = platformSettings.build_cmd.trimIndent()
            dockerPull = true
            dockerImage = "%DOCKER_IMAGE%"
            dockerRunParameters = "%DOCKER_RUN_ARGS%"
        }
        script {
            name = "ACRAMFS size check"
            id = "RUNNER_508"
            scriptContent = """
                set -x
                
                TSA_SIZE_CHECK=os/scripts/acramfs_size_check.py

                if [ ! -f "${'$'}{TSA_SIZE_CHECK}" ]
                then
                  TSA_SIZE_CHECK=os/scripts/build/teamcity/acramfs_size_check.py
                fi

                ${'$'}{TSA_SIZE_CHECK} \
                   -U %system.teamcity.auth.userId% \
                   -P %system.teamcity.auth.password% \
                   -I %build.vcs.number% \
                   -i %system.teamcity.buildType.id% \
                   -o //depot/firmware/release/main/os/config/size_check_override.txt \
                   -s 256 \
                   %env.PUBLISH_DIR%/acramfs*.bin
            """.trimIndent()
        }
        script {
            name = "Publish w/ Docker"
            id = "RUNNER_493"
            enabled = false
            scriptContent = """
                echo DO NOT PUBLISH EXPERIMENTAL BUILDS
                #Don't publish anything for personal builds
                   # [ "${'$'}{BUILD_IS_PERSONAL}" = "true" ] && exit 0
                
                #PluginList should always be set to "" in TeamCity configurations.
                #If PluginList is injected by the build then publish plugins listed in the file
                    [ -z %PluginList% ] || plgn_publish.sh -f %PluginList% || exit 1
                
                if bvat_publish.sh %build.number% "%system.teamcity.buildConfName%" %build.vcs.number% %env.PRODUCT_PLATFORM% BVAT %BVAT_URL% ${'$'}{PPV}; then
                   echo "triggered BVAT"
                else
                   echo "##teamcity[buildStatus status='SUCCESS' text='Failed to start BVAT.']"
                fi
            """.trimIndent()
            dockerPull = true
            dockerImage = "%DOCKER_IMAGE%"
            dockerRunParameters = "%DOCKER_RUN_ARGS%"
        }
        script {
            name = "Cleanup"
            id = "RUNNER_21"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = "/bin/rm -fr %teamcity.agent.work.dir%/%teamcity.build.default.checkoutDir%/*"
        }

        stepsOrder = arrayListOf("RUNNER_492", "RUNNER_508", "RUNNER_493", "RUNNER_21")
    }

    params {
        param("BuildCounter", "${dependency.depParamRefs.buildNumber}")
        param("env.MINOR", "${projectSettings.buildVerMinor}${projectSettings.buildVerDot}")
        param("env.MAJOR", projectSettings.buildVerMajor)
        param("env.PRODUCT_PLATFORM", platformSettings.product_platform)
    }

    if (platformSettings.build_plid != "")
        params.param("env.BUILD_PLID", platformSettings.build_plid)

    // In the BuildAll configuration, set a dependency upon this platform that is being created.
    buildBuildAll.dependencies.snapshot(this) {}
})

inner class UiKitProject ( private val thisName : String) : Project({
    name = thisName

//    template(MainDockerTemplate)
    template(UiKitTemplate)
    val uikPlatforms = arrayOf(
            "Bryan",
            "Cooper",
            "Gilbert",
            "Nemo"
    )

    for (uik_platform in uikPlatforms) {
        val something = "something"
        val thisPlatform = UiKitBuild(uik_platform)
//        buildType(thisPlatform)
        // This is an example of how to pause an individual platform
        // Uncommenting the following two lines will cause the Bryan platform to be paused
//        if (platform.name == "Bryan") {
//            thisPlatform.paused = true
//        }
    }

})

object UiKitTemplate : Template({
    name = "UI Kit Configuration"

    buildNumberPattern = "%BuildCounter%"
    params {
        param("BuildCounter", "%dep.FirmwareActive_Main_2_SnapshotBuild.build.counter%")
        param("artifactory_user", "bar_automation")
        param("dep.FirmwareActive_Main_DevBranchSnapshot.build.counter", "%dep.FirmwareActive_Main_2_SnapshotBuild.build.counter%")
    }
    vcs {
        root(AbsoluteId("FirmwareActive_Main_MainRedSpecBuilds"))

        cleanCheckout = true
    }

    steps {
        script {
            name = "Build"
            id = "RUNNER_451"
            scriptContent = """
                cd os
                unset BUILD_NUMBER
                mkdir -p %teamcity.build.checkoutDir%/%PLATFORM_NAME%_nfs
                make -j`nproc` BUILD_SUPPORT_UIKIT=true BUILD_PLATFORM=%PLATFORM_NAME% all
                make -j`nproc` NFSROOT=%teamcity.build.checkoutDir%/%PLATFORM_NAME%_nfs BUILD_PLATFORM=%PLATFORM_NAME% nfs-make
                make -j`nproc` NFSROOT=%teamcity.build.checkoutDir%/%PLATFORM_NAME%_nfs BUILD_PLATFORM=%PLATFORM_NAME% uikit-package-platform
            """.trimIndent()
            dockerImage = "%DOCKER_IMAGE%"
            dockerRunParameters = """
                %DOCKER_RUN_ARGS%
                -v %teamcity.agent.home.dir%:%teamcity.agent.home.dir%
            """.trimIndent()
            param("org.jfrog.artifactory.selectedDeployableServer.publishBuildInfo", "true")
            param("org.jfrog.artifactory.selectedDeployableServer.useSpecs", "true")
            param("org.jfrog.artifactory.selectedDeployableServer.urlId", "0")
            param("org.jfrog.artifactory.selectedDeployableServer.envVarsExcludePatterns", "*password*,*secret*")
            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpec", """
                {
                  "files": [
                    {
                      "pattern": "%teamcity.build.checkoutDir%/os/dist/uikit/*.tar.gz",
                      "target": "genericrepo/uikit/%PLATFORM_NAME%/v%BuildCounter%/",
                      "recursive": "false"
                    }
                  ]
                }
            """.trimIndent())
            param("org.jfrog.artifactory.selectedDeployableServer.downloadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.targetRepo", "MavenRokuInternal")
        }
        script {
            name = "publish binaries to TC"
            id = "RUNNER_531"
            scriptContent = """echo "##teamcity[publishArtifacts '%teamcity.build.checkoutDir%/os/dist/rootfs/%objdir%/acramfs.bin']""""
            param("org.jfrog.artifactory.selectedDeployableServer.downloadSpecSource", "Job configuration")
            param("org.jfrog.artifactory.selectedDeployableServer.useSpecs", "false")
            param("org.jfrog.artifactory.selectedDeployableServer.uploadSpecSource", "Job configuration")
        }
    }

    dependencies {
        artifacts(AbsoluteId("FirmwareActive_Main_2_SnapshotBuild")) {
            id = "ARTIFACT_DEPENDENCY_53"
            buildRule = lastSuccessful()
            artifactRules = ".teamcity/properties/build.finish.properties.gz"
        }
    }
})

inner class UiKitBuild(platformName : String) : BuildType({
//    id(platform.toExtId())
    this.name = platformName
//    templates(UiKitTemplate, MainDockerTemplate)

//    paused = true
})