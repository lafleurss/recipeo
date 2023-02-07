package recipeo.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import recipeo.dynamodb.models.Recipe;
import recipeo.exceptions.RecipeNotFoundException;
import recipeo.metrics.MetricsConstants;
import recipeo.metrics.MetricsPublisher;
import recipeo.models.RecipeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a recipe using {@link Recipe} to represent the model in DynamoDB.
 */
@Singleton
public class RecipeDao {
    private static final int PAGE_SIZE = 250;
    private static final int RECENT_PAGE_SIZE = 25;

    private final DynamoDBMapper dynamoDbMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a RecipeDao object.
     *
     * @param dynamoDbMapper   the {@link DynamoDBMapper} used to interact with the recipe table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public RecipeDao(DynamoDBMapper dynamoDbMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDbMapper = dynamoDbMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Returns the {@link Recipe} corresponding to the specified recipe id.
     *
     * @param userId the User ID
     * @param recipeName the Recipe ID
     * @return the stored Recipe, or null if none was found.
     */
    public Recipe getRecipe(String userId, String recipeName) {
        Recipe recipe = this.dynamoDbMapper.load(Recipe.class, userId, recipeName);

        if (recipe == null) {
            metricsPublisher.addCount(MetricsConstants.GETRECIPE_RECIPENOTFOUND_COUNT, 1);
            throw new RecipeNotFoundException("Could not find recipe with id: " + recipeName +
                    " for user with id: " + userId);
        }
        metricsPublisher.addCount(MetricsConstants.GETRECIPE_RECIPENOTFOUND_COUNT, 0);
        return recipe;
    }

    /**
     * Returns the {@link List} of {@link Recipe} corresponding to the specified user id.
     *
     * @param userId the User ID
     * @param filterType  Filter type to show ALL, FAVORITES or RECENTLY USED
     * @return the stored list of Recipes, or null if none was found.
     */
    public List<Recipe> getRecipesForUser(String userId, RecipeFilter filterType) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userId", new AttributeValue().withS(userId));
        List<Recipe> recipeList = new ArrayList<>();

        //Query from base table ALL Recipes
        if (filterType == RecipeFilter.ALL) {
            DynamoDBQueryExpression<Recipe> queryExpression = new DynamoDBQueryExpression<Recipe>()
                    .withKeyConditionExpression("userId = :userId")
                    .withExpressionAttributeValues(valueMap)
                    .withLimit(PAGE_SIZE);

            recipeList = dynamoDbMapper.queryPage(Recipe.class, queryExpression).getResults();

            //*************************
            //Query from base table FAVORITE Recipes
        } else if (filterType == RecipeFilter.FAVORITES) {
            valueMap.put(":isFavorite", new AttributeValue().withS("true"));
            DynamoDBQueryExpression<Recipe> queryExpression = new DynamoDBQueryExpression<Recipe>()
                    .withConsistentRead(false)
                    .withKeyConditionExpression("userId = :userId")
                    .withExpressionAttributeValues(valueMap)
                    .withFilterExpression("isFavorite = :isFavorite")
                    .withLimit(PAGE_SIZE);

            recipeList = dynamoDbMapper.queryPage(Recipe.class, queryExpression).getResults();

        //*************************
        //Query from base table UNCATEGORIZED only
        } else if (filterType == RecipeFilter.UNCATEGORIZED) {
            valueMap.put(":categoryName", new AttributeValue().withS("Uncategorized"));
            DynamoDBQueryExpression<Recipe> queryExpression = new DynamoDBQueryExpression<Recipe>()
                    .withConsistentRead(false)
                    .withKeyConditionExpression("userId = :userId")
                    .withExpressionAttributeValues(valueMap)
                    .withFilterExpression("categoryName = :categoryName")
                    .withLimit(PAGE_SIZE);

            recipeList = dynamoDbMapper.queryPage(Recipe.class, queryExpression).getResults();

            //Query from GSI LastAccessed sorted by descending order of lastAccessed
            //*************************
        } else if (filterType == RecipeFilter.RECENTLY_USED) {
            DynamoDBQueryExpression<Recipe> queryExpression = new DynamoDBQueryExpression<Recipe>()
                    .withIndexName(Recipe.RECENTLY_ACCESSED_RECIPES)
                    .withConsistentRead(false)
                    .withKeyConditionExpression("userId = :userId")
                    .withExpressionAttributeValues(valueMap)
                    .withScanIndexForward(false)
                    .withLimit(RECENT_PAGE_SIZE);

            recipeList = dynamoDbMapper.queryPage(Recipe.class, queryExpression).getResults();
        }
        return recipeList;
    }


