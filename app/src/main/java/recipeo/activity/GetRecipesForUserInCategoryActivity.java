package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserInCategoryRequest;
import recipeo.activity.results.GetRecipesForUserInCategoryResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the GetRecipesForUserInCategoryActivity for the RecipeoService's GetRecipe API.
 *
 * This API allows the customer to get all of their saved recipes in a particular category.
 */
public class GetRecipesForUserInCategoryActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    /**
     * Instantiates a new GetRecipesForUserInCategoryActivity object.
     *
     * @param recipeDao RecipeDao to access the recipe table.
     */
    @Inject
    public GetRecipesForUserInCategoryActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * This method handles the incoming request by retrieving the recipes for the requested
     * userId from the database. It then returns the list of recipes that are under the requested category.
     * If recipes do not exist, this should throw a RecipeNotFoundException.
     *
     * @param getRecipesForUserInCategoryRequest request object containing the user ID and categoryName
     * @return GetRecipesForUserResult object containing the API defined {@link List} of {@link RecipeModel}
     */
    public GetRecipesForUserInCategoryResult handleRequest(GetRecipesForUserInCategoryRequest
                                                                   getRecipesForUserInCategoryRequest) {
        log.info("Received GetRecipesForUserInCategoryRequest {}", getRecipesForUserInCategoryRequest);
        String userId = getRecipesForUserInCategoryRequest.getUserId();
        String category = getRecipesForUserInCategoryRequest.getCategory();
        List<Recipe> recipeList = recipeDao.getRecipesForUserInCategory(userId, category);

        if (recipeList == null || recipeList.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for user id: " + userId);
        }

        List<RecipeModel> recipeModelList = new ModelConverter().toRecipeModelList(recipeList);

        return GetRecipesForUserInCategoryResult.builder()
                .withRecipes(recipeModelList)
                .build();
    }
}
