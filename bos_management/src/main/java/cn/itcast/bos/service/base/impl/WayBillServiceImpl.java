package cn.itcast.bos.service.base.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.OrderRepository;
import cn.itcast.bos.dao.base.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.base.WayBillService;
import cn.itcast.dao.elasticSearch.WayBillIndexRepository;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;
	
	@Override
	public void save(WayBill model) {
		
		/*if (model.getOrder() != null && model.getOrder().getId() != null) {
			Order order = model.getOrder();
			System.err.println(1);
			System.out.println(order.getId());
			orderRepository.save(order);
			System.err.println(2);
			model.setOrder(null);
		}*/
		/*WayBill wayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
		if(wayBill == null){
			//运单不存在
			wayBillRepository.save(model);
		}else{
			
			try {
				Integer id = wayBill.getId();
				BeanUtils.copyProperties(wayBill, model);
				wayBill.setId(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		if (model.getSignStatus() == null || model.getSignStatus() == 1) {
			model.setSignStatus(1);
			wayBillRepository.save(model);
			wayBillIndexRepository.save(model);
		}else{
			throw new RuntimeException("已经出库的运单,不能再修改");
		}
		//保存索引
	}

	@Override
	public Page<WayBill> find(WayBill wayBill,Pageable pageable) {
		if(StringUtils.isBlank(wayBill.getWayBillNum()) 
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus()==0)){
			//无条件查询数据库  所有条件都没有输入时   就去查询数据库
			return wayBillRepository.findAll(pageable);
		}else{
			//构建查询条件
			BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
			if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
				QueryBuilder builder = new TermQueryBuilder("wayBillNum",wayBill.getWayBillNum());
				queryBuilder.must(builder);
			}
			
			//发货地址
			if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
				QueryBuilder builder = new WildcardQueryBuilder("sendAddress","*"+wayBill.getSendAddress()+"*");
				
				
				//情况二  多个词条
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(queryStringQueryBuilder);
				boolQueryBuilder.should(builder);   //(!a & c) || (a & c)   c ||a  c  a 
				queryBuilder.must(boolQueryBuilder);
			}
			
			if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
				QueryBuilder builder = new WildcardQueryBuilder("recAddress","*"+wayBill.getRecAddress()+"*");
				
				queryBuilder.must(builder);
			}
			//速运类型  等值查询
			if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
				QueryBuilder builder = new TermQueryBuilder("sendProNum",wayBill.getSendProNum());
				queryBuilder.must(builder);
			}
			
			//状态  
			if (wayBill.getSignStatus() != null  && wayBill.getSignStatus() != 0) {
				QueryBuilder builder = new TermQueryBuilder("signStatus",wayBill.getSignStatus());
				queryBuilder.must(builder);
			}
			
			SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
			searchQuery.setPageable(pageable);
			return wayBillIndexRepository.search(searchQuery);
		}
	}

	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillRepository.findByWayBillNum(wayBillNum);
	}
	
	//索引同步
	@Override
	public void synIndex() {
		List<WayBill> list = wayBillRepository.findAll();
		wayBillIndexRepository.save(list);
	}

	@Override
	public List<WayBill> find(WayBill wayBill) {
		if(StringUtils.isBlank(wayBill.getWayBillNum()) 
				&& StringUtils.isBlank(wayBill.getSendAddress())
				&& StringUtils.isBlank(wayBill.getRecAddress())
				&& (wayBill.getSignStatus() == null || wayBill.getSignStatus()==0)){
			//无条件查询数据库  所有条件都没有输入时   就去查询数据库
			return wayBillRepository.findAll();
		}else{
			//构建查询条件
			BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
			if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
				QueryBuilder builder = new TermQueryBuilder("wayBillNum",wayBill.getWayBillNum());
				queryBuilder.must(builder);
			}
			
			//发货地址
			if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
				QueryBuilder builder = new WildcardQueryBuilder("sendAddress","*"+wayBill.getSendAddress()+"*");
				
				
				//情况二  多个词条
				QueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(
						wayBill.getSendAddress()).field("sendAddress").defaultOperator(Operator.AND);
				BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
				boolQueryBuilder.should(queryStringQueryBuilder);
				boolQueryBuilder.should(builder);   //(!a & c) || (a & c)   c ||a  c  a 
				queryBuilder.must(boolQueryBuilder);
			}
			
			if (StringUtils.isNotBlank(wayBill.getRecAddress())) {
				QueryBuilder builder = new WildcardQueryBuilder("recAddress","*"+wayBill.getRecAddress()+"*");
				
				queryBuilder.must(builder);
			}
			//速运类型  等值查询
			if (StringUtils.isNotBlank(wayBill.getSendProNum())) {
				QueryBuilder builder = new TermQueryBuilder("sendProNum",wayBill.getSendProNum());
				queryBuilder.must(builder);
			}
			
			//状态  
			if (wayBill.getSignStatus() != null  && wayBill.getSignStatus() != 0) {
				QueryBuilder builder = new TermQueryBuilder("signStatus",wayBill.getSignStatus());
				queryBuilder.must(builder);
			}
			
			SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);
			Pageable pageable = new PageRequest(0, 10000);
			searchQuery.setPageable(pageable);
			return wayBillIndexRepository.search(searchQuery).getContent();
		}
	}
	
	
	
}
