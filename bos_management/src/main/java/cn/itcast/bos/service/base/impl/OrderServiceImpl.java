package cn.itcast.bos.service.base.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.OrderRepository;
import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.base.OrderService;
@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	//注入jms模板
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	private AreaRepository areaRepository;
	@Override
	public void save(Order order) {
		System.out.println(order);
		//下单时间
		order.setOrderTime(new Date());
		
		
		Area sendArea = order.getSendArea();
		sendArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),sendArea.getCity(),sendArea.getDistrict());
		
		Area recArea = order.getRecArea();
		recArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(),recArea.getCity(),recArea.getDistrict());
		
		order.setSendArea(sendArea);
		order.setRecArea(recArea);
		
		//完全地址匹配自动分单
		String FixedAreaId = WebClient.
		create("http://localhost:8081/services/customerService/findFixedAreaIdByAddress?address="+order.getSendAddress())
		.accept(MediaType.APPLICATION_JSON).get(String.class);
		//通过定区id查询定区
		if(FixedAreaId != null){
			FixedArea fixedArea = fixedAreaRepository.findOne(FixedAreaId);
			//通过定区去找快递员
			Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
			if(iterator.hasNext()){
				Courier courier = iterator.next();
				System.out.println("完全匹配自动分单成功");
				
				order.setOrderType("1");
				order.setCourier(courier);
				order.setOrderNum(UUID.randomUUID().toString());
				orderRepository.save(order);
				//生成工单
				genericWorkBill(order);
				return;
			}
		}
		
		
		//第二种自动分单  通过区域查找分区关键字和辅助关键字
		String sendAddress = order.getSendAddress();
		for(SubArea subArea :sendArea.getSubareas()){
			if(sendAddress.contains(subArea.getKeyWords())
					||sendAddress.contains(subArea.getAssistKeyWords())){
				//分区找定区
				FixedArea fixedArea2 = subArea.getFixedArea();
				//定区找快递员
				Iterator<Courier> it = fixedArea2.getCouriers().iterator();
				if(it.hasNext()){
					Courier courier = it.next();
					//订单绑定快递员  产生工单
					order.setCourier(courier);
					order.setOrderType("1");
					order.setOrderNum(UUID.randomUUID().toString());
					orderRepository.save(order);
					//生成工单
					genericWorkBill(order);
					System.out.println("关键字自动分单");
					return;
				}
			}
		}
		
		//人工分单
		System.out.println("人工分单......");
		order.setOrderType("2");
		orderRepository.save(order);
	}
	
	@Autowired
	private WorkBillRepository workBillRepository;
	
	//发短信
	public void genericWorkBill(Order order){
		//生成工单
		WorkBill workBill = new WorkBill();
		workBill.setBuildtime(new Date());
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setRemark(order.getRemark());
		workBill.setSmsNumber(UUID.randomUUID().toString());
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillRepository.save(workBill);
		final String message = "快递员:"+order.getCourier().getName()+"你好,你有一单快件需要取件,地址:"+order.getSendAddress()
		+"联系电话:"+order.getSendMobile()+"联系人:"+order.getSendName();
		//通过mq发短信给快递员
		jmsTemplate.send("bos_order", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(message);
				
				return textMessage;
			}
		});
		workBill.setPickstate("已通知");
	}

	@Override
	public Order findByorderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}

}
