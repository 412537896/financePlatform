import play.PlayJava
import sbt.Keys._
import sbt._

object FinancePlatformBuild extends Build {

  lazy val compileVersion = "2.10.0"

  lazy val common = Project(id = "common", base = file("common"))
    .settings(scalaVersion := compileVersion)
    .enablePlugins(PlayJava)

  lazy val thirdpart = Project(id = "thirdpart", base = file("thirdpart"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(common)
    .enablePlugins(PlayJava)

  lazy val trade = Project(id = "trade", base = file("trade"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(core)
    .dependsOn(account)
    .dependsOn(customer)
    .dependsOn(common).enablePlugins(PlayJava)

  lazy val core = Project(id = "core", base = file("core"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(account)
    .dependsOn(customer)
    .dependsOn(common).enablePlugins(PlayJava)

  lazy val customer = Project(id = "customer", base = file("customer"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(common).enablePlugins(PlayJava)
    .dependsOn(thirdpart).enablePlugins(PlayJava)

  lazy val account = Project(id = "account", base = file("account"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(customer)
    .dependsOn(common).enablePlugins(PlayJava)

  lazy val op = Project(id = "op", base = file("op"))
    .settings(scalaVersion := compileVersion)
    .dependsOn(customer)
    .dependsOn(core)
    .dependsOn(common).enablePlugins(PlayJava)

  lazy val financePlatform = Project(id = "financePlatform", base = file("."))
    .dependsOn(common)
    .dependsOn(thirdpart)
    .dependsOn(trade)
    .dependsOn(core)
    .dependsOn(customer)
    .dependsOn(account)
    .dependsOn(op)
    .aggregate(common, thirdpart, trade, core, customer, account,op)
    .settings(scalaVersion := compileVersion)
    .enablePlugins(PlayJava)
}
