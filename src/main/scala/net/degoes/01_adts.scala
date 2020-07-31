package net.degoes

import java.time.{Instant, LocalDate}

/*
 * INTRODUCTION
 *
 * Functional Design depends heavily on functional data modeling. Functional
 * data modeling is the task of creating precise, type-safe models of a given
 * domain using algebraic data types and generalized algebraic data types.
 *
 * In this section, you'll review basic functional domain modeling.
 */

/**
 * E-COMMERCE - EXERCISE SET 1
 *
 * Consider an e-commerce application that allows users to purchase products.
 */
object credit_card {

  /**
   * EXERCISE 1
   *
   * Using only sealed traits and case classes, create an immutable data model
   * of a credit card, which must have:
   *
   *  * Number
   *  * Name
   *  * Expiration date
   *  * Security code
   */

  case class ExpirationDate(month: Int, year: Int)

  case class CreditCard(name: String, number: String, expirationDate: ExpirationDate, secCode: String)

  /**
   * EXERCISE 2
   *
   * Using only sealed traits and case classes, create an immutable data model
   * of a product, which could be a physical product, such as a gallon of milk,
   * or a digital product, such as a book or movie, or access to an event, such
   * as a music concert or film showing.
   */
  sealed trait Product

  object Product {
    case object Physical extends Product
    case object Digital extends Product
    case object Event extends Product
  }

  /**
   * EXERCISE 3
   *
   * Using only sealed traits and case classes, create an immutable data model
   * of a product price, which could be one-time purchase fee, or a recurring
   * fee on some regular interval.
   */
  sealed trait PricingScheme

  object PricingScheme {
    case class OneTime(amount: Double) extends PricingScheme
    case class RecurringFee(amount: Double, interval: PurchaseInterval) extends PricingScheme
  }

  case class PurchaseInterval(startDate: LocalDate, interval: Int, intervalType: IntervalType)

  sealed trait IntervalType

  object IntervalType {
    case object Hour extends IntervalType
    case object Day extends IntervalType
    case object Week extends IntervalType
    case object Month extends IntervalType
  }
}

/**
 * EVENT PROCESSING - EXERCISE SET 3
 *
 * Consider an event processing application, which processes events from both
 * devices, as well as users.
 */
object events {

  /**
   * EXERCISE
   *
   * Refactor the object-oriented data model in this section to a more
   * functional one, which uses only sealed traits and case classes.
   */
  sealed trait Event {
    def id: Int
    def time: Instant
  }

  sealed trait UserEvent {
    def userName: String
  }

  object UserEvent {
    case class UserPurchase(
      id: Int,
      item: String,
      price: Double,
      time: Instant,
      userName: String) extends UserEvent

    case class UserAccountCreated(
      id: Int,
      time: Instant,
      userName: String) extends UserEvent
  }

  sealed trait DeviceEvent {
    def deviceId: Int
  }

  object DeviceEvent {
    case class SensorUpdated(
      id: Int,
      deviceId: Int,
      time: Instant,
      reading: Option[Double]) extends DeviceEvent

    case class DeviceActivated(id: Int, deviceId: Int, time: Instant) extends DeviceEvent
  }
}

/**
 * DOCUMENT EDITING - EXERCISE SET 4
 *
 * Consider a web application that allows users to edit and store documents
 * of some type (which is not relevant for these exercises).
 */
object documents {
  final case class UserId(identifier: String)
  final case class DocId(identifier: String)
  final case class DocContent(body: String)

  /**
   * EXERCISE 1
   *
   * Using only sealed traits and case classes, create a simplified but somewhat
   * realistic model of a Document.
   */
  case class Document(id: DocId, name: String, content: DocContent, createdAt: LocalDate, modifiedAt: LocalDate)

  /**
   * EXERCISE 2
   *
   * Using only sealed traits and case classes, create a model of the access
   * type that a given user might have with respect to a document. For example,
   * some users might have read-only permission on a document.
   */
  sealed trait AccessType

  object AccessType {
    case object Read extends AccessType
    case object Write extends AccessType
    case object Share extends AccessType
  }

