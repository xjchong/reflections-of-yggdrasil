plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "org.helloworldramen"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
    implementation("org.hexworks.zircon:zircon.core-jvm:2020.0.2-PREVIEW")
    implementation("org.hexworks.zircon:zircon.jvm.swing:2020.0.2-PREVIEW")
    implementation("org.hexworks.amethyst:amethyst.core:2020.0.1-PREVIEW")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}