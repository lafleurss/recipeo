package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.CreateRecipeRequest;
import recipeo.activity.results.CreateRecipeResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.InvalidAttributeValueException;
import recipeo.models.RecipeModel;
import recipeo.utils.RecipeoServiceUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;


public class CreateRecipeActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;
    private final CategoryDao categoryDao;
    /**
     * Instantiates a new CreateRecipeActivity object.
     *
     * @param recipeDao   RecipeDao to access the recipe table.
     * @param categoryDao CategoryDao to access the category table.
     */
    @Inject
    public CreateRecipeActivity(RecipeDao recipeDao, CategoryDao categoryDao) {
        this.recipeDao = recipeDao;
        this.categoryDao = categoryDao;
    }

    /**
     * This method handles the incoming request by persisting a new recipe
     * with the provided recipe name, user ID, servings, prepTime, cookTime, totalTime,
     * ingredients, instructions, categoryName(optional), tags(optional), isFavorite(optional)
     * from the request.
     * <p>
     * It then returns the newly created recipe.
     * <p>
     * If the provided recipe name or user ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createRecipeRequest request object containing the recipe name, user ID, servings, prepTime,
     *  cookTime, totalTime, ingredients, instructions, categoryName(optional),
     *  tags(optional), isFavorite(optional) associated with it
     * @return CreateRecipeResult object containing the API defined {@link RecipeModel}
     */
    public CreateRecipeResult handleRequest(CreateRecipeRequest createRecipeRequest) {
        log.info("Received CreateRecipeRequest {}", createRecipeRequest);

        String requestedCategoryName = createRecipeRequest.getCategoryName();
        String userId = createRecipeRequest.getUserId();
        String recipeName = createRecipeRequest.getRecipeName();
        List<Category> validCategoriesForUser = categoryDao.getCategoriesForUser(userId);



        //Check for illegal characters
        if (!RecipeoServiceUtils.isValidString(recipeName)) {
            throw new InvalidAttributeValueException("Recipe name [" +
                    createRecipeRequest.getRecipeName() +
                    "] contains illegal characters");
        }

        if (!RecipeoServiceUtils.isValidString(userId)) {
            throw new InvalidAttributeValueException("Recipe user ID [" +
                    createRecipeRequest.getUserId() +
                    "] contains illegal characters");
        }


        Recipe recipeToBeSaved = new Recipe();
        String isFavourite = "false";
        if (createRecipeRequest.getIsFavorite() != null) {
            isFavourite = createRecipeRequest.getIsFavorite();
        }

        Set<String> recipeTags = null;
        if (createRecipeRequest.getTags() != null) {
            recipeTags = new HashSet<>(createRecipeRequest.getTags());
        }

        String categoryName = "Uncategorized";
        if (createRecipeRequest.getCategoryName() != null &&
                !createRecipeRequest.getCategoryName().equals("Uncategorized")) {
            categoryName = createRecipeRequest.getCategoryName();

            //No matching categories found
            if (validCategoriesForUser.stream()
                    .map(category -> category.getCategoryName())
                    .noneMatch(str -> str.equals(requestedCategoryName))) {
                throw new CategoryNotFoundException("No category " + requestedCategoryName + " exists for user id: " +
                        userId);
            }
        }

        recipeToBeSaved.setRecipeId(RecipeoServiceUtils.generateRecipeId());
        recipeToBeSaved.setRecipeName(createRecipeRequest.getRecipeName());
        recipeToBeSaved.setUserId(createRecipeRequest.getUserId());
        recipeToBeSaved.setServings(createRecipeRequest.getServings());
        recipeToBeSaved.setPrepTime(createRecipeRequest.getPrepTime());
        recipeToBeSaved.setCookTime(createRecipeRequest.getCookTime());
        recipeToBeSaved.setTotalTime(createRecipeRequest.getTotalTime());
        recipeToBeSaved.setCategoryName(categoryName);
        recipeToBeSaved.setTags(recipeTags);
        recipeToBeSaved.setInstructions(createRecipeRequest.getInstructions());
        recipeToBeSaved.setIngredients(createRecipeRequest.getIngredients());
        recipeToBeSaved.setIsFavorite(isFavourite);
        recipeToBeSaved.setLastAccessed(LocalDateTime.now());

        RecipeModel recipeModel = new ModelConverter().toRecipeModel(recipeToBeSaved);
        recipeDao.saveRecipe(recipeToBeSaved);

        return CreateRecipeResult.builder()
                .withRecipe(recipeModel)
                .build();

    }
}
