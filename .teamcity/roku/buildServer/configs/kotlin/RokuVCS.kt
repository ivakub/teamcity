package roku.buildServer.configs.kotlin

import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.PerforceVcsRoot

class RokuVCS(private val root_name: String, private val root_mapping: String) : PerforceVcsRoot(
        {
            name = root_name
            port = "proxy-ci.fw.rokulabs.net:1666"
            mode = clientMapping {
                mapping = root_mapping.trimIndent()
            }
            userName = "automation"
            password = "credentialsJSON:41b80c41-4452-418b-a9af-ed82125e07a0"
        }
)