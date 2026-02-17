select a.code as 'material code', b.code as 'model code', b.name as 'model name',
    c.label as 'enats code', d.name as 'color description', a.brand_code
from material a
         left join model b on a.model_code=b.code
         left join chery_iotd_admin.sys_dict_item c on b.code=c.item_value and c.dict_type='sa_model_code_2_number'
         left join color d on a.ex_color=d.code;

select * from material where code='T7160GLBWKW0002';
select * from tracking_dealer_vehicle where vin='LNNBBDEGXSC194625';