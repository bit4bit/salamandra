package org.bit4bit.scalamandra.res

import org.bit4bit.scalamandra._

object User extends model.Model {

  schema.Char("name")
  schema.Char("login")
  schema.Char("password")

  def get_login(login: String, password: String): Int = {
    if (password == "") {
      throw new exception.LoginException("password", s"Password for $login", "password")
    } else {
      return 1
    }
  }

  override def register(model_name: String): Unit = {
  }
}

// TODO(bit4bit) usamos esto como punto de entrada
object Module {
  def register(): Unit = {
    pool.Pool.register("res.user", User)
  }
}

