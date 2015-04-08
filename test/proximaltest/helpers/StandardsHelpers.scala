package proximaltest.helpers
import models._

object StandardsHelpers {

  def fakeStandard: Standard = {
    new Standard(
      None,
      "Common Core State Standards for Mathematics",
      "These Standards define what students should understand and be able to do in their study of mathematics. Asking a student to understand something means asking a teacher to assess whether the student has understood it. But what does mathematical understanding look like? One hallmark of mathematical understanding is the ability to justify, in a way appropriate to the student's mathematical maturity, why a particular mathematical statement is true or where a mathematical rule comes from. There is a world of difference between a student who can summon a mnemonic device to expand a product such as (a + b)(x + y) and a student who can explain where the mnemonic comes from. The student who can explain the rule understands the mathematics, and may have a better chance to succeed at a less familiar task such as expanding (a + b + c)(x + y). Mathematical understanding and procedural skill are equally important, and both are assessable using mathematical tasks of sufficient richness. ",
      "Math")
  }

  def fakeEducationLevels: Seq[EducationLevel] = {
    Seq(
      new EducationLevel(None, "k", "kindergarten"),
      new EducationLevel(None, "1", "first grade"),
      new EducationLevel(None, "2", "second grade"),
      new EducationLevel(None, "3", "third grade")
    )
  }

  def fakeEducationLevel =
    new EducationLevel(None, "k", "kindergarten")

  def fakeEducationLevel2 =
    new EducationLevel(None, "1", "First Grade")

  def fakeStatement = {
    new Statement(
      None,
      None,
      None,
      Some("English"),
      Some("CCSS.ELA-Literacy.CCRA.R.1"),
      Some("Read closely to determine what the text says explicitly and to make logical inferences from it; cite specific textual evidence when writing or speaking to support conclusions drawn from the text.")
    )
  }

  def fakeStatement2 = fakeStatement.copy(notation = Some("CcSS.ELA-Literacy.CCRA.R.2"))

  def fakeStatements = {
    Seq(
      fakeStatement,
      fakeStatement2
    )
  }

}
