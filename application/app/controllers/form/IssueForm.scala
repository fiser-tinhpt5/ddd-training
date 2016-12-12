package controllers.form

import java.text.SimpleDateFormat

import model.issue.{ Issue, IssueID, Status }

/**
 * Created by septechuser on 08/12/2016.
 */
case class IssueForm(content: String, action: String, assignee: String, deadline: String)

object IssueForm {

  val defaultStatus = Status.PENDING
  val defaultID = IssueID(0)
  val sdf = new SimpleDateFormat("dd/MM/yyyy")

  def fromFormValue(issueForm: IssueForm): Issue = Issue(
    defaultID,
    issueForm.content,
    issueForm.action,
    defaultStatus,
    issueForm.assignee,
    sdf.parse(issueForm.deadline)
  )

}
