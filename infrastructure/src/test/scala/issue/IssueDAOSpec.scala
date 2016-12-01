package issue

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
  }
}

trait AutoRollbackWithFixture extends AutoRollback {

  override def fixture(implicit session: DBSession) {
    sql"""INSERT INTO `issues`
       VALUES (1, 'ddd is difficult', 'learn more', 'PENDING', 'tinh_pt', '2016-11-24')"""
      .update().apply()
  }
}

