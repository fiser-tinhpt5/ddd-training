package controllers.issue

import javax.inject.Inject

import controllers.form.{ IssueForm, StatusForm }
import lifecycle.issue.IssueRepository
import model.issue.{ IssueID, Status => IssueStatus }
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.{ Action, Controller }

/**
 * Created by septechuser on 24/11/2016.
 */
class IssueController @Inject() (issueRepository: IssueRepository, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val statusForm: Form[StatusForm] = Form(
    mapping(
      "status" -> text
    )(StatusForm.apply)(StatusForm.unapply)
  )

  val issueForm: Form[IssueForm] = Form(
    mapping(
      "content" -> text
        .verifying(Messages("form.field.required", "Content"), content => content.length > 0)
        .verifying(Messages("form.field.maxlength", "content", 1000), content => content.length < 1001),
      "action" -> text
        .verifying(Messages("form.field.required", "Action"), action => action.length > 0)
        .verifying(Messages("form.field.maxlength", "action", 1000), action => action.length < 1001),
      "assignee" -> text
        .verifying(Messages("form.field.required", "Assignee"), action => action.length > 0)
        .verifying(Messages("form.field.maxlength", "assignee", 100), action => action.length < 101),
      "deadline" -> text
        .verifying(Messages("form.field.required", "Deadline"), action => action.length > 0)
    )(IssueForm.apply)(IssueForm.unapply)
  )

  def list = Action { implicit request =>
    Ok(views.html.issue.index(issueRepository.resolveAll.get))
  }

  def updateStatus(id: Long) = Action { implicit request =>
    statusForm.bindFromRequest.fold(
      hasErrors => BadRequest,
      data => {
        (for {
          oldIssue <- issueRepository.resolveById(IssueID(id))
          newIssue = oldIssue.updateStatus(IssueStatus.fromString(data.status).get)
          _ <- issueRepository.update(newIssue)
        } yield {
          Ok
        }).get
      }
    )
  }

  def form = Action {
    Ok(views.html.issue.add(issueForm))
  }

  def add = Action { implicit request =>
    issueForm.bindFromRequest().fold(
      hasErrors => BadRequest(views.html.issue.add(hasErrors)),
      data => {
        (for {
          _ <- issueRepository.add(IssueForm.fromFormValue(data))
        } yield {
          Redirect("/").flashing("create_success" -> Messages("create.issue.success"))
        }).get
      }
    )
  }
}
