package controllers.issue

import java.text.SimpleDateFormat
import java.util.{ Date, UUID }

import exception.EntityNotFound
import issue.IssueRecord
import lifecycle.issue.IssueRepository
import model.issue.{ Issue, IssueID, Status }
import org.specs2.mock.Mockito
import play.api.test.{ FakeRequest, PlaySpecification, WithApplication }

import scala.util.{ Failure, Success }

/**
 * Created by septechuser on 25/11/2016.
 */
class IssueControllerSpec extends PlaySpecification with Mockito {

  val mockIssueRepository = mock[IssueRepository]

  val sdf = new SimpleDateFormat("dd/MM/yyyy")

  def issueControllerWithMock(mockIssueRepository: IssueRepository) = new IssueController(mockIssueRepository)

  val dummyIssue = Issue(IssueID(1L), "ddd is difficult", "learn more", Status.PENDING, "tinh_pt", new Date())

  "IssueController" should {
    "list" should {
      "return issue list" in new WithApplication() {
        mockIssueRepository.resolveAll returns Success(Seq(dummyIssue))
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult) must contain("ddd is difficult")
      }

      "dont return issue list if there is no issue" in new WithApplication() {
        mockIssueRepository.resolveAll returns Success(Seq())
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult) must contain("No issue")
      }

      "throw an exception if has error when retrieving issue list" in new WithApplication() {
        mockIssueRepository.resolveAll returns Failure(new Exception)
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository).list,
          FakeRequest(GET, "/")
        )

        status(apiResult) must throwAn[Exception]
      }
    }

    "resolveById" should {
      "show issue detail in form" in new WithApplication() {
        mockIssueRepository.resolveById(IssueID(1L)) returns Success(dummyIssue)
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository).resolveById(1L),
          FakeRequest(GET, "/issues/" + 1)
        )

        status(apiResult) mustEqual OK
        contentAsString(apiResult) must contain("ddd is difficult")
      }

      "throw exception if issue not found" in new WithApplication() {
        mockIssueRepository.resolveById(IssueID(2L)) returns Failure(new EntityNotFound("Issue not found"))
        val apiResult = call(
          issueControllerWithMock(mockIssueRepository).resolveById(2L),
          FakeRequest(GET, "/issues/" + 2)
        )

        status(apiResult) must throwAn[Exception]
      }

      "update" should {
        "success and redirect to issue list if everything is ok" in new WithApplication() {
          mockIssueRepository.resolveById(IssueID(1L)) returns Success(dummyIssue)
          mockIssueRepository.update(any[Issue]) returns Success(1)

          val apiResult = call(
            issueControllerWithMock(mockIssueRepository).update(1L),
            FakeRequest(PUT, "/issues/" + 1)
              .withFormUrlEncodedBody(
                "id" -> "1",
                "content" -> dummyIssue.content,
                "action" -> dummyIssue.action,
                "assignee" -> dummyIssue.assignee,
                "deadline" -> sdf.format(dummyIssue.deadline),
                "status" -> dummyIssue.status.status
              )
          )

          status(apiResult) mustEqual 303
        }

        "throw exception if update fail" in new WithApplication() {
          mockIssueRepository.update(any[Issue]) returns Failure(new Exception)
          val apiResult = call(
            issueControllerWithMock(mockIssueRepository).update(1L),
            FakeRequest(PUT, "/issues/" + 1)
              .withFormUrlEncodedBody(
                "id" -> "1",
                "content" -> dummyIssue.content,
                "action" -> dummyIssue.action,
                "assignee" -> dummyIssue.assignee,
                "deadline" -> sdf.format(dummyIssue.deadline),
                "status" -> dummyIssue.status.status
              )
          )

          status(apiResult) must throwAn[Exception]

        }
      }
    }
  }
}
