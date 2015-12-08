package io.megam.gradle

import org.gradle.api.Incubating
import org.gradle.tooling.model.Launchable
import org.gradle.tooling.model.Task

import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

import java.io.File

class GradleBuildManager(raw: YonpiRaw) extends BuildManager {

  val PACKAGE = "package"

  val CLEAN = "clean"

  override def build(): Unit = {

    val connection: ProjectConnection = GradleConnector.newConnector.forProjectDirectory(raw.root).connect

    val launcher: BuildLauncher = connection.newBuild

    launcher.forTasks(PACKAGE)

    /*include some build arguments:
    //build.withArguments("--no-search-upward", "-i", "--project-dir", "someProjectDir");
    //configure the standard input:
    build.setStandardInput(new ByteArrayInputStream("consume this!".getBytes()));
    //if you want to listen to the progress events:
    ProgressListener listener = null; // use your implementation
    build.addProgressListener(listener);
    //kick the build off:
    */
    launcher.setStandardOutput(System.out)
    launcher.setStandardError(System.err)
    launcher.run
    connection.close
  }

  override def clean(): Unit = {
    val connection: ProjectConnection = GradleConnector.newConnector.forProjectDirectory(raw.root).connect

    val build: BuildLauncher = connection.newBuild();

    build.forTasks(CLEAN)

    build.run
    connection.close
  }

  override def hasErrors = false

}
