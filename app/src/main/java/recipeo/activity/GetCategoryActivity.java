package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoryRequest;
import recipeo.activity.results.GetCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;
import recipeo.exceptions.CategoryNotFoundException;

import javax.inject.Inject;

public class GetCategoryActivity {
    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    /**
     * Instantiates a new GetCategoryActivity object.
     *
     * @param categoryDao CategoryDao to access the category table.
     */
    @Inject
    public GetCategoryActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * This method handles the incoming request by retrieving the category from the database.
     * <p>
     * It then returns the category.
     * <p>
     * If the category does not exist, this should throw a CategoryNotFoundException.
     *
     * @param getCategoryRequest request object containing the category ID
     * @return GetCategoryResult object containing the API defined {@link Category}
     */
    public GetCategoryResult handleRequest(GetCategoryRequest getCategoryRequest) {
        log.info("Received GetCategoryRequest {}", getCategoryRequest);
        String userId = getCategoryRequest.getUserId();
        String categoryName = getCategoryRequest.getCategoryName();
        Category category = categoryDao.getCategory(userId, categoryName);

        if (category == null) {
            throw new CategoryNotFoundException("The category name: " +
                    categoryName + " cannot be found for user id: " + userId);
        }

        return GetCategoryResult.builder()
                .withCategory(category)
                .build();
    }
}
