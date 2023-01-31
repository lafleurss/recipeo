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


    public GetCategoriesForUserResult handleRequest(GetCategoriesForUserRequest getCategoriesForUserRequest) {

        log.info("Received GetCategoriesForUserRequest{}", getCategoriesForUserRequest);
        String userId = getCategoriesForUserRequest.getUserId();
        List<Category> categoryList = categoryDao.getCategoriesForUser(userId);

        return GetCategoriesForUserResult.builder()
                .withCategories(categoryList)
                .build();
    }

}
