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
): IO[Either[List[String], Double]] =
  titles
    .traverse(dvdRepository.getDVD)
    .map(
      _.foldRight(Right(List.empty): Either[List[String], List[DVD]])(
        (cur, acc) =>
          (acc, cur) match
            case (Left(accError), Left(curError)) => Left(curError :: accError)
            case (Left(accError), Right(_))       => Left(accError)
            case (Right(_), Left(curError))       => Left(List(curError))
            case (Right(accValue), Right(curValue)) =>
              Right(curValue :: accValue)
      ).map(computeBasketPriceFromDVDs)
    )

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
