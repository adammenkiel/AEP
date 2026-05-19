plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("aep-plugin") {
            id = "pl.publicprojects.ktsplugin"
            implementationClass = "pl.publicprojects.ktsplugin.KtsPlugin"
        }
    }
}