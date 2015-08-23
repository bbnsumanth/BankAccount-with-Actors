package com.example

import akka.actor.{Actor, ActorRef}
import com.example.BankAccount.{Deposit, WithDraw}

/**
 * Created by KrazyKnight on 8/23/2015.
 */

class Transfer extends Actor {

  import Transfer._

  def receive: Receive = {
    case TransferMoney(from: ActorRef, to: ActorRef, amount: BigInt) => {
      from ! WithDraw(amount)
      context.become(awaitWithDrawal(to, amount, sender))
    }
  }

  def awaitWithDrawal(to: ActorRef, amount: BigInt, sender: ActorRef): Receive = {
    case BankAccount.Done => {
      println("withdrawal success")
      to ! Deposit(amount)
      context.become(awiatDeposit(sender))
    }
    case BankAccount.Failed => {
      sender ! Transfer.Failed
    }
  }

  def awiatDeposit(sender: ActorRef): Receive = {
    case BankAccount.Done => {
      sender ! Transfer.Done
    }
    case BankAccount.Failed => sender ! Transfer.Failed

  }

}

object Transfer {

  case class TransferMoney(from: ActorRef, to: ActorRef, amount: BigInt)

  case object Done

  case object Failed

}
