select b.sales_erp, b.name, a.paying_bank, a.bank_account, a.verification_result_desc, count(1)
from order_preallot a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code=b.dealer_code
where a.verification_result = 'INSUFFICIENT_BALANCE' and a.del_flag='0'
  and a.verification_result_desc not in (
                                         'Dealer does not have available funds',
                                         'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.',
                                         'The feed process failed because of LIMIT_OVERLINE_PROBLEM.; LIMIT_OVERLINE_PROBLEM'
    ) group by a.paying_bank, a.bank_account, a.verification_result_desc
order by a.paying_bank;


select b.sales_erp, b.name, a.paying_bank, a.bank_account, a.verification_result_desc, count(1),
       GROUP_CONCAT(a.vin)
from order_preallot a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code=b.dealer_code
where a.verification_result = 'INSUFFICIENT_BALANCE' and a.del_flag='0'
  and a.verification_result_desc in (
                                         'Dealer does not have available funds',
                                         'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.',
                                         'The feed process failed because of LIMIT_OVERLINE_PROBLEM.; LIMIT_OVERLINE_PROBLEM'
    ) group by a.paying_bank, a.bank_account, a.verification_result_desc
order by a.paying_bank