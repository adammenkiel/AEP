package pl.publicprojects.ktsplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import pl.publicprojects.ktsplugin.handler.CudaDependenciesHandler

class KtsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.dependencies.extensions.add("cudaDependencies", CudaDependenciesHandler(target))
    }
}
