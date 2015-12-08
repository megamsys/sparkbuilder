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
  YonpiRawSpec is the implementation that build the git repo
  """ ^ end ^
      "The Client Should" ^
      "Correctly builds the  Yonpis directory structure for a git repo" ! Yonpi.succeeds ^
      "Correctly builds new Yonpis jars for a git repo" ! YonpiGradle.succeeds ^
  //    "Correctly skips  Yonpis jars for an existing git repo" ! YonpiSkipGradle.succeeds ^
      //"Correctly cleans Yonpi git repo" ! YonpiCleanGradle.succeeds ^
  end

  case object Yonpi {
      def succeeds: SpecsResult = {
      val yp =   YonpiProject(new GitRepo("local", "https://github.com/megamsys/sparkbuilder.git"))
      yp.name0  must beEqualTo("sparkbuilder")
      yp.root0  must beEqualTo(new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "sparkbuilder"))
      yp.jar0   must beEqualTo(new java.io.File(yp.root0.getAbsolutePath + "/build/" + yp.name0 + ".jar"))
    }
  }

  case object YonpiGradle {

      def succeeds: SpecsResult = {
      val yp =   YonpiProject(new GitRepo(
        new java.io.File(sys.env("MEGAM_HOME") + java.io.File.separator + "megamgateway" + java.io.File.separator + "sparkbuilder").getAbsolutePath, "https://github.com/megamsys/sparkbuilder.git"))
      val yb = yp.buildJar()
      yb.toOption must beSome

    }
  }


}
