-- 各个银行有多少锁定失败的vin
select b.sales_erp, b.name, a.paying_bank, a.bank_account, a.verification_result_desc, count(1), a.create_by
from order_preallot a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code=b.dealer_code
where a.verification_result = 'INSUFFICIENT_BALANCE' and a.del_flag='0'
  and a.verification_result_desc in (
                                     'Dealer does not have available funds',
                                     'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.',
                                     'The feed process failed because of LIMIT_OVERLINE_PROBLEM.; LIMIT_OVERLINE_PROBLEM',
                                     'FEED_PROCESS_FAILED: The feed process failed because of LIMIT_OVERLINE_PROBLEM.')
  and (select count(1) from order_preallot op where op.dealer_code=a.dealer_code and op.bank_account=a.bank_account and op.verification_result='BALANCE_LOCKED') = 0
group by a.paying_bank, a.bank_account, a.verification_result_desc
order by a.paying_bank

select a.order_number, a.son, a.vin, a.paying_bank, a.bank_account, a.verification_result_desc from order_preallot a
where a.verification_result = 'INSUFFICIENT_BALANCE' and a.del_flag='0' order by a.paying_bank, a.verification_result_desc

select a.paying_bank, b.name, count(1)
from order_preallot a
         left join chery_iotd_admin.sys_brand b on a.brand_id=b.id
where a.verification_result = 'BALANCE_LOCKED' group by a.paying_bank order by a.paying_bank, b.name;

select count(1) from order_preallot a
where a.verification_result = 'BALANCE_LOCKED';
-- LVVDB11B4SC259129, LNNBBDEE2SC173850
select * from order_preallot where vin='LVVDB21B0TD032473' and del_flag='0';

select * from order_preallot
-- 220A	ZA Vehicle Cash
select a.order_number as '订单号', a.son as '分车单号', a.sap_status,
       (CASE
            WHEN a.paying_bank='WESBANK' THEN '220B'
            WHEN a.paying_bank='ABSA' THEN '220C'
            WHEN a.paying_bank='MFC' THEN '220D'
            WHEN a.paying_bank='STD' THEN '220E'
            ELSE '220A'
           END) as '实际付款银行', b.fund_type as '主单付款方式'
from order_preallot a left join vehicle_order b on a.order_number=b.code
where
    (CASE
         WHEN a.paying_bank='WESBANK' THEN '220B'
         WHEN a.paying_bank='ABSA' THEN '220C'
         WHEN a.paying_bank='MFC' THEN '220D'
         WHEN a.paying_bank='STD' THEN '220E'
         ELSE '220A'
        END) != b.fund_type and b.fund_type !='220A' and a.paying_bank is not null and a.paying_bank <> '';

select * from claim_order where claim_no='CL10255634260200007';

select count(1) from part_order_invoice where id='2022340735529037825';

select b.code, count(1) from chery_iotd_admin.sys_dict_item a left join chery_iotd_sales.model b on a.item_value=b.code
where a.dict_type='sa_model_code_2_number' group by b.code having count(1) > 1

select* from chery_iotd_admin.sys_dict_item a where a.dict_type='sa_model_code_2_number'


select a.paying_bank, count(1) from order_preallot a
where a.verification_result != 'BALANCE_LOCKED' group by a.paying_bank;

select b.sales_erp, b.name, a.vin from order_preallot a left join chery_iotd_admin.dealer_base_info b
                                                                  on a.dealer_code=b.dealer_code
where sap_status='PendingLockAmount' and a.del_flag='0';

select name, count(1) from chery_iotd_sales.model group by name having count(1) > 1;
select * from chery_iotd_sales.model where name in ('JAECOO J6 AWD', 'LEPAS 8+ 2.0T EXECUTIVE AWD');

select * from vehicle_order where code like '%59119';

select a.order_number as '订单号', a.dealer_code as '经销商编码', c.name as '经销商名称', a.vin as 'VIN', sap_status as 'sap状态', b.order_status, a.paying_bank as '付款银行'
from order_preallot a
         left join vehicle_order b on a.order_number=b.code
         left join chery_iotd_admin.dealer_base_info c on a.dealer_code=c.dealer_code
