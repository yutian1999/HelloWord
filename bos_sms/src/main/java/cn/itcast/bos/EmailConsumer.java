package cn.itcast.bos;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.utils.MailUtils;
@Service
public class EmailConsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		try {
			String content = mapMessage.getString("content");
			String email = mapMessage.getString("email");
			String activeCode = mapMessage.getString("activeCode");
			MailUtils.sendMail("速运快递邮箱绑定", content, email, activeCode);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
