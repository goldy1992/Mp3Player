apply(plugin = "jacoco")


configure<JacocoPluginExtension> {
    toolVersion = "0.8.9"
}

val fileFilter = listOf (
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
       // "**/*$[0-9].*",
        "**/dagger/**",
        "**/databinding/**",
        "**hilt_aggregated_deps**",
        "**/Hilt_**"
)


tasks {

}
//tasks.withTest{
//    extensions.configure(JacocoTaskExtension::class) {
//        destinationFile = layout.buildDirectory.file("jacoco/jacocoTest.exec").get().asFile
//        classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
//    }
//}



//tasks.withType(Test) {
//    jacoco.includeNoLocationClasses = true
//}

project.afterEvaluate {

    val uiTestSrcSet = listOf ("**/ui/**", "**/preferences/**", "**/activities/**")
    val productFlavors =  listOf("")
//        android.productFlavors.collect {
//        flavor -> flavor.name
//    }
    var applicableProductFlavor = "full"
    val buildType = "debug"
    val flavor = getProductFlavor(applicableProductFlavor, productFlavors)
    val buildVariantList = getBuildVariant(flavor, buildType)
    val buildVariant : String = buildVariantList[0]
    val buildVariantCapitalised : String  = buildVariantList[1]

    if (project.name.equals("commons")) {
        applicableProductFlavor = ""
    }
 
    val unitExecDir = "$buildDir/outputs/unit_test_code_coverage/${buildVariant}UnitTest/test${buildVariantCapitalised}UnitTest.exec"
    val unitBuildFileTree = fileTree(unitExecDir)
    /**
     * Generates a JaCoCo Report for the test coverage for the unit tests.
     */
    tasks.register("jacocoUnitTestReport") {

//    } type = JacocoReport, dependsOn = ["test${buildVariantCapitalised}UnitTest"]) {
//        group = "Reporting"
//        description = "Generate Jacoco coverage unit test reports for the ${buildVariantCapitalised} build variant."
//        reports {
//            xml.required = true
//            html.required = true
//        }

//        val unitTestExclusions = fileFilter + uiTestSrcSet
//        //val mainSrc = fileTree(mapOf("dir" to "$project.projectDir/src/main/java", excludes: unitTestExclusions)
//        val mainSrc = fileTree(mapOf("dir" to "$project.projectDir/src/main/java", "exclude" to  unitTestExclusions))
//        val mainClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}"))
//        val javaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}", "excludes" to unitTestExclusions))
//
//        sourceDirectories.from = files([mainSrc])
//        classDirectories.from = files([mainClasses, javaClasses])
//        executionData.from = unitBuildFileTree
    }

    val uiTestBuildType = "debug"
    val uiTestFlavor = getProductFlavor(applicableProductFlavor, productFlavors);
   // val (String buildVariant, String buildVariantCapitalised) = getBuildVariant(uiTestFlavor, uiTestBuildType)
    val uiBuildDir = "${buildDir}/outputs/code_coverage/${buildVariant}AndroidTest/connected"
    val uiBuildFileTree = fileTree(mapOf("dir" to uiBuildDir, "include" to "**/*.ec"))
    }

    /**
     * Generates a JaCoCo Report for the test coverage for the UI tests.
     */
    tasks.register ("jacocoUiTestReport") {}
//, type = JacocoReport, dependsOn = ["connected${buildVariantCapitalised}AndroidTest"]) {
//        group = "Reporting"
//        description = "Generate Jacoco coverage UI (Instrumented) reports for the ${buildVariantCapitalised} build variant."
//
//        reports {
//            xml.required = true
//            html.required = true
//        }
//
//        val mainSrc = fileTree(mapOf("dir" to "$project.projectDir/src/main/java", "includes" to  uiTestSrcSet))//, excludes: fileFilter)
//        val mainClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/", "includes" to ["${buildVariant}/**/ui/**", "${buildVariant}/**/preferences/**", "${buildVariant}/**/activities/**"]))//, excludes: fileFilter)
//        val javaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/", "includes" to ["${buildVariant}/**/ui/**","${buildVariant}/**/preferences/**", "${buildVariant}/**/activities/**"]))//, excludes: fileFilter)
//
//        sourceDirectories.from = files([mainSrc])
//        classDirectories.from = files([mainClasses, javaClasses])
//        executionData.from = uiBuildFileTree
//
//    }


    /**
     * Generates a JaCoCo Report that aggregates the test coverage for the unit tests and
     * instrumentation tests. This will not have any task dependencies as the unit tests and
     * instrumentation tests should already have been run.
     *
     * We use the original source code but for the classes we use a combination of the debug and
     * release variants.
     *
     * - For the release variant we use all classes excluding the ui directory.
     * - For the debug variant we ONLY use the ui directory.
     */
    task("jacocoCombinedUnitTestAndroidTestReport") {}
