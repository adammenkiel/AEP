package pl.publicprojects.ktsplugin.handler

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

class CudaDependenciesHandler(
    private val project: Project
) {
    /**
     * That function depends on two flags:
     *      lightBuild - if project have it property it's will invoke compileOnly method,
     *      in other case implementation method will be used
     *      cpuOnly - if cpuOnly = true, it adds only non-cuda dependencies,
     *      in other case it will add cuda dependencies
     *
     * @param cuda Marks if dependency is cuda dependency
     * @param name String of dependency
     */

    fun compileOnlyOrImplementationCuda(cuda: Boolean, name: String) {
        val cpuOnly = project.hasProperty("cpuOnly");
        val cmdType = if(project.hasProperty("lightBuild")) "compileOnly" else "implementation";
        if(cuda != cpuOnly) {
            project.dependencies.add(cmdType, name);
        }
    }
}
