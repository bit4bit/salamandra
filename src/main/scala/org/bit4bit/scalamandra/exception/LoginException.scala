package org.bit4bit.scalamandra.exception

// authenticator == type in python
case class LoginException(name: String, message: String, authenticator: String) extends Exception
