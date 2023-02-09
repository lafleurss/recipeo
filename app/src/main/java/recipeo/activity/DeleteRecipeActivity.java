package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.DeleteRecipeRequest;
import recipeo.activity.results.DeleteRecipeResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;

import javax.inject.Inject;

/**
 * Implementation of the DeleteRecipeActivity for the RecipeoService's GetRecipe API.
 *
 * This API allows the customer to delete one of their saved recipes.
 */
public class DeleteRecipeActivity {

    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    /**
     * Instantiates a new DeleteRecipeActivity object.
     *
     * @param recipeDao RecipeDao to access the recipe table.
     */
    @Inject
    public DeleteRecipeActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }


    /**
     * This method handles the incoming request by retrieving the recipe from the database.
     * <p>
     * It then deletes the recipe.
     * <p>
     * If the recipe does not exist, this should throw a RecipeNotFoundException.
     *
     * @param deleteRecipeRequest request object containing the recipe ID
     * @return DeleteRecipeResult object containing the API defined {@link RecipeModel}
     */
    public DeleteRecipeResult handleRequest(DeleteRecipeRequest deleteRecipeRequest) {
        log.info("Received DeleteRecipeResult {}", deleteRecipeRequest);
        String recipeId = deleteRecipeRequest.getRecipeId();
        String userId = deleteRecipeRequest.getUserId();

        Recipe recipe = recipeDao.getRecipe(userId, recipeId);

        if (recipe == null) {
            throw new RecipeNotFoundException("The recipe with id: " + recipeId +
                     " cannot be found for user id: " + userId);
        }

        Recipe deletedRecipe = recipeDao.deleteRecipe(recipe);

        RecipeModel recipeModel = new ModelConverter().toRecipeModel(deletedRecipe);

        return DeleteRecipeResult.builder()
                .withRecipe(recipeModel)
                .build();
    }

}
