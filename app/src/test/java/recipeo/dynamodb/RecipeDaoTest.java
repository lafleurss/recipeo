package recipeo.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import recipeo.dynamodb.models.Recipe;
import recipeo.metrics.MetricsPublisher;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

public class RecipeDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;
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
        String recipeName = "recipeName";
        when(dynamoDBMapper.load(Recipe.class, playlistId)).thenReturn(new Playlist());

        // WHEN
        Playlist playlist = playlistDao.getPlaylist(playlistId);

        // THEN
        assertNotNull(playlist);
        verify(dynamoDBMapper).load(Playlist.class, playlistId);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETPLAYLIST_PLAYLISTNOTFOUND_COUNT), anyDouble());

    }

}
