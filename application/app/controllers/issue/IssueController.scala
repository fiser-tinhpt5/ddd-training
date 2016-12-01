package controllers.issue

import javax.inject.Inject

import lifecycle.issue.IssueRepository
import play.api.mvc.{ Action, Controller }

/**
 * Created by septechuser on 24/11/2016.
 */
class IssueController @Inject() (issueRepository: IssueRepository) extends Controller {

  def list = Action {
    Ok(views.html.issue.index(issueRepository.resolveAll.get))
  }
}
