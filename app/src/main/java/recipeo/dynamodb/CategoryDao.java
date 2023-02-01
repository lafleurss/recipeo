package recipeo.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.metrics.MetricsConstants;
import recipeo.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accesses data for a category using {@link Category} to represent the model in DynamoDB.
 */
@Singleton
public class CategoryDao {
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;
    private static final int PAGE_SIZE = 20;

    /**
     * Instantiates a CategoryDao object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the recipe table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public CategoryDao(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Returns the {@link Category} corresponding to the specified category id.
     *
     * @param userId the User ID
     * @param categoryId the Category ID
     * @return the stored Category, or null if none was found.
     */
    public Category getCategory(String userId, String categoryId) {
        Category category = this.dynamoDbMapper.load(Category.class, userId, categoryId);

        if (category == null) {
            metricsPublisher.addCount(MetricsConstants.GETCATEGORY_CATEGORYNOTFOUND_COUNT, 1);
            throw new RecipeNotFoundException("Could not find category with id: " + categoryId + " for user with id: " + userId);
        }
        metricsPublisher.addCount(MetricsConstants.GETCATEGORY_CATEGORYNOTFOUND_COUNT, 0);
        return category;
    }

    /**
     * Saves (creates or updates) the category.
     *
     * @param category The Category to save
     * @return The Category object that was saved
     */
    public Category saveCategory(Category category) {
        this.dynamoDbMapper.save(category);
        return category;
    }

    public List<Category> getCategoriesForUser(String userId) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userId" , new AttributeValue().withS(userId));

        DynamoDBQueryExpression<Category> queryExpression = new DynamoDBQueryExpression<Category>()
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(valueMap)
                .withLimit(PAGE_SIZE);

        List<Category> categoryList = dynamoDbMapper.queryPage(Category.class, queryExpression).getResults();

        if (categoryList == null) {
            metricsPublisher.addCount(MetricsConstants.GETCATEGORIESFORUSER_CATEGORYNOTFOUND_COUNT, 1);
            throw new RecipeNotFoundException("Could not find recipes with for user with id" + userId);
        }
        metricsPublisher.addCount(MetricsConstants.GETCATEGORIESFORUSER_CATEGORYNOTFOUND_COUNT, 0);
        return categoryList;
    }
}
