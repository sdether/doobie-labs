package doobie.labs

import doobie.labs.qb.proof._
import shapeless._

package object qb {
  import Statement.Operation.{ Join, Where, Select }

  type XString = String with Singleton

  implicit def tableToStatement[A <: XString, E <: HList](t: Table[A, E])(
    implicit ab: AliasedBindings[(A, E) :: HNil]
  ): Statement[Join with Where with Select, (A, E) :: HNil] =
    Statement.fromTable(t)

  def void(as: Any*): Unit = (as, ())._2

}