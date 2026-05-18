-- material, mode, 770
select a.code as 'material code', b.code as 'model code', b.name as 'model name',
    c.label as 'enats code', d.name as 'color description', a.brand_code
from material a
         left join model b on a.model_code=b.code and b.del_flag=0
         left join chery_iotd_admin.sys_dict_item c on b.code=c.item_value and c.dict_type='sa_model_code_2_number' and c.del_flag=0
         left join color d on a.ex_color=d.code and d.del_flag=0
where a.del_flag=0;

-- dealership, 283
select a.sales_erp as 'delaer_code', a.name as 'dealer_name', group_concat(d.code) as 'brands', a.province_code, a.detail_address, a.postal_code
from chery_iotd_admin.dealer_base_info a
         left join chery_iotd_admin.sys_dept b on a.dealer_code=b.source_org_code and b.del_flag = '0'
         left join chery_iotd_admin.sys_dept_brand c on b.dept_id=c.dept_id and c.del_flag = '0'
         left join chery_iotd_admin.sys_brand d on c.brand_id=d.id and d.del_flag = '0'
where a.del_flag = '0'
  and a.dealer_type in(1, 3)
  and a.dealer_code in (
    select dealer_code from chery_iotd_admin.sale_org_dealer_relation where sale_org_code in (select code from chery_iotd_admin.sys_sales_org where type=0) and `status`=1)
group by a.sales_erp, a.name, a.province_code, a.detail_address, a.postal_code
order by a.sales_erp asc;