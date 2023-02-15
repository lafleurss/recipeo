package recipeo.activity;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipeResult;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;


public class GetRecipeActivityTest {
    @Mock
    private RecipeDao recipeDao;

    private GetRecipeActivity getRecipeActivity;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        getRecipeActivity = new GetRecipeActivity(recipeDao);
    }

    @Test
    public void handleRequest_savedRecipeFound_returnsRecipeModelInResult() {
        // GIVEN
        String expectedRecipeId = "expectedId";
        String expectedRecipeName = "expectedName";
        String expectedUserId = "expectedUserId";
        int expectedServings = 2;
        List<String> expectedTags = List.of("tag");
        List<String> expectedIngredients = List.of("ing", "ing2");
        List<String> expectedInstructions = List.of("inst", "inst");

        Recipe recipe = new Recipe();
        recipe.setRecipeId(expectedRecipeId);
        recipe.setRecipeName(expectedRecipeName);
        recipe.setUserId(expectedUserId);
        recipe.setServings(expectedServings);
        recipe.setPrepTime(20);
        recipe.setCookTime(20);
        recipe.setTotalTime(40);
        recipe.setTags(Sets.newHashSet(expectedTags));
        recipe.setIngredients(expectedIngredients);
        recipe.setInstructions(expectedInstructions);


        when(recipeDao.getRecipe(expectedUserId,expectedRecipeId)).thenReturn(recipe);

        GetRecipeRequest request = GetRecipeRequest.builder()
                .withUserId(expectedUserId)
                .withRecipeId(expectedRecipeId)
                .build();

        // WHEN
        GetRecipeResult result = getRecipeActivity.handleRequest(request);

        // THEN
        assertEquals(expectedRecipeId, result.getRecipe().getRecipeId());
        assertEquals(expectedRecipeName, result.getRecipe().getRecipeName());
        assertEquals(expectedUserId, result.getRecipe().getUserId());
        assertEquals(expectedServings, result.getRecipe().getServings());
        assertEquals(expectedTags, result.getRecipe().getTags());
    }

    @Test
    public void handleRequest_noSavedRecipesFound_throwsRecipeNotFoundException() {
        // GIVEN
        String userId = "expectedUserId";
        String recipeId = "expectedUserId";

        when(recipeDao.getRecipe(userId, recipeId)).thenReturn(null);

        //WHEN
        GetRecipeRequest request = GetRecipeRequest.builder()
                .withUserId(userId)
                .withRecipeId(recipeId)
                .build();

        assertThrows(RecipeNotFoundException.class,() -> getRecipeActivity.handleRequest(request));

    }


}
