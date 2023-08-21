package com.spindia.sahayatamoney.activities_upi

interface PaymentStatusListener {
    fun onTransactionCompleted(transactionDetails: TransactionDetails?)
    fun onTransactionSuccess()
    fun onTransactionSubmitted()
    fun onTransactionFailed()
    fun onTransactionCancelled()
    fun onAppNotFound()
}