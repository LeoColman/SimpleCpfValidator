#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.0.2")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:gradle-build-action:v3")



import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildAction
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow

val OSSRH_USERNAME by Contexts.secrets
val OSSRH_PASSWORD by Contexts.secrets
val SIGNING_KEY by Contexts.secrets
val SIGNING_PASSWORD by Contexts.secrets
val GITHUB_REF_NAME by Contexts.github


workflow(
  name = "Release",
  on = listOf(Push(tags = listOf("*"))),
  sourceFile = __FILE__
) {
  job(id = "deploy", runsOn = RunnerType.MacOSLatest) {

    uses(name = "Set up JDK", action = SetupJava(javaVersion = "22", distribution = SetupJava.Distribution.Adopt))
    uses(name = "Checkout", action = Checkout())
    uses(name = "Publish to Maven Central", action = GradleBuildAction(arguments = "publish"), env = linkedMapOf(
      "RELEASE_VERSION" to expr { GITHUB_REF_NAME },
      "ORG_GRADLE_PROJECT_mavenCentralUsername" to expr { OSSRH_USERNAME },
      "ORG_GRADLE_PROJECT_mavenCentralPassword" to expr { OSSRH_PASSWORD },
      "ORG_GRADLE_PROJECT_signingInMemoryKey" to expr { SIGNING_KEY },
      "ORG_GRADLE_PROJECT_signingInMemoryKeyPassword" to expr { SIGNING_PASSWORD }
    ))
  }
}
