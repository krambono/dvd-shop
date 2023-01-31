package adapters.secondary.dvdrepository

import cats.effect.IO
import cats.implicits.*
import com.ekinox.gateways.DVDRepository
import com.ekinox.models.DVD

class InMemoryDVDRepository() extends DVDRepository:
  private val dvds = List(
    bttfDVD(1),
    bttfDVD(2),
    bttfDVD(3),
    DVD("Star Wars"),
    DVD("Avengers"),
    DVD("Spiderman"),
    DVD("Lord of the Ring")
  )

  override def getDVD(title: String): IO[Either[String, DVD]] =
    IO.pure(
      Either.fromOption(
        dvds.find(_.title == title),
        s"The DVD '$title' is not available"
      )
    )

  private def bttfDVD(number: Int) =
    DVD("Back to the Future " + number, "Back to the Future")
