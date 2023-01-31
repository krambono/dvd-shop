package com.ekinox.usecases

import scala.collection.immutable.HashSet
import scala.collection.immutable.HashMap
import com.ekinox.utils.fNot
import com.ekinox.models.DVD
import com.ekinox.gateways.DVDRepository
import cats.effect.IO
import cats.implicits.*

def computeBasketPrice(titles: List[String])(
    dvdRepository: DVDRepository
): IO[Either[String, Double]] =
  titles
    .map(dvdRepository.getDVD)
    .sequence
    .map(_.sequence.map(computeBasketPriceFromDVDs))

private def computeBasketPriceFromDVDs(dvds: List[DVD]): Double =
  val bttfDvds = dvds.filter(checkIsBttf) // BTTF stand for "Back to the Future"
  val standardDvds = dvds.filter(checkIsStandardDvd)

  computePriceForStandardDvds(standardDvds) +
    computePriceForBttfDvds(bttfDvds)

private val checkIsBttf = DVD.checkIsFromSaga("Back to the Future")

private val checkIsStandardDvd = fNot(checkIsBttf)

private def computePriceForStandardDvds(dvds: List[DVD]): Double =
  dvds.length * 20

private def computePriceForBttfDvds(dvds: List[DVD]): Double =
  val numberOfDifferentDvds = HashSet.from(dvds).size
  val discountAccordingToDifferentDvds = HashMap((1, 1.0), (2, 0.9), (3, 0.8))
  discountAccordingToDifferentDvds
    .get(numberOfDifferentDvds)
    .map(dvds.length * 15 * _)
    .getOrElse(0.0)
