import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
}
group = "me.nick"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit5"))
    implementation("com.github.ajalt.clikt:clikt:3.0.1")
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}