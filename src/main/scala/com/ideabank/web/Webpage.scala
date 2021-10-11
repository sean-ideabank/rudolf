package com.ideabank.web

import com.ideabank.web.Webpage
import com.ideabank.web.HtmlManager
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.apache.velocity.VelocityContext
import org.slf4j.LoggerFactory

trait Webpage {
  val logger = LoggerFactory.getLogger(getClass)

  def pageName: String
  def contextProvider: (VelocityContext) => Unit

  protected val context = new VelocityContext()
  context.put("page_name", s"/html/${pageName}")

  protected def write(response: HttpServletResponse, content: String): Unit = {
    val writer = response.getWriter
    writer.println(content)
    writer.flush()
  }

  protected def writeToResponse(response: HttpServletResponse): Unit = {
    contextProvider(context)
    write(response, HtmlManager.get("main.html", context))
  }

  def resolve(request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    try {
      writeToResponse(response)
      response.setStatus(HttpServletResponse.SC_OK)
      true
    } catch {
      case e: Exception =>
        logger.error("Could not resolve a webpage", e)
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        false
    }
  }
}

class BasicWebpage(override val pageName: String) extends Webpage {
  override val contextProvider = (_: VelocityContext) => {}
}

class WebpageWithContext(override val pageName: String)(override val contextProvider: (VelocityContext) => Unit) extends Webpage
