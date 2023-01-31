import com.ekinox.adapters.secondary.FakeDVDRepository
import org.scalatest.*
import flatspec.*
import matchers.*
import com.ekinox.usecases.computeBasketPrice
import com.ekinox.models.DVD
import cats.effect.IO
import cats.effect.unsafe.implicits.global

class ComputeBasketPriceTest extends AnyFlatSpec with should.Matchers {

  val dvdRepository = new FakeDVDRepository(List(bttfDVD(bttf1), bttfDVD(bttf2), bttfDVD(bttf3), DVD(starWars)))

  "The total price" should "be 0 if the basket is empty" in {
    totalPriceShouldBe()(0)
  }

  "The computation of price" should "failed if the DVD is not known from title" in {
    totalPriceShouldHaveFailed("Avengers")("The DVD 'Avengers' is not available")
  }

  "The price of a standard DVD" should "be 20€" in {
    totalPriceShouldBe(starWars)(20)
  }

  "The total price for 3 standard DVDs" should "be 60€" in {
    totalPriceShouldBe(starWars, starWars, starWars)(60)
  }

  "The DVD's price of the BTTF saga" should "be 15€" in {
    totalPriceShouldBe(bttf1)(15)
  }

  "it" should "add a discount of 15% on BTTF DVDs when 2 different DVDs of the BTTF saga are added to the basket" in {
    totalPriceShouldBe(bttf1, bttf2)(27)
  }

  "it" should "add a discount of 20% on BTTF DVDs when 3 different DVDs of the BTTF the saga are added to the basket" in {
    totalPriceShouldBe(bttf1, bttf2, bttf3)(36)

  }

  "The total price of the basket" should "be 48 for 4 DVDs of the BTTF Saga including 3 different one" in {
    totalPriceShouldBe(bttf1, bttf2, bttf3, bttf2)(48)
  }

  "The total price of the basket" should "be 45 for 3 same DVDs of the BTTF Saga" in {
    totalPriceShouldBe(bttf1, bttf1, bttf1)(45)
  }

  "The total price of the basket" should "be 56 for 3 different DVDs of the BTTF Saga and one other standard DVD" in {
    totalPriceShouldBe(bttf1, bttf2, bttf3, starWars)(56)
  }

  private def totalPriceShouldBe(titles: String*)(total: Double) =
    computeBasketPrice(titles.toList)(dvdRepository).unsafeRunSync() shouldBe Right(total)

  private def totalPriceShouldHaveFailed(titles: String*)(error: String) =
    computeBasketPrice(titles.toList)(dvdRepository).unsafeRunSync() shouldBe Left(error)

}

val bttf1 = "Back to the Future 1"
val bttf2 = "Back to the Future 2"
val bttf3 = "Back to the Future 3"
val starWars = "Star Wars"

def bttfDVD(title: String) = DVD(title, "Back to the Future")