package issue

import java.util.Date

import scalikejdbc._
import skinny.orm._

/**
 * Created by septechuser on 24/11/2016.
 */
case class IssueRecord(id: Long, content: String, action: String, assignee: String, status: String, deadline: Date)

object IssueRecord extends SkinnyMapper[IssueRecord] {
  override lazy val defaultAlias = createAlias("i")
  override val tableName = "issues"

  override def extract(rs: WrappedResultSet, n: ResultName[IssueRecord]): IssueRecord = IssueRecord(
    id = rs.long(n.id),
    content = rs.string(n.content),
    action = rs.string(n.action),
    assignee = rs.string(n.assignee),
    status = rs.string(n.status),
    deadline = rs.date(n.deadline)
  )
}