package lifecycle.issue

import java.util.UUID
import javax.inject.Inject

import issue.{ IssueDAO, IssueRecord }
import model.issue.{ Issue, IssueID, Status }

import scala.util.Try

/**
 * Created by septechuser on 24/11/2016.
 */
class IssueRepository @Inject() (issueDAO: IssueDAO) {

  def resolveAll: Try[Seq[Issue]] = Try {
    issueDAO.resolveAll.get.map(record => record2Entity(record))
  }

  def resolveById(id: IssueID): Try[Issue] = Try {
    record2Entity((issueDAO.resolveById(id.id).get))
  }

  def update(issue: Issue): Try[Issue] = Try {
    record2Entity(issueDAO.update(entity2Record(issue)).get)
  }

  def add(issue: Issue): Try[Issue] = Try {
    issueDAO.add(entity2Record(issue)).map(record2Entity).get
  }

  private def entity2Record(issue: Issue): IssueRecord =
    IssueRecord(
      issue.id.id,
      issue.content,
      issue.action,
      issue.assignee,
      issue.status.status,
      issue.deadline
    )

  private def record2Entity(issueRecord: IssueRecord): Issue =
    Issue(
      IssueID(issueRecord.id),
      issueRecord.content,
      issueRecord.action,
      Status.fromString(issueRecord.status).get,
      issueRecord.assignee,
      issueRecord.deadline
    )
}
