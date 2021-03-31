package roku.buildServer.configs.kotlin

data class FirmwarePlatform(
        val name: String,
        val build_cmd: String,
        val build_plid: String,
        val product_platform: String
)

val platformAmarillo1080 = FirmwarePlatform(
        "Amarillo1080",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=amarillo1080 DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Amarillo_1080"
)

val platformAmarillo4k = FirmwarePlatform(
        "Amarillo4k",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=amarillo4k DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Amarillo_4K"
)

val platformAthens = FirmwarePlatform(
        "Athens",
        """
            set -euo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=athens IMAGE_TYPE=acramfs BOARD_TYPE=roku CHANGELIST_STAMP=%build.vcs.number% rootfs dist-package port-image port-image-dbg publish
        """,
        "CG",
        "Athens_His_His,Athens_TCL_TCL"
)

val platformAustin = FirmwarePlatform(
        "Austin",
        """
            set -euo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=austin DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
            """,
        "",
        "Austin"
)

val platformBandera = FirmwarePlatform(
        "Bandera",
        """
            set -euo pipefail

            export ENABLE_GCC8=y
            export PN=app
            export BUILD_VERSION=`cat ${"$"}HOME/.buildnumber.dev`
            make -C os -j`nproc` BUILD_PLATFORM=bandera BUILD_PLID=%env.BUILD_PLID% CHANGELIST_STAMP=%build.vcs.number% STRIP_DEBUG=false NEW_VENDOR=true rootfs dist-package port-image publish
        """,
        "AH",
        "Bandera_TCL_TCL"
)

val platformBenjamin = FirmwarePlatform(
        "Benjamin",
        """
            set -exuo pipefail

            make -C os -j`nproc` BUILD_PLATFORM=benjamin CHANGELIST_STAMP=%build.vcs.number% rootfs dist-package
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar.bz2
            make -C port/realtek/hank/benjamin -j`nproc` ROKU_OS_DIR=${"$"}{PWD}/porting_kit/os DEBUG_SYMBOL_DIR=${"$"}{PWD}/debug_symbols image image-dbg
            make -C os -j`nproc` BUILD_PLATFORM=benjamin CHANGELIST_STAMP=%build.vcs.number% publish
        """,
        "C2",
        "Benjamin"
)

val platformBriscoe = FirmwarePlatform(
        "Briscoe",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=bcm BUILD_PRODUCT=briscoe DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Briscoe"
)

val platformBriscoeGlobePH = FirmwarePlatform(
        "Briscoe_Globe_PH",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=bcm BUILD_PRODUCT=briscoe OEM_PARTNER=kirby rootfs port-image publish
        """,
        "",
        "Briscoe_Globe_PH"
)

val platformBryan = FirmwarePlatform(
        "Bryan",
        """
            set -euo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=bryan DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Bryan"
)

val platformCamden = FirmwarePlatform(
        "Camden",
        """
            set -euo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=camden DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image port-rescue_image publish
        """,
        "",
        "Camden_His_His"
)

val platformCooper = FirmwarePlatform(
        "Cooper",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=cooper DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Cooper"
)

val platformCooperSkyUK = FirmwarePlatform(
        "Cooper_Sky_UK",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=cooper_mvpd rootfs port-image port-image-dbg publish
        """,
        "",
        "Cooper_Sky_UK"
)

val platformDallas = FirmwarePlatform(
        "Dallas",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=dallas rootfs port-image publish
        """,
        "",
        "Dallas"
)

val platformElPaso2k = FirmwarePlatform(
        "ElPaso2k",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=elpaso2k DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image port-rescue_image publish
        """,
        "",
        "Cooper"
)

val platformFruitland4k = FirmwarePlatform(
        "Fruitland4k",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=fruitland4k DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image port-rescue_image port-roku_ecc_nand publish
        """,
        "",
        "Fruitland"
)

val platformFtWorth = FirmwarePlatform(
        "FtWorth",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=ftworth rootfs partner_custom_pkg port-image publish
        """,
        "",
        "FtWorth_TPV_INS,FtWorth_TCL_TCL,FtWorth_TCL_Hit"
)

val platformGalveston = FirmwarePlatform(
        "Galveston",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=galveston rootfs host_tools port-roku_ecc_nand publish
        """,
        "",
        "Galveston"
)

val platformGilbert = FirmwarePlatform(
        "Gilbert",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=gilbert DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Gilbert"
)

