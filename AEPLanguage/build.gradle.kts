plugins {
    id("java-library")
    id("pl.publicprojects.ktsplugin")
}

group = "pl.publicprojects.aeplanguage"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    cudaDependencies {
        compileOnlyOrImplementationCuda(false, "org.nd4j:nd4j-native-platform:1.0.0-M2.1")
        compileOnlyOrImplementationCuda(true, "org.nd4j:nd4j-cuda-11.6:1.0.0-M2.1")
        compileOnlyOrImplementationCuda(true,"org.bytedeco:cuda-platform-redist:11.6-8.3-1.5.7")
        compileOnlyOrImplementationCuda(true,"org.nd4j:nd4j-cuda-11.6:1.0.0-M2.1:windows-x86_64")
    }

    implementation("org.slf4j:slf4j-simple:2.0.13")

}

tasks.test {
    useJUnitPlatform()
}