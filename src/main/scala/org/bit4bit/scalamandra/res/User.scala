package org.bit4bit.scalamandra.res

import org.bit4bit.scalamandra._

class User extends model.Model {
}

object User extends model.ModelSQL[User] {
  def make(): User = new User()
  override def table_name = "res_user"

  schema.Char("name")
  schema.Char("login")
  schema.Char("password")

  def get_login(login: String, password: String)(implicit db: backend.Database): Long = {
    val user_model = pool.Pool.get[User.type]("res.user")

    user_model.one_by_field("login", login) match {
      case Some(current_user) =>
        val current_password = current_user.field("password").valueAsString
        if (check_password(current_password, password)) {
          return current_user.field("id").valueAsID
        } else {
          throw new exception.LoginException("password", s"Password for $login", "password")
        }
      case None =>
        throw new exception.LoginException("password", s"Unknown user for $login", "password")
    }
  }

  private def check_password(password_hash: String, password: String): Boolean = {
    return password_hash == password
  }
}
