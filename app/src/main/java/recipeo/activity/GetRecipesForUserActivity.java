package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
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

    public GetRecipesForUserResult handleRequest(GetRecipesForUserRequest getRecipesForUserRequest) {
        log.info("Received GetRecipesForUserResult {}", getRecipesForUserRequest);
        String userId = getRecipesForUserRequest.getUserId();
        List<Recipe> recipeList = recipeDao.getRecipesForUser(userId);
        List<RecipeModel> recipeModelList = new ModelConverter().toRecipeModelList(recipeList);

        return GetRecipesForUserResult.builder()
                .withRecipes(recipeModelList)
                .build();
    }
}
