package cn.itcast.dao.elasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillIndexRepository extends 
ElasticsearchRepository<WayBill, Integer>{

}
