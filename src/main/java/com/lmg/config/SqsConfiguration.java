package com.lmg.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;


import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.lmg.AwsCredentialsConfiguration;
import com.lmg.PurchaseEventListener;
import com.lmg.SqsQueue;
import javax.jms.Queue;
import javax.jms.Session;

@Configuration
@DependsOn("awsCredentialsConfiguration")
public class SqsConfiguration {


//    @Value("${aws.region:eu-west-1}")
//    private String awsRegion;

//    @Value("${sqs.events-queue-name:sqs-sample-events-queue}")
//    private String sqsQueueName;
	
    @Autowired
	AwsCredentialsConfiguration Config;

    @Autowired
    private PurchaseEventListener purchaseEventListener;

    private SQSConnection connection;

    @PostConstruct
    public void init() throws JMSException {
        connection = createConnection2();
        //AmazonSQSClient sqsClient = sqsClient();
        //purchaseEventsQueue(sqsClient);
        setupListener();
        connection.start();
    }
    
    @PreDestroy
    public void cleanUp() throws JMSException {
        connection.close();
    }

    @Bean
    public AmazonSQSClient sqsClient() {
        AmazonSQSClient sqsClient = new AmazonSQSClient();
        sqsClient.setRegion(getRegion());
        return sqsClient;
    }
    /*
     * 
     * Nouvelle methode pour recuperer un que
     * 
     */
    
    @Bean
    public SqsQueue purchaseEventsQueue(AmazonSQSClient sqsClient) {
    	SqsQueue ret = Config.getQueueNew();
        CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(ret.getCompleteName());
        CreateQueueResult createQueueResult = sqsClient.createQueue(createQueueRequest);
        return new SqsQueue(ret.getName(), createQueueResult.getQueueUrl());
    }    
  
  
    private void setupListener() throws JMSException {
        Session session = createSession();
        Queue jmsQueue = createJmsQueue(session);
        MessageConsumer consumer = createConsumer(session, jmsQueue);
        consumer.setMessageListener(purchaseEventListener);
    }    
    
    private Session createSession() throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    private Queue createJmsQueue(Session session) throws JMSException {
    	SqsQueue Q = Config.getQueue();
        return session.createQueue(Q.getCompleteName());
    }
 
    private MessageConsumer createConsumer(Session session, Queue jmsQueue) throws JMSException {
        return session.createConsumer(jmsQueue);
    }    
    
    /*
     *  Nouvelle methode
     * 
     */
    private SQSConnection createConnection2() throws JMSException {
        SQSConnectionFactory connectionFactory = getSqsConnectionFactory2();
        return connectionFactory.createConnection();
    }
    private SQSConnection createConnection() throws JMSException {
        SQSConnectionFactory connectionFactory = getSqsConnectionFactory();
        return connectionFactory.createConnection();
    }
    /////////////////////////////////////////////////////////////////////////////////
    /*
     * Nouvelle methode de creation de la connexion
     * Pourquoi faire une connexion et un client ?
     * Pas certain que cela serve réellement
     * 
     * 
     */
    private SQSConnectionFactory getSqsConnectionFactory2() {
    	
    	return SQSConnectionFactory.builder()
                .withRegion(getRegion())
                .withAWSCredentialsProvider((AWSCredentialsProvider) Config)
                .build();
    }
    private SQSConnectionFactory getSqsConnectionFactory() {
    	return SQSConnectionFactory.builder()
                .withRegion(getRegion())
                .withAWSCredentialsProvider(new DefaultAWSCredentialsProviderChain())
                .build();
    }
    ////////////////////////////////////////////////////////////////////////////////
    /*
     * La region est définie dans le fichier de configuration
     * 
     */
    private Region getRegion() {
        //return Region.getRegion(Regions.fromName(awsRegion));
    	return Config.getRegion();
    }   
}
