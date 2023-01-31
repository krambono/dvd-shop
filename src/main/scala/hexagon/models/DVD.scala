package com.ekinox.models

case class DVD(title: String, saga: Option[String])

object DVD:
  def apply(title: String, saga: String): DVD =
    DVD(title, Some(saga))

  def apply(title: String): DVD =
    DVD(title, None)

  def checkIsFromSaga(saga: String)(dvd: DVD): Boolean = dvd.saga == Some(saga)
