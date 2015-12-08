package io.megam.gradle

import scalaz._
import Scalaz._
import scalaz.Validation.FlatMap._

import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

import java.io.File

class GradleBuildManager(raw: YonpiRaw) extends BuildManager {

  override def build(): ValidationNel[Throwable, YonpiRaw] = {
    taskRun("assemble")
  }

  override def clean(): scalaz.ValidationNel[Throwable, io.megam.gradle.YonpiRaw] = {
    taskRun("clean")
  }

  private def taskRun(task: String): ValidationNel[Throwable, YonpiRaw] = {
    (Validation.fromTryCatchThrowable[io.megam.gradle.YonpiRaw, Throwable] {
      val connection: ProjectConnection = GradleConnector.newConnector.forProjectDirectory(raw.root).connect
      val launcher: BuildLauncher = connection.newBuild
      launcher.forTasks(task)
      launcher.setStandardOutput(System.out)
      launcher.setStandardError(System.err)
      launcher.run
      connection.close
      raw
    } leftMap { t: Throwable => t }).toValidationNel
  }
}
