package com.ideabank.web

import com.ideabank.web.WebpageTemplate
import org.apache.velocity.app.{Velocity, VelocityEngine}
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.apache.velocity.{Template, VelocityContext}

import java.io.StringWriter
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable.Map as MMap
import scala.io.Source

case class WebpageTemplate(name: String, template: Template) {
  def resolve(context: VelocityContext): String = {
    val sw = new StringWriter()
    template.merge(context, sw)
    sw.toString
  }
}

object HtmlManager {
  private val cache: MMap[String, WebpageTemplate] = MMap()

  val ve = new VelocityEngine
  ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
  ve.setProperty("classpath.resource.loader.class", classOf[ClasspathResourceLoader].getName)
  ve.init()

  def get(_name: String, context: VelocityContext): String = {
    val name = "/html/" + _name.trim.toLowerCase
    val webpage = cache.get(name).getOrElse {
      val template = ve.getTemplate(name)
      val wp = WebpageTemplate(name = name, template = template)
      cache.put(name, wp)
      wp
    }
    webpage.resolve(context)
  }
}
