package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.results.GetRecipeResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;

import javax.inject.Inject;

/**
 * Implementation of the GetRecipeActivity for the RecipeoService's GetRecipe API.
 *
 * This API allows the customer to get one of their saved recipes.
 */
public class GetRecipeActivity {

    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    /**
     * Instantiates a new GetRecipeActivity object.
     *
     * @param recipeDao PlaylistDao to access the recipe table.
     */
    @Inject
    public GetRecipeActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }


    /**
     * This method handles the incoming request by retrieving the recipe from the database.
     * <p>
     * It then returns the recipe.
     * <p>
     * If the recipe does not exist, this should throw a RecipeNotFoundException.
     *
     * @param getRecipeRequest request object containing the recipe ID
     * @return GetRecipeResult object containing the API defined {@link RecipeModel}
     */
    public GetRecipeResult handleRequest(GetRecipeRequest getRecipeRequest) {
        log.info("Received GetRecipeRequest {}", getRecipeRequest);
        String recipeId = getRecipeRequest.getRecipeId();
        String userId = getRecipeRequest.getUserId();
        Recipe recipe = recipeDao.getRecipe(userId, recipeId);

        if (recipe == null) {
            throw new RecipeNotFoundException("The recipe with id: " + recipeId +
                     " cannot be found for user id: " + userId);
        }

        RecipeModel recipeModel = new ModelConverter().toRecipeModel(recipe);

        return GetRecipeResult.builder()
                .withRecipe(recipeModel)
                .build();
    }

}