val platformGilbert4k = FirmwarePlatform(
        "Gilbert4k",
        """
            set -exuo pipefail
                        
            cd os
            make -j`nproc` BUILD_PLATFORM=gilbert4k DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Gilbert4K"
)

val platformGlaze = FirmwarePlatform(
        "Glaze",
        """
            set -exuo pipefail
            
            cd os
            make -j`nproc` BUILD_PLATFORM=glaze rootfs port-image port-image-dbg publish
        """,
        "",
        "Glaze"
)

val platformLiberty = FirmwarePlatform(
        "Liberty",
        """
            set -exuo pipefail
            
            cd os
            make -j`nproc` BUILD_PLATFORM=liberty rootfs partner_custom_pkg port-image publish

            #echo "##teamcity[publishArtifacts 'os/dist/*addon-ndk.tar.gz=>ADK']"
        """,
        "",
        "Lib_TCL_TCL,Lib_His_His,Lib_TPV_Ins,Lib_TPV_Shp,Lib_TPV_Hai,Lib_TCL_Ins,Lib_TPV_Gry,Lib_His_Shp,Lib_His_Shp_Bby,Lib_CH_Hit,Lib_CH_Ele,Lib_TCL_Hit,Lib_His_Ins,Lib_CH_RCA"
)

val platformLittlefield = FirmwarePlatform(
        "Littlefield",
        """
            set -exuo pipefail
            
            cd os
            make -j`nproc` BUILD_PLATFORM=littlefield rootfs port-image port-image-dbg publish
        """,
        "",
        "Littlefield"
)

val platformLongview = FirmwarePlatform(
        "Longview",
        """
            set -exuo pipefail
            
            cd os
            make -j`nproc` BUILD_PLATFORM=longview DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Longview_TCL_TCL,Longview_TPV_Ins,Longview_His_Shp_Bby,Longview_His_His,Longview_CH_Ele,Longview_TCL_Hit,Longview_CH_RCA,Longview_TPV_Ele,Longview_His_Shp,Longview_TCL_Onn,Longview_Fun_Phi,Longview_Fun_San,Longview_Fun_Mag,Longview_TCL_Phi,Longview_TCL_San,Longview_MTC_JVC,Longview_MTC_WH,Longview_MTC_Onn,Longview_CH_Onn,Longview_TPV_Onn,Longview_MTC_Turnkey"
)

val platformMadison = FirmwarePlatform(
        "Madison",
        """
            set -exuo pipefail
            
            make -C os -j`nproc` BUILD_PLATFORM=madison CHANGELIST_STAMP=%build.vcs.number% rootfs dist-package
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar.bz2
            make -C port/realtek/hank/madison -j`nproc` ROKU_OS_DIR=${"$"}{PWD}/porting_kit/os DEBUG_SYMBOL_DIR=${"$"}{PWD}/debug_symbols image
            make -C os -j`nproc` BUILD_PLATFORM=madison CHANGELIST_STAMP=%build.vcs.number% publish
        """,
        "CU",
        "Madison"
)

val platformMalone = FirmwarePlatform(
        "Malone",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=malone DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Malone_TCL_TCL,Malone_His_His,Malone_Fun_Phi,Malone_Fun_Mag,Malone_Fun_San,Malone_His_Shp,Malone_TCL_Multiple,Malone_CH_Multiple,Malone_CH_Onn,Malone_CVTE-DIA_Multiple,Malone_His_Onn,Malone_CVTE-MTC_Onn,Malone_CVTE-MTC_Multiple,Malone_CH_Philco"
)

val platformMarlin = FirmwarePlatform(
        "Marlin",
        """
            set -exuo pipefail
            
            make -C os -j`nproc` BUILD_PLATFORM=marlin CHANGELIST_STAMP=%build.vcs.number% rootfs dist-package
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar
            tar xf os/dist/rootfs/Carbon*.OBJ/export/porting_kit-*.tar.bz2
            make -C port/realtek/hank/marlin -j`nproc` ROKU_OS_DIR=${"$"}{PWD}/porting_kit/os DEBUG_SYMBOL_DIR=${"$"}{PWD}/debug_symbols image
            make -C os -j`nproc` BUILD_PLATFORM=marlin CHANGELIST_STAMP=%build.vcs.number% publish
        """,
        "CR",
        "Marlin"
)

val platformMidland = FirmwarePlatform(
        "Midland",
        """
            set -exuo pipefail
            
            cd os
            make -j`nproc` BUILD_PLATFORM=midland DRM_PUBLISH_DEV_PLUGINS=true rootfs port-image publish
        """,
        "",
        "Mid_TCL_TCL,Mid_TCL_Ins,Mid_TCL_Phi,Mid_Fun_Mag,Mid_Fun_Phi,Mid_Fun_San,Mid_His_Shp_Bby,Mid_TPV_Shp,Mid_TPV_Ins,Mid_TPV_Ele,Mid_TCL_Hit,Mid_TCL_Onn,Mid_CVTE_RCA,Mid_His_His,Mid_MTC_JVC,Mid_CVTE-Fun_San,Mid_CVTE-Fun_Phi,Mid_CVTE-Fun_Mag,Mid_CNC_Onn,Mid_MTC_WH,Mid_MTC_Onn,Mid_CH_Onn,Mid_MTC_Turnkey,Mid_TCL_Multiple,Mid_CH_Multiple,Mid_CVTE-TPV_Onn,Mid_His_Shp"
)

