select c.sales_erp, c.name, a.product_code, a.model_code, d.name as 'mode_description', a.create_time,
    sum(a.actual_purchase_quantity)-sum(if(b.allocated is null, 0, b.allocated)) as 'unallocated',
    sum(a.actual_purchase_quantity),
    sum(if(b.allocated is null, 0, b.allocated)) as 'allocated'
from (
    select ab.dealer_code, aa.product_code, aa.model_code, date(ab.create_time) as create_time,
    sum(if(aa.actual_purchase_quantity is null, 0, aa.actual_purchase_quantity)) as actual_purchase_quantity
    from vehicle_order_detail aa
    left join vehicle_order ab on aa.order_number=ab.code
    where aa.del_flag='0'
    and ab.del_flag = 0 and (ab.order_status != 'PendingSubmission' or (ab.order_status = 'PendingSubmission' and ab.allocation_status in ('PARTIAL', 'ALL')) or (ab.order_status = 'PendingSubmission' and ab.order_source = 'Subsidiary'))
    and ab.order_status in ('ReviewPass')
    and ab.brand_id in (2014313302632443906, 2014313302636638210, 2014313302640832514, 2014313302640832515)
    group by ab.dealer_code, aa.product_code, aa.model_code, date(ab.create_time)
    having actual_purchase_quantity > 0) a
left join (
    select aa.dealer_code, aa.product_code, aa.model_code, date(ab.create_time) as create_time, count(1) as allocated
    from order_preallot aa
    left join vehicle_order ab on aa.order_number=ab.code
    where aa.del_flag='0' and aa.cancel_status='NotCancelled' and aa.sap_status <> 'Cancelled'
    and ab.del_flag = 0 and (ab.order_status != 'PendingSubmission' or (ab.order_status = 'PendingSubmission' and ab.allocation_status in ('PARTIAL', 'ALL')) or (ab.order_status = 'PendingSubmission' and ab.order_source = 'Subsidiary'))
    and ab.order_status in ('ReviewPass')
    and ab.brand_id in (2014313302632443906, 2014313302636638210, 2014313302640832514, 2014313302640832515)
    group by aa.dealer_code, aa.product_code, aa.model_code, date(ab.create_time)) b
on a.dealer_code=b.dealer_code and a.product_code=b.product_code and a.model_code=b.model_code and a.create_time=b.create_time
left join chery_iotd_admin.dealer_base_info c on a.dealer_code=c.dealer_code
left join model d on d.code=a.model_code
group by c.sales_erp, c.name, a.product_code, a.model_code, d.name, a.create_time
having (sum(a.actual_purchase_quantity)-sum(if(b.allocated is null, 0, b.allocated))) > 0
order by c.name;


select c.sales_erp, c.name, a.model_code, d.name as 'mode_description', a.create_time,
       sum(a.actual_purchase_quantity)-sum(if(b.allocated is null, 0, b.allocated)) as 'unallocated',
    sum(a.actual_purchase_quantity),
       sum(if(b.allocated is null, 0, b.allocated)) as 'allocated'
from (
         select ab.dealer_code, aa.model_code, date(ab.create_time) as create_time,
     sum(if(aa.actual_purchase_quantity is null, 0, aa.actual_purchase_quantity)) as actual_purchase_quantity
    from vehicle_order_detail aa
    left join vehicle_order ab on aa.order_number=ab.code
where aa.del_flag='0'
  and ab.del_flag = 0 and (ab.order_status != 'PendingSubmission' or (ab.order_status = 'PendingSubmission' and ab.allocation_status in ('PARTIAL', 'ALL')) or (ab.order_status = 'PendingSubmission' and ab.order_source = 'Subsidiary'))
  and ab.order_status in ('ReviewPass')
  and ab.brand_id in (2014313302632443906, 2014313302636638210, 2014313302640832514, 2014313302640832515)
group by ab.dealer_code, aa.model_code, date(ab.create_time)
having actual_purchase_quantity > 0) a
    left join (
select aa.dealer_code, aa.model_code, date(ab.create_time) as create_time, count(1) as allocated
from order_preallot aa
    left join vehicle_order ab on aa.order_number=ab.code
where aa.del_flag='0' and aa.cancel_status <> 'CancellationSuccessful' and aa.sap_status <> 'Cancelled'
  and ab.del_flag = 0 and (ab.order_status != 'PendingSubmission' or (ab.order_status = 'PendingSubmission' and ab.allocation_status in ('PARTIAL', 'ALL')) or (ab.order_status = 'PendingSubmission' and ab.order_source = 'Subsidiary'))
  and ab.order_status in ('ReviewPass')
  and ab.brand_id in (2014313302632443906, 2014313302636638210, 2014313302640832514, 2014313302640832515)
group by aa.dealer_code, aa.model_code, date(ab.create_time)) b
on a.dealer_code=b.dealer_code and a.model_code=b.model_code and a.create_time=b.create_time
    left join chery_iotd_admin.dealer_base_info c on a.dealer_code=c.dealer_code
    left join model d on d.code=a.model_code
group by c.sales_erp, c.name, a.model_code, d.name, a.create_time
having (sum(a.actual_purchase_quantity)-sum(if(b.allocated is null, 0, b.allocated))) > 0
order by c.name;