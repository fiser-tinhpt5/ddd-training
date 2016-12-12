package issue

import exception.EntityNotFound
import scalikejdbc.{ AutoSession, DBSession }

import scala.util.Try

/**
 * Created by septechuser on 24/11/2016.
 */

class IssueDAO {
  def resolveAll(implicit session: DBSession = AutoSession): Try[Seq[IssueRecord]] = Try { IssueRecord.findAll() }

  def resolveById(id: Long)(implicit session: DBSession = AutoSession): Try[IssueRecord] = Try {
    IssueRecord.findById(id) match {
      case Some(issueRecord) => issueRecord
      case None              => throw new EntityNotFound("Issue not found")
    }
  }

  def update(issueRecord: IssueRecord)(implicit session: DBSession = AutoSession): Try[IssueRecord] = Try {
    IssueRecord.updateById(issueRecord.id)
      .withAttributes(
        'status -> issueRecord.status
      )

    resolveById(issueRecord.id).get
  }

  def add(issueRecord: IssueRecord)(implicit session: DBSession = AutoSession): Try[IssueRecord] = Try {
    val newIssueID = IssueRecord.createWithAttributes(
      'content -> issueRecord.content,
      'action -> issueRecord.action,
      'assignee -> issueRecord.assignee,
      'status -> issueRecord.status,
      'deadline -> issueRecord.deadline
    )

    resolveById(newIssueID).get
  }
}
