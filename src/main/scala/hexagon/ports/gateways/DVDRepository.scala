package com.ekinox.gateways

import com.ekinox.models.DVD
import cats.effect.IO

trait DVDRepository:
  def getDVD(title: String): IO[Either[String, DVD]]
