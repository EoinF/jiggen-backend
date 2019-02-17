import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.eoinf.jiggen"
version = "1.0-SNAPSHOT"

buildscript {
    var kotlin_version: String by extra
    var spark_version: String by extra
    var gson_version: String by extra
    var log4j_version: String by extra
    var jiggen_version: String by extra
    var gdx_version: String by extra
    var hibernate_validator_version: String by extra

    val spring_boot_version = "2.0.1.RELEASE"
    kotlin_version = "1.3.10"
    spark_version = "2.8.0"
    gson_version = "2.8.5"
    log4j_version = "2.9.0"
    jiggen_version = "1.1"
    gdx_version = "1.9.9"
    hibernate_validator_version = "6.0.13.Final"

    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$spring_boot_version")
    }
}

apply {
    plugin("kotlin")
    plugin("war")
    plugin("org.springframework.boot")
    // Required because spring boot 2.x.x is dumb and requires it
    plugin("io.spring.dependency-management")
}

val kotlin_version: String by extra
val spark_version: String by extra
val gson_version: String by extra
val log4j_version: String by extra
val jiggen_version: String by extra
val gdx_version: String by extra
val hibernate_validator_version: String by extra

repositories {
    mavenCentral()
    mavenLocal()
    maven { setUrl("https://oss.sonatype.org/content/repositories/releases/") }
}

dependencies {
    // JPA Data (We are going to use Repositories, Entities, Hibernate, etc...)
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    // Required because spring boot 2.x.x is dumb and requires it
    compile(group="org.jetbrains.kotlin", name="kotlin-reflect", version=kotlin_version)
    testRuntime("com.h2database:h2")

    // Use MySQL Connector-J
    compile("mysql:mysql-connector-java")

    // Provides validation annotations for ConfigurationProperties
    compile(group="org.hibernate", name="hibernate-validator", version=hibernate_validator_version)

    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile(group="com.sparkjava", name="spark-core", version=spark_version)
    compile(group="com.google.code.gson", name="gson", version=gson_version)
    compile(group="org.apache.logging.log4j", name="log4j-core", version=log4j_version)
    compile(group="org.apache.logging.log4j", name="log4j-api", version=log4j_version)

    compile(group="com.github.eoinf.jiggen", name="core", version=jiggen_version)

    // Required for the texture packer
    compile(group="com.badlogicgames.gdx", name="gdx-platform", version=gdx_version, classifier="natives-desktop")
    compile(group="com.badlogicgames.gdx", name="gdx-tools", version=gdx_version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<War> {
    enabled = true
    archiveName = "jiggen-backend.war"
    manifest {
        attributes(
                mapOf("Implementation-Title" to "Jiggen backend rest service",
                        "Implementation-Version" to version,
                        "Main-Class" to "com.github.eoinf.jiggen.MainKt")
        )
    }
}
