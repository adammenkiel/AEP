plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation ("org.nd4j:nd4j-cuda-11.6:1.0.0-M2.1")
    implementation("org.bytedeco:cuda-platform-redist:11.6-8.3-1.5.7")
    implementation("org.nd4j:nd4j-cuda-11.6:1.0.0-M2.1:windows-x86_64")

    implementation("org.slf4j:slf4j-simple:2.0.13")

}

tasks.test {
    useJUnitPlatform()
}