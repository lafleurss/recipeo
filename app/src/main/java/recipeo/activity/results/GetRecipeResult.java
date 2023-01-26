package recipeo.activity.results;

import recipeo.dynamodb.models.Recipe;
import recipeo.models.RecipeModel;

public class GetRecipeResult {
    private final RecipeModel recipe;

    private GetRecipeResult(RecipeModel recipe) {
        this.recipe = recipe;
    }

    public RecipeModel getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "GetRecipeResult{" +
                "recipe=" + recipe +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RecipeModel recipe;

        public Builder withRecipe(RecipeModel recipe) {
            this.recipe = recipe;
            return this;
        }

        public GetRecipeResult build() {
            return new GetRecipeResult(recipe);
        }
    }
}