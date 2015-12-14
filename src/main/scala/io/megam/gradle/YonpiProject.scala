package io.megam.gradle

import scalaz._
import Scalaz._
import scalaz.Validation.FlatMap._
import org.megam.common.git.GitRepo
import org.apache.commons.io.FileUtils

case class YonpiRaw(git: GitRepo) {

  val name = git.name.repo

  FileUtils.forceMkdir(new java.io.File(git.local))

  val root = new java.io.File(git.local)

  val jar = new java.io.File(root.getAbsolutePath + java.io.File.separator + "build" + java.io.File.separator + "libs" + java.io.File.separator + name + ".jar")

  def exists = jar.exists.successNel[Throwable]

  override def toString() = "(" + git.toString + "," + name + "," + root.getAbsolutePath + "," + jar.getAbsolutePath + "," + exists + ")"

}

object YonpiProject {
  def apply(underlying: GitRepo): YonpiProject = {
    val project = new YonpiProject(new YonpiRaw(underlying))
    project.init
    project
  }
}

class YonpiProject(underlying: YonpiRaw) extends scala.collection.mutable.Publisher[YonpiProjectEvent] {

  private var hasBeenBuilt = false

  private var buildManager0: BuildManager = null

  val name0 = underlying.name

  val jar0 = underlying.jar

  val root0 = underlying.root

  def init: Unit = println("building jar ..." + underlying.toString)

  def buildJar(delete: Boolean = false): ValidationNel[Throwable, YonpiRaw] = {
    if (delete) clean

    underlying.exists.flatMap { x =>
      if (!x) {
        (for {
          mg <- org.megam.common.git.MGit.clone(underlying.git)
          bd <- build()
        } yield {
          underlying
        })
      } else {
        underlying.successNel[Throwable]
      }
    }

  }

  private def build(): ValidationNel[Throwable, YonpiRaw] = {
    hasBeenBuilt = true
    val b = buildManager.build
    b flatMap (x => publish(BuildSuccess).successNel[Throwable])
    b
  }

  private def buildManager: BuildManager = {
    if (buildManager0 == null) {
      buildManager0 = new GradleBuildManager(underlying)
    }
    buildManager0
  }

  def clean(): Unit = {
    if (buildManager != null)   buildManager.clean

    cleanOutput
    resetBuildCompiler
  }

  private def cleanOutput(): Unit = FileUtils.deleteDirectory(underlying.root)

  private def resetBuildCompiler(): Unit = {
    buildManager0 = null
    hasBeenBuilt = false
  }

  /** Should only be called when `this` project is being deleted or closed by the playframework */
  def dispose(): Unit = {
    resetBuildCompiler()
  }

  override def toString: String = "YonpiProject: {" + underlying.toString + "," + hasBeenBuilt + "}"

}
