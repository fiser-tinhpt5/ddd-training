package model.issue

import java.util.Date

/**
 * Created by septechuser on 24/11/2016.
 */
case class Issue(id: IssueID, content: String, action: String, status: Status, assignee: String, deadline: Date) {
  def updateStatus(newStatus: Status): Issue = this.copy(status = newStatus)
}
