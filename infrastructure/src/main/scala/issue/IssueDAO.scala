package issue

import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
 * Created by septechuser on 24/11/2016.
 */

class IssueDAO {
  def resolveAll(implicit session: DBSession = AutoSession): Try[List[IssueRecord]] = Try { IssueRecord.findAll() }
}
