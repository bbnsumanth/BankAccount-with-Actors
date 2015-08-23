package com.example

import akka.actor.{Actor, Props}

/**
 * Created by KrazyKnight on 8/23/2015.
 */
class MainP extends Actor {

  val from = context.actorOf(Props[BankAccount], "account A")
  val to = context.actorOf(Props[BankAccount], "account B")

  from ! BankAccount.Deposit(100)


  def receive = {
    case BankAccount.Done => {
      println("deposit success")
      transfer(50)
    }
    case BankAccount.Failed => {
      println("deposit Failed")
    }
  }

  def transfer(amount: BigInt) = {
    val transaction = context.actorOf(Props[Transfer], "transferActor")
    transaction ! Transfer.TransferMoney(from, to, amount)
    context.become(awaitingTransfer)
  }

  def awaitingTransfer: Receive = {
    case Transfer.Done => {
      println("transfer success")
      context.stop(self)
    }
    case Transfer.Failed => {
      println("transfer failed")
      context.stop(self)
    }
  }
}