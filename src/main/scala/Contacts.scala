import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}


import scala.collection.mutable.ListBuffer

case class Person(phone: String, name: String, genre:String, email: String)

class Contacts {
    private val URL = "jdbc:mysql://localhost/Data2017"
    private val USER = "root"
    private val PASS = "123456"

    private var conn: Connection = _
    private var pstmt: PreparedStatement = _
    private var rs: ResultSet = _

  var personlist = ListBuffer[Person]()
    var length = 0

  private def loadData(): Unit = {
        val sql = "select * from Contacts"

        try {
          conn = DriverManager.getConnection(URL,USER,PASS)
          pstmt = conn.prepareStatement(sql)
          rs = pstmt.executeQuery()

          while (rs.next()) {
            var tmpP = Person(rs.getString("phone"), rs.getString("name"), rs.getString("type"), rs.getString("email"))
            personlist += tmpP
          }
        }catch {
          case e => e.printStackTrace()
        }
       rs.close()
       pstmt.close()
       conn.close()

      personlist = personlist.sortWith((x,y) => x.name.toUpperCase.compareTo(y.name.toUpperCase) < 0)
  }

  private def userView(): Unit = {
    println("---------------------------------------------")
    println("------------------|Telephone|----------------")
    println("---------------------------------------------")
    printV()

    def printV(): Unit = {
      var ch1 = '0'
      var ch2 = '0'
      for (x <- personlist) {
        ch1 = x.name.charAt(0)
        if(ch1.toUpper != ch2.toUpper)(println(ch1.toUpper+":"))
        println("   "+x.name+"  "+x.phone+"  "+x.genre+"  "+x.email)
        ch2 = ch1
      }
    }
  }

  def mainProgram(): Unit = {
    this.loadData()
    var flag = true
    var content = "123"
    while (flag) {
      this.userView()
      content = Console.readLine()
      mainSearch(content)
    }
  }

  def mainSearch(cmd: String): Unit = {
   if (cmd.length  == 1) {
     if (cmd.charAt(0).isLetter) {
       var isNameInitial = false

       for (p <- personlist) {
         if(p.name.charAt(0).toUpper == cmd.charAt(0).toUpper) (println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email),isNameInitial = true)
       }
       if (!isNameInitial) {
         for(p <- personlist) {
           if (p.name.indexOf(cmd) > 0)( println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email))
         }
       }
     } else if(cmd.charAt(0).isDigit) {
       var isPhoneInitial = false

       for (p <- personlist) {
         if (p.phone.charAt(0) == cmd.charAt(0))(println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email),isPhoneInitial=true)
       }
       if (!isPhoneInitial) {
         for (p <- personlist) {
           if (p.phone.indexOf(cmd) > 0) (println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email))
         }
       }
     }
   } else if (cmd.length() > 1) {
     var isSubString = false
     if(stringIsDigit(cmd)) {
       for (p <- personlist) (if (p.phone.indexOf(cmd) > 0) (println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email)))
     } else {
       for (p <- personlist) (if(p.name.toUpperCase().indexOf(cmd.toUpperCase()) >= 0) (println(p.name+"  "+p.phone+"  "+p.genre+"  "+p.email)))
     }

   }

    def stringIsDigit(s: String): Boolean = {
      val pattern = """^(\d+)$""".r
      s match {
        case pattern(_*) => true
        case _ => false
      }
    }

  Console.readLine()
  }

}



