package controllers.form

import java.text.SimpleDateFormat

import model.issue.{ Issue, IssueID, Status }

/**
 * Created by septechuser on 01/12/2016.
 */
case class IssueForm(id: Long, content: String, action: String, assignee: String, status: String, deadline: String)

object IssueForm {
  val sdf = new SimpleDateFormat("dd/MM/yyyy")

  def fromFormValue(issueForm: IssueForm): Issue = Issue(
    IssueID(issueForm.id),
    issueForm.content,
    issueForm.action,
    Status.fromString(issueForm.status).get,
    issueForm.assignee,
    sdf.parse(issueForm.deadline)
  )

  def toFormValue(issue: Issue): IssueForm = IssueForm(
    issue.id.id,
    issue.content,
    issue.action,
    issue.assignee,
    issue.status.status,
    sdf.format(issue.deadline)
  )
}
