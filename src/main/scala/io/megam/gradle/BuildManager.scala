package io.megam.gradle

/**
 * Abstraction which exposes a buildmanager.
 */
trait BuildManager {

  def build: Unit

  /** Can be used to clean an compiler's internal state. */
  def clean: Unit

  def hasErrors: Boolean
}
