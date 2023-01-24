package recipeo.activity.dependency;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDbClientProvider {

    public DynamoDbClientProvider() {
    }

    /**
     * Provides a DynamoDBClient instance.
     *
     * @return AmazonDynamoDB object
     */

    public static AmazonDynamoDB getDynamoDBClient() {
        return getDynamoDBClient(Regions.US_EAST_2);
    }

    public static AmazonDynamoDB getDynamoDBClient(Regions region) {
        if (null == region) {
            throw new IllegalArgumentException("region cannot be null");
        } else {
            return AmazonDynamoDBClientBuilder.standard().withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).withRegion(region).build();
        }
    }
}
