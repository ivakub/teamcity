package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.Template

object MainDockerTemplate : Template({
    name = "FirmwareExample Docker Main Parameters"

    params {
        param("DOCKER_IMAGE", "bar.artifactory.us-east-1.tools.roku.com/buildtools/firmware/main/buildtools-ubuntu-20.04-ci:2793198-2790908")
        param("DOCKER_RUN_ARGS", """
            -u ${'$'}USER
            -e USER=${'$'}USER
            -e LANG=${'$'}LANG
            -e MINOR=${'$'}MINOR
            -e MAJOR=${'$'}MAJOR
            -e BVAT_URL=${'$'}BVAT_URL
            -e ADD_TARGET=${'$'}ADD_TARGET
            -e PRODUCT_PLATFORM=${'$'}PRODUCT_PLATFORM
            -e ICECC_DISABLED=1
            -v /keys:/keys
            -v /certs:/certs
            -v /tmp:/tmp
            --privileged
            --network host
        """.trimIndent())

    }

    requirements {
        equals("BUILDTYPE", "official", "RQ_1373")
        equals("DOCKER_AGENT", "true", "RQ_1374")
        equals("AMAZONLINUX_AGENT", "true", "RQ_1375")
    }
})
