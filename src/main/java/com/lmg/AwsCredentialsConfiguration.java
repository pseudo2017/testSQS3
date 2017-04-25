package com.lmg;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(AwsCredentialsProperties.class)
public class AwsCredentialsConfiguration implements AWSCredentials {
    AWSCredentials Lcredentials = null;
    AWSCredentials Tcredentials = null;
    @Autowired
    private AwsCredentialsProperties awsCredentialsProperties;

    @PostConstruct
    public void init() {
        //System.setProperty("aws.accessKeyId", awsCredentialsProperties.getAccessKeyId());
        //System.setProperty("aws.secretKey", awsCredentialsProperties.getSecretKey());
    	try {
    		Lcredentials = new ProfileCredentialsProvider().getCredentials();
    	} catch (Exception e) {
    		throw new AmazonClientException(
                "Cannot load the credentials from the credential profiles file. " +
                "Please make sure that your credentials file is at the correct " +
                "location (~/.aws/credentials), and is in valid format.",
                e);
    	}
    	//
        // Step 1. On utilise le long-term credentials du compte qui exécute to call the
        // AWS Security Token Service (STS) AssumeRole API, specifying 
        // the ARN for the role DynamoDB-RO-role in research@example.com.
        AWSSecurityTokenServiceClient stsClient = new  AWSSecurityTokenServiceClient(Lcredentials);
        //
        AssumeRoleRequest assumeRequest = new AssumeRoleRequest()
                .withRoleArn(awsCredentialsProperties.getParam_assumed_role())
                .withDurationSeconds(3600)
                .withRoleSessionName(awsCredentialsProperties.getParam_sessionName());
        //
        AssumeRoleResult assumeResult = stsClient.assumeRole(assumeRequest);
        Tcredentials =
        new BasicSessionCredentials(
                    assumeResult.getCredentials().getAccessKeyId(),
                    assumeResult.getCredentials().getSecretAccessKey(),
                    assumeResult.getCredentials().getSessionToken());
    }

	@Override
	public String getAWSAccessKeyId() {
		// TODO Auto-generated method stub
		return Tcredentials.getAWSAccessKeyId();
	}

	@Override
	public String getAWSSecretKey() {
		// TODO Auto-generated method stub
		return Tcredentials.getAWSSecretKey();
	}
    
    //
	public SqsQueue getQueue()
	{
		SqsQueue ret = new SqsQueue(awsCredentialsProperties.getParam_queue_name(), awsCredentialsProperties.getParam_queue_url());
		return ret;
	}
	public SqsQueue getQueueNew()
	{
		SqsQueue ret = new SqsQueue(awsCredentialsProperties.getParam_queue_name_new(), awsCredentialsProperties.getParam_queue_url());
		return ret;
	}
	
	public Region getRegion()
	{
		Region ret  ;
		String nomRegion = awsCredentialsProperties.getParam_region();
		ret = Region.getRegion(Regions.fromName(nomRegion));
		return ret;
	}
    
}
