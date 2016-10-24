package models

import com.google.inject.{Inject, Singleton}
import slick.dbio.SuccessAction
import slick.driver.PostgresDriver
import slick.jdbc.meta.MTable
//import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by stanikol on 10/21/16.
  */
@Singleton
class CreateDatabase @Inject() (dBService: DBService){
  import slick.driver.PostgresDriver.api._
  // Tables.images.schema.create
  val tables = MTable.getTables
  val createTables = tables.map { tables =>

    if (!tables.exists(_.name.name == Tables.images.baseTableRow.tableName)) {
      println(s"Creating ${Tables.images.baseTableRow.tableName}")
      Tables.images.schema.create
    }

    if (!tables.exists(_.name.name == Tables.categories.baseTableRow.tableName)) {
      println(s"Creating ${Tables.categories.baseTableRow.tableName}")
      Tables.categories.schema.create
    }

    if (!tables.exists(_.name.name == Tables.goods.baseTableRow.tableName)) {
      println(s"Creating ${Tables.goods.baseTableRow.tableName}")
      Tables.goods.schema.create
    }
  }
  dBService.run(createTables)

  def cr[T](tables: Vector[MTable], tbl: TableQuery[_]) = tbl.baseTableRow

}
