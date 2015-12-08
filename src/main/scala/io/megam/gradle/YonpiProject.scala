package io.megam.gradle

import scalaz._
import Scalaz._
import scalaz.Validation.FlatMap._
import org.megam.common.git.GitRepo

case class YonpiRaw(git: GitRepo) {

  val MEGAM_HOME = sys.env.get("MEGAM_HOME") map {x => x + java.io.File.separator + "megamgateway"}

  val name = git.name.repo

  val root = new java.io.File(MEGAM_HOME.getOrElse("/var/lib/megam/megamgateway") + java.io.File.separator + name)

  val jar   = new java.io.File(root.getAbsolutePath +  java.io.File.separator + "build" + java.io.File.separator + name + ".jar")

  val exists  = if (!jar.exists) { true.successNel[Throwable] }
    else {(new RuntimeException(jar.getAbsolutePath  + " exists.")).failureNel[Boolean]}

  override def toString() = "(" + git.toString  + "," + name + "," + root.getAbsolutePath+ "," +  jar.getAbsolutePath + "," +exists + ")"

}


object YonpiProject {
  def apply(underlying: GitRepo): YonpiProject = {
    val project = new YonpiProject(new YonpiRaw(underlying))
    project.init
    project
  }
}

class YonpiProject (underlying: YonpiRaw) extends scala.collection.mutable.Publisher[YonpiProjectEvent] {

  private var hasBeenBuilt = false

  private var buildManager0: BuildManager = null

  val name0 = underlying.name

  val jar0 = underlying.jar

  val root0 = underlying.root

  def init: Unit = println("building jar ..." + underlying.toString)

  def buildJar(delete: Boolean = false):ValidationNel[Throwable, YonpiRaw] = {
    if (delete) clean

    for {
          ue  <- underlying.exists
          mg  <- org.megam.common.git.MGit.clone(underlying.git)
          bd  <- build()
          } yield   {
            underlying
         }
  }

  private def build(): ValidationNel[Throwable, GitRepo] = {
    hasBeenBuilt = true
    buildManager.build

    if (!buildManager.hasErrors) {
      publish(BuildSuccess)
    }
    new RuntimeException("testing").failureNel[GitRepo]
  }


 private def buildManager: BuildManager = {
    if (buildManager0 == null) {
      buildManager0 = new GradleBuildManager(underlying)
    }
    buildManager0
  }

 def clean() = {
    if (buildManager != null)
      buildManager.clean

    cleanOutput
    resetBuildCompiler
  }

  private def cleanOutput(): Unit = underlying.root.delete

  private def resetBuildCompiler(): Unit = {
    buildManager0 = null
    hasBeenBuilt = false
  }


  /** Should only be called when `this` project is being deleted or closed by the playframework */
  def dispose(): Unit = {
      resetBuildCompiler()
  }


override def toString: String = "YonpiProject: {" + underlying.toString +  "," + hasBeenBuilt +"}"


}
