package doobie.labs.qb

trait Expr[A] { self =>
  def sql: String // needs to be a fragment

  private def untypedComparison(e: Expr[_], op: String): Expr[Boolean] =
    Expr.untypedComparison(this, e, op)

  // Some common comparison operations. All untyped in SQL :shrug:
  def ===(e: Expr[_]) = untypedComparison(e, "=")
  def =/=(e: Expr[_]) = untypedComparison(e, "!=")
  def <  (e: Expr[_]) = untypedComparison(e, "<")
  def <= (e: Expr[_]) = untypedComparison(e, "<=")
  def >  (e: Expr[_]) = untypedComparison(e, ">")
  def >= (e: Expr[_]) = untypedComparison(e, ">=")

  // Boolean comparisons are typed
  def and(e: Expr[Boolean])(
    implicit ev: A =:= Boolean
  ): Expr[Boolean] = { void(ev); untypedComparison(e, "AND") }

  def or(e: Expr[Boolean])(
    implicit ev: A =:= Boolean
  ): Expr[Boolean] = { void(ev); untypedComparison(e, "AND") }

}

object Expr {

  def untypedComparison(a: Expr[_], b: Expr[_], op: String): Expr[Boolean] =
    new Expr[Boolean] {
      val sql = s"(${a.sql} $op ${b.sql})"
    }

  def not(e: Expr[Boolean]): Expr[Boolean] =
    new Expr[Boolean] {
      def sql = s"(NOT ${e.sql})"
    }

}