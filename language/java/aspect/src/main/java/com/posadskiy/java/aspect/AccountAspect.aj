public aspect AccountAspect {
  final int MIN_BALANCE = 10;
  
  pointcut callWithdraw(int amount, Account acc) :
    call(boolean Account.withdraw(int)) && args(amount) && target(acc);
}
