package com.bank.banksystem.model;

public enum TranscationStatus {
    INCOME, // income from other user, + balance
    EXPENSE, // expense to other user, - balance
    RECHARGE, // recharge from outside, + balance
    WITHDRAW // withdraw to outside, - balance
}
