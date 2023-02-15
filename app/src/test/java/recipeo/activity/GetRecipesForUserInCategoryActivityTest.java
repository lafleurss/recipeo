package recipeo.activity;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import recipeo.activity.requests.GetRecipesForUserInCategoryRequest;
import recipeo.activity.requests.GetRecipesForUserRequest;
import recipeo.activity.results.GetRecipesForUserInCategoryResult;
import recipeo.activity.results.GetRecipesForUserResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.models.RecipeFilter;
import recipeo.models.RecipeModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetRecipesForUserInCategoryActivityTest {
    @Mock
    private RecipeDao recipeDao;

    private GetRecipesForUserInCategoryActivity getRecipesForUserInCategoryActivity;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        getRecipesForUserInCategoryActivity = new GetRecipesForUserInCategoryActivity(recipeDao);
    }

    @Test
    public void handleRequest_savedRecipesFoundInCategory_returnsListOfRecipeModelInResult() {
        // GIVEN
        String expectedUserId = "expectedUserId";

        String expectedRecipeId1 = "expectedId1";
        String expectedRecipeName1 = "expectedName1";


        String expectedRecipeId2 = "expectedId2";
        String expectedRecipeName2 = "expectedName2";

        int expectedServings = 2;
        List<String> expectedTags = List.of("tag");
        List<String> expectedIngredients = List.of("ing", "ing2");
        List<String> expectedInstructions = List.of("inst", "inst");

        Recipe recipe_1 = new Recipe();
        recipe_1.setRecipeId(expectedRecipeId1);
        recipe_1.setRecipeName(expectedRecipeName1);
        recipe_1.setUserId(expectedUserId);
        recipe_1.setServings(expectedServings);
        recipe_1.setPrepTime(20);
        recipe_1.setCookTime(20);
        recipe_1.setTotalTime(40);
        recipe_1.setTags(Sets.newHashSet(expectedTags));
        recipe_1.setIngredients(expectedIngredients);
        recipe_1.setInstructions(expectedInstructions);
        recipe_1.setCategoryName("Specials");

        Recipe recipe_2 = new Recipe();
        recipe_2.setRecipeId(expectedRecipeId2);
        recipe_2.setRecipeName(expectedRecipeName2);
        recipe_2.setUserId(expectedUserId);
        recipe_2.setServings(expectedServings);
        recipe_2.setPrepTime(20);
        recipe_2.setCookTime(20);
        recipe_2.setTotalTime(40);
        recipe_2.setTags(Sets.newHashSet(expectedTags));
        recipe_2.setIngredients(expectedIngredients);
        recipe_2.setInstructions(expectedInstructions);
        recipe_1.setCategoryName("Specials");


        when(recipeDao.getRecipesForUserInCategory(expectedUserId, "Specials")).thenReturn(List.of(recipe_1, recipe_2));

        //WHEN
        GetRecipesForUserInCategoryRequest request = GetRecipesForUserInCategoryRequest.builder()
                .withUserId(expectedUserId)
                .withCategoryName("Specials")
                .build();

        GetRecipesForUserInCategoryResult result = getRecipesForUserInCategoryActivity.handleRequest(request);

        RecipeModel r1 = new ModelConverter().toRecipeModel(recipe_1);
        RecipeModel r2 = new ModelConverter().toRecipeModel(recipe_2);

        assertEquals(List.of(r1, r2), result.getRecipes());
    }

    @Test
    public void handleRequest_noSavedRecipesFoundInCategory_throwsRecipeNotFoundException() {
        // GIVEN
        String expectedUserId = "expectedUserId";

        when(recipeDao.getRecipesForUserInCategory(expectedUserId, "Specials")).thenReturn(null);

        //WHEN
        GetRecipesForUserInCategoryRequest request = GetRecipesForUserInCategoryRequest.builder()
                .withUserId(expectedUserId)
                .withCategoryName("Specials")
                .build();

        assertThrows(RecipeNotFoundException.class,() -> getRecipesForUserInCategoryActivity.handleRequest(request));

    }


}
