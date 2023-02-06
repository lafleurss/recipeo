package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.CreateRecipeRequest;
import recipeo.activity.results.CreateRecipeResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.InvalidAttributeValueException;
import recipeo.models.RecipeModel;
import recipeo.utils.RecipeoServiceUtils;

import java.time.LocalDateTime;

import javax.inject.Inject;

import static recipeo.utils.CollectionUtils.copyToSet;

public class CreateRecipeActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;
    /**
     * Instantiates a new CreateRecipeActivity object.
     *
     * @param recipeDao RecipeDao to access the recipe table.
     */
    @Inject
    public CreateRecipeActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * This method handles the incoming request by persisting a new recipe
     * with the provided recipe name, user ID, servings, prepTime, cookTime, totalTime,
     * ingredients, instructions, categoryName(optional), tags(optional), isFavorite(optional)
     * from the request.
     * <p>
     * It then returns the newly created recipe.
     * <p>
     * If the provided recipe name, ingredients, instructions or user ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createRecipeRequest request object containing the ecipe name, user ID, servings, prepTime,
     *  cookTime, totalTime, ingredients, instructions, categoryName(optional),
     *  tags(optional), isFavorite(optional) associated with it
     * @return CreateRecipeResult object containing the API defined {@link RecipeModel}
     */
    public CreateRecipeResult handleRequest(CreateRecipeRequest createRecipeRequest) {
        log.info("Received CreateRecipeRequest {}", createRecipeRequest);

        //Check for illegal characters
        if (!RecipeoServiceUtils.isValidString(createRecipeRequest.getRecipeName())) {
            throw new InvalidAttributeValueException("Recipe name [" +
                    createRecipeRequest.getRecipeName() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(createRecipeRequest.getUserId())) {
            throw new InvalidAttributeValueException("Recipe user ID [" +
                    createRecipeRequest.getUserId() +
                    "] contains illegal characters");
        }


        Recipe recipeToBeSaved = new Recipe();
        recipeToBeSaved.setRecipeId(RecipeoServiceUtils.generateRecipeId());
        recipeToBeSaved.setRecipeName(createRecipeRequest.getRecipeName());
        recipeToBeSaved.setUserId(createRecipeRequest.getUserId());
        recipeToBeSaved.setServings(createRecipeRequest.getServings());
        recipeToBeSaved.setPrepTime(createRecipeRequest.getPrepTime());
        recipeToBeSaved.setCookTime(createRecipeRequest.getCookTime());
        recipeToBeSaved.setTotalTime(createRecipeRequest.getTotalTime());
        recipeToBeSaved.setCategoryName(createRecipeRequest.getCategoryName());
        recipeToBeSaved.setTags(copyToSet(createRecipeRequest.getTags()));
        recipeToBeSaved.setIsFavorite("false");
        recipeToBeSaved.setLastAccessed(LocalDateTime.now());

        RecipeModel recipeModel = new ModelConverter().toRecipeModel(recipeToBeSaved);
        recipeDao.saveRecipe(recipeToBeSaved);

        return CreateRecipeResult.builder()
                .withRecipe(recipeModel)
                .build();

    }
}
