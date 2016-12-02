package controllers.issue

import javax.inject.Inject

import controllers.form.IssueForm
import lifecycle.issue.IssueRepository
import model.issue.IssueID
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{ Action, Controller }

/**
 * Created by septechuser on 24/11/2016.
 */
class IssueController @Inject() (issueRepository: IssueRepository) extends Controller {

  val issueForm: Form[IssueForm] = Form(
    mapping(
      "id" -> longNumber,
      "content" -> nonEmptyText(maxLength = 100),
      "action" -> nonEmptyText(maxLength = 100),
      "assignee" -> text,
      "status" -> text,
      "deadline" -> text
    )(IssueForm.apply)(IssueForm.unapply)
  )

  def list = Action { implicit request =>
    Ok(views.html.issue.index(issueRepository.resolveAll.get))
  }

  def resolveById(id: Long) = Action { implicit request =>
    val issue = issueRepository.resolveById(IssueID(id)).get
    val issueView = IssueForm.toFormValue(issue)

    Ok(views.html.issue.view(issueView, issueForm))
  }

  def update(id: Long) = Action { implicit request =>
    issueForm.bindFromRequest.fold(
      hasError => BadRequest(views.html.issue.view(hasError.get, hasError)),
      issueForm => {
        val issue = IssueForm.fromFormValue(issueForm)
        (for {
          _ <- issueRepository.resolveById(IssueID(id))
          _ <- issueRepository.update(issue)
        } yield {
          Redirect("/")
        }).get
      }
    )
  }
}
