plugins {
	java
    alias(libs.plugins.springframework)
    alias(libs.plugins.spring.dependency.management)
}

group = "com"
version = "0.0.1-SNAPSHOT"

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
}

tasks.withType<Test> {
	useJUnitPlatform()
}
