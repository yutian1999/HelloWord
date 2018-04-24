package com.itcast.bos.web.action;

import java.io.File;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
@Service
public class QuartzConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		String realPath = "E:\\develop\\sts_space\\bos_mine\\bos_fore\\src\\main\\webapp\\freeMark";
		TextMessage textMessage = (TextMessage) message;
		try {
			String ids= textMessage.getText();
			String[] split = ids.split(",");
			for (String id : split) {
				File file = new File(realPath,id+".html");
				System.out.println(file);
				file.delete();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
