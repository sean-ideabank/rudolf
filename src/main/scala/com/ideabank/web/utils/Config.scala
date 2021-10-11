package com.ideabank.web.utils

import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.slf4j.LoggerFactory

object Config {
  private val logger = LoggerFactory.getLogger(this.getClass)
  val instance: Configuration = try {
    val cfgs = new Configurations()
    val cfgFileName = System.getProperty("config_file", "/config.cfg")
    val file = this.getClass.getResource(cfgFileName)
    val cfg = cfgs.properties(file)
    cfg
  } catch {
    case ex: Throwable =>
      logger.error("error while loading configuration", ex)
      sys.error("Failed to initialize configuration")
  }
}
