package recipeo.converters;

import recipeo.dynamodb.models.Recipe;
import recipeo.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts between Data and API models.
 */
public class ModelConverter {
    /**
     * Converts a provided {@link Recipe} into a {@link RecipeModel} representation.
     *
     * @param recipe the recipe to convert
     * @return the converted recipemodel
     */
    public RecipeModel toRecipeModel(Recipe recipe) {
        ZonedDateTimeConverter converter = new ZonedDateTimeConverter();
        List<String> tags = null;
        if (recipe.getTags() != null) {
            tags = new ArrayList<>(recipe.getTags());
        }
        List<String> ingredients = null;
        if (recipe.getIngredients() != null) {
            ingredients = new ArrayList<>(recipe.getIngredients());
        }

        List<String> instructions = null;
        if (recipe.getInstructions() != null) {
            instructions = new ArrayList<>(recipe.getInstructions());
        }

        String isFavorite = "false";
        if (recipe.getIsFavorite() != null) {
            isFavorite = recipe.getIsFavorite();
        }

        return RecipeModel.builder()
                .withUserId(recipe.getUserId())
                .withRecipeId(recipe.getRecipeId())
                .withRecipeName(recipe.getRecipeName())
                .withServings(recipe.getServings())
                .withTags(tags)
                .withPrepTime(recipe.getPrepTime())
                .withCookTime(recipe.getCookTime())
                .withTotalTime(recipe.getTotalTime())
                .withCategoryName(recipe.getCategoryName())
                .withIngredients(ingredients)
                .withInstructions(instructions)
                .withIsFavorite(isFavorite)
                .withLastAccessed(converter.convert(recipe.getLastAccessed()))
                .build();
    }


    /**
     * Converts a list of Recipes to a list of RecipesModels.
     *
     * @param recipes The Recipes to convert to RecipesModels
     * @return The converted list of RecipesModels
     */
    public List<RecipeModel> toRecipeModelList(List<Recipe> recipes) {
        List<RecipeModel> recipeModels = new ArrayList<>();

        for (Recipe recipe : recipes) {
            recipeModels.add(toRecipeModel(recipe));
        }

        return recipeModels;
    }
}
