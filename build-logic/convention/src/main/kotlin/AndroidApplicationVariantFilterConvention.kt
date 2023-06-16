import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.github.goldy1992.mp3player.configureBuildVariants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationVariantFilterConvention : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            val extension = extensions.getByType<ApplicationAndroidComponentsExtension>()
            configureBuildVariants(extension)
        }

    }
}