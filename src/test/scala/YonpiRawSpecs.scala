package test

import org.specs2.mutable._
import org.specs2.Specification
import java.net.URL
import org.specs2.matcher.MatchResult
import org.specs2.execute.{ Result => SpecsResult }

import io.megam.gradle._
import org.megam.common.git._
import org.junit.runner.RunWith
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.runner.JUnitRunner
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class YonpiRawSpec extends Specification {

  def is =
    "YonpiRawSpec".title ^ end ^ """
  YonpiRawSpec is the implementation that builds the git repo
  """ ^ end ^
      "The Client Should" ^
      "Correctly builds Yonpis directory structure for a git repo" ! Yonpi.succeeds ^
      "Correctly builds Yonpis jars for a git repo" ! YonpiGradle.succeeds ^
      "Correctly cleans build Yonpi jars for a git repo" ! YonpiCleanBuildGradle.succeeds ^
      "Correctly cleans Yonpi jar" ! YonpiCleanGradle.succeeds ^
      end

  case object Yonpi {
    def succeeds: SpecsResult = {
      val yp = YonpiProject(new GitRepo("local", "https://github.com/megamsys/sparkbuilder.git"))
      yp.name0 must beEqualTo("sparkbuilder")
      yp.root0 must beEqualTo(new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "yonpis/sparkbuilder"))
      yp.jar0 must beEqualTo(new java.io.File(yp.root0.getAbsolutePath + "/build/libs/" + yp.name0 + ".jar"))
    }
  }

  case object YonpiGradle {
    def succeeds: SpecsResult = {
      val yp = YonpiProject(new GitRepo(
        new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "yonpis/sparkbuilder").getAbsolutePath, "https://github.com/megamsys/sparkbuilder.git"))
      yp.buildJar().toOption.get must haveClass[io.megam.gradle.YonpiRaw]
    }
  }

  case object YonpiCleanGradle {
    def succeeds: SpecsResult = {
      val yp = YonpiProject(new GitRepo(
        new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "yonpis/sparkbuilder").getAbsolutePath, "https://github.com/megamsys/sparkbuilder.git"))
      yp.clean().toOption must beSome
    }
  }

  case object YonpiCleanBuildGradle {
    def succeeds: SpecsResult = {
      val yp = YonpiProject(new GitRepo(
        new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "yonpis/sparkbuilder").getAbsolutePath, "https://github.com/megamsys/sparkbuilder.git"))
      yp.buildJar(true).toOption.get must haveClass[io.megam.gradle.YonpiRaw]
    }
  }

}
