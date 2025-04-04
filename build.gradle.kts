plugins {
    java
    alias(libs.plugins.springframework)
    alias(libs.plugins.spring.dependency.management)
}

group = "com"
version = "0.0.1-SNAPSHOT"
ext["junit-jupiter.version"] = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.springframework)
    implementation(libs.hikari)
    implementation(libs.postgresql)
    implementation(libs.spring.jdbc)

    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.mockito)
    testImplementation(libs.spring.boot.starter.test)

    testImplementation(platform("org.junit:junit-bom:5.12.1"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
