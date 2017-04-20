package com.lmg;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import com.lmg.model.Event;
import com.lmg.model.PurchaseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;



@Component
public class PurchaseEventListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseEventListener.class);

    @Autowired
    private EventReader eventReader;

    private String getMessageText(SQSTextMessage message) {
        try {
            return message.getText();
        } catch (JMSException e) {
            throw new UnreadableSqsMessageException("Failed to get message text from message", e);
        }
    }

	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
        String messageText = getMessageText((SQSTextMessage) arg0);
        LOGGER.info("Received SQS message with text: {}", messageText);
        
        
        Event<PurchaseData> purchaseEvent = eventReader.readEvent(messageText, PurchaseData.class);
        LOGGER.info("Purchase event received: {}", purchaseEvent);
    }
		

}
