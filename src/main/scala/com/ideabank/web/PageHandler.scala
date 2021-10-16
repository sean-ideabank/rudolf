package com.ideabank.web

import com.ideabank.web.types.Image
import com.ideabank.web.utils.Config
import com.ideabank.web.{BasicWebpage, Webpage}
import jakarta.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}
import org.apache.velocity.VelocityContext
import org.slf4j.LoggerFactory

import java.time.LocalDate
import javax.imageio.ImageIO
import scala.collection.mutable.Map as MMap
import scala.io.Source
import scala.jdk.CollectionConverters.*

trait PageHandler {
  val logger = LoggerFactory.getLogger(this.getClass)
  val config = Config.instance
  val imageCache = MMap.empty[String, Image]
  val indexPage = config.getString("index_page", "index.html")

  /**
   * Represents the default page
   * @return
   */
  def homePage: Webpage

  def getImage(imgName: String): Image = {
    imageCache.get(imgName).getOrElse {
      val bi = ImageIO.read(getClass.getResource(s"/img/${imgName}"))
      val img = Image(name = imgName, bi = bi)
      imageCache.put(imgName, img)
      img
    }
  }

  def loadImage(response: HttpServletResponse, imageName: String): Boolean = {
    try {
      val image = getImage(imageName)
      val outputStream = response.getOutputStream
      ImageIO.write(image.bi, image.imageType.toString, outputStream)
      response.setContentType(image.imageType.mimeType)
      response.setStatus(HttpServletResponse.SC_OK)
      true
    } catch {
      case e: Exception =>
        logger.error(s"Could not load the image ${imageName}", e)
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        false
    }
  }

  def loadFile(response: HttpServletResponse, fileName: String): Boolean = {
    try {
      val writer = response.getOutputStream
      val reader = getClass.getResourceAsStream(fileName)
      reader.transferTo(writer)
      writer.flush()
      true
    } catch {
      case e: Exception =>
        logger.error(s"Could not load the file ${fileName}", e)
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
        false
    }
  }

  def loadWebpage(request: HttpServletRequest, response: HttpServletResponse, paths: List[String]): Boolean = {
    val page: Webpage = paths match {
      case this.indexPage :: _ => homePage
      case pageName :: _ => new BasicWebpage(pageName)
      case _ => homePage
    }

    page.resolve(request, response)
  }

  def handle(pagePath: String, request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    val paths = pagePath.trim.toLowerCase.split("/").toList.filterNot(_.trim.isEmpty)
    paths match {
      case "img" :: imgName :: Nil => loadImage(response, imgName)
      case "pages" :: rest => loadWebpage(request, response, rest)
      case "favicon.ico" :: _ => loadImage(response, "favicon.png")
      case "css" :: cssName :: Nil => loadFile(response, "/css/" + cssName)
      case _ => loadWebpage(request, response, List("index.html"))
    }
  }
}
