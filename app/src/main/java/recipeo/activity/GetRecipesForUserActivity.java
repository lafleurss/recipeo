package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeFilter;
import recipeo.models.RecipeModel;

import java.util.List;

import javax.inject.Inject;
/**
 * Implementation of the GetRecipesForUserActivity for the RecipeoService's GetRecipe API.
 *
 * This API allows the customer to get all of their saved recipes.
 */
public class GetRecipesForUserActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    /**
     * Instantiates a new GetRecipesForUserActivity object.
     *
     * @param recipeDao RecipeDao to access the recipe table.
     */
    @Inject
    public GetRecipesForUserActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * This method handles the incoming request by retrieving the recipes for the requested
     * userId from the database. It then returns the list of recipes.
     * If recipes do not exist, this should throw a RecipeNotFoundException.
     *
     * @param getRecipesForUserRequest request object containing the user ID
     * @return GetRecipesForUserResult object containing the API defined {@link List} of {@link RecipeModel}
     */
    public GetRecipesForUserResult handleRequest(GetRecipesForUserRequest getRecipesForUserRequest) {
        log.info("Received GetRecipesForUserResult {}", getRecipesForUserRequest);
        String userId = getRecipesForUserRequest.getUserId();
        RecipeFilter filterType = getRecipesForUserRequest.getFilterType();
        List<Recipe> recipeList = recipeDao.getRecipesForUser(userId, filterType);

        if (recipeList == null || recipeList.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for user id: " + userId);
        }

        List<RecipeModel> recipeModelList = new ModelConverter().toRecipeModelList(recipeList);

        return GetRecipesForUserResult.builder()
                .withRecipes(recipeModelList)
                .build();
    }
}