    /**
     * Returns the {@link List} of {@link Recipe} corresponding to the specified user id and category name.
     *
     * @param userId the User ID
     * @param category  custom category name
     * @return the stored list of Recipes, or null if none was found.
     */
    public List<Recipe> getRecipesForUserInCategory(String userId, String category) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userId", new AttributeValue().withS(userId));
        valueMap.put(":categoryName", new AttributeValue().withS(category));

        List<Recipe> recipeList = new ArrayList<>();

        //Query from base table
        DynamoDBQueryExpression<Recipe> queryExpression = new DynamoDBQueryExpression<Recipe>()
                .withConsistentRead(false)
                .withKeyConditionExpression("userId = :userId")
                .withExpressionAttributeValues(valueMap)
                .withFilterExpression("categoryName = :categoryName")
                .withLimit(PAGE_SIZE);

        recipeList = dynamoDbMapper.queryPage(Recipe.class, queryExpression).getResults();


        return recipeList;
    }


    /**
     * Saves (creates or updates) the recipe.
     *
     * @param recipe The Recipe to save
     * @return The Recipe object that was saved
     */
    public Recipe saveRecipe(Recipe recipe) {
        this.dynamoDbMapper.save(recipe);
        return recipe;
    }

    /**
     * Perform a search (via a "scan") of the recipe table for recipes matching the given criteria.
     * Both "recipeName" and "tags" attributes are searched.
     * The criteria are an array of Strings. Each element of the array is search individually.
     * ALL elements of the criteria array must appear in the recipeName or the tags (or both).
     * Searches are CASE SENSITIVE.
     *
     * @param criteria an array of String containing search criteria.
     * @return a List of Recipe objects that match the search criteria.
     */
    public List<Recipe> searchRecipes(String[] criteria) {
        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();

        if (criteria.length > 0) {
            Map<String, AttributeValue> valueMap = new HashMap<>();
            String valueMapNamePrefix = ":c";

            StringBuilder nameFilterExpression = new StringBuilder();
            StringBuilder tagsFilterExpression = new StringBuilder();

            for (int i = 0; i < criteria.length; i++) {
                valueMap.put(valueMapNamePrefix + i,
                        new AttributeValue().withS(criteria[i]));
                nameFilterExpression.append(
                        filterExpressionPart("recipeName", valueMapNamePrefix, i));
                tagsFilterExpression.append(
                        filterExpressionPart("tags", valueMapNamePrefix, i));
            }

            dynamoDBScanExpression.setExpressionAttributeValues(valueMap);
            dynamoDBScanExpression.setFilterExpression(
                    "(" + nameFilterExpression + ") or (" + tagsFilterExpression + ")");
        }

        return this.dynamoDbMapper.scan(Recipe.class, dynamoDBScanExpression);
    }


    private StringBuilder filterExpressionPart(String target, String valueMapNamePrefix, int position) {
        String possiblyAnd = position == 0 ? "" : "and ";
        return new StringBuilder()
                .append(possiblyAnd)
                .append("contains(")
                .append(target)
                .append(", ")
                .append(valueMapNamePrefix).append(position)
                .append(") ");
    }



}
