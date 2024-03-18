plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // reports after testing
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false

        xml.outputLocation = project.layout.buildDirectory.file("coverage/result.xml")
        html.outputLocation = layout.buildDirectory.dir("coverage")
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
}
