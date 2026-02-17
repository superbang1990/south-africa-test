select c.sales_erp, c.name, a.bank_name, a.bank_account, a.account_type, a.free_days,
count(if(b.verification_result='BALANCE_LOCKED', 1, null)) as '锁定成功订单数',
count(if(b.verification_result='INSUFFICIENT_BALANCE', 1, null)) as '当前锁定失败订单数',
count(if(b.verification_result='INSUFFICIENT_BALANCE' and b.verification_result_desc in ('Dealer does not have available funds', 'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.', 'The feed process failed because of LIMIT_OVERLINE_PROBLEM.; LIMIT_OVERLINE_PROBLEM', 'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.'), 1, null)) as '因为余额不足锁定失败次数'
from chery_iotd_admin.dealer_bank_account a
         left join order_preallot b on a.bank_name=b.paying_bank and a.bank_account=b.bank_account
         left join chery_iotd_admin.dealer_base_info c on a.dealer_code=c.dealer_code
where a.del_flag='0'
group by c.sales_erp, c.name, a.bank_name, a.bank_account, a.account_type, a.free_days;

