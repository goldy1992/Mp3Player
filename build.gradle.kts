plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.sonarqube) apply true
}

sonarqube {
    properties {
        property("sonar.projectKey", "goldy1992_Mp3Player")
        property("sonar.organization", "goldy1992-github")
        property("sonar.host.url", "https://sonarcloud.io")
        // added ui exclusions due to https://issuetracker.google.com/issues/231616123
        // https://github.com/jacoco/jacoco/issues/1312
        property(
            "sonar.coverage.exclusions",
            "**Test**,**dagger**,**Injector**,**LoggingUtils.*,**android**,**/databinding/**,**hilt_aggregated_deps**,**/Hilt_**,**/FFTAudioProcessor**,**/DefaultMediaBrowser**,**/ui/**"
        )
        property("sonar.coverage.inclusions", "**/main/**")
    }
}