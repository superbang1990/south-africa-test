select b.sales_erp,
       b.name,
       a.paying_bank,
       a.bank_account,
       a.account_type,
       (select count(1)
        from order_preallot i
        where i.bank_account = a.bank_account
          and i.account_type = a.account_type
          and i.verification_result = 'BALANCE_LOCKED') as 'ever_locked_times', a.verification_result_desc,
       group_concat(distinct a.vin) as 'vin_list'
from order_preallot a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code = b.dealer_code
         left join vehicle_order c on a.order_number = c.code
where a.verification_result in ('INSUFFICIENT_BALANCE', 'LOCK_FAILED')
  and a.del_flag = '0'
  and a.cancel_status <> 'CancellationSuccessful'
  and c.locked_amount_status <> 'ALL'
-- and a.paying_bank='MFC'
group by a.paying_bank, a.bank_account, a.verification_result_desc
order by a.paying_bank asc, a.verification_result_desc asc;

select a.vin, a.material_code,
       a.introductor_time,
       c.name as introduced_by,
       d.sales_erp as dealer_code,
       d.name as dealer_name,
       a.release_time,
       b.response
from actual_so_supervision_report_record a
         left join actual_so_supervision_report_feedback b on a.vin=b.vin
         left join chery_iotd_admin.sys_user c on a.introductor_user_id=c.user_id
         left join chery_iotd_admin.dealer_base_info d on a.release_dealer_code=d.dealer_code
where a.report_status='EnatisVerifyFailed';

select a.vin, a.material_code,
       a.introductor_time,
       c.name as introduced_by,
       d.sales_erp as dealer_code,
       d.name as dealer_name,
       a.release_time,
       b.response
from actual_so_supervision_report_record a
         left join actual_so_supervision_report_feedback b on a.vin=b.vin
         left join chery_iotd_admin.sys_user c on a.introductor_user_id=c.user_id
         left join chery_iotd_admin.dealer_base_info d on a.release_dealer_code=d.dealer_code
where a.report_status='EnatisVerifyFailed';

select *
from vehicle_order

select b.sales_erp as 'delaer_code', b.name 'dealer_name', a.bank_name,
       a.bank_account,
       a.account_type as 'plan code', a.free_days
from chery_iotd_admin.dealer_bank_account a
         left join chery_iotd_admin.dealer_base_info b on a.dealer_code = b.dealer_code
order by dealer_name

select *
from vehicle_order
where code = 'VO10255587260200001';
select *
from order_preallot
where order_number = 'VO10255587260200001';
select *
from stock_subsidiary_vehicle
where vin in ('LVVDB21B1TD037519');

select distinct sap_status
from order_preallot
select *
from order_preallot
where vin = 'LVVDB21B1TD037519'
update order_preallot
set sap_status='PendingSettlementAmountFeedback',
    normal_flag='Abnormal'
where vin in ('LNNBBDDW1SG110483')
  and del_flag = '0';

select *
from part_purchase_order
where order_number = 'PP10255499260200001';
select *
from stock_part_take_order

select a.code as 'material_code', a.brand_code as 'brand_code', a.model_code, c.label as 'model_number'
from material a
         left join model b on a.model_code = b.code
         left join chery_iotd_admin.sys_dict_item c
                   on a.model_code = c.item_value and c.dict_type = 'sa_model_code_2_number';

select *
from sys_oauth_client_details
where client_scene_biz_code = 'SO_SUPERVISION_REPORT';

select distinct create_type
from customer
where dealer_code = 'QR20260283641';
select *
from customer

update customer
set create_type='Dealer'
where create_type = 'Oem';
select distinct
from customer

select *
from vehicle_base_info
where vin = 'LVVDD21B6RC08965'

select *
from chery_iotd_admin.dealer_base_info
where sales_erp = '10255491';
select *
from chery_iotd_admin.sys_dept
where source_org_code in ('QR20260248366')

-- 10255514，QR20260248366, 2018604556469997633
update stock_dealer_vehicle
set dealer_code='QR20260248366',
    dept_id='2018604556469997633'
