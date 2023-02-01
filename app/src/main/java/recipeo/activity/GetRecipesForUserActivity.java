package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of the GetRecipesForUserActivity for the RecipeoService's GetRecipe API.
 *
 * This API allows the customer to get all of their saved recipes.
 */
public class GetRecipesForUserActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    @Inject
    public GetRecipesForUserActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * This method handles the incoming request by retrieving the recipes for the requested
     * userId from the database.
     * <p>
     * It then returns the list of recipes.
     * <p>
     * If recipes do not exist, this should throw a RecipeNotFoundException.
     *
     * @param getRecipesForUserRequest request object containing the user ID
     * @return GetRecipesForUserResult object containing the API defined {@link List<RecipeModel>}
     */
    public GetRecipesForUserResult handleRequest(GetRecipesForUserRequest getRecipesForUserRequest) {
        log.info("Received GetRecipesForUserResult {}", getRecipesForUserRequest);
        String userId = getRecipesForUserRequest.getUserId();
        List<Recipe> recipeList = recipeDao.getRecipesForUser(userId);

        if (recipeList == null || recipeList.isEmpty()) {
            throw new RecipeNotFoundException("No recipes found for user id: " + userId);
        }

        List<RecipeModel> recipeModelList = new ModelConverter().toRecipeModelList(recipeList);

        return GetRecipesForUserResult.builder()
                .withRecipes(recipeModelList)
                .build();
    }
}
