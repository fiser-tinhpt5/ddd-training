package lifecycle.issue

import java.util.Date

import exception.EntityNotFound
import issue.{ IssueDAO, IssueRecord }
import model.issue.{ Issue, IssueID, Status }
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

import scala.util.{ Failure, Success }

/**
 * Created by septechuser on 25/11/2016.
 */
class IssueRepositorySpec extends PlaySpecification with Mockito {
  val mockIssueDAO = mock[IssueDAO]

  def issueRepositoryWithMock(mockIssueDAO: IssueDAO) = new IssueRepository(mockIssueDAO)

  val dummyIssueRecord = IssueRecord(1L, "ddd is difficult", "learn more", "tinh_pt", "PENDING", new Date())

  "IssueRepository" should {
    "resolveAll" should {
      "return issue list" in {
        mockIssueDAO.resolveAll returns Success(Seq(dummyIssueRecord))
        val issues = issueRepositoryWithMock(mockIssueDAO).resolveAll.get

        issues must haveSize(1)
        issues.head.id.id mustEqual (1)
        issues.head.content mustEqual ("ddd is difficult")
        issues.head.action mustEqual ("learn more")
      }

      "return empty list if there is no issue" in {
        mockIssueDAO.resolveAll returns Success(Seq())
        val issues = issueRepositoryWithMock(mockIssueDAO).resolveAll.get

        issues must haveSize(0)
      }
    }

    "resolveById" should {
      "return issue if found" in {
        mockIssueDAO.resolveById(1) returns Success(dummyIssueRecord)
        val issue = issueRepositoryWithMock(mockIssueDAO).resolveById(IssueID(1)).get

        issue.content mustEqual ("ddd is difficult")
        issue.action mustEqual ("learn more")
        issue.assignee mustEqual ("tinh_pt")
        issue.status.status mustEqual ("PENDING")
      }

      "throw EntityNotFound if issue is not found" in {
        mockIssueDAO.resolveById(1) returns Failure(new EntityNotFound(("Issue not found")))
        val issue = issueRepositoryWithMock(mockIssueDAO).resolveById(IssueID(1))

        issue must beFailedTry[Issue]
      }
    }

    "update" should {
      "return 1 if update success" in {
        val updatedIssueRecord = IssueRecord(1L, "ddd is difficult", "learn more", "tinh_pt", "RESOLVED", new Date())
        val updatedIssue = Issue(IssueID(1L), "ddd is difficult", "learn more", Status.RESOLVED, "tinh_pt", new Date())
        mockIssueDAO.update(updatedIssueRecord) returns Success(1)

        val updateOK = issueRepositoryWithMock(mockIssueDAO).update(updatedIssue).get

        updateOK mustEqual (1)
      }

      "return 0 if entity not found and update fail" in {
        val updatedIssueRecord = IssueRecord(1L, "ddd is difficult", "learn more", "tinh_pt", "RESOLVED", new Date())
        val updatedIssue = Issue(IssueID(1L), "ddd is difficult", "learn more", Status.RESOLVED, "tinh_pt", new Date())
        mockIssueDAO.update(updatedIssueRecord) returns Success(0)

        val updateOK = issueRepositoryWithMock(mockIssueDAO).update(updatedIssue).get

        updateOK mustEqual (0)
      }
    }
  }
}
