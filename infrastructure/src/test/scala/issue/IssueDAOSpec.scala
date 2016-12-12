package issue

import java.util.Date

import play.api.test.PlaySpecification
import scalikejdbc._
import scalikejdbc.specs2.mutable.AutoRollback
import skinny._

/**
 * Created by septechuser on 25/11/2016.
 */
class IssueDAOSpec extends PlaySpecification {

  SkinnyDBsWithEnv(SkinnyEnv.Test).setupAll()

  val issueDAO = new IssueDAO

  "IssueDAO" should {
    "resolveAll" should {
      "return empty list if there is no issue" in new AutoRollback {
        val issues = issueDAO.resolveAll.get
        issues must haveSize(0)
      }

      "return issue list" in new AutoRollbackWithFixture {

        val issues = issueDAO.resolveAll.get
        issues must haveSize(1)
        issues.head.id mustEqual (1)
        issues.head.content mustEqual ("ddd is difficult")
        issues.head.action mustEqual ("learn more")
        issues.head.status mustEqual ("PENDING")
      }
    }

    "resolveById" should {
      "return issueRecord if found" in new AutoRollbackWithFixture {
        val issueRecord = issueDAO.resolveById(1).get

        issueRecord.content mustEqual ("ddd is difficult")
        issueRecord.action mustEqual ("learn more")
        issueRecord.status mustEqual ("PENDING")
        issueRecord.assignee mustEqual ("tinh_pt")
      }

      "throw EntityNotFound if not found" in new AutoRollback {
        val issueRecord = issueDAO.resolveById(1)

        issueRecord must beFailedTry[IssueRecord]
      }
    }

    "update" should {
      "return issue record with changed status if update success" in new AutoRollbackWithFixture {
        val dummyIssueRecord = IssueRecord(1, "ddd is difficult", "learn more", "tinh_pt", "RESOLVED", new Date())
        val updatedIssueRecord = issueDAO.update(dummyIssueRecord).get

        updatedIssueRecord.id mustEqual 1
        updatedIssueRecord.status mustEqual ("RESOLVED")
        updatedIssueRecord.content mustEqual ("ddd is difficult")
        updatedIssueRecord.action mustEqual ("learn more")
        updatedIssueRecord.assignee mustEqual ("tinh_pt")
      }

      "return failure if update fail or cant return issue record" in new AutoRollback {
        val dummyIssueRecord = IssueRecord(1, "ddd is difficult", "learn more", "tinh_pt", "RESOLVED", new Date())
        issueDAO.update(dummyIssueRecord).get must throwAn[Exception]
      }
    }

    "add" should {
      "return new issue record if success" in new AutoRollback {
        val dummyIssueRecord = IssueRecord(2, "ddd is difficult", "learn more", "tinh_pt", "RESOLVED", new Date())
        val newIssue = issueDAO.add(dummyIssueRecord).get

        newIssue.assignee mustEqual ("tinh_pt")
        newIssue.content mustEqual ("ddd is difficult")
        newIssue.action mustEqual ("learn more")
        newIssue.status mustEqual ("RESOLVED")
      }
    }
  }

}

trait AutoRollbackWithFixture extends AutoRollback {

  override def fixture(implicit session: DBSession) {
    sql"""INSERT INTO `issues`
       VALUES (1, 'ddd is difficult', 'learn more', 'PENDING', 'tinh_pt', '2016-11-24')"""
      .update().apply()
  }
}

