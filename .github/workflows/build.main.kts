#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:actions__setup-gradle:v4")


import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow


workflow(
  name = "Build",
  on = listOf(Push(), PullRequest()),
  sourceFile = __FILE__
) {
  job(id = "build", runsOn = RunnerType.UbuntuLatest) {
    uses(name = "Setup JDK", action = SetupJava(javaVersion = "22", distribution = SetupJava.Distribution.Adopt))
    uses(name = "Checkout", action = Checkout())
    uses(name = "Setup Gradle", action = ActionsSetupGradle())
    run(name = "Run Build", command = "./gradlew build")
  }
}
