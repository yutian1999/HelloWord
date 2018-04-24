package cn.itcast.bos;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Service;
@Service
public class OrderConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("短信:"+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
