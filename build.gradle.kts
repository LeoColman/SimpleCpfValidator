import io.gitlab.arturbosch.detekt.Detekt

plugins {
  kotlin("multiplatform") version "2.2.21"
  id("com.vanniktech.maven.publish") version "0.35.0"
  signing
  id("org.jetbrains.dokka") version "2.1.0"
  id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "br.com.colman.simplecpfvalidator"
version = System.getenv("RELEASE_VERSION") ?: "local"

repositories {
  mavenCentral()
}

kotlin {
  applyDefaultHierarchyTemplate()

  jvm()
  js {
    browser()
    nodejs()
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  macosX64()
  macosArm64()

  linuxX64()
  linuxArm64()

  sourceSets {
    val jvmTest by getting {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:6.0.4")
        implementation("io.kotest:kotest-property:6.0.7")
      }
    }
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

mavenPublishing {
  publishToMavenCentral(automaticRelease = true)
  signAllPublications()

  pom {
    name.set("SimpleCpfValidator")
    description.set("Simple CPF Validator")
    url.set("https://www.github.com/LeoColman/SimpleCpfValidator")


    scm {
      connection.set("scm:git:http://www.github.com/LeoColman/SimpleCpfValidator/")
      developerConnection.set("scm:git:http://github.com/LeoColman/")
      url.set("https://www.github.com/LeoColman/SimpleCpfValidator")
    }

    licenses {
      license {
        name.set("The Apache 2.0 License")
        url.set("https://opensource.org/licenses/Apache-2.0")
      }
    }

    developers {
      developer {
        id.set("LeoColman")
        name.set("Leonardo Colman Lopes")
        email.set("dev@leonardo.colman.com.br")
      }
    }
  }
}

tasks.register("detektAll") {
  dependsOn(tasks.withType<Detekt>())
}
