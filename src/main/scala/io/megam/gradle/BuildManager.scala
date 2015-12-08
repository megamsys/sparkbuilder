package io.megam.gradle

import scalaz._
import Scalaz._
import scalaz.Validation.FlatMap._

/**
 * Abstraction which exposes a buildmanager.
 */
trait BuildManager {

  /* build by running gradle assemble */
  def build: ValidationNel[Throwable, YonpiRaw]

  /** cleans a yonpi gradle driven project  */
  def clean: ValidationNel[Throwable, YonpiRaw]

}
