package com.example

import akka.actor.Actor
import com.example.BankAccount.{Balance, WithDraw, Failed, Done}

/**
 * Created by KrazyKnight on 8/23/2015.
 */

class BankAccount extends Actor {



  private var balance: BigInt = 0

  def receive = {
    case BankAccount.Deposit(amount) => {
      if (amount > 0) {
        balance = balance + amount
        sender ! Done
      } else sender ! Failed
    }
    case WithDraw(amount) => {
      if (amount > 0 && balance >= amount) {
        balance = balance - amount
        sender ! Done
      } else sender ! Failed
    }
    case Balance => {
      sender ! balance
    }
  }
}
object BankAccount {

  case class Deposit(amount: BigInt)

  case class WithDraw(amount: BigInt)

  case object Done

  case object Failed

  case object Balance

}

