-- 每日lock amount之后上报sap数量
select b.code, count(if(verification_result='BALANCE_LOCKED', 1, NULL))
from order_preallot a
         left join chery_iotd_admin.sys_brand b on a.brand_id=b.id
where date(a.transmit_time_to_sap) = '2026-02-26'
  and a.paying_bank <> '' and a.paying_bank is not null
group by b.code;