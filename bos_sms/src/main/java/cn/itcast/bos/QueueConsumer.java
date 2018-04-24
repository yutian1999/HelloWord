package cn.itcast.bos;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

@Service
public class QueueConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
			MapMessage msg = (MapMessage) message;
			try {
				System.out.println("短信发送成功,手机号:"+msg.getString("telephone")+"验证码是:"+
				msg.getString("msg"));
			} catch (JMSException e) {
				e.printStackTrace();
			}
	}
	
}
