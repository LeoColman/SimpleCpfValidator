import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.7.20"
  `maven-publish`
  signing
  id("org.jetbrains.dokka") version "1.7.20"
  id("io.gitlab.arturbosch.detekt") version "1.21.0"
}

group = "br.com.colman.simplecpfvalidator"
version = System.getenv("RELEASE_VERSION") ?: "local"

repositories {
  mavenCentral()
}

dependencies {
  val kotestVersion = "5.5.3"
  testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
  testImplementation("io.kotest:kotest-property:$kotestVersion")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
  useJUnitPlatform()
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").allSource)
}

val javadocJar by tasks.registering(Jar::class) {
  dependsOn("dokkaHtml")
  archiveClassifier.set("javadoc")
  from("$buildDir/dokka")
}

publishing {
  repositories {
    maven("https://oss.sonatype.org/service/local/staging/deploy/maven2") {
      credentials {
        username = System.getenv("OSSRH_USERNAME")
        password = System.getenv("OSSRH_PASSWORD")
      }
    }
  }

  publications {
    register("mavenJava", MavenPublication::class) {
      from(components["java"])
      artifact(sourcesJar.get())
      artifact(javadocJar.get())

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
            email.set("leonardo.dev@colman.com.br")
          }
        }
      }
    }
  }
}

val signingKey: String? by project
val signingPassword: String? by project

signing {
  useGpgCmd()
  if (signingKey != null && signingPassword != null) {
    useInMemoryPgpKeys(signingKey, signingPassword)
  }

  sign(publishing.publications["mavenJava"])
}
