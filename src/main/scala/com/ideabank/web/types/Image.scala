package com.ideabank.web.types

import org.eclipse.jetty.http.MimeTypes

import java.awt.image.BufferedImage

enum ImageType(val mimeType: String) {
  case JPG extends ImageType("images/jpeg")
  case PNG extends ImageType("images/png")
  case GIF extends ImageType("images/gif")
}

case class Image(name: String, bi: BufferedImage) {
  val imageType: ImageType = ImageType.valueOf(name.split("\\.").last.trim.toUpperCase)
}
