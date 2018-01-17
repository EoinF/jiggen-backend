import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.eoinf.jiggen"
version = "1.0-SNAPSHOT"

buildscript {
    var kotlin_version: String by extra
    var spark_version: String by extra
    var gson_version: String by extra
    var log4j_version: String by extra

    val spring_boot_version = "1.5.9.RELEASE"
    kotlin_version = "1.2.10"
    spark_version = "2.5.4"
    gson_version = "2.8.2"
    log4j_version = "2.9.0"

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
}

val kotlin_version: String by extra
val spark_version: String by extra
val gson_version: String by extra
val log4j_version: String by extra

repositories {
    mavenCentral()
}

dependencies {
    // JPA Data (We are going to use Repositories, Entities, Hibernate, etc...)
    compile("org.springframework.boot:spring-boot-starter-data-jpa")

    // Use MySQL Connector-J
    compile("mysql:mysql-connector-java")

    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile(group="com.sparkjava", name="spark-core", version=spark_version)
    compile(group="com.google.code.gson", name="gson", version=gson_version)
    compile(group="org.apache.logging.log4j", name="log4j-core", version=log4j_version)
    compile(group="org.apache.logging.log4j", name="log4j-api", version=log4j_version)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<War> {
    archiveName = "jiggen-backend.war"
    manifest {
        attributes(
                mapOf("Implementation-Title" to "Jiggen backend rest service",
                        "Implementation-Version" to version,
                        "Main-Class" to "com.github.eoinf.jiggen.MainKt")
        )
    }
}