where vin in (
    'LVVDB21B4SD570415');
update tracking_subsidiary_vehicle
set dealer_code='QR20260248366'
where vin in (
    'LVVDB21B4SD570415');

-- LVVDB21B0TC032012 ：SAB1 - VDS DENEL Yard
select *
from stock_subsidiary_vehicle
where vin = 'LVVDB21B0TC032012';
select *
from tracking_subsidiary_vehicle
where vin = 'LVVDB21B0TC032012';
select *
from vehicle_base_info
where vin = 'LVVDB21B0TC032012';


insert into stock_dealer_vehicle (vin, material_code, brand_id, status_code, inbound_time, sale_org_code, del_flag,
                                  tenant_id, dealer_code, dept_id)
values ('LVVDB11B3SC222847', 'T7150TMGVHC0010', '2014313302632443906',
        'InStock', now(), '2200', 0,
        '570272200', 'QR20260298752', '2018604551260672124');


-- 更新交货单状态为子公司已开票
update chery_iotd_sales.order_despatch
set shipment_note_status ='SaleOrgInvoiced'
where vin in (select vin
              from (select oi.vin
                    from order_invoice oi
                             left join order_despatch od on oi.delivery_note_number = od.delivery_note_number
                             left join order_preallot op on od.preallot_note_number = op.son
                             left join vehicle_order obi on oi.order_number = obi.code
                    where oi.invoice_type = 'ZA20'
                      and oi.old_flag is null
                      and oi.red_credit_flag is null
                      and oi.del_flag = '0'
                      and op.del_flag = '0'
                      and op.sap_status in ('SaleOrgAwaitingInvoice', 'SaleOrgPosted')) temp);

-- 更新子公司车辆跟踪状态为子公司已开票
update chery_iotd_sales.tracking_subsidiary_vehicle
set status_code ='SaleOrgInvoiced',
    status_name = '子公司已开票'
where vin in (select vin
              from (select oi.vin
                    from order_invoice oi
                             left join order_despatch od on oi.delivery_note_number = od.delivery_note_number
                             left join order_preallot op on od.preallot_note_number = op.son
                             left join vehicle_order obi on oi.order_number = obi.code
                    where oi.invoice_type = 'ZA20'
                      and oi.old_flag is null
                      and oi.red_credit_flag is null
                      and oi.del_flag = '0'
                      and op.del_flag = '0'
                      and op.sap_status in ('SaleOrgAwaitingInvoice', 'SaleOrgPosted')) temp)
  AND del_flag = '0';

-- 更新经销商车辆状态为子公司已开票
update chery_iotd_sales.tracking_dealer_vehicle
set status_code ='SaleOrgInvoiced',
    status_name = '子公司已开票'
where vin in (select vin
              from (select oi.vin
                    from order_invoice oi
                             left join order_despatch od on oi.delivery_note_number = od.delivery_note_number
                             left join order_preallot op on od.preallot_note_number = op.son
                             left join vehicle_order obi on oi.order_number = obi.code
                    where oi.invoice_type = 'ZA20'
                      and oi.old_flag is null
                      and oi.red_credit_flag is null
                      and oi.del_flag = '0'
                      and op.del_flag = '0'
                      and op.sap_status in ('SaleOrgAwaitingInvoice', 'SaleOrgPosted')) temp)
  AND del_flag = '0';

-- 1. 更新分车单状态为子公司已开票
update order_preallot
set sap_status ='SaleOrgInvoiced'
where vin in (select vin
              from (select oi.vin
                    from order_invoice oi
                             left join order_despatch od on oi.delivery_note_number = od.delivery_note_number
                             left join order_preallot op on od.preallot_note_number = op.son
                             left join vehicle_order obi on oi.order_number = obi.code
                    where oi.invoice_type = 'ZA20'
                      and oi.old_flag is null
                      and oi.red_credit_flag is null
                      and oi.del_flag = '0'
                      and op.del_flag = '0'
                      and op.sap_status in ('SaleOrgAwaitingInvoice', 'SaleOrgPosted')) temp);