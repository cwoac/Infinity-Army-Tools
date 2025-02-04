import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * This file was generated by the Gradle 'init' task.
 */


repositories {
    mavenCentral()
}


plugins {
    java
    id("org.openjfx.javafxplugin")
    id("net.codersoffortune.infinity.java-conventions")
    id("edu.sc.seis.launch4j")
    kotlin("jvm")
    kotlin("plugin.serialization")
}


dependencies {
    //implementation("net.codersoffortune.infinity:Core:1.0.0-SNAPSHOT")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.openjfx:javafx-controls:15.0.1")
    implementation("org.openjfx:javafx-fxml:15.0.1")
    implementation("org.apache.maven.plugins:maven-shade-plugin:3.2.4")
    implementation("com.akathist.maven.plugins.launch4j:launch4j-maven-plugin:2.5.2")
    implementation(project(":Core"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("io.ktor:ktor-client-core:1.6.8")
    implementation("io.ktor:ktor-client-cio:1.6.8")
}

description = "Gui"

javafx {
    version = "15.0.1"
    modules("javafx.controls", "javafx.fxml")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    jar {

    }
}

launch4j {
    mainClassName = "net.codersoffortune.infinity.gui.Entrypoint"
    outfile = "InfinityArmyTools.exe"
    headerType = "console"
    dontWrapJar = true
}



val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}