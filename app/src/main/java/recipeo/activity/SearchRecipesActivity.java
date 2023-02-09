package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.activity.requests.SearchRecipesRequest;
import recipeo.activity.results.SearchRecipesResult;
import recipeo.converters.ModelConverter;
import recipeo.dynamodb.RecipeDao;
import recipeo.dynamodb.models.Recipe;
import recipeo.models.RecipeModel;

import java.util.List;

import javax.inject.Inject;

import static recipeo.utils.NullUtils.ifNull;

public class SearchRecipesActivity {
    private final Logger log = LogManager.getLogger();
    private final RecipeDao recipeDao;

    /**
     * Instantiates a new SearchPlaylistsActivity object.
     *
     * @param recipeDao RecipeDao to access the recipes table.
     */
    @Inject
    public SearchRecipesActivity(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    /**
     * This method handles the incoming request by searching for recipe from the database.
     * <p>
     * It then returns the matching recipes, or an empty result list if none are found.
     *
     * @param searchRecipesRequest request object containing the search criteria
     * @return SearchRecipesResult result object containing the recipes that match the
     * search criteria.
     */
    public SearchRecipesResult handleRequest(final SearchRecipesRequest searchRecipesRequest) {
        log.info("Received SearchRecipesRequest {}", searchRecipesRequest);

        String criteria = ifNull(searchRecipesRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Recipe> results = recipeDao.searchRecipes(criteriaArray);
        List<RecipeModel> recipeModels = new ModelConverter().toRecipeModelList(results);

        return SearchRecipesResult.builder()
                .withRecipes(recipeModels)
                .build();
    }

}
