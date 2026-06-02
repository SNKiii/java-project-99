plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "5.1.0.4882"
}

sonarqube {
    properties {
        property("sonar.projectKey", "SNKiii_java-project-99")
        property("sonar.organization", "snkiii")
    }
}

group = "com.example"
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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    implementation("io.micrometer:micrometer-registry-prometheus")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register("bootRunDev") {
    group = "application"
    description = "Run Spring Boot application with 'dev' profile"
    doFirst {
        System.setProperty("spring.profiles.active", "dev")
    }
    finalizedBy("bootRun")
}

tasks.register("bootRunProd") {
    group = "application"
    description = "Run Spring Boot application with 'prod' profile"
    doFirst {
        System.setProperty("spring.profiles.active", "prod")
    }
    finalizedBy("bootRun")
}
