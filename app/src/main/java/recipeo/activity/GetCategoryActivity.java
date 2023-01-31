package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetCategoryRequest;
import recipeo.activity.results.GetCategoriesForUserResult;
import recipeo.activity.results.GetCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;

import javax.inject.Inject;
import java.util.List;

public class GetCategoryActivity {
    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    @Inject
    public GetCategoryActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public GetCategoryResult handleRequest(GetCategoryRequest getCategoryRequest) {
        log.info("Received GetCategoryRequest {}", getCategoryRequest);
        String userId = getCategoryRequest.getUserId();
        String categoryName = getCategoryRequest.getCategoryName();
        Category category = categoryDao.getCategory(userId,categoryName);

        return GetCategoryResult.builder()
                .withCategory(category)
                .build();
        }
}
