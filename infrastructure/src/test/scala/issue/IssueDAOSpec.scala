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
      "return 1 if update success" in new AutoRollbackWithFixture {
        val dummyIssueRecord = IssueRecord(1, "ddd is difficult", "learn more", "RESOLVED", "tinh_pt", new Date())
        issueDAO.update(dummyIssueRecord).get mustEqual (1)
      }

      "return 0 if update fail" in new AutoRollback {
        val dummyIssueRecord = IssueRecord(1, "ddd is difficult", "learn more", "RESOLVED", "tinh_pt", new Date())
        issueDAO.update(dummyIssueRecord).get mustEqual (0)
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

