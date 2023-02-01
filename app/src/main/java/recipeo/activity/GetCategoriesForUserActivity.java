package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoriesForUserRequest;
import recipeo.activity.results.GetCategoriesForUserResult;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;

import javax.inject.Inject;
import java.util.List;

public class GetCategoriesForUserActivity {
    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    @Inject
    public GetCategoriesForUserActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * This method handles the incoming request by retrieving the categories for the requested
     * userId from the database.
     * <p>
     * It then returns the list of categories.
     * <p>
     * If categories do not exist, this should throw a CategoryNotFoundException.
     *
     * @param getCategoriesForUserRequest request object containing the user ID
     * @return GetCategoriesForUserResult object containing the API defined {@link List<Category>}
     */
    public GetCategoriesForUserResult handleRequest(GetCategoriesForUserRequest getCategoriesForUserRequest) {

        log.info("Received GetCategoriesForUserRequest{}", getCategoriesForUserRequest);
        String userId = getCategoriesForUserRequest.getUserId();
        List<Category> categoryList = categoryDao.getCategoriesForUser(userId);

        if (categoryList == null || categoryList.isEmpty()) {
            throw new CategoryNotFoundException("No categories found for user id: " + userId);
        }

        return GetCategoriesForUserResult.builder()
                .withCategories(categoryList)
                .build();
    }

}
