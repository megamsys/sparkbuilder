package io.megam.gradle

import io.megam.gradle.YonpiProject
import org.megam.common.git._

object Main {
  def main(args: Array[String]) {
    YonpiProject(new GitRepo("/home/ram/code/megam/home/megamgateway", "https://github.com/megamsys/sparkbuilder.git")).buildJar()
  }
}
