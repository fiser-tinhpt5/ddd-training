package lifecycle.issue

import java.util.Date

import issue.{ IssueDAO, IssueRecord }
import org.specs2.mock.Mockito
import play.api.test.PlaySpecification

import scala.util.Success

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
  }
}
