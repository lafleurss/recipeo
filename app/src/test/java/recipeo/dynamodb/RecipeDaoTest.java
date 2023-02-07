package recipeo.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.metrics.MetricsConstants;
import recipeo.metrics.MetricsPublisher;
import recipeo.models.RecipeFilter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

public class RecipeDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private QueryResultPage<Recipe> queryResultPage;
    @Mock
    private MetricsPublisher metricsPublisher;

    private RecipeDao recipeDao;

    @BeforeEach
    public void setup() {
        openMocks(this);
        recipeDao = new RecipeDao(dynamoDBMapper, metricsPublisher);
    }

    @Test
    public void getRecipe_withRecipeNameUserId_callsMapperWithPartitionKeyAndRangeKey() {
        // GIVEN
        String userId = "userId";
        String recipeName = "recipeName";
        when(dynamoDBMapper.load(Recipe.class, userId, recipeName)).thenReturn(new Recipe());

        // WHEN
        Recipe recipe = recipeDao.getRecipe(userId, recipeName);

        // THEN
        assertNotNull(recipe);
        verify(dynamoDBMapper).load(Recipe.class,userId, recipeName);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETRECIPE_RECIPENOTFOUND_COUNT), anyDouble());

    }

    @Test
    public void getRecipesForUser_withUserIdAndFilter_callsMapperWithPartitionKeyAndRangeKey() {
        // GIVEN
        String userId = "userId";
        when(dynamoDBMapper.queryPage(eq(Recipe.class), any())).thenReturn(queryResultPage);

        // WHEN
        recipeDao.getRecipesForUser(userId, RecipeFilter.ALL);

        // THEN
        verify(dynamoDBMapper).queryPage(eq(Recipe.class),any());
        verify(queryResultPage).getResults();
    }

    @Test
    public void getRecipesForUserInCategory_withUserIdAndFilter_callsMapperWithPartitionKeyAndRangeKey() {
        // GIVEN
        String userId = "userId";
        String categoryName = "categoryName";

        when(dynamoDBMapper.queryPage(eq(Recipe.class), any())).thenReturn(queryResultPage);

        // WHEN
        recipeDao.getRecipesForUserInCategory(userId, categoryName);

        // THEN
        verify(dynamoDBMapper).queryPage(eq(Recipe.class),any());
        verify(queryResultPage).getResults();
    }


    @Test
    public void getRecipe_withNonExistentRecipeName_throwsRecipeNotFoundException() {
        // GIVEN
        String nonexistentRecipeName = "I DO NOT EXIST";
        String userId = "userId";

        when(dynamoDBMapper.load(Recipe.class, userId, nonexistentRecipeName)).thenReturn(null);

        // WHEN + THEN
        assertThrows(RecipeNotFoundException.class, () -> recipeDao.getRecipe(userId, nonexistentRecipeName));
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETRECIPE_RECIPENOTFOUND_COUNT), anyDouble());
    }

    @Test
    public void saveRecipe_callsMapperWithRecipe() {
        // GIVEN
        Recipe recipe = new Recipe();

        // WHEN
        Recipe result = recipeDao.saveRecipe(recipe);

        // THEN
        verify(dynamoDBMapper).save(recipe);
        assertEquals(recipe, result);
    }

}