where vin in (
    'LVVDB21B5TDD11274') and a.del_flag='0' order by vin

select * from chery_iotd_admin.dealer_base_info
where sales_erp like '%597';

-- 查询未配置floorplan的经销商
select a.dealer_code, a.sales_erp, a.name, b.total as 'ploorplan 数量'
from chery_iotd_admin.dealer_base_info a
         left join (select dealer_code as b_dealer_code, count(1) as total from chery_iotd_admin.dealer_bank_account group by dealer_code) b on a.dealer_code=b.b_dealer_code

where b.total is null and a.sales_erp like '10%';

select * from chery_iotd_admin.dealer_base_info where status=0;

select * from material group by code;
select * from model

select
    a.code as '物料号', a.name as '物料名称', a.in_color  as '内饰颜色编码', a.ex_color  as '外饰颜色编码', b.m_mode,
    a.model_code as '型号编码', a.status as '物料状态', b.name as '型号描述',
    a.vehicle_version as '版本', c.name as '版本名称' , a.power_system as '动力系统',
    a.engine_cylinder, a.brand_code as '品牌编码', ic.name as '内饰颜色', ec.name as '外饰颜色',
    a.update_by , a.update_time, a.row_version, a.pt_name, a.ar_name, a.tr_name ,
    a.th_name, a.es_name, a.it_name, a.eu_name
from material a
         left join model b on a.model_code = b.code and b.del_flag = '0' and b.tenant_id = 570272200
         left join vehicle_version c on a.vehicle_version = c.code and c.del_flag = '0' and c.tenant_id = 570272200
         left join color as ic on ic.code=a.in_color and ic.del_flag='0'
         left join color as ec on ec.code=a.ex_color and ec.del_flag='0'
where a.del_flag = '0' and a.tenant_id = 570272200;

select distinct a.order_number, a.son, a.vin, b.sales_erp, b.name, a.paying_bank, a.bank_account,
                a.verification_result, a.verification_result_desc
from order_preallot a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code=b.dealer_code
where a.verification_result = 'INSUFFICIENT_BALANCE' order by b.sales_erp;

select * from order_preallot where vin='LNNBBDAT7SD829605';

select a.code, a.model_code, b.name from material a
                                             left join model b on a.model_code=b.code
where a.code in ('T64803JBXLX0002', 'T64803JUELX0002');

select model_code from material where code in ('T64803JBXLX0002', 'T64803JCMLX0002', 'T64803JGXLX0002', 'T64803JUELX0002', 'T64803JSJLX0002');

select a.code, a.model_code, c.price_type, c.price,
       count(if(d.verification_result='BALANCE_LOCKED', 1, null)) as '已卖出数量'
from material a
         left join model b on a.model_code=b.code
         left join material_price c on a.code=c.material_code
         left join order_preallot d on d.model_code=b.code
where a.code in ('T64803JBXLX0002', 'T64803JCMLX0002', 'T64803JGXLX0002', 'T64803JUELX0002', 'T64803JSJLX0002')
group by a.code, a.model_code, c.price_type, c.price

select * from model where code='T9PROPINPHEV';
select * from model where m_mode='' or m_mode is null;
select * from order_preallot a where model_code='T9PROPINPHEV';
select distinct normal_flag from order_preallot

update model set m_mode='10330300' where code='T9PROPINPHEV';
update order_preallot set sap_status='PendingSettlementAmountFeedback', normal_flag='Abnormal' where son in ('VO1025559826020001800001', 'VO1025559626020000600001', 'VO1025559626020000700001');

select * from vehicle_order where code='VO10255585260200002';
update vehicle_order set locked_amount_status='All' where code='VO10255585260200002';

select UNIX_TIMESTAMP(date('2026-02-16 00:00:00')),  FROM_UNIXTIME(1771192800000/1000), @@global.time_zone, @@session.time_zone

