package proximaltest.models

import org.scalatestplus.play._
import play.api.Play.current
import play.api.db.slick.DB
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import proximaltest.helpers._

class SetsSpec extends PlaySpec with Results {
  import models._

  "Set Model" should {
    "create an Set" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val aset = Sets.create(SetHelpers.sampleSet)
          aset.id must not be empty
        }
      }
    }

    "find a set" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        DB.withSession { implicit s =>
          val aset = Sets.create(SetHelpers.sampleSet)
          aset.id must not be empty
          val asetFound = Sets.find(aset.id.get).get
          asetFound.title.get mustEqual aset.title.get
        }
      }

    }
  }

}
