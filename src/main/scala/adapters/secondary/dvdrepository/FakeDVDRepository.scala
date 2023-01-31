package com.ekinox.adapters.secondary

import com.ekinox.gateways.DVDRepository
import com.ekinox.models.DVD
import cats.effect.IO
import cats.implicits.*

class FakeDVDRepository(private val dvds: List[DVD]) extends DVDRepository:

  override def getDVD(title: String): IO[Either[String, DVD]] =
    IO.pure(Either.fromOption(dvds.find(_.title == title), s"The DVD '$title' is not available"))
    
  


