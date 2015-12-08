package io.megam.gradle

trait YonpiProjectEvent

case object BuildSuccess extends YonpiProjectEvent {
  override def toString = "BUILDSUCCESS"
}
