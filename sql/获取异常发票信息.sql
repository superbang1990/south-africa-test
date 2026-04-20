select *
from (
    select file_name from chery_iotd_sales.order_invoice where old_flag is null or old_flag=0
                     union all
    select file_name from chery_iotd_part.part_order_invoice where old_flag is null or old_flag=0) a,
    (select original_name, file_size from chery_iotd_sales.common_attachment
                                    union all
    select original_name, file_size from chery_iotd_part.common_attachment) b
where a.file_name=b.original_name
and b.file_size=0;