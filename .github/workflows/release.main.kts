#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.6.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v5")
@file:DependsOn("actions:setup-java:v5")
@file:DependsOn("gradle:actions__setup-gradle:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow

val OSSRH_USERNAME by Contexts.secrets
val OSSRH_PASSWORD by Contexts.secrets
val SIGNING_KEY by Contexts.secrets
val SIGNING_PASSWORD by Contexts.secrets
val REF_NAME by Contexts.github


workflow(
  name = "Release",
  on = listOf(Push(tags = listOf("*"))),
  sourceFile = __FILE__
) {
  job(id = "release", runsOn = RunnerType.MacOSLatest) {
    uses(name = "Setup JDK", action = SetupJava(javaVersion = "22", distribution = SetupJava.Distribution.Adopt))
    uses(name = "Checkout", action = Checkout())
    uses(name = "Setup Gradle", action = ActionsSetupGradle())

    run(
      name = "Publish to Maven Central",
      command = "./gradlew publish",
      env = linkedMapOf(
        "RELEASE_VERSION" to expr { REF_NAME },
        "ORG_GRADLE_PROJECT_mavenCentralUsername" to expr { OSSRH_USERNAME },
        "ORG_GRADLE_PROJECT_mavenCentralPassword" to expr { OSSRH_PASSWORD },
        "ORG_GRADLE_PROJECT_signingInMemoryKey" to expr { SIGNING_KEY },
        "ORG_GRADLE_PROJECT_signingInMemoryKeyPassword" to expr { SIGNING_PASSWORD }
      )
    )
  }
}
