package recipeo.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.activity.requests.CreateRecipeRequest;
import recipeo.activity.results.CreateRecipeResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.CategoryNotFoundException;
import recipeo.exceptions.InvalidAttributeValueException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateRecipeActivityTest {

    private CreateRecipeActivity createRecipeActivity;

    @Mock
    private RecipeDao recipeDao;
    @Mock
    private CategoryDao categoryDao;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        createRecipeActivity = new CreateRecipeActivity(recipeDao, categoryDao);
    }

    @Test
    public void handleRequest_addRecipeNoCategoryName_returnsRecipeInResult() {
        // GIVEN
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(prepTime)
                .withCookTime(cookTime)
                .withTotalTime(totalTime)
                .withUserId(userId)
                .withTags(tags)
                .build();

        // WHEN
        CreateRecipeResult result = createRecipeActivity.handleRequest(request);

        // THEN
        verify(recipeDao).saveRecipe(any(Recipe.class));
        assertEquals(recipeName, result.getRecipe().getRecipeName());
        assertEquals("false", result.getRecipe().getIsFavorite());
        assertEquals("Uncategorized", result.getRecipe().getCategoryName());
        assertEquals(userId, result.getRecipe().getUserId());
        assertEquals(tags, result.getRecipe().getTags());
        assertEquals(ingredients, result.getRecipe().getIngredients());
        assertEquals(instructions, result.getRecipe().getInstructions());
        assertEquals(prepTime, result.getRecipe().getPrepTime());
        assertEquals(cookTime, result.getRecipe().getCookTime());
        assertEquals(totalTime, result.getRecipe().getTotalTime());
        assertEquals(servings, result.getRecipe().getServings());

    }

    @Test
    public void handleRequest_addRecipeNoTags_returnsRecipeInResult() {
        // GIVEN
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(prepTime)
                .withCookTime(cookTime)
                .withTotalTime(totalTime)
                .withUserId(userId)
                .build();

        // WHEN
        CreateRecipeResult result = createRecipeActivity.handleRequest(request);

        // THEN
        verify(recipeDao).saveRecipe(any(Recipe.class));
        assertEquals(recipeName, result.getRecipe().getRecipeName());
        assertEquals("false", result.getRecipe().getIsFavorite());
        assertEquals("Uncategorized", result.getRecipe().getCategoryName());
        assertEquals(userId, result.getRecipe().getUserId());
        assertNull(result.getRecipe().getTags());
        assertEquals(ingredients, result.getRecipe().getIngredients());
        assertEquals(instructions, result.getRecipe().getInstructions());
        assertEquals(prepTime, result.getRecipe().getPrepTime());
        assertEquals(cookTime, result.getRecipe().getCookTime());
        assertEquals(totalTime, result.getRecipe().getTotalTime());
        assertEquals(servings, result.getRecipe().getServings());

    }

    @Test
    public void handleRequest_addRecipeInvalidRecipeName_throwsInvalidAttributeValueException() {
        // GIVEN
        String recipeName = "expected\\%$@#Name";
        String userId = "expectedUserId";
        Integer servings = 2;
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
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
        assertThrows(InvalidAttributeValueException.class , () -> createRecipeActivity.handleRequest(request));

    }

    @Test
    public void handleRequest_addRecipeWithCategoryName_returnsRecipeInResult() {
        // GIVEN
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        String categoryName = "category1";
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

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

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(20)
                .withCookTime(20)
                .withTotalTime(40)
                .withUserId(userId)
                .withTags(tags)
                .withCategoryName(categoryName)
                .build();

        // WHEN
        when(categoryDao.getCategoriesForUser(userId)).thenReturn(List.of(categoryTwo, categoryOne));
        CreateRecipeResult result = createRecipeActivity.handleRequest(request);

        // THEN
        verify(recipeDao).saveRecipe(any(Recipe.class));
        assertEquals(recipeName, result.getRecipe().getRecipeName());
        assertEquals("false", result.getRecipe().getIsFavorite());
        assertEquals(userId, result.getRecipe().getUserId());
        assertEquals(tags, result.getRecipe().getTags());
        assertEquals(ingredients, result.getRecipe().getIngredients());
        assertEquals(instructions, result.getRecipe().getInstructions());
        assertEquals(prepTime, result.getRecipe().getPrepTime());
        assertEquals(cookTime, result.getRecipe().getCookTime());
        assertEquals(totalTime, result.getRecipe().getTotalTime());
        assertEquals(servings, result.getRecipe().getServings());
        assertEquals(categoryName, result.getRecipe().getCategoryName());

    }

    @Test
    public void handleRequest_addRecipeWithIsFavoriteSet_returnsRecipeInResult() {
        // GIVEN
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        String isFavorite = "true";
        String categoryName = "category1";
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        String category1 = "category1";
        String category1Description = "category1";

        Category categoryOne = new Category();
        categoryOne.setCategoryName(category1);
        categoryOne.setUserId(userId);
        categoryOne.setCategoryDescription(category1Description);

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
                .withIsFavorite(isFavorite)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(20)
                .withCookTime(20)
                .withTotalTime(40)
                .withUserId(userId)
                .withTags(tags)
                .withCategoryName(categoryName)
                .build();

        // WHEN
        when(categoryDao.getCategoriesForUser(userId)).thenReturn(List.of(categoryOne));
        CreateRecipeResult result = createRecipeActivity.handleRequest(request);

        // THEN
        verify(recipeDao).saveRecipe(any(Recipe.class));
        assertEquals(recipeName, result.getRecipe().getRecipeName());
        assertEquals(isFavorite, result.getRecipe().getIsFavorite());
        assertEquals(userId, result.getRecipe().getUserId());
        assertEquals(tags, result.getRecipe().getTags());
        assertEquals(ingredients, result.getRecipe().getIngredients());
        assertEquals(instructions, result.getRecipe().getInstructions());
        assertEquals(prepTime, result.getRecipe().getPrepTime());
        assertEquals(cookTime, result.getRecipe().getCookTime());
        assertEquals(totalTime, result.getRecipe().getTotalTime());
        assertEquals(servings, result.getRecipe().getServings());
        assertEquals(categoryName, result.getRecipe().getCategoryName());

    }

    @Test
    public void handleRequest_addRecipeWithCategoryNameThatDoesntExist_throwsCategoryNotFoundException() {
        // GIVEN
        String recipeName = "expectedName";
        String userId = "expectedUserId";
        Integer servings = 2;
        String categoryName = "categoryDoesNotExist";
        List<String> tags = List.of("tag");
        List<String> ingredients = List.of("ing1", "ing2");
        List<String> instructions = List.of("inst1", "inst2", "inst3");
        Integer prepTime = 20;
        Integer cookTime = 20;
        Integer totalTime = 40;

        String category1 = "category1";
        String category1Description = "category1";

        Category categoryOne = new Category();
        categoryOne.setCategoryName(category1);
        categoryOne.setUserId(userId);
        categoryOne.setCategoryDescription(category1Description);

        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .withRecipeName(recipeName)
                .withInstructions(instructions)
                .withIngredients(ingredients)
                .withServings(servings)
                .withPrepTime(20)
                .withCookTime(20)
                .withTotalTime(40)
                .withUserId(userId)
                .withTags(tags)
                .withCategoryName(categoryName)
                .build();

        when(categoryDao.getCategoriesForUser(userId)).thenReturn(List.of(categoryOne));

        // WHEN THEN
        assertThrows(CategoryNotFoundException.class, () -> createRecipeActivity.handleRequest(request));

    }
}
