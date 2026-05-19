plugins {
    id("application")
    id("pl.publicprojects.ktsplugin")
    id ("me.champeau.jmh") version("0.7.2")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

val cpuOnly = project.hasProperty("cpuOnly");


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
    implementation(project(":AEPPredictor"))
    implementation(project(":AEPLanguage"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.slf4j:slf4j-nop:2.0.13")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

}

configurations {
    jmhRuntimeClasspath {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    jmhCompileClasspath {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
}



tasks.test {
    useJUnitPlatform()
}
application {
    mainClass = "pl.publicprojects.predictor.basic.examples.VirtualPoolESClusterExample"
}

tasks.shadowJar {
    archiveBaseName.set("AEP")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())

    configurations = listOf(project.configurations.runtimeClasspath.get())
}

