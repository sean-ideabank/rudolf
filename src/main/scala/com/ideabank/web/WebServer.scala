package com.ideabank.web

import com.ideabank.web.utils.Config
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.server.{Request, Server, ServerConnector}
import org.eclipse.jetty.util.thread.QueuedThreadPool
import org.slf4j.LoggerFactory

abstract class WebServer {
  val config = Config.instance
  val logger = LoggerFactory.getLogger(getClass.getSimpleName)
  val threadPool = new QueuedThreadPool(config.getInt("max_threads"))
  threadPool.setName("server")

  val port = config.getInt("port")

  def pageHandler: PageHandler

  def main = {
    logger.info("Starting the Webserver")

    // Create a Server instance.
    val server = new Server(threadPool)

    // Create a ServerConnector to accept connections from clients.
    val connector = new ServerConnector(server)
    connector.setPort(port)
    server.addConnector(connector)

    // Set a simple Handler to handle requests/responses.
    server.setHandler(new SessionHandler() {
      override def doHandle(target: String, jettyRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
        pageHandler.handle(target, request, response)
        jettyRequest.setHandled(true)
        logger.info(s"target: ${target} from ${jettyRequest.getRemoteHost}:${jettyRequest.getRemoteAddr} by ${jettyRequest.getRemoteUser}")
      }

      override def doStop() = {
        logger.info("Webserver is terminated.")
        super.doStop()
      }
    })

    // Start the Server so it starts accepting connections from clients.
    server.start()
    logger.info(s"Webserver is running on port ${port}")
  }
}