  /**
   * EXERCISE 3
   *
   * Using only sealed traits and case classes, create a model of the
   * permissions that a user has on a set of documents they have access to.
   * Do not store the document contents themselves in this model.
   */
  case class DocPermissions(permissions: Map[DocId, List[AccessType]]) {
    def check(docId: DocId, permission: AccessType): Boolean =
      permissions.get(docId).exists(_.contains(permission))
  }
}

/**
 * BANKING - EXERCISE SET 5
 *
 * Consider a banking application that allows users to hold and transfer money.
 */
object bank {

  final case class UserId(identifier: String)

  /**
   * EXERCISE 1
   *
   * Using only sealed traits and case classes, develop a model of a customer at a bank.
   */
  case class Customer(id: UserId, name: String, birthDate: LocalDate)

  /**
   * EXERCISE 2
   *
   * Using only sealed traits and case classes, develop a model of an account
   * type. For example, one account type allows the user to write checks
   * against a given currency. Another account type allows the user to earn
   * interest at a given rate for the holdings in a given currency.
   */
  sealed trait AccountType

  object AccountType {
    case object Savings extends AccountType
    case object Checking extends AccountType
    case object Retirement extends AccountType
    case object Deposit extends AccountType
  }

  /**
   * EXERCISE 3
   *
   * Using only sealed traits and case classes, develop a model of a bank
   * account, including details on the type of bank account, holdings, customer
   * who owns the bank account, and customers who have access to the bank account.
   */
  case class Account(balance: Double, accountType: AccountType, owner: Customer, allowedAccess: Set[Customer])
}

/**
 * STOCK PORTFOLIO - GRADUATION PROJECT
 *
 * Consider a web application that allows users to manage their portfolio of investments.
 */
object portfolio {

  import net.degoes.bank.Account

  final case class UserId(identifier: String)

  /**
   * EXERCISE 1
   *
   * Using only sealed traits and case classes, develop a model of a stock
   * exchange. Ensure there exist values for NASDAQ and NYSE.
   */
  sealed trait Exchange

  object Exchange {
    case object NASDAQ extends Exchange
    case object NYSE extends Exchange
    case object MOEX extends Exchange
  }

  /**
   * EXERCISE 2
   *
   * Using only sealed traits and case classes, develop a model of a currency
   * type.
   */
  sealed trait CurrencyType

  object CurrencyType {
    case object EUR extends CurrencyType
    case object USD extends CurrencyType
    case object RUB extends CurrencyType
  }

  /**
   * EXERCISE 3
   *
   * Using only sealed traits and case classes, develop a model of a stock
   * symbol. Ensure there exists a value for Apple's stock (APPL).
   */
  case class StockSymbol(name: String, exchange: Exchange)

  object StockSymbol {
    val appleStock: StockSymbol = StockSymbol("AAPL", Exchange.NASDAQ)
  }

  /**
   * EXERCISE 4
   *
   * Using only sealed traits and case classes, develop a model of a portfolio
   * held by a user of the web application.
   */
  case class Portfolio(
    userId: UserId,
    balance: BigDecimal,
    currencyType: CurrencyType,
    instruments: List[StockSymbol])

  /**
   * EXERCISE 5
   *
   * Using only sealed traits and case classes, develop a model of a user of
   * the web application.
   */
  case class User(id: UserId, name: String, birthDate: LocalDate, account: Account)

  /**
   * EXERCISE 6
   *
   * Using only sealed traits and case classes, develop a model of a trade type.
   * Example trade types might include Buy and Sell.
   */
  sealed trait TradeType

  object TradeType {
    case object Buy extends TradeType
    case object Sell extends TradeType
  }

  /**
   * EXERCISE 7
   *
   * Using only sealed traits and case classes, develop a model of a trade,
   * which involves a particular trade type of a specific stock symbol at
   * specific prices.
   */
  case class Trade(
    tradeType: TradeType,
    exchange: Exchange,
    symbol: StockSymbol,
    userId: UserId,
    price: BigDecimal,
    currencyType: CurrencyType,
    quantity: Double)
}
