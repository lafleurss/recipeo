package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.UpdateCategoryRequest;
import recipeo.activity.results.CreateCategoryResult;
import recipeo.activity.results.UpdateCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.InvalidAttributeValueException;
import recipeo.utils.RecipeoServiceUtils;

import javax.inject.Inject;

public class UpdateCategoryActivity {
    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    @Inject
    public UpdateCategoryActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
    /**
     * This method handles the incoming request by retrieving the category, updating it,
     * and persisting the category.
     * <p>
     * It then returns the updated category.
     * <p>
     * If the category does not exist, this should throw a CategoryNotFoundException.
     * <p>
     * If the provided category name or user ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the user ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updateCategoryRequest request object containing the user ID, category name, and category description
     *                              associated with it
     * @return UpdateCategoryResult object containing the API defined {@link Category}
     */
    public UpdateCategoryResult handleRequest(UpdateCategoryRequest updateCategoryRequest) {
        log.info("Received UpdateCategoryActivity {}", updateCategoryRequest);

        //Check for illegal characters
        if (!RecipeoServiceUtils.isValidString(updateCategoryRequest.getCategoryName())) {
            throw new InvalidAttributeValueException("Category name [" + updateCategoryRequest.getCategoryName() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(updateCategoryRequest.getUserId())) {
            throw new InvalidAttributeValueException("Category user ID [" + updateCategoryRequest.getUserId() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(updateCategoryRequest.getCategoryDescription())) {
            throw new InvalidAttributeValueException("Category description [" + updateCategoryRequest.getCategoryDescription() +
                    "] contains illegal characters");
        }

        String userId = updateCategoryRequest.getUserId();
        String categoryName = updateCategoryRequest.getCategoryName();
        String categoryDescription = updateCategoryRequest.getCategoryDescription();

        Category categoryToBeSaved = new Category();
        categoryToBeSaved.setCategoryName(categoryName);
        categoryToBeSaved.setCategoryDescription(categoryDescription);
        categoryToBeSaved.setUserId(userId);

        Category category = categoryDao.getCategory(userId, categoryName);

        if (category == null){
            throw new CategoryNotFoundException("The category name: " + categoryName + " cannot be found for user id: " + userId);
        }

        categoryDao.saveCategory(categoryToBeSaved);

        return UpdateCategoryResult.builder()
                .withCategory(categoryToBeSaved)
                .build();

        }
}
