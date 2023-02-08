package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.UpdateRecipeRequest;
import recipeo.activity.results.UpdateRecipeResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.InvalidAttributeValueException;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeModel;
import recipeo.utils.RecipeoServiceUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static recipeo.utils.CollectionUtils.copyToSet;

public class UpdateRecipeActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;
    private final CategoryDao categoryDao;
    /**
     * Instantiates a new UpdateRecipeActivity object.
     *
     * @param recipeDao   RecipeDao to access the recipe table.
     * @param categoryDao CategoryDao to access the category table.
     */
    @Inject
    public UpdateRecipeActivity(RecipeDao recipeDao, CategoryDao categoryDao) {
        this.recipeDao = recipeDao;
        this.categoryDao = categoryDao;
    }

    /**
     * This method handles the incoming request to update an existing recipe
     * with the provided recipe name, user ID, servings, prepTime, cookTime, totalTime,
     * ingredients, instructions, categoryName(optional), tags(optional), isFavorite(optional)
     * from the request.
     * <p>
     * It then returns the updated created recipe.
     * <p>
     * If the provided recipe name or user ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param updateRecipeRequest request object containing the recipe name, user ID, servings, prepTime,
     *  cookTime, totalTime, ingredients, instructions, categoryName(optional),
     *  tags(optional), isFavorite(optional) associated with it
     * @return CreateRecipeResult object containing the API defined {@link RecipeModel}
     */
    public UpdateRecipeResult handleRequest(UpdateRecipeRequest updateRecipeRequest) {
        log.info("Received CreateRecipeRequest {}", updateRecipeRequest);

        String requestedCategoryName = updateRecipeRequest.getCategoryName();
        String userId = updateRecipeRequest.getUserId();
        String recipeName = updateRecipeRequest.getRecipeName();

        //Check for illegal characters
        if (!RecipeoServiceUtils.isValidString(recipeName)) {
            throw new InvalidAttributeValueException("Recipe name [" +
                    updateRecipeRequest.getRecipeName() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(userId)) {
            throw new InvalidAttributeValueException("Recipe user ID [" +
                    updateRecipeRequest.getUserId() +
                    "] contains illegal characters");
        }

        Recipe recipeToBeSaved = recipeDao.getRecipe(updateRecipeRequest.getUserId(),
                updateRecipeRequest.getRecipeId());

        if (recipeToBeSaved == null){
            throw new RecipeNotFoundException("Recipe name [" +
                    updateRecipeRequest.getRecipeName() +
                    "] does not exist");
        }

        List<Category> validCategoriesForUser = categoryDao.getCategoriesForUser(userId);

        //Check UserID change
        if (!Objects.equals(updateRecipeRequest.getUserId(), recipeToBeSaved.getUserId())) {
            throw new InvalidAttributeValueException("You must own the Recipe to update it!");
        }

        String isFavourite = updateRecipeRequest.getIsFavorite();
        if (isFavourite != null) {
            if (!isFavourite.equals("true") || !isFavourite.equals("false")) {
                throw new InvalidAttributeValueException("Valid values for isFavorite are 'true' or 'false' only");
            }
        }
        if (requestedCategoryName != null && validCategoriesForUser != null) {
            if (validCategoriesForUser.stream()
                    .map(category -> category.getCategoryName())
                    .noneMatch(str -> str.equals(requestedCategoryName))) {
                throw new CategoryNotFoundException("No category " + requestedCategoryName + " exists for user id: " +
                        userId);
            }
        }

        if (updateRecipeRequest.getIsFavorite() != null) {
            recipeToBeSaved.setIsFavorite(updateRecipeRequest.getIsFavorite());
        }

        if (updateRecipeRequest.getTags() != null) {
            recipeToBeSaved.setTags(copyToSet(updateRecipeRequest.getTags()));
        }

        if (updateRecipeRequest.getServings() != null) {
            recipeToBeSaved.setServings(updateRecipeRequest.getServings());
        }

        if (updateRecipeRequest.getPrepTime() != null) {
            recipeToBeSaved.setPrepTime(updateRecipeRequest.getPrepTime());
        }

        if (updateRecipeRequest.getCookTime() != null) {
            recipeToBeSaved.setCookTime(updateRecipeRequest.getCookTime());
        }

        if (updateRecipeRequest.getTotalTime() != null) {
            recipeToBeSaved.setTotalTime(updateRecipeRequest.getTotalTime());
        }

        if (updateRecipeRequest.getInstructions() != null) {
            recipeToBeSaved.setInstructions(updateRecipeRequest.getInstructions());
        }

        if (updateRecipeRequest.getIngredients() != null) {
            recipeToBeSaved.setIngredients(updateRecipeRequest.getIngredients());
        }

        if (updateRecipeRequest.getCategoryName() != null) {
            recipeToBeSaved.setCategoryName(updateRecipeRequest.getCategoryName());
        }


        recipeToBeSaved.setLastAccessed(LocalDateTime.now());

        RecipeModel recipeModel = new ModelConverter().toRecipeModel(recipeToBeSaved);
        recipeDao.saveRecipe(recipeToBeSaved);

        return UpdateRecipeResult.builder()
                .withRecipe(recipeModel)
                .build();

    }
}
