package controllers.issue

import java.util.{ Date, UUID }

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
  }
}
