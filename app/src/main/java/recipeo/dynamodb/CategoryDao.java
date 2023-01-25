package recipeo.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import recipeo.dynamodb.models.Category;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.metrics.MetricsConstants;
import recipeo.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a category using {@link Category} to represent the model in DynamoDB.
 */
@Singleton
public class CategoryDao {
    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;

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
            throw new RecipeNotFoundException("Could not find category with id " + categoryId + " for user with id" + userId);
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
    public Category savePlaylist(Category category) {
        this.dynamoDbMapper.save(category);
        return category;
    }

}