val platformMVPDAmarillo1080 = FirmwarePlatform(
        "MVPD-Amarillo1080",
        """
            set -exuo pipefail

            cd os            
            make -j`nproc` BUILD_PLATFORM=amarillo1080_mvpd rootfs port-image port-image-dbg publish
        """,
        "",
        "Amarillo1080_Sky_UK,Amarillo1080_Sky_IT,Amarillo1080_Globe_PH,Amarillo1080_Sky_IE,Amarillo1080_Sky_DE"
)

val platformMVPDAustin = FirmwarePlatform(
        "MVPD-Austin",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=austin BUILD_PRODUCT=skydi rootfs port-image publish
        """,
        "",
        "Victoria,SkyDI,SkyIT,Newcastle,Dublin"
)

val platformMVPDGilbert = FirmwarePlatform(
        "MVPD-Gilbert",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=gilbert_mvpd rootfs port-image port-image-dbg publish
        """,
        "",
        "Reno_TCL_TCL"
)

val platformNative = FirmwarePlatform(
        "Native",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=native all publish
        """,
        "",
        ""
)

val platformNemo = FirmwarePlatform(
        "Nemo",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=nemo DRM_PUBLISH_DEV_PLUGINS=true CHANGELIST_STAMP=%build.vcs.number% rootfs port-image dist-package publish
        """,
        "",
        "Nemo"
)

val platformReno = FirmwarePlatform(
        "Reno",
        """
            set -exuo pipefail

            export ENABLE_GCC8=y
            export PN=app
            export BUILD_VERSION=`cat ${"$"}HOME/.buildnumber.dev`
            make -C os -j`nproc` BUILD_PLATFORM=reno BUILD_PLID=%env.BUILD_PLID% CHANGELIST_STAMP=%build.vcs.number% DRM_PUBLISH_DEV_PLUGINS=true STRIP_DEBUG=false rootfs port-image dist-package port-rescue_image publish
        """,
        "88",
        "Reno_TCL_TCL"
)

val platformRichmond = FirmwarePlatform(
        "Richmond",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=richmond rootfs port-image publish
        """,
        "",
        "Richmond"
)

val platformRollingwood = FirmwarePlatform(
        "Rollingwood",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=rollingwood rootfs port-image port-image-dbg publish
        """,
        "",
        "Rollingwood_Telstra_AU"
)

val platformRoma = FirmwarePlatform(
        "Roma",
        """
            set -exuo pipefail
 
            export ENABLE_GCC8=y
            export PN=app
            export BUILD_VERSION=`cat ${"$"}HOME/.buildnumber.dev`
            make -C os -j`nproc` BUILD_PLATFORM=roma BUILD_PLID=%env.BUILD_PLID% CHANGELIST_STAMP=%build.vcs.number% STRIP_DEBUG=false rootfs dist-package
            EXPORT_DIR=`readlink -f os/dist/rootfs/Carbon*.OBJ/export`
            make -C port/realtek/roma -j`nproc` ROKU_OS_DIR=${'$'}{EXPORT_DIR}/porting_kit/os  DEBUG_SYMBOL_DIR=${'$'}{EXPORT_DIR}/debug_symbols image
            make -C os -j`nproc` BUILD_PLATFORM=roma BUILD_PLID=%env.BUILD_PLID% CHANGELIST_STAMP=%build.vcs.number% STRIP_DEBUG=false port-rescue_image publish
        """,
        "AV",
        "Roma_MTC_Onn,Roma_CH_Philco,Roma_MTC_Multiple,Roma_His_His"
)

val platformSmiley = FirmwarePlatform(
        "Smiley",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=smiley rootfs port-image port-image-dbg publish
        """,
        "",
        "Smiley"
)

val platformSugarland = FirmwarePlatform(
        "Sugarland",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=bcm BUILD_PRODUCT=sugarland rootfs port-image publish

            # temporarily removed addon-ndk-publish from make due to BAR-4493
            # echo "##teamcity[publishArtifacts 'os/dist/*addon-ndk.tar.gz=>ADK']"
        """,
        "",
        "Sugarland"
)

val platformSydney = FirmwarePlatform(
        "Sydney",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=austin BUILD_PRODUCT=skydi OEM_PARTNER='sydney sanantonio spicewood' rootfs port-image publish
        """,
        "",
        "Sydney,SanAntonio,Spicewood"
)

val platformTyler = FirmwarePlatform(
        "Tyler",
        """
            set -exuo pipefail

            cd os
            make -j`nproc` BUILD_PLATFORM=tyler rootfs port-image publish

            #echo "##teamcity[publishArtifacts 'os/dist/*addon-ndk.tar.gz=>ADK']"
        """,
        "",
        "Tyler"
)

