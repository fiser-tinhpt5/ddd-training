package controllers.issue

import java.util.Date

import exception.EntityNotFound
import lifecycle.issue.IssueRepository
import model.issue.{ Issue, IssueID, Status }
import org.specs2.mock.Mockito
import play.api.i18n.MessagesApi
import play.api.test.{ FakeRequest, PlaySpecification, WithApplication }
import scalikejdbc.DBSession

import scala.util.{ Failure, Success }

/**
 * Created by septechuser on 25/11/2016.
 */
class IssueControllerSpec extends PlaySpecification with Mockito {

  val mockIssueRepository = mock[IssueRepository]
  val mockMessageApi = mock[MessagesApi]

  def issueControllerWithMock(mockIssueRepository: IssueRepository, mockMessageApi: MessagesApi) = new IssueController(mockIssueRepository, mockMessageApi)

  val dummyIssue = Issue(IssueID(1L), "ddd is difficult", "learn more", Status.PENDING, "tinh_pt", new Date())

  "IssueController" should {
    "list" should {
      "return issue list" in new WithApplication() {
        mockIssueRepository.resolveAll returns Success(Seq(dummyIssue))
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult) must contain("ddd is difficult")
      }

      "dont return issue list if there is no issue" in new WithApplication() {
        mockIssueRepository.resolveAll returns Success(Seq())
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult) must contain("No issue")
      }

      "throw an exception if has error when retrieving issue list" in new WithApplication() {
        mockIssueRepository.resolveAll returns Failure(new Exception)
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) must throwAn[Exception]
      }
    }

    "update" should {
      "success and return status code 200 if everything is ok" in new WithApplication() {
        val newIssue = dummyIssue.updateStatus(Status.RESOLVED)
        mockIssueRepository.resolveById(IssueID(1)) returns Success(dummyIssue)
        mockIssueRepository.update(newIssue) returns Success(newIssue)

        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).updateStatus(1L),
          FakeRequest("PUT", "/issues/" + 1)
            .withFormUrlEncodedBody(
              "status" -> "RESOLVED"
            )
        )

        status(apiResult) mustEqual 200
      }

      "throw an exception if not found issue or update fail" in new WithApplication() {
        val newIssue = dummyIssue.updateStatus(Status.RESOLVED)
        mockIssueRepository.resolveById(IssueID(1)) returns Failure(new EntityNotFound("Issue not found"))
        mockIssueRepository.update(newIssue) returns Failure(new Throwable)

        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).updateStatus(1L),
          FakeRequest("PUT", "/issues/" + 1)
            .withFormUrlEncodedBody(
              "status" -> "RESOLVED"
            )
        )

        status(apiResult) must throwAn[Exception]
      }
    }

    "form" should {
      "redirect to create new issue form" in new WithApplication() {
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).form,
          FakeRequest("GET", "/issues")
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult).toLowerCase must contain("create new issue")
      }
    }

    "add" should {
      "success and redirect to issue list when everything is ok" in new WithApplication() {
        mockIssueRepository.add(any[Issue]) returns Success(dummyIssue)

        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).add,
          FakeRequest("POST", "/issues")
            .withFormUrlEncodedBody(
              "content" -> dummyIssue.content,
              "action" -> dummyIssue.action,
              "assignee" -> dummyIssue.assignee,
              "deadline" -> "08/12/2016"
            )
        )

        status(apiResult) mustEqual 303
        flash(apiResult).get("create_success").get mustEqual ("Create new issue successfully")
      }

      "return status 400 if form error" in new WithApplication() {
        mockIssueRepository.add(any[Issue]) returns Failure(new Exception)

        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).add,
          FakeRequest("POST", "/issues")
            .withFormUrlEncodedBody(
              "content" -> dummyIssue.content,
              "action" -> dummyIssue.action,
              "assignee" -> dummyIssue.assignee
            )
        )

        status(apiResult) mustEqual 400
      }

      "throw an exception if add fail" in new WithApplication() {
        mockIssueRepository.add(any[Issue]) returns Failure(new Exception)

        val apiResult = call(
          issueControllerWithMock(mockIssueRepository, mockMessageApi).add,
          FakeRequest("POST", "/issues")
            .withFormUrlEncodedBody(
              "content" -> dummyIssue.content,
              "action" -> dummyIssue.action,
              "assignee" -> dummyIssue.assignee,
              "deadline" -> "08/12/2016"
            )
        )

        status(apiResult) must throwAn[Exception]
      }
    }
  }
}
