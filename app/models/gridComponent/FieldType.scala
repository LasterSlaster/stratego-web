package stratego.model.gridComponent

object FieldType extends Enumeration {

  type FieldType = Value
  val EMPTY_FIELD = Value("[  ]")//TODO: Think about removing this value
  val NO_FIELD = Value("[--]")
  val A_SIDE = Value("[AA]")
  val B_SIDE = Value("[BB]")

}