//, type = JacocoReport) {
//        group = "Reporting"
//        description = "Generate Jacoco coverage reports for both UI and Unit Tests on the ${buildVariantCapitalised} build."
//
//        reports {
//            xml.required = true
//            html.required = true
//        }
//
//        val mainSrc = fileTree(mapOf("dir" to "$project.projectDir/src/main/java", "excludes" to fileFilter))
//        val kotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}", "excludes" to  fileFilter))
//        //val releaseKotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}", excludes: fileFilter + ["com/github/goldy1992/mp3player/client/ui**"])
//        //val debugKotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}/com/github/goldy1992/mp3player/client/ui/", excludes:fileFilter)
//        val javaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}", "excludes" to fileFilter))// + ["com/github/goldy1992/mp3player/client/ui**"])
//        //val releaseJavaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}", excludes: fileFilter + ["com/github/goldy1992/mp3player/client/ui**"])
//        //val debugJavaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}/com/github/goldy1992/mp3player/client/ui/",excludes: fileFilter)
//
//       // val mainClasses = releaseKotlinClasses + debugKotlinClasses
//       // val javaClasses = releaseJavaClasses + debugJavaClasses
//
//        sourceDirectories.from = files([mainSrc])
//        classDirectories.from = files([kotlinClasses, javaClasses])
//        executionData.from = unitBuildFileTree + uiBuildFileTree
//    }

//    task jacocoCombinedReport(type: JacocoReport) {
//        group = "Reporting"
//        description = "Generate Jacoco coverage reports for both UI and Unit Tests on the ${buildVariantCapitalised} build."
//
//        reports {
//            xml.required = true
//            html.required = true
//        }
//
////        val mainSrc = fileTree(mapOf("dir" to "$project.projectDir/src/main/java", excludes: fileFilter)
////        val kotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}", excludes: fileFilter)
////        //val releaseKotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}", excludes: fileFilter + ["com/github/goldy1992/mp3player/client/ui**"])
////        //val debugKotlinClasses = fileTree(mapOf("dir" to "${buildDir}/tmp/kotlin-classes/${buildVariant}/com/github/goldy1992/mp3player/client/ui/", excludes:fileFilter)
////        val javaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}", excludes: fileFilter + ["com/github/goldy1992/mp3player/client/ui**"])
////        //val releaseJavaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}", excludes: fileFilter + ["com/github/goldy1992/mp3player/client/ui**"])
////        //val debugJavaClasses = fileTree(mapOf("dir" to "${buildDir}/intermediates/javac/${buildVariant}/com/github/goldy1992/mp3player/client/ui/",excludes: fileFilter)
////
////        // val mainClasses = releaseKotlinClasses + debugKotlinClasses
////        // val javaClasses = releaseJavaClasses + debugJavaClasses
////
////        sourceDirectories.from = files([mainSrc])
////        classDirectories.from = files([kotlinClasses, javaClasses])
//        executionData.from = unitBuildFileTree.plus(uiBuildFileTree)
//    }
//}

fun getBuildVariant(flavor : String, buildType : String ) : List<String> {
    val buildVariant : String = if (flavor.isBlank()) buildType else "${flavor}${buildType.capitalize()}"
    return listOf(buildVariant, buildVariant.capitalize())
}





fun getProductFlavor(applicableProductFlavor : String, productFlavors : List<String>) : String {
    var flavor = ""
    if (!productFlavors.isEmpty() && productFlavors.contains(applicableProductFlavor) ) {
        flavor = applicableProductFlavor
    }
    return flavor
}


