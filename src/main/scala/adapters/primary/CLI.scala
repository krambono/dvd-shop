package com.ekinox.adapters.primary

import cats.implicits.*
import scala.io.Source
import scala.util.{Failure, Success, Try}
import com.ekinox.usecases.computeBasketPrice
import adapters.secondary.dvdrepository.InMemoryDVDRepository
import cats.effect.IOApp
import cats.effect.ExitCode
import cats.effect.IO

object CLI extends IOApp:
  def run(args: List[String]): IO[ExitCode] =
    parseArguments(args) match
      case Left(error) => exit(error)
      case Right(file) =>
        (for
          titles <- readLinesFromFile(file)
          potentialPrice <- computeBasketPrice(titles)(
            new InMemoryDVDRepository()
          )
          exitCode <- handleResult(potentialPrice)
        yield (exitCode)).orElse(exit("An error occurred"))

  private def handleResult(
      potentialPrice: Either[List[String], Double]
  ): IO[ExitCode] =
    potentialPrice match
      case Left(errors) => exit(errors.mkString("\n"))
      case Right(price) => success(price)

  private def parseArguments(args: Seq[String]): Either[String, String] =
    val length = args.length
    if length != 1 then
      Left("A unique command line argument is required:\n\tsbt \"run [FILE]\"")
    else Right(args.head)

  private def readLinesFromFile(file: String): IO[List[String]] =
    IO {
      val source = Source.fromFile(file)
      val lines = source.getLines().toList
      source.close()
      lines
    }

  private def exit(error: String): IO[ExitCode] =
    IO.consoleForIO.println(error).as(ExitCode.Error)

  private def success(price: Double): IO[ExitCode] =
    IO.consoleForIO
      .println(s"The total price of the basket is ${price.toString}€")
      .as(ExitCode.Success)
