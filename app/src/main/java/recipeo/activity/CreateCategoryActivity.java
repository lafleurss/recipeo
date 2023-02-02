package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.CreateCategoryRequest;
import recipeo.activity.results.CreateCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;
import recipeo.exceptions.InvalidAttributeValueException;
import recipeo.utils.RecipeoServiceUtils;

import javax.inject.Inject;


public class CreateCategoryActivity {

    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    /**
     * Instantiates a new CreateCategoryActivity object.
     *
     * @param categoryDao CategoryDao to access the category table.
     */
    @Inject
    public CreateCategoryActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * This method handles the incoming request by persisting a new category
     * with the provided category name and customer ID from the request.
     * <p>
     * It then returns the newly created category.
     * <p>
     * If the provided category name, description or user ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createCategoryRequest request object containing the category name, description
     *                               and userId associated with it
     * @return CreateCategoryResult object containing the API defined {@link Category}
     */
    public CreateCategoryResult handleRequest(CreateCategoryRequest createCategoryRequest) {
        log.info("Received CreateCategoryRequest {}", createCategoryRequest);

        //Check for illegal characters
        if (!RecipeoServiceUtils.isValidString(createCategoryRequest.getCategoryName())) {
            throw new InvalidAttributeValueException("Category name [" +
                     createCategoryRequest.getCategoryName() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(createCategoryRequest.getUserId())) {
            throw new InvalidAttributeValueException("Category user ID [" +
                    createCategoryRequest.getUserId() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(createCategoryRequest.getCategoryDescription())) {
            throw new InvalidAttributeValueException("Category description [" +
                     createCategoryRequest.getCategoryDescription() +
                    "] contains illegal characters");
        }

        String userId = createCategoryRequest.getUserId();
        String categoryName = createCategoryRequest.getCategoryName();
        String categoryDescription = createCategoryRequest.getCategoryDescription();

        Category categoryToBeSaved = new Category();
        categoryToBeSaved.setCategoryName(categoryName);
        categoryToBeSaved.setCategoryDescription(categoryDescription);
        categoryToBeSaved.setUserId(userId);

        categoryDao.saveCategory(categoryToBeSaved);

        return CreateCategoryResult.builder()
                .withCategory(categoryToBeSaved)
                .build();

    }
}
