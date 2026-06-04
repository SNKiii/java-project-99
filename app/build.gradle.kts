plugins {
        id("java")
        id("org.springframework.boot") version "3.4.0"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.micrometer:micrometer-registry-prometheus")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    testImplementation("org.instancio:instancio-junit:4.0.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    implementation("org.springframework.boot:spring-boot-starter-security")

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
tasks.bootJar {
    archiveFileName.set("app.jar")
}