package recipeo.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.activity.requests.CreateRecipeRequest;
import recipeo.activity.requests.UpdateRecipeRequest;
import recipeo.activity.results.CreateRecipeResult;
import recipeo.activity.results.UpdateRecipeResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.InvalidAttributeChangeException;
import recipeo.exceptions.RecipeNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UpdateRecipeActivityTest {
    private UpdateRecipeActivity updateRecipeActivity;

    @Mock
    private RecipeDao recipeDao;
    @Mock
    private CategoryDao categoryDao;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        updateRecipeActivity = new UpdateRecipeActivity(recipeDao, categoryDao);
    }

    @Test
    public void handleRequest_updateRecipeWithNewValidCategoryName_returnsRecipeInResult() {
        // GIVEN
        String recipeId = "recipeId";
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;
        String categoryName = "category2";

        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setUserId(userId);
        recipe.setRecipeName(recipeName);
        recipe.setServings(servings);
        recipe.setServings(servings);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setTotalTime(totalTime);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);

        String category1 = "category1";
        String category1Description = "category1";

        Category categoryOne = new Category();
        categoryOne.setCategoryName(category1);
        categoryOne.setUserId(userId);
        categoryOne.setCategoryDescription(category1Description);

        String category2 = "category2";
        String category2Description = "category2";

        Category categoryTwo = new Category();
        categoryTwo.setCategoryName(category2);
        categoryTwo.setUserId(userId);
        categoryTwo.setCategoryDescription(category2Description);

        UpdateRecipeRequest request = UpdateRecipeRequest.builder()
                .withRecipeId(recipeId)
                .withRecipeName(recipeName)
                .withUserId(userId)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(prepTime)
                .withCookTime(cookTime)
                .withTotalTime(totalTime)
                .withUserId(userId)
                .withTags(tags)
                .withCategoryName(categoryOne.getCategoryName())
                .build();

        // WHEN
        when(recipeDao.getRecipe(userId, recipeId)).thenReturn(recipe);
        when(categoryDao.getCategoriesForUser(userId)).thenReturn(List.of(categoryTwo, categoryOne));
        UpdateRecipeResult result = updateRecipeActivity.handleRequest(request);

        // THEN
        verify(recipeDao).saveRecipe(any(Recipe.class));

        assertEquals(recipeName, result.getRecipe().getRecipeName());
        assertEquals(recipeId, result.getRecipe().getRecipeId());
        assertEquals("false", result.getRecipe().getIsFavorite());
        assertEquals(userId, result.getRecipe().getUserId());
        assertEquals(tags, result.getRecipe().getTags());
        assertEquals(ingredients, result.getRecipe().getIngredients());
        assertEquals(instructions, result.getRecipe().getInstructions());
        assertEquals(prepTime, result.getRecipe().getPrepTime());
        assertEquals(cookTime, result.getRecipe().getCookTime());
        assertEquals(totalTime, result.getRecipe().getTotalTime());
        assertEquals(servings, result.getRecipe().getServings());
        assertEquals(categoryOne.getCategoryName(), result.getRecipe().getCategoryName());

    }


    @Test
    public void handleRequest_updateRecipeIdDoesNotExist_throwsRecipeNotFoundException() {
        // GIVEN
        String recipeId = "recipeIdDoesntExist";
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setUserId(userId);
        recipe.setRecipeName(recipeName);
        recipe.setServings(servings);
        recipe.setServings(servings);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setTotalTime(totalTime);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);


        UpdateRecipeRequest request = UpdateRecipeRequest.builder()
                .withRecipeId(recipeId)
                .withRecipeName(recipeName)
                .withUserId(userId)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(prepTime)
                .withCookTime(cookTime)
                .withTotalTime(totalTime)
                .withUserId(userId)
                .withTags(tags)
                .build();

        // WHEN THEN
        when(recipeDao.getRecipe(userId, recipeId)).thenReturn(null);
        when(categoryDao.getCategoriesForUser(userId)).thenReturn(null);

        assertThrows(RecipeNotFoundException.class, () -> updateRecipeActivity.handleRequest(request));

    }

    @Test
    public void handleRequest_updateRecipeCategoryNameDoesNotExist_throwsCategoryNotFoundException() {
        // GIVEN
        String recipeId = "recipeIdDoesntExist";
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;
        String categoryName = "nonExistentCategory";

        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setUserId(userId);
        recipe.setRecipeName(recipeName);
        recipe.setServings(servings);
        recipe.setServings(servings);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setTotalTime(totalTime);
        recipe.setInstructions(instructions);
        recipe.setIngredients(ingredients);
        recipe.setCategoryName("Uncategorized");

        String category1 = "category1";
        String category1Description = "category1";

        Category categoryOne = new Category();
        categoryOne.setCategoryName(category1);
        categoryOne.setUserId(userId);
        categoryOne.setCategoryDescription(category1Description);


        UpdateRecipeRequest request = UpdateRecipeRequest.builder()
                .withRecipeId(recipeId)
                .withRecipeName(recipeName)
                .withUserId(userId)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(prepTime)
                .withCookTime(cookTime)
                .withTotalTime(totalTime)
                .withUserId(userId)
                .withTags(tags)
                .withCategoryName(categoryName)
                .build();

        // WHEN THEN
        when(recipeDao.getRecipe(userId, recipeId)).thenReturn(recipe);
        when(categoryDao.getCategoriesForUser(userId)).thenReturn(List.of(categoryOne));

        assertThrows(CategoryNotFoundException.class, () -> updateRecipeActivity.handleRequest(request));

    }
}
